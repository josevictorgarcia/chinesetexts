package com.tesseract.demo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesseract.demo.Service.CollectionService;
import com.tesseract.demo.dto.CollectionDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/flashcards")
public class FlashcardControlerRest {

    @Autowired
    CollectionService collectionService;

    @GetMapping("/")
    public List<CollectionDTO> getCollections() {
        return collectionService.getAllCollections();
    }
    
}
