package org.nico.quoted.controller;

import org.nico.quoted.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // TODO Should be configurable, not hardcoded
public class MarkdownController {
    private final MarkdownService markdownService;

    @Autowired
    public MarkdownController(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    @GetMapping("/generate-markdown")
    public String generateMarkdownFiles() {
        try {
            markdownService.generateMarkdownFiles();
        } catch (RuntimeException e) {
            return "Error generating markdown files: " + e.getMessage();
        }

        return "Markdown files generated successfully!";
    }

    @GetMapping(path = "/generate-markdown/download")
    public ResponseEntity<Resource> download(String param) throws IOException {

        String filename = "out/The Bible.md";

        Resource resource = new org.springframework.core.io.FileSystemResource(filename);

        // InputStreamResource resource = new InputStreamResource(new FileInputStream(filename));

        // Set the content type and attachment header for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}

