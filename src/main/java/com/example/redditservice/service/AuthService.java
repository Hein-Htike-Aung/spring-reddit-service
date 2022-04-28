package com.example.redditservice.service;

import com.example.redditservice.dto.*;
import com.example.redditservice.exception.SpringRedditException;
import com.example.redditservice.model.User;
import com.example.redditservice.model.VerificationToken;
import com.example.redditservice.repository.UserRepo;
import com.example.redditservice.repository.VerificationTokenRepo;
import com.example.redditservice.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final VerificationTokenRepo verificationTokenRepo;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepo.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail(
                "Please Activate your Account",
                user.getEmail(),
                "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepo.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepo.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("User not found with name - " + username));

        user.setEnabled(true);
        userRepo.save(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        // Authentication with username and password
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticate);

        // Generate Token and send it to client
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication();
        String username = ((JwtAuthenticationToken)principal).getName();

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name - " + username));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        // Validate Refresh token
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        // Generate New token and send it to client
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public void deleteRefreshToken(RefreshTokenRequest refreshTokenRequest) {

        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }
}
