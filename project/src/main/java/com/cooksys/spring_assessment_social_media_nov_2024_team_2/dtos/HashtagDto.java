package com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@NoArgsConstructor
@Data
public class HashtagDto {

    private String label;
    private Timestamp firstUsed;
    private Timestamp lastUsed;

}