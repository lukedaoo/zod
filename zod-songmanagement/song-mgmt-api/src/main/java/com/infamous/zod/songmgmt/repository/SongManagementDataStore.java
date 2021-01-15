package com.infamous.zod.songmgmt.repository;

import com.infamous.framework.persistence.EntityDataStore;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SongManagementDataStore extends EntityDataStore {

    public static final String DS_NAME = "SONG_MGMT_DS";

    @PersistenceContext(unitName = "song-mgmt-ds")
    private EntityManager m_entityManager;

    public SongManagementDataStore() {

    }

    @Override
    public EntityManager getEntityManager() {
        return m_entityManager;
    }

    @Override
    public String getDataStoreName() {
        return DS_NAME;
    }
}
