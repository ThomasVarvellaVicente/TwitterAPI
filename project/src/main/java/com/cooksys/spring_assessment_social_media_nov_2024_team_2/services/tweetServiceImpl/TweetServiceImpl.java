package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.tweetServiceImpl;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.*;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers.HashtagMapper;
import org.springframework.stereotype.Service;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Hashtag;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Tweet;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.User;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.BadRequestException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotAuthorizedException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotFoundException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers.TweetMapper;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers.UserMapper;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories.HashtagRepository;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories.TweetRepository;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories.UserRepository;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService{

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	private final UserRepository userRepository;
	private final HashtagRepository hashtagRepository;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;


	private void validateCredentials(CredentialsDto credentialsDto) {
	    if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
	        throw new BadRequestException("Invalid credentials provided."+credentialsDto+"wtf");
	    }

	    Optional<User> requestingUserOpt = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
	    if (requestingUserOpt.isEmpty() || requestingUserOpt.get().isDeleted()) {
	        throw new NotAuthorizedException("Invalid credentials or user is not active.");
	    }

	    User user = requestingUserOpt.get();

	    if (!user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
	        throw new NotAuthorizedException("Invalid credentials or user is not active.");
	    }
	}
	
	private Tweet getNonDeletedTweetById(Long id) {
	    return tweetRepository.findByIdAndDeletedFalse(id)
	            .orElseThrow(() -> new NotFoundException("Tweet with id " + id + " not found or has been deleted."));
	}

	private User getUserByCredentials(CredentialsDto credentialsDto) {
	    return userRepository.findByCredentialsUsername(credentialsDto.getUsername())
	            .orElseThrow(() -> new NotAuthorizedException("Invalid credentials: User not found"));
	}

	public static List<Tweet> getAllTweetsAfter(Tweet root) {
	    if (root == null) return new ArrayList<>();
	    
	    List<Tweet> allTweets = new ArrayList<>();
	    Set<Tweet> visited = new HashSet<>();
	    Deque<Tweet> stack = new ArrayDeque<>();
	    
	    stack.push(root); 
	    visited.add(root); 

	    while (!stack.isEmpty()) {
	        Tweet current = stack.pop();
	        allTweets.add(current);

	        for (Tweet reply : current.getReplies()) {
	            if (!visited.contains(reply)) {
	                stack.push(reply);
	                visited.add(reply);
	            }
	        }
	    }

	    allTweets.sort(Comparator.comparing(Tweet::getPosted)); 
	    return allTweets;
	}

	@Override
    public List<TweetResponseDto> getAllTweets() {
        List<Tweet> tweets = tweetRepository.findByDeletedFalseOrderByPostedDesc();
        return tweetMapper.entitiesToDtos(tweets);
    }

	@Override
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
	    // Validate the credentials
	    validateCredentials(tweetRequestDto.getCredentials());
	    
	    if (tweetRequestDto.getContent() == null || tweetRequestDto.getContent().trim().isEmpty()) {
	        throw new BadRequestException("Tweet content cannot be empty.");
	    }

	    Optional<User> author = userRepository.findByCredentialsUsername(tweetRequestDto.getCredentials().getUsername());
	    
	    if (author.isPresent()) {
	        User user = author.get();
	        Tweet newTweet = new Tweet();
	        newTweet.setContent(tweetRequestDto.getContent());
	        newTweet.setAuthor(user);

	        Tweet savedTweet = tweetRepository.save(newTweet);

	        processHashtags(savedTweet);
	        processMentions(savedTweet);

	        return tweetMapper.entityTpDto(savedTweet);
	    } else {
	        throw new NotAuthorizedException("Invalid credentials: User not found");
	    }
	}

	private void processMentions(Tweet savedTweet) {
	    String content = savedTweet.getContent();
	    List<User> mentionedUsers = new ArrayList<>();  // List to store mentioned users
	    
	    if (content.contains("@")) {
	        String[] words = content.split(" ");
	        
	        for (String word : words) {
	            if (word.startsWith("@")) {
	                String username = word.substring(1);  // Remove the '@' symbol
	                
	                User mentionedUser = userRepository.findByCredentialsUsername(username)
	                        .orElseGet(() -> {
	                          
	                        	throw new NotAuthorizedException("Invalid credentials: User not found");
	                        });
	                
	                mentionedUsers.add(mentionedUser);
	            }
	        }
	    }
	    
	    savedTweet.setMentionedUsers(mentionedUsers);
	    
	}

	private void processHashtags(Tweet savedTweet) {
		String content = savedTweet.getContent();
		List<Hashtag> hashtags = new ArrayList<>();
		if(content.contains("#")) {
			String[] words = content.split(" ");
			for (String word : words) {
				if(word.startsWith("#")){
					Hashtag hashtag = hashtagRepository.findByLabel(word)
		                    .orElseGet(() -> {
		                        Hashtag newHashtag = new Hashtag();
		                        newHashtag.setLabel(word);
		                        newHashtag.setFirstUsed(new Timestamp(System.currentTimeMillis()));
		                        return newHashtag;
		                    });
					hashtag.setLastUsed(new Timestamp(System.currentTimeMillis()));
					hashtags.add(hashtag);
				}
			}
			
		}
		savedTweet.setHashtags(hashtags);
	    hashtagRepository.saveAll(hashtags);
	    
		
	}

	@Override
	public TweetResponseDto getAllTweetByTweetId(Long id) {
		Tweet tweet = getNonDeletedTweetById(id);
	   return tweetMapper.entityTpDto(tweet);
	}

	@Override
	public void likeTweet(long id, CredentialsDto credentialsDto) {		
		validateCredentials(credentialsDto);

	    Optional<User> requestingUserOpt = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
	    User user = requestingUserOpt.get();
	    Tweet tweet = getNonDeletedTweetById(id);

	    if (tweet.getUserLikes().contains(user)) {
	        throw new BadRequestException("User has already liked this tweet.");
	    }
	    tweet.getUserLikes().add(user);
	    user.getLikedTweets().add(tweet);
	    
	    userRepository.save(user);
	    tweetRepository.save(tweet);
	}

	@Override
	public List<UserResponseDto> getTweetLikes(Long id) {
		Tweet tweet = getNonDeletedTweetById(id);
		List<User> activeUsers = tweet.getUserLikes().stream()
		        .filter(user -> !user.isDeleted()) 
		        .collect(Collectors.toList());
		
		return userMapper.entitiesToDtos(activeUsers);
	}

	@Override
	public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
		validateCredentials(credentialsDto);
	    Optional<User> author = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
	    User user = author.get();

	    Tweet originalTweet = getNonDeletedTweetById(id);

	    Tweet repostedTweet = new Tweet();
	    repostedTweet.setContent(originalTweet.getContent());
	    repostedTweet.setAuthor(user);
	    repostedTweet.setRepostOf(originalTweet);
	    repostedTweet.setPosted(new Timestamp(System.currentTimeMillis()));

	    Tweet savedRepostedTweet = tweetRepository.save(repostedTweet);  


	    return tweetMapper.entityTpDto(savedRepostedTweet);  
	}

	@Override
	public List<TweetResponseDto> getReposts(Long id) {
		Tweet tweet = getNonDeletedTweetById(id);
		List<Tweet> tweets = tweet.getReposts().stream()
		        .filter(user -> !user.isDeleted()) 
		        .collect(Collectors.toList());

		
        return tweetMapper.entitiesToDtos(tweets);
	}

	@Override
	public TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto) {
		Tweet originalTweet = getNonDeletedTweetById(id);
	    User user = getUserByCredentials(tweetRequestDto.getCredentials());
		
	    Tweet replyTweet = new Tweet();
		replyTweet.setContent(tweetRequestDto.getContent());
		replyTweet.setAuthor(user);
		replyTweet.setInReplyTo(originalTweet);
		replyTweet.setPosted(new Timestamp(System.currentTimeMillis()));

	    Tweet savedReplyTweet = tweetRepository.save(replyTweet); 
	    
	    processHashtags(savedReplyTweet);
        processMentions(savedReplyTweet);
        
	    return tweetMapper.entityTpDto(savedReplyTweet);  
	}

	
	@Override
	public List<TweetResponseDto> getRepliesFromId(Long id) {
		Tweet tweet = getNonDeletedTweetById(id);
		List<Tweet> tweets = tweet.getReplies().stream()
		        .filter(user -> !user.isDeleted()) 
		        .collect(Collectors.toList());

		
        return tweetMapper.entitiesToDtos(tweets);

	}

	@Override
	public ContextDto getContextFromId(Long id) {
		Tweet tweet = getNonDeletedTweetById(id);
		ContextDto contextDto = new ContextDto();
		 contextDto.setTarget(tweetMapper.entityTpDto(tweet));
		 List<Tweet> beforeReplies = new ArrayList<>();
		 List<Tweet> afterReplies = tweet.getReplies();
		 afterReplies.add(tweet); 
	     getAllTweetsAfter(tweet); 

	        afterReplies.sort(Comparator.comparing(Tweet::getPosted));

		Tweet inReplyTo = tweet.getInReplyTo();
		while(inReplyTo != null ) {
			if(!inReplyTo.isDeleted()) {
					beforeReplies.add(inReplyTo);
				}
			inReplyTo = inReplyTo.getInReplyTo();
			
		}
		
		contextDto.setBefore(tweetMapper.entitiesToDtos(beforeReplies));
		contextDto.setAfter(tweetMapper.entitiesToDtos(afterReplies));

		return contextDto;
	}

	// GET tweets/{id}/tags
	@Override
	public List<HashtagDto> getTweetHashtags(Long id) {

		Tweet tweet = getNonDeletedTweetById(id);

		if(tweet.isDeleted()) {
			throw new NotFoundException("Tweet does not exist.");
		}
		List<Hashtag> hashtags = tweet.getHashtags();

		return hashtagMapper.entitiesToDto(hashtags);
	}

	// tweets/{id}/mentions
	@Override
	public List<UserResponseDto> getTweetMentions(Long id) {
		Tweet tweet = getNonDeletedTweetById(id);

		if(tweet.isDeleted()) {
			throw new NotFoundException("Tweet does not exist.");
		}
		List <User> users = tweet.getMentionedUsers();

		return userMapper.entitiesToDtos(users);
	}

	// 	DELETE tweets/{id}
	@Override
	public TweetResponseDto deleteTweet(CredentialsDto credentialsDto, Long id) {

		if (credentialsDto == null || credentialsDto.getUsername() == null || credentialsDto.getPassword() == null) {
        throw new BadRequestException("Invalid credentials provided.");
     }

     Optional<Tweet> tweetToFind = tweetRepository.findById(id);
     if (tweetToFind.isEmpty()) {
        throw new NotFoundException("Tweet does not exist.");
     }

     Tweet tweet = tweetToFind.get();

     User author = tweet.getAuthor();

	 if (!author.getCredentials().getUsername().equals(credentialsDto.getUsername()) ||
	 	!author.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
		 throw new NotAuthorizedException("User is not authorized to delete this tweet.");
	 }

     if (!tweet.isDeleted()) {
        tweet.setDeleted(true);
        tweetRepository.save(tweet);
     }
     return tweetMapper.entityTpDto(tweet);

	}

}
