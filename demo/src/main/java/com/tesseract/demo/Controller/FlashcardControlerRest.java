package com.tesseract.demo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tesseract.demo.Model.Collection;
import com.tesseract.demo.Model.Flashcard;
import com.tesseract.demo.Model.Text;
import com.tesseract.demo.Model.Word;
import com.tesseract.demo.Service.CollectionService;
import com.tesseract.demo.Service.DictionaryService;
import com.tesseract.demo.Service.FlashcardService;
import com.tesseract.demo.Service.TextService;
import com.tesseract.demo.dto.CollectionDTO;
import com.tesseract.demo.dto.FlashcardDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/flashcards")
public class FlashcardControlerRest {

    @Autowired
    CollectionService collectionService;

    @Autowired
    FlashcardService flashcardService;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    TextService textService;

    @GetMapping("/")
    public List<CollectionDTO> getCollections() {
        return collectionService.getAllCollections();
    }
    
    @GetMapping("/{id}")
    public FlashcardDTO[] getFlashcards(@PathVariable long id) {
        return flashcardService.getFlashcards(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putWordToCollection(@PathVariable long id, @RequestParam String chinese, @RequestParam long textId) {
        Word word = dictionaryService.getWord(chinese);
        Text example = textService.findText(textId);
        Collection collection = collectionService.getCollection(id);
        if(word != null && example != null && collection != null){
            Flashcard f = new Flashcard(word, example, collection);
            flashcardService.save(f);
            return ResponseEntity.ok().body("Flashcard añadida con éxito");
        }
        return ResponseEntity.badRequest().body("No existe la palabra o el texto o la colección");
    }
    
}
