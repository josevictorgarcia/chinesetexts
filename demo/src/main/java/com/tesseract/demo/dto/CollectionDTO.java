package com.tesseract.demo.dto;

import java.time.LocalDate;
import java.util.List;

public record CollectionDTO(
    Long id,
    String title,
    LocalDate date,
    List<FlashcardDTO> flashcards) {
}
