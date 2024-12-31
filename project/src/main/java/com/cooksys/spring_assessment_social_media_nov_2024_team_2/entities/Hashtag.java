package com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@NoArgsConstructor
@Data

public class Hashtag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String label;

    @CreationTimestamp
    private Timestamp firstUsed;

    @CreationTimestamp
    private Timestamp lastUsed;

    @ManyToMany(mappedBy="hashtags")
    private List<Tweet> tweets;

}
