package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services;

import java.util.List;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.*;

public interface TweetService {


	List<UserResponseDto> getTweetMentions(Long id);

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto getAllTweetByTweetId(Long id);

	void likeTweet(long id, CredentialsDto credentialsDto);

	List<UserResponseDto> getTweetLikes(Long id);

	TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);

	List<TweetResponseDto> getReposts(Long id);

	TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto);

	List<TweetResponseDto> getRepliesFromId(Long id);

	ContextDto getContextFromId(Long id);

	List<HashtagDto> getTweetHashtags(Long id);

    TweetResponseDto deleteTweet(CredentialsDto credentialsDto, Long id);
}
