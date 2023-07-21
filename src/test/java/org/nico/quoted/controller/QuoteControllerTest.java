package org.nico.quoted.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc
@SpringBootTest
//@ActiveProfiles("test")
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
    void testUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/quotes"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
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
}