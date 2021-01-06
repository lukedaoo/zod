package com.infamous.zod.storage.controller.impl;

import com.infamous.framework.file.FileStorageException;
import com.infamous.zod.storage.model.StorageFileVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class CommonStorageFileControllerImpl {

    public static byte[] getFileAsByteArray(String fileLocation) {
        File file = new File(fileLocation);
        byte[] bytesArray = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytesArray);
        } catch (IOException e) {
            throw new FileStorageException("File not found");
        }
        return bytesArray;
    }

    public static StorageFileVO mockDTO() {
        return mockDTO("1");
    }

    public static StorageFileVO mockDTO(String id) {
        return StorageFileVO.builder()
            .fileName("my_war.txt")
            .id(id)
            .build();
    }
}
