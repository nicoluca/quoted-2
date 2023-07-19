package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/update-quote") // Do NOT use same path as QuoteRepository - otherwise, it will override the repository
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class QuoteController {

    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Quote> updateQuote(@PathVariable("id") Long id, @RequestParam("userId") UUID userId, @RequestBody Quote quote) {
        quote.setId(id);

        try {
            return ResponseEntity.ok(quoteService.update(quote, userId));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}") // Not using Spring Data default, as we want to clean up empty sources
    public ResponseEntity<Quote> deleteQuote(@PathVariable("id") Long id, @RequestParam("userId") UUID userId) {
        try {
            return ResponseEntity.ok(quoteService.delete(id, userId));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
