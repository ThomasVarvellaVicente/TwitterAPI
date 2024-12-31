package com.cooksys.spring_assessment_social_media_nov_2024_team_2.controllers.advice;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.ErrorDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.BadRequestException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotAuthorizedException;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// If an exception is thrown in one of the controllers
// ControllerAdvice will try to handle that exception if we define a method for it

// *NOTE TO EVERYONE*

// When we add things into the Controllers, we don't need to return new responseEntities
// See Creating & Handling Custom Exceptions about 14 mins in for syntax
// Instead of something like "return new ResponseEntity<>(HttpStatus.NOT_FOUND),
// throw new BadRequestException("All fields required for creating a new user");


@ControllerAdvice(basePackages = { "com.cooksys.spring_assessment_social_media_nov_2024_team_2.controllers" })
@ResponseBody
public class SocialMediaControllerAdvice {

    // BadRequest Exception
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException) {
        return new ErrorDto(badRequestException.getMessage());
    }

    // NotFound Exception
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException notFoundException) {
        return new ErrorDto(notFoundException.getMessage());
    }

    // NotAuthorized Exception
    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotAuthorizedException notAuthorizedException) {
        return new ErrorDto(notAuthorizedException.getMessage());
    }

}
