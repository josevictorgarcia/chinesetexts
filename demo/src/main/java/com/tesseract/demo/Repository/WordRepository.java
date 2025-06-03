package com.tesseract.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tesseract.demo.Model.Word;

public interface WordRepository extends JpaRepository<Word, Long>{
    Optional<Word> findById(long id);
    Optional<Word> findByChinese(String chinese);
    boolean existsByChinese(String chinese);
}
