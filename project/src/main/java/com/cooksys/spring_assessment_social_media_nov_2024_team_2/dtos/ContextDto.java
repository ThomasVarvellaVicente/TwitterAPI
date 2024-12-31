package com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContextDto {

	private TweetResponseDto target;
	
	private List<TweetResponseDto> before;
	
	private List<TweetResponseDto> after;
}
