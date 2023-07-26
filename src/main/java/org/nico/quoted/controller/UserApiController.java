package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.User;
import org.nico.quoted.exception.AuthenticationException;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

// TODO
@RestController
@RequestMapping("/user-api")
@CrossOrigin(origins = "http://localhost:4200") // TODO Needs to accept mobile app?
public class UserApiController {

    private final UserRepository userRepository;
    private final QuoteRepository quoteRepository;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserApiController(UserRepository userRepository, QuoteRepository quoteRepository) {
        this.userRepository = userRepository;
        this.quoteRepository = quoteRepository;
    }

    @PostMapping("/post-quote")
    public ResponseEntity<Quote> createQuote(@RequestBody Quote quote,
                                             @RequestParam("secret") int secret,
                                             @RequestParam("email") String email) {

        logger.info("createQuote() called with quote " + quote + " and secret and email " + email);

        if (!isCorrectSecret(secret, email)) {
            logger.warning("Incorrect secret" + secret + " for email " + email);
            throw new AuthenticationException("Incorrect secret or email.");
        }

        if (!isValidQuote(quote)) {
            logger.warning("Invalid quote " + quote);
            throw new IllegalArgumentException("Invalid quote,");
        }

        logger.info("Correct secret for email " + email);
        User user = userRepository.findByEmail(email).orElseThrow();

        quote.setUser(user);
        Quote createdQuote = quoteRepository.save(quote);

        logger.info("Returning created quote " + quote);
        return new ResponseEntity<>(createdQuote, HttpStatus.CREATED);
    }

    private boolean isValidQuote(Quote quote) {
        return quote != null && quote.getText() != null && !quote.getText().isBlank();
    }

    private boolean isCorrectSecret(int secret, String email) {
        return secret == SecretUtil.getSecret(email);
    }

}
