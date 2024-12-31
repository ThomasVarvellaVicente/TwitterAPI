package com.cooksys.spring_assessment_social_media_nov_2024_team_2.controllers;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.CredentialsDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.TweetResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.UserRequestDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.UserResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Tweet;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.BadRequestException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotAuthorizedException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotFoundException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto){
        return userService.createUser(userRequestDto);
    }

    @GetMapping
    public List<UserResponseDto> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUser(@PathVariable String username){
        return userService.getUser(username);
    }

    @PostMapping("/@{username}/follow")
    public ResponseEntity<Void> followUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
        try {
            userService.followUser(username, credentialsDto);
            return ResponseEntity.ok().build();
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PatchMapping("/@{username}")
    public UserResponseDto updateUserProfile(@RequestBody UserRequestDto userRequestDto, @PathVariable String username){
        return userService.updateUserProfile(userRequestDto, username);
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@RequestBody CredentialsDto credentialsDto, @PathVariable String username){
        return userService.deleteUser(credentialsDto, username);
    }

    @PostMapping("/@{username}/unfollow")
    public ResponseEntity<Void> unfollowUser(@PathVariable String username, @RequestBody CredentialsDto credentialsDto) {
        userService.unfollowUser(username, credentialsDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getFollowing(@PathVariable String username){
        return userService.getFollowing(username);
    }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getFollowers(@PathVariable String username){
        return userService.getFollowers(username);
    }

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username){
        return userService.getUserTweets(username);
    }

    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getUserMentions(@PathVariable String username){
        return userService.getUserMentions(username);
    }

    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getUserFeed(@PathVariable String username){
        return userService.getUserFeed(username);
    }

}
