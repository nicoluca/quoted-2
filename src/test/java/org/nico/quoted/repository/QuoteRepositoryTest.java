package org.nico.quoted.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@TestPropertySource("classpath:test.properties") // To create-drop the database
class QuoteRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;

    private Quote quote;

    @BeforeEach
    void setUp() {
        quote = new Quote();
        quote.setText("Test quote");
        quote = quoteRepository.save(quote);
    }

    @AfterEach
    void tearDown() {
        quoteRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testQuoteIsSaved() {
        assertTrue(quoteRepository.findById(quote.getId()).isPresent());
        assertEquals(1, quoteRepository.count());
    }

    @Test
    void testFindByTextContainingIgnoreCaseOrSourceNameContainingIgnoreCase() {
        String searchText = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Quote> quotes = quoteRepository.findByTextContainingIgnoreCaseOrSourceNameContainingIgnoreCase(searchText, pageable);

        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());

        quotes.forEach(quote ->
                assertTrue(quote.getText().toUpperCase().contains(searchText.toUpperCase()) ||
                quote.getSource().getName().toUpperCase().contains(searchText.toUpperCase())));
    }

    @Test
    void testFindBySourceId() {
        Long sourceId = 1L; // Replace with the actual source ID
        Pageable pageable = PageRequest.of(0, 10);
        Page<Quote> quotes = quoteRepository.findBySourceId(sourceId, pageable);

        assertNotNull(quotes);
        assertTrue(quotes.isEmpty());
    }

    @Test
    void testFindBySourceIsNull() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Quote> quotes = quoteRepository.findBySourceIsNull(pageable);

        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());
    }
}
