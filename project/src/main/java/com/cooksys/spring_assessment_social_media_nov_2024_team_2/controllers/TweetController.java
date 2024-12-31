package com.cooksys.spring_assessment_social_media_nov_2024_team_2.controllers;

import java.util.List;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.*;
import org.springframework.web.bind.annotation.*;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.TweetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	private final TweetService tweetService;
	 
	@GetMapping
	public List<TweetResponseDto> getAllTweets() {
	    return tweetService.getAllTweets();
	  }
	
	@GetMapping("/{id}")
	public TweetResponseDto getAllTweetsByTweetId(@PathVariable Long id) {
	    return tweetService.getAllTweetByTweetId(id);
	  }
	
	@PostMapping
	public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto){

		return tweetService.createTweet(tweetRequestDto);
	}

	@PostMapping("/{id}/like")
	public void likeTweet(@PathVariable long id,@RequestBody CredentialsDto credentialsDto) {
		tweetService.likeTweet(id,credentialsDto);
	}
	
	@GetMapping("/{id}/likes")
	public List <UserResponseDto> getTweetLikes(@PathVariable Long id){
		return tweetService.getTweetLikes(id);
	}

	@GetMapping("/{id}/tags")
	public List <HashtagDto> getTweetHashtags(@PathVariable Long id){
		return tweetService.getTweetHashtags(id);
	}
	
	@PostMapping("/{id}/repost")
	public TweetResponseDto repostTweet(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
		return tweetService.repostTweet( id,credentialsDto);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getReposts(@PathVariable Long id){
		return tweetService.getReposts(id);
	}
	
	@PostMapping ("/{id}/reply")
	public TweetResponseDto replyToTweet(@PathVariable Long id,  @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.replyToTweet(id, tweetRequestDto);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getRepliesFromId(@PathVariable Long id){
		return tweetService.getRepliesFromId(id);
	}
	
	@GetMapping("/{id}/context")
	public ContextDto getContextFromId(@PathVariable Long id) {
		return tweetService.getContextFromId(id);
	}

	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getTweetMentions(@PathVariable Long id){
		return tweetService.getTweetMentions(id);
	}

	@DeleteMapping("/{id}")
	public TweetResponseDto deleteTweet(@RequestBody CredentialsDto credentialsDto, @PathVariable Long id) {
		return tweetService.deleteTweet(credentialsDto, id);
	}

}
