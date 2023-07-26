package org.nico.quoted.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.User;
import org.nico.quoted.exception.AuthenticationException;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
@SpringBootTest
class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteController quoteController;

    private Quote quote;

    @BeforeEach
    public void setUp() {
        quote = new Quote();
        quote.setText("Test quote");
        quote.setId(1L);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new User()));
    }

    @Test
    @Order(1)
    void testUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/quotes"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void findAllByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/quotes")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void findByText() throws Exception {
        when(quoteRepository.findByText(any(String.class), any(Long.class), any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quotes/findByText")
                        .with(jwt())
                        .param("query", "Test quote")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(quoteRepository).findByText(any(String.class), any(Long.class), any(Pageable.class));
    }

    @Test
    void findBySource() throws Exception {
        when(quoteRepository.findBySourceIdAndUserId(any(Long.class), any(Long.class), any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quotes/findBySource")
                        .with(jwt())
                        .param("sourceId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(quoteRepository).findBySourceIdAndUserId(any(Long.class), any(Long.class), any(Pageable.class));
    }

    @Test
    void findBySourceIsNull() throws Exception {
        when(quoteRepository.findBySourceIsNullAndUserId(any(Long.class), any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quotes/findBySourceIsNull")
                        .with(jwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(quoteRepository).findBySourceIsNullAndUserId(any(Long.class), any(Pageable.class));
    }

    @Test
    void createQuote() throws Exception {
        when(quoteRepository.save(any(Quote.class))).thenReturn(quote);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/quotes")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Test quote\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(quote.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Test quote"))
                .andDo(print());

        verify(quoteRepository).save(any(Quote.class));
    }

    @Test
    public void testPatchQuote() throws Exception {
        quote.setText("Updated quote");
        when(quoteRepository.findById(any(Long.class))).thenReturn(Optional.of(quote));
        when(quoteService.update(any(Quote.class), any(User.class))).thenReturn(quote);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/quotes/{id}", quote.getId())
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated quote\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(quote.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Updated quote"))
                .andDo(print());

        verify(quoteService).update(any(Quote.class), any(User.class));
    }

    @Test
    public void testDeleteQuote() throws Exception {
        when(quoteService.delete(any(Long.class), any(User.class))).thenReturn(quote);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/quotes/{id}", quote.getId())
                .with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(quote.getId()))
                .andDo(print());

        verify(quoteService).delete(any(Long.class), any(User.class));
    }

    @Test
    public void testDeleteQuoteIllegalAccess() throws Exception {
        when(quoteService.delete(any(Long.class), any(User.class))).thenThrow(new AuthenticationException());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/quotes/{id}", quote.getId())
                .with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        verify(quoteService).delete(any(Long.class), any(User.class));
    }
}