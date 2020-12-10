package com.infamous.zod.ftp.um.impl;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.dao.AbstractDAO;
import com.infamous.zod.ftp.model.FTPUser;
import com.infamous.zod.ftp.model.FTPUserKey;
import com.infamous.zod.ftp.um.FTPDataStore;
import com.infamous.zod.ftp.um.FTPUserDAO;

public class FTPUserDAOImpl extends AbstractDAO<FTPUser, FTPUserKey> implements FTPUserDAO {

    public FTPUserDAOImpl(DataStoreManager dataStoreManager) {
        super(dataStoreManager, FTPUser.class, FTPDataStore.DS_NAME);
    }
}
