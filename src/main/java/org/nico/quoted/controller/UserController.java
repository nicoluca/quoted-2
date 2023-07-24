package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Secret;
import org.nico.quoted.domain.Source;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.service.UserService;
import org.nico.quoted.util.AuthUtil;
import org.nico.quoted.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final SourceRepository sourceRepository;
    private final QuoteRepository quoteRepository;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, SourceRepository sourceRepository, QuoteRepository quoteRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.sourceRepository = sourceRepository;
        this.quoteRepository = quoteRepository;
    }

    @GetMapping
    public ResponseEntity<User> save() {
        try {
            return ResponseEntity.ok(retrieveExistingUser());
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(createNewUser());
        }
    }

    @GetMapping("/get-secret")
    public ResponseEntity<Secret> getSecretNumber() {
        String email = AuthUtil.getEmail();
        int secretNumber = SecretUtil.getSecret(email);
        Secret secret = new Secret(secretNumber);
        return ResponseEntity.ok(secret);
    }

    private User retrieveExistingUser() {
        String email = userService.getAuthenticatedUser().getEmail();
        return userRepository.findByEmail(email).orElseThrow();
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
