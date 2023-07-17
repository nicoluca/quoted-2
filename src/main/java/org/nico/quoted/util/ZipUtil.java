package org.nico.quoted.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private static final Logger logger = Logger.getLogger(ZipUtil.class.getName());

    // From https://stackoverflow.com/a/32052016/11292952
    public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        if (sourceDirPath == null || sourceDirPath.isBlank())
            throw new IllegalArgumentException("Source directory path cannot be null or blank");

        if (zipFilePath == null || zipFilePath.isBlank())
            throw new IllegalArgumentException("Zip file path cannot be null or blank");

        Path p = Files.createFile(Paths.get(zipFilePath));

        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            logger.severe("Error zipping directory " + sourceDirPath + " to file " + zipFilePath);
                        }
                    });
        }
    }
}
