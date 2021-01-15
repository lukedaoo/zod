package com.infamous.zod.songmgmt.repository.impl;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.dao.AbstractDAO;
import com.infamous.zod.songmgmt.model.Song;
import com.infamous.zod.songmgmt.model.SongKey;
import com.infamous.zod.songmgmt.repository.SongDAO;
import com.infamous.zod.songmgmt.repository.SongManagementDataStore;

public class SongDAOImpl extends AbstractDAO<Song, SongKey> implements SongDAO {

    public SongDAOImpl(DataStoreManager dataStoreManager) {
        super(dataStoreManager, Song.class, SongManagementDataStore.DS_NAME);
    }
}
