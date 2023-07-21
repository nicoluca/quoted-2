package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.service.QuoteService;
import org.nico.quoted.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/quotes") // Do NOT use same path as QuoteRepository - otherwise, it will override the repository
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class QuoteController {

    private final QuoteService quoteService;
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public QuoteController(QuoteService quoteService, QuoteRepository quoteRepository, UserRepository userRepository) {
        this.quoteService = quoteService;
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Page<Quote> findAllByUser(Pageable pageable) {
        logger.info("findAllByUserId() called");

        String email = AuthUtil.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow();

        logger.info("Returning quotes for user with email" + user.getEmail());
        return quoteRepository.findAllByUserId(user.getId(), pageable);
    }

    @PostMapping
    public ResponseEntity<Quote> createQuote(@RequestBody Quote quote) {
        logger.info("createQuote() called");

        String email = AuthUtil.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow();

        quote.setUser(user);

        Quote createdQuote = quoteRepository.save(quote);

        logger.info("Returning created quote " + quote);
        return ResponseEntity.ok(createdQuote);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Quote> patchQuote(@PathVariable("id") long id, @RequestBody Quote quote) {
        logger.info("updateQuote() called");
        String email = AuthUtil.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow();

        quote.setId(id);

        if (quoteRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        try {
            quote = quoteService.update(quote, user);
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().build();
        }

        logger.info("Returning updated quote " + quote.getText() + " for user with email" + user.getEmail());
        return ResponseEntity.ok(quote); // TODO Needs to catch IllegalAccessException
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Quote> deleteQuote(@PathVariable("id") long id) {
        logger.info("deleteQuote() called");

        String email = AuthUtil.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow();

        try {
            logger.info("Returning deleted quote " + id + " for user with email: " + user.getEmail());
            return ResponseEntity.ok(quoteService.delete(id, user));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
