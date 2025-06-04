package com.tesseract.demo.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.tesseract.demo.Model.Word;

@Mapper(componentModel = "spring")
public interface WordMapper {

    WordDTO toDTO(Word word);

    List<WordDTO> toDTO(List<Word> words);

    Word toDomain(WordDTO wordDTO);

    List<Word> toDomain(List<WordDTO> words);
}

