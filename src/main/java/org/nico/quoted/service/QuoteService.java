package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
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
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class QuoteService implements Update<Quote>, Delete<Quote> {

    private final SourceRepository sourceRepository;
    private final QuoteRepository quoteRepository;

    private final Logger logger = Logger.getLogger(QuoteService.class.getName());

    @Autowired
    public QuoteService(SourceRepository sourceRepository, QuoteRepository quoteRepository) {
        this.sourceRepository = sourceRepository;
        this.quoteRepository = quoteRepository;
    }

    @Override
    @Transactional
    public Quote update(Quote quoteToUpdate, UUID userId) throws IllegalAccessException {
        Quote quoteFromDb = quoteRepository.findById(quoteToUpdate.getId()).orElseThrow();

        verifyQuoteOwnership(userId, quoteFromDb);

        if (!quoteToUpdate.getText().equals(quoteFromDb.getText()))
            quoteFromDb.setText(quoteToUpdate.getText());

        Source sourceFromQuoteToUpdate = quoteToUpdate.getSource();

        if (sourceFromQuoteToUpdate != null) {
            Source sourceFromDb = sourceRepository.findByNameAndUserId(sourceFromQuoteToUpdate.getName(), userId);
            sourceFromQuoteToUpdate = sourceFromDb != null ? sourceFromDb : sourceFromQuoteToUpdate;
            sourceFromQuoteToUpdate = sourceRepository.save(sourceFromQuoteToUpdate);
            quoteFromDb.setSource(sourceFromQuoteToUpdate);
        }

        sourceRepository.deleteEmptySources();
        return quoteRepository.save(quoteFromDb);
    }

    private static void verifyQuoteOwnership(UUID userId, Quote quoteFromDb) throws IllegalAccessException {
        if (!quoteFromDb.getUser().getId().equals(userId))
            throw new IllegalAccessException("User ID does not match");
    }

    @Override
    public Quote delete(Long id, UUID userId) throws IllegalAccessException {
        Quote quote = quoteRepository.findById(id).orElseThrow();
        verifyQuoteOwnership(userId, quote);

        quoteRepository.deleteById(id);
        sourceRepository.deleteEmptySources();
        return quote;
    }

    public List<Quote> findAllByUserId(UUID userId) {
        logger.info("Getting all quotes for user with ID: " + userId);
        int page = 0;
        int pageSize = 1000;

        List<Quote> allQuotes = new ArrayList<>();

        while (true) {
            logger.info("Getting quotes from page: " + page);
            Page<Quote> quotePage = quoteRepository.findAllByUserId(userId, PageRequest.of(page, pageSize));
            List<Quote> quotes = quotePage.getContent();

            if (quotes.isEmpty()) {
                logger.info("No more quotes found on page: " + page);
                break;
            }

            allQuotes.addAll(quotes);
            page++;
        }

        return allQuotes;
    }
    
}
