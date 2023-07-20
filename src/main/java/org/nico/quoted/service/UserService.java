package org.nico.quoted.service;

import org.nico.quoted.domain.User;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.util.StringUtil;
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
        email = formatEmail(email);
        validateEmail(email);

        if (userRepository.findByEmail(email).isPresent())
            return userRepository.findByEmail(email).get().getId();
        else
            return userRepository.save(new User(email)).getId(); // TODO this is dangerous misleading behaviour
    }


    private String formatEmail(String email) {
        email = email.toLowerCase();
        email = email.trim();
        email = email.replaceAll("\"", "");
        return email;
    }

    private void validateEmail(String email) {
        if (email.isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");
        if (!StringUtil.isCorrectEmail(email))
            throw new IllegalArgumentException("Invalid email");

    }
}
