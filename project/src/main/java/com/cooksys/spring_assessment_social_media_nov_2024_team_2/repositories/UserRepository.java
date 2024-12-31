package com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByCredentialsUsername(String username);

    List<User> findByDeletedFalse();
}
