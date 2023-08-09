package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Secret;
import org.nico.quoted.domain.Source;
import org.nico.quoted.domain.User;
import org.nico.quoted.exception.AuthenticationException;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.service.UserService;
import org.nico.quoted.util.AuthUtil;
import org.nico.quoted.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final SourceRepository sourceRepository;
    private final QuoteRepository quoteRepository;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, SourceRepository sourceRepository, QuoteRepository quoteRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.sourceRepository = sourceRepository;
        this.quoteRepository = quoteRepository;
    }

    @GetMapping
    public ResponseEntity<User> save() {
        logger.info("UserController.save() invoked.");
        logger.info("Trying to retrieve existing user...");

        Optional<User> existingUser = retrieveExistingUser();

        if (existingUser.isPresent()) {
            logger.info("Existing user found. Returning user...");
            return ResponseEntity.ok(existingUser.get());
        } else {
            logger.info("No existing user found. Creating new user...");
            return ResponseEntity.ok(createNewUser());
        }
    }

    @GetMapping("/get-secret")
    public ResponseEntity<Secret> getSecretNumber() {
        logger.info("UserController.getSecretNumber() invoked.");
        String email = AuthUtil.getEmail();
        int secretNumber = SecretUtil.getSecret(email);
        Secret secret = new Secret(secretNumber);
        logger.info("Returning secret number...");
        return ResponseEntity.ok(secret);
    }

    private Optional<User> retrieveExistingUser() {
        logger.info("Trying to retrieve existing user...");
        try {
            return Optional.of(userService.getAuthenticatedUser());
        } catch (AuthenticationException e) {
            logger.info("No existing user found.");
            return Optional.empty();
        }
    }

    private User createNewUser() {
        String  email = AuthUtil.getEmail();
        User user = new User();
        user.setEmail(email);
        user = userRepository.save(user); // Set ID

        createSampleQuote(user);
        return user;
    }

    private void createSampleQuote(User user) {
        logger.info("Creating sample quote for new user...");
        Source source = new Source();
        source.setName("Sample Source");
        source.setUser(user);
        source = sourceRepository.save(source);

        Quote quote = new Quote();
        quote.setText("Sample Quote");
        quote.setSource(source);
        quote.setUser(user);
        quoteRepository.save(quote);
    }

}
