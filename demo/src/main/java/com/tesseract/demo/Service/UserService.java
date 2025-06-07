package com.tesseract.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tesseract.demo.Model.User;
import com.tesseract.demo.Repository.UserRepository;
import com.tesseract.demo.dto.UserDTO;
import com.tesseract.demo.dto.UserMapper;
import com.tesseract.demo.dto.UserWithPasswordDTO;
import com.tesseract.demo.dto.UserWithPasswordMapper;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserWithPasswordMapper userWithPasswordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User save(User user){
        return userRepository.save(user);
    }

    public UserDTO save(UserWithPasswordDTO user){
        if(userRepository.findByEmail(user.email()).isPresent()){
            return null;
        } else{
            User newUser = toDomain(user);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return toDTO(userRepository.save(newUser));
        }
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

    public User toDomain(UserWithPasswordDTO user){
        return userWithPasswordMapper.toDomain(user);
    }
}
