package org.nico.quoted.controller;

import org.nico.quoted.domain.User;
import org.nico.quoted.service.ExportService;
import org.nico.quoted.service.UserService;
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
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class ExportController {
    private final ExportService exportService;
    private final UserService userService;

    @Value("${quoted.out-zip}")
    private String filename;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public ExportController(ExportService exportService, UserService userService) {
        this.exportService = exportService;
        this.userService = userService;
    }

    @GetMapping(path = "/download")
    public ResponseEntity<StreamingResponseBody> download() {
        Resource resource;
        User user = userService.getAuthenticatedUser();

        try {
            resource = exportService.generateMarkdownZip(user);
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

