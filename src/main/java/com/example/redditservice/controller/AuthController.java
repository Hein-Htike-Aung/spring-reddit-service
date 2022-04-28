package com.example.redditservice.controller;

import com.example.redditservice.dto.AuthenticationResponse;
import com.example.redditservice.dto.LoginRequest;
import com.example.redditservice.dto.RefreshTokenRequest;
import com.example.redditservice.dto.RegisterRequest;
import com.example.redditservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody RegisterRequest registerRequest
    ) {
        authService.signUp(registerRequest);

        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(
            @PathVariable("token") String token
    ) {
        this.authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(
            @RequestBody LoginRequest loginRequest
    ) {
        return authService.login(loginRequest);
    }

    /*
    * if the jwt token is expired, generate new token by using refresh token
    * (No need to login Again)
    * */
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        authService.deleteRefreshToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Delete Refresh Token and Logout successfully");
    }

}
