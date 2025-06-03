package com.tesseract.demo.Service;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.Text;
import com.tesseract.demo.Model.Word;
import com.tesseract.demo.Repository.TextRepository;
import com.tesseract.demo.Repository.WordRepository;
import com.tesseract.demo.dto.TextDTO;
import com.tesseract.demo.dto.TextMapper;

@Service
public class TextService {
    
    @Autowired
    private TextRepository textRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private JiebaService jiebaService;

    @Autowired
    private TextMapper textMapper;

    public TextDTO save(Text text){     //Este metodo es solo para inicializar los datos (toma Text directamente)
        if(textRepository.findByTitleSpanish(text.getTitleEnglish()).isPresent() || textRepository.findByTitleSpanish(text.getTitleSpanish()).isPresent()){
            return null;
        } else{
            return toDTO(textRepository.save(text));
        }
    }

    public TextDTO save(TextDTO text){  //Este metodo es el que se llama al enviar el formulario (con DTOs)
        Text newText = toDomain(text);
        newText.setCreationDate(LocalDate.now());
        if(textRepository.findByTitleSpanish(newText.getTitleEnglish()).isPresent() || textRepository.findByTitleSpanish(newText.getTitleSpanish()).isPresent()){
            return null;
        } else{
            return toDTO(textRepository.save(newText));
        }
    }

    public Word save(Word word){
        if(wordRepository.existsByChinese(word.getChinese())){
            return null;
        } else{
            return wordRepository.save(word);
        }
    }

    public Word[] save(Word[] words){
        List<Word> newWords = new ArrayList<>();
        for(Word word : words){
            newWords.add(save(word));
        }
        return newWords.toArray(new Word[0]);
    }

    public void createTextImage(long id, InputStream inputStream, long size) {
        Text text = textRepository.findById(id).orElseThrow();
        text.setImage(BlobProxy.generateProxy(inputStream, size));
        textRepository.save(text);
    }

    public TextDTO getText(long id){
        Optional<Text> text = this.textRepository.findById(id);
        if(text.isPresent()){
            return toDTO(text.get());
        } else {
            return null;
        }
    }

    public List<TextDTO> getTextsOrderByCreationDateDesc(){
        return toDTOs(textRepository.findAllByOrderByCreationDateDesc());
    }

    public Resource getTextImage(Long textId) {
        Text text = textRepository.findById(textId).orElseThrow();
        try {
            return new InputStreamResource(text.getImage().getBinaryStream());
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user image", e);
        }
    }

    public Word[] getPendingWords(TextDTO text) {
        List<String> textSegmented = jiebaService.segment(text.text());
        List<Word> pendingWords = new ArrayList<>();

        for (String word : textSegmented) {
            if (word != null && !word.trim().isEmpty() && !wordRepository.existsByChinese(word)) {
                pendingWords.add(new Word(word, null, null, null));
            }
        }
        return pendingWords.toArray(new Word[0]);
    }


    public String[][] getTextSpanish(TextDTO text){
        List<String> textSegmented = jiebaService.segment(text.text());
        List<String> words = dictionaryService.translateToSpanish(textSegmented);

        String[] chineseArray = textSegmented.toArray(new String[0]);
        String[] spanishArray = words.toArray(new String[0]);

        String[][] result = new String[2][];
        result[0] = chineseArray;
        result[1] = spanishArray;
        return result;
    }

    private TextDTO toDTO(Text text){
        return textMapper.toDTO(text);
    }

    private Text toDomain(TextDTO textDTO){
        return textMapper.toDomain(textDTO);
    }

    private List<TextDTO> toDTOs(List<Text> texts) {
        return textMapper.toDTO(texts);
    }
}
