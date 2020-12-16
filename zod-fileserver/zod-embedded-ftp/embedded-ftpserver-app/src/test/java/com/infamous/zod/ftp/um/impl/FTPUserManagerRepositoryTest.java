package com.infamous.zod.ftp.um.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.sensitive.service.PasswordEncryptor;
import com.infamous.zod.ftp.model.FTPPassword;
import com.infamous.zod.ftp.model.FTPUser;
import com.infamous.zod.ftp.model.FTPUserName;
import com.infamous.zod.ftp.um.FTPUserDAO;
import java.util.Arrays;
import org.apache.ftpserver.ftplet.FtpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FTPUserManagerRepositoryTest {

    private FTPUserDAO m_dao;
    private PasswordEncryptor m_passwordEncryptor;

    @BeforeEach
    public void setup() {
        m_dao = mock(FTPUserDAO.class);
        m_passwordEncryptor = mock(PasswordEncryptor.class);
    }

    @Test
    void getAllUserNames() throws FtpException {
        FTPUserManagerRepository repo = new FTPUserManagerRepository(m_dao, m_passwordEncryptor, null);

        when(m_dao.findAll()).thenReturn(Arrays.asList(
            new FTPUser(new FTPUserName("admin"), new FTPPassword("password")),
            new FTPUser(new FTPUserName("admin1"), new FTPPassword("password"))
        ));

        String[] actualUserNames = repo.getAllUserNames();
        String[] expectedUserNames = new String[]{"admin", "admin1"};

        assertEquals(expectedUserNames.length, actualUserNames.length);
        assertEquals(expectedUserNames[0], actualUserNames[0]);
        assertEquals(expectedUserNames[1], actualUserNames[1]);
    }

    @Test
    void getAllUserNames2() throws FtpException {
        FTPUserManagerRepository repo = new FTPUserManagerRepository(m_dao, m_passwordEncryptor, null);

        when(m_dao.findAll()).thenReturn(null);

        String[] actualUserNames = repo.getAllUserNames();
        assertEquals(0, actualUserNames.length);
    }
}