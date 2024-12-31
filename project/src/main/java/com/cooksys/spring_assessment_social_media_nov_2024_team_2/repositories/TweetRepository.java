package com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Tweet;
//Note to david, double check the repo import
@Repository
public interface TweetRepository extends JpaRepository <Tweet, Long>{

	List<Tweet> findByDeletedFalseOrderByPostedDesc();

	Optional<Tweet> findByIdAndDeletedFalse(Long id);

	List<Tweet> findAllByInReplyToAndDeletedFalse(Tweet tweet);

}
