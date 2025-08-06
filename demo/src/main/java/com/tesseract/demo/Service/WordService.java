package com.tesseract.demo.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesseract.demo.Model.Word;
import com.tesseract.demo.Repository.WordRepository;
import com.tesseract.demo.dto.TextDTO;
import com.tesseract.demo.dto.WordDTO;
import com.tesseract.demo.dto.WordMapper;

@Service
public class WordService {

    @Autowired
    private JiebaService jiebaService;
    
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private WordMapper wordMapper;

    public WordDTO save(WordDTO word){
        Word newWord = toDomain(word);
        if(wordRepository.existsByChinese(newWord.getChinese())){
            return null;
        } else{
            return toDTO(wordRepository.save(newWord));
        }
    }

    public WordDTO[] save(WordDTO[] words){
        List<WordDTO> newWords = new ArrayList<>();
        for(WordDTO word : words){
            newWords.add(save(word));
        }
        return newWords.toArray(new WordDTO[0]);
    }

    public WordDTO[] getPendingWords(TextDTO text) {
        List<String> textSegmented = jiebaService.segment(text.text());
        List<WordDTO> pendingWords = new ArrayList<>();

        for (String word : textSegmented) {
            if (word != null && !word.trim().isEmpty() && !wordRepository.existsByChinese(word)) {
                pendingWords.add(toDTO(new Word(word, null, null, null)));
            }
        }
        return pendingWords.toArray(new WordDTO[0]);
    }

    private String[] parseJsonArray(String jsonArrayString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonArrayString, String[].class);
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public List<WordDTO> saveWords(List<String> words) {
        List<WordDTO> savedWords = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> jsonBody = new HashMap<>();
        jsonBody.put("text", words); // ✅ Pasamos la lista como objeto, no como string

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonBody, headers);

        String baseUrl = "http://localhost:5001";

        // Llamar a cada endpoint para obtener los arrays completos
        ResponseEntity<String> responseEnglish = restTemplate.postForEntity(baseUrl + "/getWordsEnglish", request, String.class);
        ResponseEntity<String> responseSpanish = restTemplate.postForEntity(baseUrl + "/getWordsSpanish", request, String.class);
        ResponseEntity<String> responsePinyin = restTemplate.postForEntity(baseUrl + "/getWordsPinyin", request, String.class);

        // Parsear las respuestas (que son JSON array strings) a String[]
        String[] arrayEnglish = parseJsonArray(responseEnglish.getBody());
        String[] arraySpanish = parseJsonArray(responseSpanish.getBody());
        String[] arrayPinyin = parseJsonArray(responsePinyin.getBody());

        for (int i = 0; i < words.size(); i++) {
            String chineseWord = words.get(i);
            if (chineseWord != null && !chineseWord.trim().isEmpty() && !wordRepository.existsByChinese(chineseWord)) {
                String english = i < arrayEnglish.length ? arrayEnglish[i] : null;
                String spanish = i < arraySpanish.length ? arraySpanish[i] : null;
                String pinyin = i < arrayPinyin.length ? arrayPinyin[i] : null;

                Word newWord = new Word(chineseWord, pinyin, english, spanish);
                Word saved = wordRepository.save(newWord);
                savedWords.add(toDTO(saved));
            }
        }

        return savedWords;
    }

    public WordDTO getWord(String chinese) {
        Optional<Word> word = wordRepository.findByChinese(chinese);
        if (word.isPresent()) {
            return toDTO(word.get());
        } else {
            return null;
        }
    }

    public WordDTO updateOrSave(WordDTO wordDTO) {
        Optional<Word> word = wordRepository.findByChinese(wordDTO.chinese());
        if (word.isPresent()) {
            if(!wordDTO.pinyin().isEmpty()){
                word.get().setPinyin(wordDTO.pinyin());
            }
            if(!wordDTO.english().isEmpty()){
                word.get().setEnglish(wordDTO.english());
            }
            if(!wordDTO.spanish().isEmpty()){
                word.get().setSpanish(wordDTO.spanish());
            }
            return toDTO(wordRepository.save(word.get()));
        } else {
            return save(wordDTO);
        }
    }

    private WordDTO toDTO(Word word){
        return wordMapper.toDTO(word);
    }

    private Word toDomain(WordDTO wordDTO){
        return wordMapper.toDomain(wordDTO);
    }

}
