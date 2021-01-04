package com.infamous.zod.storage.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageFileConverterTest {

    private StorageFileConverter m_converter;

    @BeforeEach
    public void setup() {
        m_converter = new StorageFileConverter();
    }

    @Test
    public void testConvertToVO() {
        StorageFileVO vo = m_converter.toDTO(mockEntity());

        assertNotNull(vo);
        assertEquals("1", vo.getId());
        assertTrue(vo.isEnabled());
        assertEquals("file-name.txt", vo.getFileName());
    }

    @Test
    public void testConvertToEntity() {
        StorageFile entity = m_converter.toEntity(mockDTO());

        assertNotNull(entity);
        assertEquals("1", entity.getId());
        assertTrue(entity.isEnabled());
        assertEquals("file-name.txt", entity.getFileName());
    }

    private StorageFile mockEntity() {
        return StorageFile.builder()
            .id("1")
            .enabled(true)
            .fileName("file-name.txt")
            .build();
    }


    private StorageFileVO mockDTO() {
        return StorageFileVO.builder()
            .id("1")
            .enabled(true)
            .fileName("file-name.txt")
            .build();
    }

}