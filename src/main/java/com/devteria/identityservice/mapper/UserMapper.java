package com.devteria.identityservice.mapper;

import com.devteria.identityservice.dto.request.UserCreateReq;
import com.devteria.identityservice.dto.request.UserUpdateReq;
import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User addUser(UserCreateReq userCreateReq);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateReq userUpdateReq);

   // @Mapping(target = "dateOfBirth", source = "firstName", ignore = true)  target cái bị thay đổi, source cái gốc, ignore source
   UserResponse userToUserResponse(User user);
}
