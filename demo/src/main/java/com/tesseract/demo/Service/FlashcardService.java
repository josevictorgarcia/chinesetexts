package com.tesseract.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.Flashcard;
import com.tesseract.demo.Repository.FlashcardRepository;

@Service
public class FlashcardService {
    
    @Autowired
    private FlashcardRepository flashcardRepository;

    public Flashcard save(Flashcard flashcard){
        return flashcardRepository.save(flashcard);
    }
}
