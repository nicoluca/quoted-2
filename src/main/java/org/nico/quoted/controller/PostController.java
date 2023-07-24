package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

// TODO
@RestController
@RequestMapping("/post-quote")
@CrossOrigin(origins = "http://localhost:4200") // TODO Needs to accept mobile app?
public class PostController {

    private final UserRepository userRepository;
    private final QuoteRepository quoteRepository;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public PostController(UserRepository userRepository, QuoteRepository quoteRepository) {
        this.userRepository = userRepository;
        this.quoteRepository = quoteRepository;
    }

    // TODO
    // Implement secret
    // Disable JWT for this endpoint
    // Test with Postman
    // Show secret in UI to user
    // Test Apple shortcut

    @PostMapping
    public ResponseEntity<Quote> createQuote(@RequestBody Quote quote, @RequestParam("secret") String secret, @RequestParam("email") String email) {
        logger.info("createQuote() called with quote " + quote + " and secret " + secret + " and email " + email);

        if (!isCorrectSecret(secret, email)) {
            logger.warning("Incorrect secret" + secret + " for email " + email);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(email).orElseThrow();

        quote.setUser(user);
        Quote createdQuote = quoteRepository.save(quote);

        logger.info("Returning created quote " + quote);
        return new ResponseEntity<>(createdQuote, HttpStatus.CREATED);
    }

    private boolean isCorrectSecret(String secret, String email) {
        // TODO
        if (secret.equals("secret")) {
            return true;
        }
        return false;
    }


}
