package com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {

    // Need this because RuntimeException is serializable
    @Serial
    private static final long serialVersionUID = -1366201883544859517L;

    private String message;
}
