package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MarkdownService {
    private final QuoteRepository quoteRepository;
    private final SourceRepository sourceRepository;
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public MarkdownService(QuoteRepository quoteRepository, SourceRepository sourceRepository) {
        this.quoteRepository = quoteRepository;
        this.sourceRepository = sourceRepository;
    }

    public void generateMarkdownFiles() {
        Iterable<Source> sources = sourceRepository.findAll();
        Iterable<Quote> quotes = quoteRepository.findAll();
        List<Quote> quoteList = new ArrayList<>();
        quotes.forEach(quoteList::add);

        for (Source source : sources) {
            List<Quote> sourceQuotes = quoteList.stream()
                    .filter(quote -> quote.getSource().equals(source))
                    .toList();

            String fileName = source.getName() + ".md";
            String content = generateMarkdownContent(source, sourceQuotes);

            try {
                // Check if out directory exists
                createDirectoryIfNotExists("out");


                FileWriter fileWriter = new FileWriter("out/" + fileName);
                fileWriter.write(content);
                fileWriter.close();
            } catch (IOException e) {
                // Handle the exception appropriately
                logger.severe("Error writing to file " + fileName);
                throw new RuntimeException(e);
            }
        }
    }

    private void createDirectoryIfNotExists(String out) {
        Path path = Paths.get(out);

        if (!path.toFile().exists()) {
            try {
                path.toFile().mkdir();
            } catch (SecurityException e) {
                // Handle the exception appropriately
                logger.severe("Error creating directory " + out);
            }
        }
    }

    private String generateMarkdownContent(Source source, List<Quote> quotes) {
        StringBuilder content = new StringBuilder();
        content.append("# ").append(source.getName()).append("\n\n");

        for (Quote quote : quotes)
            content.append("- ").append(quote.getText()).append("\n");

        return content.toString();
    }
}
