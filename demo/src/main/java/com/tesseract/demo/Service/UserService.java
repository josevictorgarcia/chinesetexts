package com.tesseract.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.User;
import com.tesseract.demo.Repository.UserRepository;
import com.tesseract.demo.dto.UserDTO;
import com.tesseract.demo.dto.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    
    public User save(User user){
        return userRepository.save(user);
    }

    public UserDTO findById (long id) {
        return toDTO(userRepository.findById(id).orElseThrow());
    }

    private UserDTO toDTO(User user){
        return userMapper.toDTO(user);
    }

    public UserDTO findByEmail(String email) {
        return toDTO(userRepository.findByEmail(email).orElseThrow());
    }
}
