package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.HashtagDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.TweetResponseDto;

import java.util.List;

public interface HashtagService {

    List<HashtagDto> getAllHashtags();

    List<TweetResponseDto> getTweetsByHashtag(String label);
}
