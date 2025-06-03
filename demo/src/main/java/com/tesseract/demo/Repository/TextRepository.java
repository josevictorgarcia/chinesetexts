package com.tesseract.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tesseract.demo.Model.Text;

public interface TextRepository extends JpaRepository<Text, Long>{
    Optional<Text> findById(long id);
    Optional<Text> findByTitleSpanish(String titleSpanish);
    Optional<Text> findByTitleEnglish(String titleEnglish);
    List<Text> findAllByOrderByCreationDateDesc();
}
