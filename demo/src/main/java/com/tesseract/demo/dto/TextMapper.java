package com.tesseract.demo.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tesseract.demo.Model.Text;

@Mapper(componentModel = "spring")
public interface TextMapper {

    TextDTO toDTO(Text text);

    List<TextDTO> toDTO(List<Text> texts);

    @Mapping(target = "image", ignore = true)

    Text toDomain(TextDTO textDTO);
}
