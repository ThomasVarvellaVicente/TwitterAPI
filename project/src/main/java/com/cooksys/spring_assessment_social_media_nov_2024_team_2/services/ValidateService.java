package com.cooksys.spring_assessment_social_media_nov_2024_team_2.services;

import org.springframework.stereotype.Service;

public interface ValidateService {

    boolean tagExists(String label);

    boolean usernameExists(String username);

    boolean usernameAvailable(String username);
}
