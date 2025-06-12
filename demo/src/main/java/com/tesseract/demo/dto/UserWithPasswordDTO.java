package com.tesseract.demo.dto;

import java.util.List;

public record UserWithPasswordDTO (
    Long id,
    String email,
    String name,
    String language,
    List<CollectionDTO> collections,
    List<String> roles,
    String password,
    String newPassword){
}
