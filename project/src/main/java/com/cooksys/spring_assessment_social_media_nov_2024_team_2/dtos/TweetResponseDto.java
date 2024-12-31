package com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {


	  private Long id;

	  private Timestamp posted;
	  
	  private String content;
	  
	  private TweetResponseDto inReplyTo;
	  
	  private TweetResponseDto repostOf;


}
