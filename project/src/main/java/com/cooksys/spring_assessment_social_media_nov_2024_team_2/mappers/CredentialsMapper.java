package com.cooksys.spring_assessment_social_media_nov_2024_team_2.mappers;

import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.CredentialsDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.dtos.CredentialsResponseDto;
import com.cooksys.spring_assessment_social_media_nov_2024_team_2.entities.Credentials;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    CredentialsResponseDto entityToDto(Credentials entity);
    List<CredentialsResponseDto> entitiesToDtos(List<Credentials> entities);

    Credentials dtoToEntity(CredentialsDto credentialsDto);
    List<Credentials> dtosToEntities(List<CredentialsDto> credentialDtos);


}
