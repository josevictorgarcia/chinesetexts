package com.tesseract.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.Word;
import com.tesseract.demo.Repository.WordRepository;

@Service
public class DictionaryService {

    @Autowired
    private WordRepository wordRepository;

    public Word save(Word word){
        if(wordRepository.findByChinese(word.getChinese()).isPresent()){
            return null;
        } else{
            return wordRepository.save(word);
        }
    }

    public Word addEnglishTranslation(Word word, Word newWord){
        Optional<Word> optional = wordRepository.findByChinese(word.getChinese());
        if(optional.isPresent()){
            optional.get().setEnglish(newWord.getEnglish());
            return wordRepository.save(optional.get());
        } else {
            return null;
        }
    }

    public Word addSpanishTranslation(Word word, Word newWord){
        Optional<Word> optional = wordRepository.findByChinese(word.getChinese());
        if(optional.isPresent()){
            optional.get().setSpanish(newWord.getSpanish());
            return wordRepository.save(optional.get());
        } else {
            return null;
        }
    }

    public Word putWord(Word word, Word newWord){
        Optional<Word> optional = wordRepository.findByChinese(word.getChinese());
        if(optional.isPresent()){
            optional.get().copy(newWord);
            return wordRepository.save(optional.get());
        } else {
            return null;
        }
    }

    public List<String> translateToEnglish(List<String> chineseText){
        List<String> translatedText = new ArrayList<>();
        for(String word : chineseText){
            Optional<Word> optional = wordRepository.findByChinese(word);
            if(optional.isPresent()){
                translatedText.add(optional.get().getEnglish());
            } else {
                translatedText.add("");
            }
        }
        return translatedText;
    }

    public List<String> translateToSpanish(List<String> chineseText){
        List<String> translatedText = new ArrayList<>();
        for(String word : chineseText){
            Optional<Word> optional = wordRepository.findByChinese(word);
            if(optional.isPresent()){
                translatedText.add(optional.get().getSpanish());
            } else {
                translatedText.add("");
            }
        }
        return translatedText;
    }
}
