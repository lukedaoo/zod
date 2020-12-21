package com.infamous.zod.storage.converter;

import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.ModelConverter;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileVO;

public class StorageFileConverter extends ModelConverter<StorageFile, StorageFileVO> {

    public StorageFileConverter() {
        super(StorageFileConverter.convertToDTOProcessor(), StorageFileConverter.convertToEntityProcessor());
    }

    private static ConvertProcessor<StorageFile, StorageFileVO> convertToDTOProcessor() {
        return new ConvertProcessor<>(storageFile -> StorageFileVO.builder()
            .id(storageFile.getId())
            .fileName(storageFile.getFileName())
            .fileSize(storageFile.getFileSize())
            .enabled(storageFile.isEnabled())
            .build());
    }

    private static ConvertProcessor<StorageFileVO, StorageFile> convertToEntityProcessor() {
        return new ConvertProcessor<>(vo -> StorageFile.builder()
            .id(vo.getId())
            .fileName(vo.getFileName())
            .fileSize(vo.getFileSize())
            .enabled(vo.isEnabled())
            .extension(vo.getExtension())
            .build());
    }
}