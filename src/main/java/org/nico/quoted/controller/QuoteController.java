package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Quote updateQuote(@PathVariable("id") Long id, @RequestBody Quote quote) {
        quote.setId(id);
        return quoteService.update(quote);
    }

    @DeleteMapping("/{id}") // Not using Spring Data default, as we want to clean up empty sources
    public Quote deleteQuote(@PathVariable("id") Long id) {
        return quoteService.delete(id);
    }
}
