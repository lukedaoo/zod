package com.infamous.framework.file;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface FileService {

    Path getRootFolder();

    void setRootFolder(Path path);

    Path createDirectory(String direction);

    boolean delete(Path path, String fileName);

    boolean delete(String fileName);

    long store(Path path, InputStream is, String fileName);

    long store(InputStream is, String fileName);

    String getFilePath(Path path, String fileName);

    String getFilePath(String fileName);

    InputStream getFile(Path path, String fileName);

    InputStream getFile(String fileName);

    byte[] getFileAsByteArray(Path path, String fileName);

    byte[] getFileAsByteArray(String fileName);

    File getFilePhysical(Path path, String fileName);

    File getFilePhysical(String fileName);

    List<String> listAll(Path location);

    List<String> listAll();
}
