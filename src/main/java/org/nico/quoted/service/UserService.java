package org.nico.quoted.service;

import org.nico.quoted.domain.User;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService { // TODO User deletion

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        String email = AuthUtil.getEmail();
        return userRepository.findByEmail(email).orElseThrow();
    }

}
