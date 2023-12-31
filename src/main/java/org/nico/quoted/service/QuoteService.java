package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.domain.User;
import org.nico.quoted.exception.AuthenticationException;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class QuoteService implements Delete<Quote>, Update<Quote>, FindAll<Quote> {

    private final SourceRepository sourceRepository;
    private final QuoteRepository quoteRepository;

    private final Logger logger = Logger.getLogger(QuoteService.class.getName());

    @Autowired
    public QuoteService(SourceRepository sourceRepository, QuoteRepository quoteRepository) {
        this.sourceRepository = sourceRepository;
        this.quoteRepository = quoteRepository;
    }

    public List<Quote> findAll(User user) {
        Pageable pageable = PageRequest.of(0, 1000);
        List<Quote> quotes = new ArrayList<>();

        while (true) {
            Page<Quote> quotePage = quoteRepository.findAllByUserId(user.getId(), pageable);
            List<Quote> quotesFromPage = quotePage.getContent();

            if (quotesFromPage.isEmpty())
                break;

            quotes.addAll(quotesFromPage);
            pageable = quotePage.nextPageable();
        }
        return quotes;
    }

    @Transactional
    public Quote update(Quote quoteToUpdate, User user) {
        Quote quoteFromDb = quoteRepository.findById(quoteToUpdate.getId())
                .orElseThrow(() -> new NoSuchElementException("Quote not found."));

        verifyQuoteOwnership(user, quoteFromDb);

        if (!quoteToUpdate.getText().equals(quoteFromDb.getText()))
            quoteFromDb.setText(quoteToUpdate.getText());

        Source sourceFromQuoteToUpdate = quoteToUpdate.getSource();

        if (sourceFromQuoteToUpdate != null) {
            Optional<Source> sourceFromDb = sourceRepository.findByName(sourceFromQuoteToUpdate.getName());
            sourceFromQuoteToUpdate = sourceFromDb.orElse(sourceFromQuoteToUpdate);
            sourceFromQuoteToUpdate.setUser(user);
            sourceFromQuoteToUpdate = sourceRepository.save(sourceFromQuoteToUpdate);
            quoteFromDb.setSource(sourceFromQuoteToUpdate);
        }

        sourceRepository.deleteEmptySources();
        return quoteRepository.save(quoteFromDb);
    }

    @Override
    public Quote delete(Long id, User user) {
        Quote quote = quoteRepository.findById(id).orElseThrow();
        verifyQuoteOwnership(user, quote);

        quoteRepository.deleteById(id);
        sourceRepository.deleteEmptySources();
        return quote;
    }

    private static void verifyQuoteOwnership(User user, Quote quoteFromDb) {
        if (!quoteFromDb.getUser().equals(user))
            throw new AuthenticationException("You are not authorized to perform this action.");
    }

}
