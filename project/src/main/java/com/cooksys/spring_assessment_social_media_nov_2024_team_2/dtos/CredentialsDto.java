package com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;

@NoArgsConstructor
@Data
public class CredentialsDto {
    private String username;
    private String password;
}
