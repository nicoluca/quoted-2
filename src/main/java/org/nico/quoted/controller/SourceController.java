package org.nico.quoted.controller;

import org.nico.quoted.domain.Source;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.SourceRepository;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/sources")
public class SourceController {

    private final SourceRepository sourceRepository;

    private final UserRepository userRepository;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public SourceController(SourceRepository sourceRepository, UserRepository userRepository) {
        this.sourceRepository = sourceRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Page<Source> findAllByUser(Pageable pageable) {
        logger.info("findAllByUserId() called");

        String email = AuthUtil.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow();

        logger.info("Returning quotes for user with email" + user.getEmail());
        return sourceRepository.findAllByUserId(user.getId(), pageable);
    }

}
