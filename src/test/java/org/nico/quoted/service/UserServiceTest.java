package org.nico.quoted.service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.nico.quoted.exception.AuthenticationException;
import org.nico.quoted.repository.UserRepository;
import org.nico.quoted.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @Test
    void getAuthenticatedUser() {
        try (MockedStatic<AuthUtil> mockedAuthUtil = mockStatic(AuthUtil.class)) {
            mockedAuthUtil.when(AuthUtil::getEmail).thenReturn(null);
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
            assertThrows(AuthenticationException.class, () -> userService.getAuthenticatedUser());
            verify(userRepository).findByEmail(any());
        }
    }
}