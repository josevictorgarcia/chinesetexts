package com.tesseract.demo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesseract.demo.Service.CollectionService;
import com.tesseract.demo.Service.FlashcardService;
import com.tesseract.demo.dto.CollectionDTO;
import com.tesseract.demo.dto.FlashcardDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/flashcards")
public class FlashcardControlerRest {

    @Autowired
    CollectionService collectionService;

    @Autowired
    FlashcardService flashcardService;

    @GetMapping("/")
    public List<CollectionDTO> getCollections() {
        return collectionService.getAllCollections();
    }
    
    @GetMapping("/{id}")
    public FlashcardDTO[] getFlashcards(@PathVariable long id) {
        return flashcardService.getFlashcards(id);
    }
    
}
