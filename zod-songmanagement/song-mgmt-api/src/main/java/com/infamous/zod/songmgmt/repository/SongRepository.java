package com.infamous.zod.songmgmt.repository;

import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.model.SongVO;
import java.util.Collection;
import java.util.List;

public interface SongRepository {

    boolean upload(SongVO song);

    void upload(Collection<SongVO> songs);

    boolean update(SongVO song);

    void update(Collection<SongVO> songs);

    List<Song> list();
}
