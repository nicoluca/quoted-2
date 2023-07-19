package org.nico.quoted.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class QuoteRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    private User user;
    private Quote quote;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        user = new User();
        user.setEmail("test-email");
        user = userRepository.save(user);

        quote = new Quote();
        quote.setText("Test quote");
        quote.setUser(user);

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
    void findAllByUserId() {
        Page<Quote> quotes = quoteRepository.findAllByUserId(user.getId(), pageable);

        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());
        assertEquals(1, quotes.getTotalElements());
    }

    @Test
    void testFindByText() {
        String searchText = "test";
        Page<Quote> quotes = quoteRepository.findByText(searchText, user.getId(), pageable);

        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());

        quotes.forEach(quote ->
                assertTrue(quote.getText().toUpperCase().contains(searchText.toUpperCase()) ||
                quote.getSource().getName().toUpperCase().contains(searchText.toUpperCase())));
    }

    @Test
    void testFindBySourceId() {
        Long sourceId = 1L; // Replace with the actual source ID
        Page<Quote> quotes = quoteRepository.findBySourceIdAndUserId(sourceId, user.getId(), pageable);

        assertNotNull(quotes);
        assertTrue(quotes.isEmpty());
    }

    @Test
    void testFindBySourceIsNull() {
        Page<Quote> quotes = quoteRepository.findBySourceIsNullAndUserId(user.getId(), pageable);

        assertNotNull(quotes);
        assertFalse(quotes.isEmpty());
    }
}
