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

    private Path m_root;

    @Override
    public Path getRootFolder() {
        return m_root;
    }

    @Override
    public void setRootFolder(Path path) {
        LOGGER.debug("Using folder [{}] as root for File Service", path);
        m_root = path;
    }

    @Override
    public Path createDirectory(String direction) {
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
    public boolean delete(String location) {
        Path path = Paths.get(location);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileStorageException("Failed to deleting [" + location + "]", e);
        }
        return false;
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
            LOGGER.trace("Writes " + target + " [" + bytes + "] (bytes) to the filesystem");

            return bytes;
        } catch (IOException e) {
            throw new FileStorageException("Failed to storage file [" + fileName + "]", e);
        }
    }

    @Override
    public long store(InputStream is, String fileName) {
        return store(m_root, is, fileName);
    }

    @Override
    public String getFilePath(Path path, String fileName) {
        check(path == null, "Path not found");
        check(isEmpty(fileName), "Target file name is null or empty");

        return path.resolve(fileName).toString();
    }

    @Override
    public String getFilePath(String fileName) {
        return getFilePath(m_root, fileName);
    }

    @Override
    public InputStream getFile(Path path, String fileName) {
        File file = getFilePhysical(path, fileName);
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            throw new FileStorageException("Error while reading file [" + fileName + "]", e);
        }
    }

    @Override
    public InputStream getFile(String fileName) {
        return getFile(m_root, fileName);
    }

    @Override
    public byte[] getFileAsByteArray(Path path, String fileName) {
        File file = getFilePhysical(path, fileName);
        byte[] bytesArray = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytesArray);
        } catch (IOException e) {
            throw new FileStorageException("Error while reading file [" + fileName + "]", e);
        }
        return bytesArray;
    }

    @Override
    public byte[] getFileAsByteArray(String fileName) {
        return getFileAsByteArray(m_root, fileName);
    }

    @Override
    public File getFilePhysical(Path path, String fileName) {
        String filePath = getFilePath(path, fileName);
        return new File(filePath);
    }

    @Override
    public File getFilePhysical(String fileName) {
        File file = getFilePhysical(m_root, fileName);
        check(!file.exists(), "File [" + fileName + "] does not exist");
        return file;
    }

    @Override
    public List<String> listAll(Path location) {
        check(location == null, "Location folder is null");
        try {
            return Files.walk(location)
                .filter(path -> !path.equals(location))
                .map(path -> location.relativize(path).toString())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileStorageException("Failed to read stored files at " + location, e);
        }
    }

    @Override
    public List<String> listAll() {
        return listAll(m_root);
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
