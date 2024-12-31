package com.cooksys.spring_assessment_social_media_nov_2024_team_2.controllers;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.UserService;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

    private final ValidateService validateService;

    // GET validate/tag/exists/{label}
    // Checks whether or not a given hashtag exists.
    @GetMapping("/tag/exists/{label}")
    public boolean tagExists(@PathVariable String label) {
        return validateService.tagExists(label);
    }

    // GET validate/username/exists/@{username}
    // Checks whether or not a given username exists.
    @GetMapping("/username/exists/@{username}")
    public boolean usernameExists(@PathVariable String username) {
        return validateService.usernameExists(username);
    }

    // GET validate/username/available/@{username}
    // Checks whether or not a given username is available.
    @GetMapping("username/available/@{username}")
    public boolean usernameAvailable(@PathVariable String username) {
        return validateService.usernameAvailable(username);
    }

}
