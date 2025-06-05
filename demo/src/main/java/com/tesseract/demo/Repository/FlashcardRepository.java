package com.tesseract.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tesseract.demo.Model.Flashcard;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long>{
    List<Flashcard> findByCollectionId(Long collectionId);
}
