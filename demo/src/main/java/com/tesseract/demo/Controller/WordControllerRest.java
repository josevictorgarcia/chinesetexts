package com.tesseract.demo.Controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tesseract.demo.Model.Word;
import com.tesseract.demo.Service.DictionaryService;
import com.tesseract.demo.Service.WordService;
import com.tesseract.demo.dto.TextDTO;
import com.tesseract.demo.dto.WordDTO;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/words")
public class WordControllerRest {

    @Autowired
    private WordService wordService;

    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/pendingWords")
    public ResponseEntity<WordDTO[]> getPendingWords(@RequestParam String text) {
        TextDTO textDTO = new TextDTO(null, null, null, text, null, null, null, null, null, null);
        WordDTO [] pendingWords = wordService.getPendingWords(textDTO);
        return ResponseEntity.status(HttpStatus.OK).body(pendingWords);
    }

    @PostMapping("/createdWords")
    public ResponseEntity<WordDTO[]> createWords(@RequestBody WordDTO[] words) {
        if (words == null || words.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        WordDTO[] savedWords = wordService.save(words);

        // Filtramos los que realmente se guardaron (no fueron null)
        List<WordDTO> nonNullWords = Arrays.stream(savedWords)
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());

        if (nonNullWords.isEmpty()) {
            // Ninguna palabra fue nueva (todas duplicadas)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new WordDTO[0]);
        }

        // Opcional: podrías construir un Location URI si lo necesitas
        // Aquí devolvemos solo el array de palabras guardadas
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(nonNullWords.toArray(new WordDTO[0]));
    }

    @PostMapping("/words")          //Hacer un controlador a parte para las words.
    public ResponseEntity<Word> postWord(@RequestBody Word word) {
        Word newWord = dictionaryService.save(word);
        if(newWord == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newWord.getId()).toUri();
        return ResponseEntity.created(location).body(newWord);
    }

    @GetMapping("/textWords")
    public ResponseEntity<WordDTO[]> getTextWords(@RequestParam String text) {
        String[] originalTextArray = text.split(",");
        WordDTO[] pinyinResults = new WordDTO[originalTextArray.length];

        for (int i = 0; i < originalTextArray.length; i++) {
            pinyinResults[i] = wordService.getWord(originalTextArray[i]);
        }
        
        // Devolver el array de resultados
        return ResponseEntity.status(HttpStatus.OK).body(pinyinResults);
    }
    
    @PutMapping("/edit")
    public ResponseEntity<WordDTO> editWord(@RequestBody WordDTO word) {
        WordDTO updatedWord = wordService.updateOrSave(word);
        if (updatedWord == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updatedWord);
    }

}
