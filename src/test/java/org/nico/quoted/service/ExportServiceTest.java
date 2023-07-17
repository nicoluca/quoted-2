package org.nico.quoted.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class ExportServiceTest {

    @Autowired
    private ExportService exportService;

    @MockBean
    private QuoteRepository quoteRepository;

    @MockBean
    private SourceRepository sourceRepository;

    @Value("${quoted.out-zip}")
    private String TEMP_OUT_ZIP;

    @AfterEach
    public void tearDown() {
        exportService.cleanUp();
    }

    @Test
    public void testGenerateMarkdownZip() throws IOException {

        // Setup
        List<Source> sources = new ArrayList<>();
        List<Quote> quotes = new ArrayList<>();
        sources.add(Source.builder().id(1L).name("Test Source").build());
        quotes.add(
                Quote.builder()
                        .id(1L)
                        .text("Test Quote")
                        .source(sources.get(0))
                        .datetimeCreated(Timestamp.valueOf("2020-01-01 00:00:00"))
                            .build());

        // Mocks
        when(quoteRepository.findAll()).thenReturn(quotes);
        when(sourceRepository.findAll()).thenReturn(sources);

        //  Execution
        Resource zipResource = exportService.generateMarkdownZip();

        // Verification
        verify(quoteRepository, times(1)).findAll();
        verify(sourceRepository, times(1)).findAll();

        // Asserts
        assertTrue(zipResource.exists());
    }

    @Test
    public void testCleanUp() throws IOException {
        Path tempOutDir = Paths.get(ExportService.TEMP_OUT_DIR);
        Path tempOutZip = Paths.get(TEMP_OUT_ZIP);

        tempOutDir.toFile().mkdir();
        tempOutZip.toFile().createNewFile();

        exportService.cleanUp();

        assertFalse(tempOutDir.toFile().exists());
        assertFalse(tempOutZip.toFile().exists());
    }
}