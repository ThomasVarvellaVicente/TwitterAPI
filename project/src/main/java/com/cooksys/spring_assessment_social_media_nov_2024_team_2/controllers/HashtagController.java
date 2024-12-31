package com.cooksys.spring_assessment_social_media_nov_2024_team_2.controllers;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.HashtagDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.TweetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Hashtag;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.HashtagService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class HashtagController {

    private HashtagService hashtagService;

    @GetMapping
    public List<HashtagDto> getAllHashtags() {
        return hashtagService.getAllHashtags();
    }

    // GET tags/{label}
    // Retrieves all (non-deleted) tweets tagged with the given hashtag label in reverse chronological order
    @GetMapping("/{label}")
    public List<TweetResponseDto> getTweetsByHashtag(@PathVariable String label) {
        return hashtagService.getTweetsByHashtag(label);
    }

}
