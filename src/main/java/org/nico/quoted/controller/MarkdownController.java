package org.nico.quoted.controller;

import org.nico.quoted.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

