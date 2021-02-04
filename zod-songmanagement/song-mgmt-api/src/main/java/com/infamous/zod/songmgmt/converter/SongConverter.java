package com.infamous.zod.songmgmt.converter;

import com.infamous.framework.converter.ConvertProcessor;
import com.infamous.framework.converter.ModelConverter;
import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.model.SongVO;

public class SongConverter extends ModelConverter<Song, SongVO> {

    public SongConverter() {
        super(SongConverter.convertToSongVo(), SongConverter.convertToSong());
    }

    private static ConvertProcessor<Song, SongVO> convertToSongVo() {
        return new ConvertProcessor<>(song -> SongVO.builder()
            .id(song.getId())
            .title(song.getTitle())
            .fileId(song.getFileId())
            .enabled(song.isEnabled())
            .build());
    }

    private static ConvertProcessor<SongVO, Song> convertToSong() {
        return new ConvertProcessor<>(vo -> Song.builder()
            .id(vo.getId())
            .title(vo.getTitle())
            .fileId(vo.getFileId())
            .enabled(vo.isEnabled())
            .build());
    }
}
