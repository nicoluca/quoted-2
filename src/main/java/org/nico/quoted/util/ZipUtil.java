package org.nico.quoted.util;

import java.io.*;
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
//
//    public static void zipDirectory(String inputDirectory, String outputZipFile) {
//        if (inputDirectory == null || inputDirectory.isBlank())
//            throw new IllegalArgumentException("Input directory cannot be null or blank");
//
//        if (outputZipFile == null || outputZipFile.isBlank())
//            throw new IllegalArgumentException("Output zip file cannot be null or blank");
//
//        try {
//            logger.info("Zipping directory " + inputDirectory + " to file " + outputZipFile);
//            FileOutputStream fos = new FileOutputStream(outputZipFile);
//            ZipOutputStream zipOut = new ZipOutputStream(fos);
//            File fileToZip = new File(inputDirectory);
//
//            processDirectory(fileToZip, fileToZip.getName(), zipOut);
//
//            zipOut.close();
//            fos.close();
//            logger.info("Zip file has been created successfully.");
//        } catch (IOException e) {
//            logger.severe("Error zipping directory " + inputDirectory + " to file " + outputZipFile);
//            e.printStackTrace();
//        }
//    }
//
//    private static void processDirectory(File directory, String parentDirectoryName, ZipOutputStream zipOut) throws IOException {
//
//        if (!directory.isDirectory()) {
//            logger.warning("Not a directory: " + directory.getName());
//            return;
//        }
//
//        File[] files = directory.listFiles();
//
//        if (files == null) {
//            logger.warning("No files found in directory " + directory.getName());
//            return;
//        }
//
//        logger.info("Processing directory " + directory.getName() + " with " + files.length + " files...");
//
//        for (File file : files) {
//            logger.info("Processing " + file.getName() + "...");
//
//            if (file.isDirectory()) {
//                processDirectory(file, parentDirectoryName + "/" + file.getName(), zipOut);
//                continue;
//            }
//
//            FileInputStream fis = new FileInputStream(file);
//            String zipEntryName = parentDirectoryName + "/" + file.getName();
//            ZipEntry zipEntry = new ZipEntry(zipEntryName);
//            zipOut.putNextEntry(zipEntry);
//
//            byte[] bytes = new byte[1024];
//            int length;
//            while ((length = fis.read(bytes)) >= 0) {
//                zipOut.write(bytes, 0, length);
//            }
//
//            fis.close();
//        }
//    }
//}
