package com.infamous.framework.file;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface FileService {

    Path createDirection(String direction);

    long store(Path path, InputStream is, String fileName);

    String getFilePath(Path path, String fileName);

    InputStream getFile(Path path, String fileName);

    byte[] getFileAsByteArray(Path path, String fileName);

    List<String> listAll(Path location);
}
