package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.UserServiceImpl;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.*;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Credentials;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Profile;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Tweet;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.User;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.BadRequestException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotAuthorizedException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotFoundException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers.*;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories.TweetRepository;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories.UserRepository;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final TweetMapper tweetMapper;
    private final CredentialsMapper credentialsMapper;
    private final TweetRepository tweetRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        if (userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
            throw new BadRequestException("Credentials and Profile must not be null");
        }

        String username = userRequestDto.getCredentials().getUsername();
        String password = userRequestDto.getCredentials().getPassword();
        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username must not be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password must not be empty");
        }

        Optional<User> existingUser = userRepository.findByCredentialsUsername(username);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (user.isDeleted()) {


                if (!user.getCredentials().getPassword().equals(password)) {
                    throw new NotAuthorizedException("Password does not match existing user");
                }

                user.setDeleted(false);
                user.setProfile(profileMapper.dtoToEntity(userRequestDto.getProfile()));

                User reactivatedUser = userRepository.save(user);
                return userMapper.entityToDto(reactivatedUser);
            }

            // If the user is not deleted, throw BadRequestException indicating user already exists
            throw new BadRequestException("Username is already taken");
        }

        // Create a new user from scratch
        User userEntity = userMapper.dtoToEntity(userRequestDto);

        userEntity.setDeleted(false);
        userEntity.setJoined(new Timestamp(System.currentTimeMillis()));

        // Set embeddable objects
        Credentials credentials = userEntity.getCredentials();
        if (credentials != null) {
            credentials.setUsername(username);
            credentials.setPassword(password);
        } else {
            throw new BadRequestException("User credentials are not properly set");
        }

        Profile profile = userEntity.getProfile();
        if (profile != null) {
            String email = profile.getEmail();
            if (email == null || email.trim().isEmpty()) {
                throw new BadRequestException("Email is required to create a user");
            }
            profile.setFirstName(userRequestDto.getProfile().getFirstName());
            profile.setLastName(userRequestDto.getProfile().getLastName());
            profile.setEmail(userRequestDto.getProfile().getEmail());
            profile.setPhone(userRequestDto.getProfile().getPhone());
        } else {
            throw new BadRequestException("User profile is not properly set");
        }

        // Save and return the new user
        User savedUser = userRepository.save(userEntity);
        return userMapper.entityToDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return userMapper.entitiesToDtos(userRepository.findByDeletedFalse());
    }

    @Override
    public UserResponseDto getUser(String username) {
        // Validate that the username is not null or empty
        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username must not be null or empty");
        }

        Optional<User> userOpt = userRepository.findByCredentialsUsername(username);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User with username '" + username + "' does not exist");

        }
        User user = userOpt.get();

        if (user.isDeleted()) {
            throw new NotFoundException("User with username '" + username + "' has been deleted");
        }

        return userMapper.entityToDto(user);
    }

    @Override
    public void followUser(String username, CredentialsDto credentialsDto) {

        if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
            throw new BadRequestException("Invalid credentials provided.");
        }

        Optional<User> requestingUserOpt = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        if (requestingUserOpt.isEmpty() || requestingUserOpt.get().isDeleted()) {
            throw new NotAuthorizedException("Invalid credentials or user is not active.");
        }

        User requestingUser = requestingUserOpt.get();

        if (!requestingUser.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new NotAuthorizedException("Invalid credentials or user is not active.");
        }

        Optional<User> targetUserOpt = userRepository.findByCredentialsUsername(username);
        if (targetUserOpt.isEmpty() || targetUserOpt.get().isDeleted()) {
            throw new NotFoundException("Target user not found or is deleted.");
        }

        User targetUser = targetUserOpt.get();


        if (requestingUser.getFollowing().contains(targetUser)) {
            throw new BadRequestException("You are already following this user.");
        }

        requestingUser.getFollowing().add(targetUser);

        targetUser.getFollowers().add(requestingUser);

        userRepository.save(requestingUser);
        userRepository.save(targetUser);
    }

    @Override
    public UserResponseDto updateUserProfile(UserRequestDto userRequestDto, String username) {

        CredentialsDto credentialsDto = userRequestDto.getCredentials();
        if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
            throw new BadRequestException("Invalid credentials provided.");
        }

        ProfileDto profileDto = userRequestDto.getProfile();
        if (profileDto == null) {
            throw new BadRequestException("Profile information is required to update the user.");
        }

        Optional<User> userOpt = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        if (userOpt.isEmpty() || userOpt.get().isDeleted()) {
            throw new NotAuthorizedException("Invalid credentials or user is not active.");
        }

        User user = userOpt.get();

        if (!user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new NotAuthorizedException("Invalid credentials or user is not active.");
        }

        if (!user.getCredentials().getUsername().equals(username)) {
            throw new NotFoundException("User not found.");
        }

        Profile profile = user.getProfile();
        if (profileDto.getFirstName() != null && !profileDto.getFirstName().trim().isEmpty()) {
            profile.setFirstName(profileDto.getFirstName());
        }
        if (profileDto.getLastName() != null && !profileDto.getLastName().trim().isEmpty()) {
            profile.setLastName(profileDto.getLastName());
        }
        if (profileDto.getEmail() != null && !profileDto.getEmail().trim().isEmpty()) {
            profile.setEmail(profileDto.getEmail());
        }
        if (profileDto.getPhone() != null && !profileDto.getPhone().trim().isEmpty()) {
            profile.setPhone(profileDto.getPhone());
        }

        userRepository.save(user);
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto deleteUser(CredentialsDto credentialsDto, String username) {
        if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
            throw new BadRequestException("Invalid credentials provided.");
        }

        Optional<User> userOpt = userRepository.findByCredentialsUsername(username);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found.");
        }

        User user = userOpt.get();

        if (!user.getCredentials().getUsername().equals(credentialsDto.getUsername()) ||
                !user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new NotAuthorizedException("Invalid credentials.");
        }

        if (!user.isDeleted()) {
            user.setDeleted(true);
            userRepository.save(user);
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public void unfollowUser(String username, CredentialsDto credentialsDto) {

        if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
            throw new BadRequestException("Invalid credentials provided.");
        }

        Optional<User> requestingUserOpt = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        if (requestingUserOpt.isEmpty() || requestingUserOpt.get().isDeleted()) {
            throw new NotAuthorizedException("Invalid credentials or user is not active.");
        }

        User requestingUser = requestingUserOpt.get();

        if (!requestingUser.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            throw new NotAuthorizedException("Invalid credentials or user is not active.");
        }

        Optional<User> targetUserOpt = userRepository.findByCredentialsUsername(username);
        if (targetUserOpt.isEmpty() || targetUserOpt.get().isDeleted()) {
            throw new NotFoundException("Target user not found or is deleted.");
        }

        User targetUser = targetUserOpt.get();

        if (!requestingUser.getFollowing().contains(targetUser)) {
            throw new BadRequestException("You are not following this user.");
        }

        requestingUser.getFollowing().remove(targetUser);
        targetUser.getFollowers().remove(requestingUser);

        userRepository.save(requestingUser);
        userRepository.save(targetUser);
    }

    @Override
    public List<UserResponseDto> getFollowing(String username) {
        Optional<User> userOpt = userRepository.findByCredentialsUsername(username);
        if (userOpt.isEmpty() || userOpt.get().isDeleted()) {
            throw new NotFoundException("User not found or is inactive.");
        }

        User user = userOpt.get();
        List<User> activeFollowing = new ArrayList<>();


        for (User followingUser : user.getFollowing()) {
            if (!followingUser.isDeleted()) {
                activeFollowing.add(followingUser);
            }
        }


        return userMapper.entitiesToDtos(activeFollowing);
    }

    @Override
    public List<UserResponseDto> getFollowers(String username) {

        Optional<User> userOpt = userRepository.findByCredentialsUsername(username);
        if (userOpt.isEmpty() || userOpt.get().isDeleted()) {
            throw new NotFoundException("User not found or is inactive.");
        }

        User user = userOpt.get();

        List<User> activeFollowers = new ArrayList<>();

        for (User followerUser : user.getFollowers()) {
            if (!followerUser.isDeleted()) {
                activeFollowers.add(followerUser);
            }
        }

        return userMapper.entitiesToDtos(activeFollowers);
    }

    @Override
    public List<TweetResponseDto> getUserTweets(String username) {

        Optional<User> userOpt = userRepository.findByCredentialsUsername(username);
        if (userOpt.isEmpty() || userOpt.get().isDeleted()) {
            throw new NotFoundException("User not found or is inactive.");
        }

        User user = userOpt.get();

        List<Tweet> allTweets = user.getTweets();

        List<Tweet> activeTweets = new ArrayList<>();
        for (Tweet tweet : allTweets) {
            if (!tweet.isDeleted()) {
                activeTweets.add(tweet);
            }
        }

        activeTweets.sort((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()));

        return tweetMapper.entitiesToDtos(activeTweets);
    }

    // GET users/@{username}/mentions
    // Retrieves all non-deleted tweets where the user with the given username is mentioned in reverse chronological order
    @Override
    public List<TweetResponseDto> getUserMentions(String username) {

        //  Throws NotFoundException if the user doesn't exist
        Optional<User> userToFind = userRepository.findByCredentialsUsername(username);
        if (userToFind.isEmpty()) {
            throw new NotFoundException("User not found.");
        }

        // Changes the Optional back to a User
        User mentionedUser = userToFind.get();

        // Get All Tweets
        List <Tweet> tweets = tweetRepository.findByDeletedFalseOrderByPostedDesc();

        // Loop through all tweets and add the tweets that are not deleted and where the mentionedUsers contain this user
        List <Tweet> tweetsWithMentions = new ArrayList<>();

        for (Tweet t : tweets) {
            if (!t.isDeleted() && t.getMentionedUsers().contains(mentionedUser)) {
                tweetsWithMentions.add(t);
            }
        }

        return tweetMapper.entitiesToDtos(tweetsWithMentions);
    }

    // GET users/@{username}/feed
    // Retrieves all (non-deleted) tweets authored by the user with the given username & users they are following
    @Override
    public List<TweetResponseDto> getUserFeed(String username) {

        //  Throws NotFoundException if the user doesn't exist (deleted or never created)
        Optional<User> userToFind = userRepository.findByCredentialsUsername(username);
        if (userToFind.isEmpty() || userToFind.get().isDeleted()) {
            throw new NotFoundException("User not found.");
        }

        // Changes the Optional back to a User
        User user = userToFind.get();

        // Get All Tweets
        List <Tweet> tweets = tweetRepository.findByDeletedFalseOrderByPostedDesc();

        // Loops through all tweets
        List <Tweet> feedTweets = new ArrayList<>();

        // Adds non-deleted tweets whose author is the user or their follower to feedTweets
        for (Tweet t : tweets) {
            if (!t.isDeleted() && t.getAuthor().equals(user) || t.getAuthor().equals(user.getFollowing())) {
                feedTweets.add(t);
            }
        }
        return tweetMapper.entitiesToDtos(feedTweets);
    }

}