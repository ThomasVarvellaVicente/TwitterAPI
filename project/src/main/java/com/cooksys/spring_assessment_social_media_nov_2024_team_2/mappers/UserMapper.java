package com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.UserRequestDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.UserResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CredentialsMapper.class, ProfileMapper.class})
public interface UserMapper {

    @Mapping(target="username", source="credentials.username")
    UserResponseDto entityToDto(User entity);

    List<UserResponseDto> entitiesToDtos(List<User> enitites);

    User dtoToEntity(UserRequestDto userRequestDto);

    List<User> dtoToEntities(List<UserRequestDto> userRequestDtos);
}
