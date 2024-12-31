package com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException {

    // Need this because RuntimeException is serializable
    @Serial
    private static final long serialVersionUID = 7330599768013662765L;

    private String message;

}
