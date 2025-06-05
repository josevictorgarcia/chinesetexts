package com.tesseract.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tesseract.demo.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    List<User> findAllByOrderByEmail();
}