package org.nico.quoted.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

// @SpringJUnitConfig
@SpringBootTest
public class QuoteServiceTest {

    @Autowired
    private QuoteService quoteService;

    @MockBean
    private SourceRepository sourceRepository;

    @MockBean
    private QuoteRepository quoteRepository;

    private Quote testQuote;

    @BeforeEach
    public void setup() {
        testQuote = new Quote();
        testQuote.setId(1L);
        testQuote.setText("Original text");
    }

    @Test
    public void testUpdate() {
        // Setup
        Source testSource = new Source();
        testSource.setId(1L);
        testSource.setName("Test Source");
        testQuote.setSource(testSource);

        Quote mockQuoteFromDb = new Quote();
        mockQuoteFromDb.setId(1L);
        mockQuoteFromDb.setText("Old text");
        mockQuoteFromDb.setSource(testSource);

        Source mockSourceFromDb = new Source();
        mockSourceFromDb.setId(1L);
        mockSourceFromDb.setName("Test Source");

        // Mocks
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(mockQuoteFromDb));
        when(sourceRepository.findByName("Test Source")).thenReturn(mockSourceFromDb);
        when(sourceRepository.save(mockSourceFromDb)).thenReturn(mockSourceFromDb);
        when(quoteRepository.save(any(Quote.class))).thenReturn(mockQuoteFromDb);

        // Execution
        Quote updatedQuote = quoteService.update(testQuote);

        // Verification
        verify(quoteRepository).findById(1L);
        verify(sourceRepository).findByName("Test Source");
        verify(sourceRepository).save(mockSourceFromDb);
        verify(quoteRepository).save(mockQuoteFromDb);
        verify(sourceRepository).deleteEmptySources();

        // Assertions
        assertNotNull(updatedQuote);
        assertEquals(1L, updatedQuote.getId());
        assertEquals("Original text", updatedQuote.getText());
        assertEquals(mockSourceFromDb, updatedQuote.getSource());
    }

    @Test
    public void testDelete() {
        // Setup
        Quote mockQuoteFromDb = new Quote();
        mockQuoteFromDb.setId(1L);
        mockQuoteFromDb.setText("Test quote");

        // Mocks
        when(quoteRepository.findById(1L)).thenReturn(Optional.of(mockQuoteFromDb));

        // Execution
        Quote deletedQuote = quoteService.delete(1L);

        // Verification
        verify(quoteRepository).findById(1L);
        verify(quoteRepository).deleteById(1L);
        verify(sourceRepository).deleteEmptySources();

        // Assertions
        assertNotNull(deletedQuote);
        assertEquals(1L, deletedQuote.getId());
        assertEquals("Test quote", deletedQuote.getText());
    }
}
