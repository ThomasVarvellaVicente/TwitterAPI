package com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class Profile {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;


}
