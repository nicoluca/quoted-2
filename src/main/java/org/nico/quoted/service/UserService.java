package org.nico.quoted.service;

import org.nico.quoted.domain.User;
import org.nico.quoted.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService { // TODO User deletion

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID getUserId(String email) {
        if (userRepository.findByEmail(email).isPresent())
            return userRepository.findByEmail(email).get().getId();
        else
            return userRepository.save(new User(email)).getId();
    }
}
