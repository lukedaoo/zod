package com.infamous.zod.songmgmt.repository.impl;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.dao.AbstractDAO;
import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.repository.SongDAO;
import com.infamous.zod.songmgmt.repository.SongManagementDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SongDAOImpl extends AbstractDAO<Song, Long> implements SongDAO {

    @Autowired
    public SongDAOImpl(DataStoreManager dataStoreManager) {
        super(dataStoreManager, Song.class, SongManagementDataStore.DS_NAME);
    }
}
