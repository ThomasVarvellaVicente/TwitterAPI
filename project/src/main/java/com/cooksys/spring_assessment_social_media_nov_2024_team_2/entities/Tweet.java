package com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
@Data
public class Tweet {
	
	@Id
	@GeneratedValue
	private Long Id;
	
	@ManyToOne
    private User author;

	@CreationTimestamp
	private Timestamp posted;
	
	private boolean deleted = false;
	
	private String content;
	
	@ManyToOne
	private Tweet repostOf;

	@OneToMany(mappedBy="repostOf")
    private List<Tweet> reposts;

	//make sure to map from the tweet entity
	@ManyToMany
	@JoinTable(
			name = "user_mentions",
			joinColumns = @JoinColumn(name = "tweet_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> mentionedUsers;
	

	//replicate for reposts
	@ManyToOne
	private Tweet inReplyTo;

	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies;

	
	@ManyToMany
	@JoinTable
	(
			name = "tweet_hashtag",
			joinColumns = @JoinColumn(name = "tweet_id"),
			inverseJoinColumns = @JoinColumn(name = "hashtag_id")
	)
	private List<Hashtag> hashtags;

	@ManyToMany(mappedBy="likedTweets")
    private List<User> userLikes;

	
}
