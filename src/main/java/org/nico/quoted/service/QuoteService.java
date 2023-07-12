package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Quote update(Quote quoteToUpdate) {
        Quote quoteFromDb = quoteRepository.findById(quoteToUpdate.getId()).orElseThrow();

        if (!quoteToUpdate.getText().equals(quoteFromDb.getText()))
            quoteFromDb.setText(quoteToUpdate.getText());

        Source sourceFromQuoteToUpdate = quoteToUpdate.getSource();

        if (sourceFromQuoteToUpdate != null) {
            Source sourceFromDb = sourceRepository.findByName(sourceFromQuoteToUpdate.getName());
            sourceFromQuoteToUpdate = sourceFromDb != null ? sourceFromDb : sourceFromQuoteToUpdate;
            sourceFromQuoteToUpdate = sourceRepository.save(sourceFromQuoteToUpdate);
            quoteFromDb.setSource(sourceFromQuoteToUpdate);
        }

        sourceRepository.deleteEmptySources();
        return quoteRepository.save(quoteFromDb);
    }

    public Quote delete(Long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow();
        quoteRepository.deleteById(id);
        sourceRepository.deleteEmptySources();
        return quote;
    }
}
