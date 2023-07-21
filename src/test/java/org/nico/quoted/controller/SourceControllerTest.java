package org.nico.quoted.controller;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Source;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.SourceRepository;
import org.nico.quoted.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
class SourceControllerTest {

    @Autowired
    private SourceController sourceController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SourceRepository sourceRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @Order(1)
    void testUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sources"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void findAllByUser() throws Exception {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new User()));
        when(sourceRepository.findAllByUserId(any(Long.class), any())).thenReturn(Page.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/sources")
                .with(jwt())
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }
}