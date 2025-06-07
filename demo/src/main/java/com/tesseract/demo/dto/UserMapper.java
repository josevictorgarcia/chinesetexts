package com.tesseract.demo.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.tesseract.demo.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    List<UserDTO> toDTO(List<User> users);

    User toDomain(UserDTO userDTO);

    List<User> toDomain(List<UserDTO> users);
}