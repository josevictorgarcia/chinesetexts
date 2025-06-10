package com.tesseract.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tesseract.demo.Model.Collection;
import com.tesseract.demo.Model.User;

public interface CollectionRepository extends JpaRepository<Collection, Long>{
    Optional<Collection> findById(Long id);
    List<Collection> findAllByOrderByDateDesc();
    List<Collection> findByUserOrderByDateDesc(User user);
}
