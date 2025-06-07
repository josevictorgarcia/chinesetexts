package com.tesseract.demo.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.tesseract.demo.Model.User;

@Mapper(componentModel = "spring")
public interface UserWithPasswordMapper {

    UserWithPasswordDTO toDTO(User user);

    List<UserDTO> toDTO(List<User> user);

    User toDomain(UserWithPasswordDTO userWithPasswordDTO);

    List<User> toDomain(List<UserWithPasswordDTO> users);
}