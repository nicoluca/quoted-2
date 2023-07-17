package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.repository.QuoteRepository;
import org.nico.quoted.repository.SourceRepository;
import org.nico.quoted.util.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ExportService {
    private final QuoteRepository quoteRepository;
    private final SourceRepository sourceRepository;
    private final Logger logger = Logger.getLogger(getClass().getName());

    static final String TEMP_OUT_DIR = "temp-out";

    @Value("${quoted.out-zip}")
    String TEMP_OUT_ZIP;

    @Autowired
    public ExportService(QuoteRepository quoteRepository, SourceRepository sourceRepository) {
        this.quoteRepository = quoteRepository;
        this.sourceRepository = sourceRepository;
    }

    public Resource generateMarkdownZip() throws IOException {
        logger.info("Generating markdown zip...");
        generateMarkdownFiles();
        ZipUtil.pack(TEMP_OUT_DIR, TEMP_OUT_ZIP);
        logger.info("Markdown zip generated.");
        return new FileSystemResource(TEMP_OUT_ZIP);
    }

    public void cleanUp() {
        logger.info("Deleting temp files...");
        FileSystemUtils.deleteRecursively(Paths.get(TEMP_OUT_DIR).toFile());
        FileSystemUtils.deleteRecursively(Paths.get(TEMP_OUT_ZIP).toFile());
        logger.info("Temp files deleted.");
    }

    void generateMarkdownFiles() {
        logger.info("Generating markdown files");
        Iterable<Source> sources = sourceRepository.findAll();
        Iterable<Quote> quotes = quoteRepository.findAll();
        List<Quote> quoteList = new ArrayList<>();
        quotes.forEach(quoteList::add);

        logger.info("Found " + quoteList.size() + " quotes from " + sources.spliterator().getExactSizeIfKnown() + " sources");

        for (Source source : sources) {
            logger.info("Generating markdown file for source " + source.getName());

            List<Quote> sourceQuotes = quoteList.stream()
                    .filter(quote -> quote.getSource().equals(source))
                    .toList();

            String fileName = source.getName() + ".md";
            String content = generateMarkdownContent(source, sourceQuotes);

            try {
                createDirectoryIfNotExists(TEMP_OUT_DIR);

                FileWriter fileWriter = new FileWriter(TEMP_OUT_DIR + "/" + fileName);
                fileWriter.write(content);
                fileWriter.close();
            } catch (IOException e) {
                // Handle the exception appropriately
                logger.severe("Error writing to file " + fileName);
                throw new RuntimeException(e);
            }
        }

        logger.info("Markdown files generated");
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
