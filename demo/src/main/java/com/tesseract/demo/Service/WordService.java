package com.tesseract.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
