package com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
