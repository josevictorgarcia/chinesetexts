package com.tesseract.demo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.Flashcard;
import com.tesseract.demo.Model.Text;
import com.tesseract.demo.Model.Word;
import com.tesseract.demo.Model.Collection;
import com.tesseract.demo.Repository.FlashcardRepository;
import com.tesseract.demo.dto.CollectionDTO;
import com.tesseract.demo.dto.CollectionMapper;
import com.tesseract.demo.dto.FlashcardDTO;
import com.tesseract.demo.dto.TextDTO;
import com.tesseract.demo.dto.TextMapper;
import com.tesseract.demo.dto.WordDTO;
import com.tesseract.demo.dto.WordMapper;

@Service
public class FlashcardService {
    
    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private TextMapper textMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    public Flashcard save(Flashcard flashcard){
        return flashcardRepository.save(flashcard);
    }

    public FlashcardDTO[] getFlashcards(long id) {
        return flashcardRepository.findByCollectionId(id)
            .stream()
            .map(flashcard -> new FlashcardDTO(
                flashcard.getId(),
                toDTO(flashcard.getWord()),
                toDTO(flashcard.getExample()),
                toDTO(flashcard.getCollection())
            ))
            .toArray(FlashcardDTO[]::new);  // ← Convierte List a FlashcardDTO[]
    }

    private WordDTO toDTO(Word word){
        return wordMapper.toDTO(word);
    }

    private TextDTO toDTO(Text text){
        return textMapper.toDTO(text);
    }

    private CollectionDTO toDTO(Collection collection){
        return collectionMapper.toDTO(collection);
    }
}
