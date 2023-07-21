package org.nico.quoted.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Source;
import org.nico.quoted.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        user.setId(1L);
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
        Optional<Source> foundSource = sourceRepository.findByName(SOURCE_NAME);

        // Assert that the retrieved source is not null and has the correct name
        assertTrue(foundSource.isPresent());
        assertEquals("Test Source", foundSource.get().getName());
    }

    @Test
    void testFindByNameNotFound() {
        Optional<Source> foundSource = sourceRepository.findByName("Not Found");
        assertTrue(foundSource.isEmpty());
    }

    @Test
    void testDeleteEmptySources() {
        sourceRepository.deleteEmptySources();

        assertTrue(sourceRepository.findByName(SOURCE_NAME).isEmpty());
        assertEquals(0, sourceRepository.count());
    }
}
