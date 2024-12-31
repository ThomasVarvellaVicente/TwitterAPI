package com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Credentials credentials;

    @Embedded
    private Profile profile;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp joined;

    @Column(nullable = false)
    private boolean deleted;

    @ManyToMany
    @JoinTable(
            name = "followers_following"
    )
    private List<User> following;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> likedTweets;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

    @ManyToMany(mappedBy = "mentionedUsers")
    private List<Tweet> tweetMentions;


}
