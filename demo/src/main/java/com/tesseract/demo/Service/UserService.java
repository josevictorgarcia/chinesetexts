package com.tesseract.demo.Service;

import java.util.Optional;

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

    public UserWithPasswordDTO save(String email, UserWithPasswordDTO user){
        Optional<User> userToUpdate = userRepository.findByEmail(email);
        if(userToUpdate.isPresent()){
            if(!user.newPassword().isEmpty() && !passwordEncoder.matches(user.password(), userToUpdate.get().getPassword()) || user.password().isEmpty() && !user.newPassword().isEmpty() || !user.password().isEmpty() && user.newPassword().isEmpty()){
                //Mandamos un dto de error, para informar al frontend de que las contraseñas no coinciden
                return new UserWithPasswordDTO(null, "error", null, null, null, null, null, null);
            }
            if(!user.newPassword().isEmpty() && passwordEncoder.matches(user.password(), userToUpdate.get().getPassword())){
                userToUpdate.get().setPassword(passwordEncoder.encode(user.newPassword()));
            }
            if(!user.name().isEmpty()){ 
                userToUpdate.get().setName(user.name());
            }
            if(!user.language().isEmpty()){
                userToUpdate.get().setLanguage(user.language());
            }
            return userWithPasswordMapper.toDTO(userRepository.save(userToUpdate.get()));
        } else{
            return null;
        }
    }

    public boolean deleteUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        } else {
            return false;
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

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

    public UserWithPasswordDTO findUserWithPasswordDTOByEmail(String email){
        return userWithPasswordMapper.toDTO(userRepository.findByEmail(email).orElseThrow());
    }

    public User toDomain(UserWithPasswordDTO user){
        return userWithPasswordMapper.toDomain(user);
    }

    public User toDomain(UserDTO userDTO){
        return userMapper.toDomain(userDTO);
    }
}
