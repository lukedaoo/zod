package com.infamous.zod.songmgmt.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.model.SongVO;
import org.junit.jupiter.api.Test;

class SongConverterTest {

    private SongConverter m_songConverter = new SongConverter();


    @Test
    void testConvertToDto() {
        SongVO vo = m_songConverter.toDTO(mockEntity());
        assertEquals("title", vo.getTitle());
        assertEquals("1234-567", vo.getFileId());
        assertNull(vo.getId());
    }

    @Test
    void testConvertToEntity() {
        Song entity = m_songConverter.toEntity(mockDto());

        assertEquals("title", entity.getTitle());
        assertEquals("1234", entity.getFileId());
        assertNull(entity.getId());
    }


    private SongVO mockDto() {
        return SongVO.builder()
            .title("title")
            .fileId("1234")
            .build();
    }


    private Song mockEntity() {
        return Song.builder()
            .title("title")
            .fileId("1234-567")
            .build();
    }
}