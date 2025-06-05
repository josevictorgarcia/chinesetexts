package com.tesseract.demo.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.tesseract.demo.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User word);

    List<UserDTO> toDTO(List<User> words);

    User toDomain(UserDTO userDTO);

    List<User> toDomain(List<UserDTO> users);
}