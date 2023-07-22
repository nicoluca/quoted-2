package org.nico.quoted.controller;

import org.nico.quoted.domain.User;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.service.UserService;
import org.nico.quoted.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<User> save() {
        try {
            return ResponseEntity.ok(existingUser());
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(newUser());
        }
    }

    private User existingUser() {
        String email = userService.getAuthenticatedUser().getEmail();
        return userRepository.findByEmail(email).orElseThrow();
    }

    private User newUser() {
        String  email = AuthUtil.getEmail();
        User user = new User();
        user.setEmail(email);
        return userRepository.save(user);
    }


}
