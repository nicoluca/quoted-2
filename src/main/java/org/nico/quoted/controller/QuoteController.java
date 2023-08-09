package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.service.QuoteService;
import org.nico.quoted.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/quotes") // Do NOT use same path as QuoteRepository - otherwise, it will override the repository
public class QuoteController {

    private final QuoteService quoteService;
    private final QuoteRepository quoteRepository;
    private final UserService userService;

    @Value("${quoted.max_quotes_per_user}")
    private int maxQuotesPerUser;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public QuoteController(QuoteService quoteService, QuoteRepository quoteRepository, UserService userService) {
        this.quoteService = quoteService;
        this.quoteRepository = quoteRepository;
        this.userService = userService;
    }

    @GetMapping
    public Page<Quote> findAllByUser(Pageable pageable) {
        logger.info("findAllByUserId() called");

        User user = userService.getAuthenticatedUser();

        logger.info("Returning quotes for user with email" + user.getEmail());
        return quoteRepository.findAllByUserId(user.getId(), pageable);
    }

    @GetMapping("/findByText")
    public Page<Quote> findByText(@RequestParam("query") String query, Pageable pageable) {
        logger.info("searchByText() called with query " + query);

        User user = userService.getAuthenticatedUser();

        logger.info("Returning quotes for user with email" + user.getEmail());
        return quoteRepository.findByText(query, user.getId(), pageable);
    }

    @GetMapping("/findBySource")
    public Page<Quote> findBySource(@RequestParam("sourceId") long sourceId, Pageable pageable) {
        logger.info("searchBySource() called with sourceId " + sourceId);

        User user = userService.getAuthenticatedUser();

        logger.info("Returning quotes for user with email" + user.getEmail());
        return quoteRepository.findBySourceIdAndUserId(sourceId, user.getId(), pageable);
    }

    @GetMapping("findBySourceIsNull")
    public Page<Quote> findBySourceIsNull(Pageable pageable) {
        logger.info("searchBySourceIsNull() called");

        User user = userService.getAuthenticatedUser();

        logger.info("Returning quotes for user with email" + user.getEmail());
        return quoteRepository.findBySourceIsNullAndUserId(user.getId(), pageable);
    }

    @PostMapping
    public ResponseEntity<Quote> createQuote(@RequestBody Quote quote) {
        logger.info("createQuote() called");

        User user = userService.getAuthenticatedUser();
        quote.setUser(user);

        if (quoteRepository.countByUserId(user.getId()) >= this.maxQuotesPerUser) {
            logger.warning("User with email " + user.getEmail() + " has reached the maximum number of quotes of " + this.maxQuotesPerUser);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Quote createdQuote = quoteRepository.save(quote);

        logger.info("Returning created quote " + quote);
        return new ResponseEntity<>(createdQuote, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Quote> patchQuote(@PathVariable("id") long id, @RequestBody Quote quote) {
        logger.info("updateQuote() called");

        User user = userService.getAuthenticatedUser();
        quote.setId(id);
        quote.setUser(user);
        quote = quoteService.update(quote, user);

        logger.info("Returning updated quote " + quote.getText() + " for user with email" + user.getEmail());
        return ResponseEntity.ok(quote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Quote> deleteQuote(@PathVariable("id") long id) {
        logger.info("deleteQuote() called");

        User user = userService.getAuthenticatedUser();

        Quote quoteToDelete = quoteService.delete(id, user);
        logger.info("Returning deleted quote " + id + " for user with email: " + user.getEmail());
        return ResponseEntity.ok(quoteToDelete);
    }
}
