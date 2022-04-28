package com.example.redditservice.service;

import com.example.redditservice.dto.SubRedditDto;
import com.example.redditservice.exception.SpringRedditException;
import com.example.redditservice.mapper.SubRedditMapper;
import com.example.redditservice.model.SubReddit;
import com.example.redditservice.repository.SubRedditRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubRedditService {

    private final SubRedditRepo subRedditRepo;
    private final SubRedditMapper subRedditMapper;

    public SubRedditDto save(SubRedditDto subRedditDto) {
        SubReddit subReddit = subRedditMapper.mapDtoToSubReddit(subRedditDto);
        SubReddit save = subRedditRepo.save(subReddit);
        subRedditDto.setId(save.getId());
        return subRedditDto;
    }

    public List<SubRedditDto> getAll() {
        return subRedditRepo.findAll()
                .stream()
                .map(subRedditMapper::mapSubRedditToDto)
                .collect(Collectors.toList());
    }

    public SubRedditDto getSubReddit(Long id) {
        SubReddit subReddit = subRedditRepo.findById(id)
                .orElseThrow(() -> new SpringRedditException("No SubReddit found with id - " + id));
        return subRedditMapper.mapSubRedditToDto(subReddit);
    }
}
