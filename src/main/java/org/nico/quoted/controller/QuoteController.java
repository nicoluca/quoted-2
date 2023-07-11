package org.nico.quoted.controller;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@CrossOrigin("http://localhost:4200") // TODO: replace with a property
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
}
