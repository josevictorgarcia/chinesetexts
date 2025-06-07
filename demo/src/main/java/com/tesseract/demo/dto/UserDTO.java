package com.tesseract.demo.dto;

import java.util.List;

public record UserDTO (
    Long id,
    String email,
    String name,
    String language,
    List<CollectionDTO> collections,
    List<String> roles){
}
