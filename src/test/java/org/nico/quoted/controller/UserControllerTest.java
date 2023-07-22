package org.nico.quoted.controller;

import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.User;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void saveUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void saveExistingUser() throws Exception {
        when(userService.getAuthenticatedUser()).thenReturn(new User());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService).getAuthenticatedUser();
        verify(userRepository).findByEmail(any());
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.getAuthenticatedUser()).thenThrow(new NoSuchElementException());
        when(userRepository.save(any())).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .with(jwt()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService).getAuthenticatedUser();
        verify(userRepository).save(any());
    }
}