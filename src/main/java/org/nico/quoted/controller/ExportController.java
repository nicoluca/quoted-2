package org.nico.quoted.controller;

import lombok.NonNull;
import org.nico.quoted.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class ExportController {
    private final ExportService exportService;

    @Value("${quoted.out-zip}")
    private String filename;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping(path = "/download-quotes")
    public ResponseEntity<StreamingResponseBody> download() throws IOException {
            Resource resource = exportService.generateMarkdownZip();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream -> {
                        try {
                            outputStream.write(resource.getInputStream().readAllBytes());
                        } catch (IOException e) {
                            logger.severe("Error writing to output stream: " + e.getMessage());
                            throw new RuntimeException(e);
                        } finally {
                            exportService.cleanUp();
                        }
                    });
    }
}

