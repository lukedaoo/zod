package com.infamous.framework.file;

import com.infamous.framework.logging.ZodLogger;
import com.infamous.framework.logging.ZodLoggerUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

public class FileServiceImpl implements FileService {

    private static final ZodLogger LOGGER = ZodLoggerUtil.getLogger(FileServiceImpl.class, "file.service");

    @Override
    public Path createDirection(String direction) {
        Path path = Paths.get(direction);
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            if (!(e instanceof FileAlreadyExistsException)) {
                LOGGER.error("Error while creating direction [" + path + "]", e);
            }
            return path;
        }
        return path;
    }

    @Override
    public long store(Path path, InputStream is, String fileName) {
        check(path == null, "Path not found");
        check(is == null, "InputStream is null");
        check(isEmpty(fileName), "Target file name is null or empty");
        Path target = path.resolve(fileName);
        try {
            if (Files.exists(target)) {
                LOGGER.info("The file [" + target + "] will replaced by new file");
            }
            long bytes = Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.trace("Writes " + target + " [" + bytes + "] to the filesystem");

            return bytes;
        } catch (IOException e) {
            throw new FileStorageException("Failed to storage file [" + fileName + "]", e);
        }
    }

    @Override
    public String getFilePath(Path path, String fileName) {
        check(path == null, "Path not found");
        check(isEmpty(fileName), "Target file name is null or empty");

        return path.resolve(fileName).toString();
    }

    @Override
    public InputStream getFile(Path path, String fileName) {
        String filePath = getFilePath(path, fileName);
        File file = new File(filePath);
        check(!file.exists(), "File [" + fileName + "] does not exist");
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            throw new FileStorageException("Error while reading file [" + fileName + "]", e);
        }
    }

    @Override
    public byte[] getFileAsByteArray(Path path, String fileName) {
        String filePath = getFilePath(path, fileName);
        File file = new File(filePath);
        byte[] bytesArray = new byte[(int) file.length()];

        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray);
            fis.close();

        } catch (IOException e) {
            throw new FileStorageException("Error while reading file [" + fileName + "]", e);
        }
        return bytesArray;
    }

    @Override
    public List<String> listAll(Path location) {
        check(location == null, "Location folder is null");
        try {
            return Files.walk(location, 1)
                .filter(path -> !path.equals(location))
                .map(path -> location.relativize(path).getFileName().toString())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileStorageException("Failed to read stored files at " + location, e);
        }
    }


    private void check(boolean condition, String message) {
        if (condition) {
            throw new FileStorageException(message);
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
