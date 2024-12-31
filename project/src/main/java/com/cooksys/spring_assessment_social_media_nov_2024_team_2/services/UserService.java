package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.CredentialsDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.TweetResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.UserRequestDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getUsers();

    UserResponseDto getUser(String username);

    void followUser(String username, CredentialsDto credentialsDto);

    UserResponseDto updateUserProfile(UserRequestDto userRequestDto, String username);

    UserResponseDto deleteUser(CredentialsDto credentialsDto, String username);

    void unfollowUser(String username, CredentialsDto credentialsDto);

    List<UserResponseDto> getFollowing(String username);

    List<UserResponseDto> getFollowers(String username);

    List<TweetResponseDto> getUserTweets(String username);

    List<TweetResponseDto> getUserMentions(String username);

    List<TweetResponseDto> getUserFeed(String username);
}
