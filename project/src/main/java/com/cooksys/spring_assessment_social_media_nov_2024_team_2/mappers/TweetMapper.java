package com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.TweetRequestDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.TweetResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Tweet;





@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

	TweetResponseDto entityTpDto(Tweet entity);
	TweetRequestDto entitiyToRequestDto(Tweet entity);
//	Tweet requestToEntity (TweetRequestDto tweetRequestDto);
//	List<Tweet> requestToEntityList(List<TweetRequestDto> questionTweetDto);
	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);
}
