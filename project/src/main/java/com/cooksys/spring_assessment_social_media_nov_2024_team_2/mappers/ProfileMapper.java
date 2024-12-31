package com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.ProfileDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.ProfileResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Profile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileResponseDto entityToDto(Profile entity);
    List<ProfileResponseDto> entitiesToDtos(List<Profile> profiles);

    Profile dtoToEntity(ProfileDto dto);
    List<Profile> dtosToEntities(List<ProfileDto> profileDtos);
}