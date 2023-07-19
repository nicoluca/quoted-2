package org.nico.quoted.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.nico.quoted.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ExportControllerTest {

    @Autowired
    private ExportController exportController;

    @MockBean
    private ExportService exportService;

    @Value("${quoted.out-zip}")
    private String filename;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exportController = new ExportController(exportService);
    }

    @Test
    void testDownload_SuccessfulFileDownload() throws IOException {
        Resource resource = new FileSystemResource(
                Objects.requireNonNull(
                        Thread.currentThread().getContextClassLoader().getResource("quotes.zip")).getFile());
        when(exportService.generateMarkdownZip(any(UUID.class))).thenReturn(resource);
        doNothing().when(exportService).cleanUp();

        ResponseEntity<StreamingResponseBody> response = exportController.download(UUID.randomUUID());

        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());

        StreamingResponseBody responseBody = response.getBody();
        OutputStream outputStream = mock(OutputStream.class);
        assert responseBody != null;
        responseBody.writeTo(outputStream);
        verify(outputStream).write(any(byte[].class));

        verify(exportService).cleanUp();
    }

    @Test
    void testDownload_FileDownloadError() throws IOException {
        when(exportService.generateMarkdownZip(any(UUID.class))).thenThrow(IOException.class);

        ResponseEntity<StreamingResponseBody> response = exportController.download(UUID.randomUUID());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Verify cleanup method not called
        verify(exportService, never()).cleanUp();
    }

}
