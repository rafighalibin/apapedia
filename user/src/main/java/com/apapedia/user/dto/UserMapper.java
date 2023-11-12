package com.apapedia.user.dto;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.*;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User createUserRequestDTOToUser(CreateUserRequestDTO createUserRequestDTO);

    User updateUserRequestDTOToUser(UpdateUserRequestDTO updateUserRequestDTO);

    UpdateUserRequestDTO userToUpdateUserRequestDTO(User user);
}
