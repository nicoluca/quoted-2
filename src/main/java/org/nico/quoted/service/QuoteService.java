package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class QuoteService implements Update<Quote>, Delete<Quote> {

    private final SourceRepository sourceRepository;
    private final QuoteRepository quoteRepository;

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
    
}
