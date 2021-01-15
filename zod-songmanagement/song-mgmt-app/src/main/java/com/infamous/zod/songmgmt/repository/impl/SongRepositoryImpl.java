package com.infamous.zod.songmgmt.repository.impl;

import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.model.SongVO;
import com.infamous.zod.songmgmt.repository.SongRepository;
import java.util.Collection;
import java.util.List;

public class SongRepositoryImpl implements SongRepository {

    @Override
    public boolean upload(SongVO song) {
        return false;
    }

    @Override
    public void upload(Collection<SongVO> songs) {

    }

    @Override
    public boolean update(SongVO song) {
        return false;
    }

    @Override
    public void update(Collection<SongVO> songs) {

    }

    @Override
    public List<Song> list() {
        return null;
    }
}
