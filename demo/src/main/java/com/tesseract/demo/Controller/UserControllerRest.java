package com.tesseract.demo.Controller;


import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tesseract.demo.Service.UserService;
import com.tesseract.demo.dto.UserDTO;
import com.tesseract.demo.dto.UserWithPasswordDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/users")
public class UserControllerRest {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    public UserWithPasswordDTO getUser(@RequestParam String email){
        return userService.findUserWithPasswordDTOByEmail(email);
    }

    @GetMapping("/me")
	public UserDTO me(HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if(principal != null) {
			return userService.findByEmail(principal.getName());
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
		}
	}

    @PostMapping("/")
    public ResponseEntity<UserDTO> postUser(@RequestBody UserWithPasswordDTO user) {
        UserDTO newUser = userService.save(user);
        if(newUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newUser.id()).toUri();
        return ResponseEntity.created(location).body(newUser);
    }
    
    @PutMapping("/{email}")
    public ResponseEntity<UserWithPasswordDTO> putUserByEmail(@PathVariable String email, @RequestBody UserWithPasswordDTO user) {
        UserWithPasswordDTO newUser = userService.save(email, user);
        if(newUser == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newUser);
    }
    
}