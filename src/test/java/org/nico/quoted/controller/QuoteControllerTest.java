package org.nico.quoted.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.nico.quoted.domain.Quote;
import org.nico.quoted.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @InjectMocks
    private QuoteController quoteController;

    private Quote quote;

    @BeforeEach
    public void setUp() {
        quote = new Quote();
        quote.setText("Test quote");
        quote.setId(1L);
    }

    @Test
    public void testUpdateQuote() throws Exception {
        quote.setText("Updated quote");
        when(quoteService.update(any(Quote.class), any(UUID.class))).thenReturn(quote);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/update-quote/{id}", quote.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated quote\"}")
                        .param("userId", UUID.randomUUID().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(quote.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Updated quote"))
                .andDo(print());

        verify(quoteService).update(any(Quote.class), any(UUID.class));
    }

    @Test
    public void testDeleteQuote() throws Exception {
        when(quoteService.delete(any(Long.class), any(UUID.class))).thenReturn(quote);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/update-quote/{id}", quote.getId())
                .param("userId", UUID.randomUUID().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(quote.getId()))
                .andDo(print());

        verify(quoteService).delete(any(Long.class), any(UUID.class));
    }
}