package org.nico.quoted.controller;

import org.nico.quoted.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.UUID;
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
    public ResponseEntity<StreamingResponseBody> download(@RequestParam("userId") UUID userId) {
        Resource resource;
        try {
            resource = exportService.generateMarkdownZip(userId);
        } catch (IOException e) {
            logger.severe("Error generating zip file: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

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

