package com.apapedia.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.dto.response.CreateUserResponseDTO;
import com.apapedia.user.model.*;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    UserModel updateUserRequestDTOToUser(UpdateUserRequestDTO updateUserRequestDTO);

    @Mapping(target = "role", ignore = true)
    UpdateUserRequestDTO userToUpdateUserRequestDTO(UserModel user);

    UserModel findByUsername(String username);

    @Mapping(target = "role", ignore = true)
    UserModel createUserRequestDTOToUserModel(CreateUserRequestDTO createUserRequestDTO); 

    @Mapping(target = "role", ignore = true)
    CreateUserResponseDTO createUserResponseDTOToUserModel(UserModel userModel);
}
