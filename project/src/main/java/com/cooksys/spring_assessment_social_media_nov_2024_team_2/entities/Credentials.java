package com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Credentials {
    private String username;
    private String password;
}
