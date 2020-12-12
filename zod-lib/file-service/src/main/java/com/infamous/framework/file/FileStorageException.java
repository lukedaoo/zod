package com.infamous.framework.file;

public class FileStorageException extends RuntimeException {

    public FileStorageException() {
        super();
    }

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(Throwable e) {
        super(e);
    }

    public FileStorageException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
