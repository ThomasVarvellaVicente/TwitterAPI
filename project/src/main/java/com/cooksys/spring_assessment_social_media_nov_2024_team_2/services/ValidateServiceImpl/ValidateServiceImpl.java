package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.ValidateServiceImpl;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Hashtag;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.repositories.HashtagRepository;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.UserService;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final HashtagRepository hashtagRepository;
    private final UserService userService;

    // GET validate/tag/exists/{label}
    // Checks whether or not a given hashtag exists.
    @Override
    public boolean tagExists(String label) {
        Optional<Hashtag> hashtagToFind = hashtagRepository.findByLabel("#" + label);
        return hashtagToFind.isPresent();
    }

    // GET validate/username/exists/@{username}
    // Checks whether or not a given username exists.
    @Override
    public boolean usernameExists(String username) {
        return userService.getUser(username) != null;
    }

    // GET validate/username/available/@{username}
    // Checks whether or not a given username is available.
    @Override
    public boolean usernameAvailable(String username ) {
        return userService.getUser(username) == null;
    }

}