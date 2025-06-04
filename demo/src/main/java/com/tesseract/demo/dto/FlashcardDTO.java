package com.tesseract.demo.dto;

public record FlashcardDTO(
    Long id,
    WordDTO word,
    TextDTO example) {
}
