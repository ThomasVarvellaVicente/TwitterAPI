package com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Hashtag;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository <Hashtag, Long> {
	Optional <Hashtag> findByLabel(String label);
}