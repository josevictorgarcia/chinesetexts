package com.tesseract.demo.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tesseract.demo.Service.TextService;
import com.tesseract.demo.dto.TextDTO;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/texts")
public class TextControllerRest {

    @Autowired
    private TextService textService;
    
    @GetMapping("/")
    public List<TextDTO> getTexts() {
        return textService.getTextsOrderByCreationDateDesc();
    }

    @GetMapping("/{id}")
    public TextDTO getText(@PathVariable long id) {
        return textService.getText(id);
    }

    @PostMapping("/")
    public ResponseEntity<TextDTO> postText(@RequestBody TextDTO text) {    //Ver que pasa con el id y la imagen
        TextDTO newText = textService.save(text);
        if(newText == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newText.id()).toUri();
        return ResponseEntity.created(location).body(newText);
    }

    @PostMapping("/{id}/image")
	public ResponseEntity<Object> createUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {
		textService.createTextImage(id, imageFile.getInputStream(), imageFile.getSize());
        URI location = fromCurrentRequest().build().toUri();
		return ResponseEntity.created(location).build();
	}

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getProfileImage(@PathVariable long id) throws SQLException {
        Resource profileImage = textService.getTextImage(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(profileImage);
    }

    @GetMapping("/{id}/SpanishText")
    public ResponseEntity<String[][]> getTextSpanish(@PathVariable long id){
        TextDTO text = this.textService.getText(id);
        if (text == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            String [][] result = textService.getTextSpanish(text);
            // Devolver el Map con el mapeo
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    /*@GetMapping("/{id}/EnglishText")
    public ResponseEntity<String[][]> getTextEnglish(@PathVariable long id){
        Text text = this.textService.getText(id);
        if (text == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            List<String> textSegmented = jiebaService.segment(text.getText());
            List<String> words = dictionaryService.translateToEnglish(textSegmented);

            String[] chineseArray = textSegmented.toArray(new String[0]);
            String[] englishArray = words.toArray(new String[0]);

            String[][] result = new String[2][];
            result[0] = chineseArray;
            result[1] = englishArray;

            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }*/

    /*@GetMapping("/{id}/translation")
    public ResponseEntity<Map<String, String>> getTranslation(@PathVariable long id) {
        Text text = this.textService.getText(id);
        
        if (text == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            String originalText = text.getText();
            String translatedText = text.getTranslation();
            
            String[] originalSentences = originalText.split("\\。");
            String[] translatedSentences = translatedText.split("\\.");
            
            Map<String, String> map = new LinkedHashMap<>();
            
            int length = Math.min(originalSentences.length, translatedSentences.length);
            
            for (int i = 0; i < length; i++) {
                if (!originalSentences[i].isEmpty() && !translatedSentences[i].isEmpty()) {
                    map.put(originalSentences[i], translatedSentences[i]); // Guardamos la relación en el Map
                }
            }
            
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
    }*/    
    
}

/*
 * Situandonos en branchVictor, subimos nuestros cambios a nuestra rama:
 * git add .
 * git commit -m feature-1
 * git push origin branchVictor
 * Ahora juntamos nuestros cambios con los de la rama main del repositorio:
 * git fetch origin
 * git pull origin main
 * git checkout branchVictor
 * git merge main
 * git push -u origin branchVictor
 */