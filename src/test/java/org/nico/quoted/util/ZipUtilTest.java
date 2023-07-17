package org.nico.quoted.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilTest {

    private final String tempDir = "tempDir";
    private final String tempFile = "tempFile.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory
        new File(tempDir).mkdirs();
        new File(tempDir  + "/" + tempDir).mkdirs();

        // Create a temporary file
        Path path = Paths.get(tempDir + "/" + tempFile);
        Files.createFile(path);
        Path path2 = Paths.get(tempDir + "/" + tempDir + "/" + tempFile);
        Files.createFile(path2);
    }

    @AfterEach
    void tearDown() {
        FileSystemUtils.deleteRecursively(new File(tempDir));
    }

    @Test
    void zipDirectory() throws IOException {
        // Zip the directory
        ZipUtil.pack(tempDir, tempDir + "/temp.zip");

        // Check if the zip file exists
        assertTrue(new File(tempDir + "/temp.zip").exists());
    }
}