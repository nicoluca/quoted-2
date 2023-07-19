package org.nico.quoted.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Source;
import org.nico.quoted.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SourceRepositoryTest {

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private UserRepository userRepository;

    private Source source;
    private final String SOURCE_NAME = "Test Source";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user = userRepository.save(user);

        source = new Source();
        source.setName(SOURCE_NAME);
        source.setUser(user);
        source = sourceRepository.save(source);
    }

    @AfterEach
    void tearDown() {
        sourceRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testSourceIsSaved() {
        assertTrue(sourceRepository.findById(source.getId()).isPresent());
        assertEquals(1, sourceRepository.count());
    }

    @Test
    void testFindByName() {
        Source foundSource = sourceRepository.findByNameAndUserId(SOURCE_NAME, user.getId());

        // Assert that the retrieved source is not null and has the correct name
        assertNotNull(foundSource);
        assertEquals("Test Source", foundSource.getName());
    }

    @Test
    void testFindByNameNotFound() {
        Source foundSource = sourceRepository.findByNameAndUserId("Not Found", user.getId());
        assertNull(foundSource);
    }

    @Test
    void testDeleteEmptySources() {
        sourceRepository.deleteEmptySources();

        assertNull(sourceRepository.findByNameAndUserId("Empty Source", user.getId()));
        assertEquals(0, sourceRepository.count());
    }
}
