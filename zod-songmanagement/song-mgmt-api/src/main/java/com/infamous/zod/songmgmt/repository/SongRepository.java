package com.infamous.zod.songmgmt.repository;

import com.infamous.zod.base.rest.entity.UploadResult;
import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.model.SongVO;
import java.util.Collection;
import java.util.List;

public interface SongRepository {

    boolean upload(SongVO song);

    UploadResult<SongVO> upload(Collection<SongVO> songs);

    boolean update(SongVO song);

    void update(Collection<SongVO> songs);

    List<SongVO> list();
}
