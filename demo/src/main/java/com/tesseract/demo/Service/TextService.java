package com.tesseract.demo.Service;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.Text;
import com.tesseract.demo.Repository.TextRepository;
import com.tesseract.demo.dto.TextDTO;
import com.tesseract.demo.dto.TextMapper;

@Service
public class TextService {
    
    @Autowired
    private TextRepository textRepository;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private JiebaService jiebaService;

    @Autowired
    private TextMapper textMapper;

    public TextDTO save(Text text){     //Este metodo es solo para inicializar los datos (toma Text directamente)
        if(textRepository.findByTitleEnglish(text.getTitleEnglish()).isPresent() || textRepository.findByTitleSpanish(text.getTitleSpanish()).isPresent()){
            return null;
        } else{
            return toDTO(textRepository.save(text));
        }
    }

    public TextDTO save(TextDTO text){  //Este metodo es el que se llama al enviar el formulario (con DTOs)
        Text newText = toDomain(text);
        newText.setCreationDate(LocalDate.now());
        if(textRepository.findByTitleEnglish(newText.getTitleEnglish()).isPresent() || textRepository.findByTitleSpanish(newText.getTitleSpanish()).isPresent()){
            return null;
        } else{
            return toDTO(textRepository.save(newText));
        }
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

    public Text findText(long id){
        Optional<Text> text = this.textRepository.findById(id);
        if(text.isPresent()){
            return text.get();
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

    public String[][] getTextEnglish(TextDTO text){
        List<String> textSegmented = jiebaService.segment(text.text());
        List<String> words = dictionaryService.translateToEnglish(textSegmented);

        String[] chineseArray = textSegmented.toArray(new String[0]);
        String[] englishArray = words.toArray(new String[0]);

        String[][] result = new String[2][];
        result[0] = chineseArray;
        result[1] = englishArray;
        return result;
    }

    public boolean deleteText(long id) {
        if (textRepository.existsById(id)) {
            textRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
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
