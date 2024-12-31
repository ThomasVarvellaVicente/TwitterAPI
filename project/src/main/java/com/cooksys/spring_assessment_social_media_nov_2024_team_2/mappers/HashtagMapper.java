package com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers;

import org.mapstruct.Mapper;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Hashtag;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.HashtagDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    HashtagDto entityToDto(Hashtag hashtag);

    List<HashtagDto> entitiesToDto(List<Hashtag> hashtags);

}