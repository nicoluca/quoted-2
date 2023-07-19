package org.nico.quoted.service;

import org.nico.quoted.domain.Quote;
import org.nico.quoted.domain.Source;
import org.nico.quoted.repository.QuoteRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ExportService {

    @Value("${quoted.date-time-format}")
    private String DATE_TIME_FORMAT;
    private final QuoteRepository quoteRepository;
    private final Logger logger = Logger.getLogger(getClass().getName());

    static final String TEMP_OUT_DIR = "temp-out";

    @Value("${quoted.out-zip}")
    String TEMP_OUT_ZIP;

    @Autowired
    public ExportService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Resource generateMarkdownZip(UUID userId) throws IOException {
        logger.info("Generating markdown zip...");
        generateMarkdownFiles(userId);
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

    void generateMarkdownFiles(UUID userId) {
        logger.info("Generating markdown files");
        Iterable<Quote> quotes = quoteRepository.findAllByUserId(userId);

        logger.info("Found " + quotes.spliterator().getExactSizeIfKnown() + " quotes...");
        logger.info("Preparing quotes...");
        Map<Source, List<Quote>> sourceQuoteMap = generateSourceQuoteMap(quotes);

        logger.info("Quotes prepared. Found " + sourceQuoteMap.size() + " sources.");
        logger.info("Generating markdown files...");
        sourceQuoteMap.keySet().forEach(source ->
                generateMarkdownFile(source, sourceQuoteMap.get(source)));

        logger.info("Markdown files generated");
    }

    private Map<Source, List<Quote>> generateSourceQuoteMap(Iterable<Quote> quoteList) {
        Source unknownSource = new Source();
        unknownSource.setName("(No source)");

        Map<Source, List<Quote>> sourceQuoteMap = new HashMap<>();

        for (Quote quote : quoteList) {
            if (quote.getSource() == null)
                quote.setSource(unknownSource);

            if (sourceQuoteMap.containsKey(quote.getSource()))
                sourceQuoteMap.get(quote.getSource()).add(quote);
            else {
                List<Quote> list = new ArrayList<>();
                list.add(quote);
                sourceQuoteMap.put(quote.getSource(), list);
            }
        }

        return sourceQuoteMap;
    }

    private void generateMarkdownFile(Source source, List<Quote> quotes) {
        logger.info("Generating markdown file for source " + source.getName());

        String fileName = source.getName() + ".md";
        String content = generateMarkdownContent(source, quotes);

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

    private void createDirectoryIfNotExists(String out) {
        Path path = Paths.get(out);

        if (!path.toFile().exists()) {
            try {
                path.toFile().mkdir();
            } catch (SecurityException e) {
                logger.severe("Error creating directory " + out);
                throw new RuntimeException(e);
            }
        }
    }

    private String generateMarkdownContent(Source source, List<Quote> quotes) {
        StringBuilder content = new StringBuilder();
        content.append("# ").append(source.getName()).append("\n\n");

        quotes.forEach(quote -> {
            content.append("- ").append(quote.getText()).append(" ");
            content.append("*(").append(quote.getDatetimeCreated().toLocalDateTime().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))).append(")*");
            content.append("\n");
        });

        return content.toString();
    }
}
