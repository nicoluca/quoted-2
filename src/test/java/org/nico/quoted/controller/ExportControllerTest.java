package org.nico.quoted.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nico.quoted.domain.User;
import org.nico.quoted.service.ExportService;
import org.nico.quoted.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ExportControllerTest {

    @Autowired
    private ExportController exportController;

    @MockBean
    private ExportService exportService;

    @MockBean
    private UserService userService;

    @Value("${quoted.out-zip}")
    private String filename;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDownload_SuccessfulFileDownload() throws IOException {
        Resource resource = new FileSystemResource(
                Objects.requireNonNull(
                        Thread.currentThread().getContextClassLoader().getResource("quotes.zip")).getFile());
        User user = new User();

        when(exportService.generateMarkdownZip(any(User.class))).thenReturn(resource);
        doNothing().when(exportService).cleanUp();
        when(userService.getAuthenticatedUser()).thenReturn(user);

        ResponseEntity<StreamingResponseBody> response = exportController.download();

        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());

        StreamingResponseBody responseBody = response.getBody();
        OutputStream outputStream = mock(OutputStream.class);
        assert responseBody != null;
        responseBody.writeTo(outputStream);
        verify(outputStream).write(any(byte[].class));

        verify(exportService).cleanUp();
        verify(userService).getAuthenticatedUser();
    }

    @Test
    void testDownload_FileDownloadError() throws IOException {
        when(exportService.generateMarkdownZip(any(User.class))).thenThrow(new IOException());
        doNothing().when(exportService).cleanUp();
        when(userService.getAuthenticatedUser()).thenReturn(new User());

        ResponseEntity<StreamingResponseBody> response = exportController.download();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Verify cleanup method not called
        verify(exportService, never()).cleanUp();
        verify(userService).getAuthenticatedUser();
    }

}
