package com.infamous.zod.ftp.um.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.infamous.framework.sensitive.core.MessageDigestAlgorithm;
import com.infamous.framework.sensitive.service.PasswordEncryptor;
import com.infamous.framework.sensitive.service.SaltedPasswordEncryptor;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.model.FTPPassword;
import com.infamous.zod.ftp.model.FTPUser;
import com.infamous.zod.ftp.model.FTPUserKey;
import com.infamous.zod.ftp.model.FTPUserName;
import com.infamous.zod.ftp.um.FTPUserDAO;
import java.util.Arrays;
import java.util.Collections;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FTPUserManagerRepositoryTestWithDummyData {

    private FTPUserDAO m_dao;
    private PasswordEncryptor m_passwordEncryptor;
    FTPUserManagerRepository m_repo;

    @BeforeEach
    public void setup() {
        m_dao = mock(FTPUserDAO.class);
        m_passwordEncryptor = mock(PasswordEncryptor.class);
        m_repo = new FTPUserManagerRepository(m_dao, m_passwordEncryptor, null);
    }

    @Test
    void getAllUserNames() throws FtpException {

        when(m_dao.findAll()).thenReturn(Arrays.asList(
            new FTPUser(new FTPUserName("admin"), new FTPPassword("password")),
            new FTPUser(new FTPUserName("admin1"), new FTPPassword("password"))
        ));

        String[] actualUserNames = m_repo.getAllUserNames();
        String[] expectedUserNames = new String[]{"admin", "admin1"};

        assertEquals(expectedUserNames.length, actualUserNames.length);
        assertEquals(expectedUserNames[0], actualUserNames[0]);
        assertEquals(expectedUserNames[1], actualUserNames[1]);
    }

    @Test
    void getAllUserNames_WhenDAOReturnsNull() throws FtpException {
        FTPUserManagerRepository repo = new FTPUserManagerRepository(m_dao, m_passwordEncryptor, null);

        when(m_dao.findAll()).thenReturn(null);

        String[] actualUserNames = repo.getAllUserNames();
        assertEquals(0, actualUserNames.length);
    }

    @Test
    public void testHashPassword() {
        m_repo.hashPassword("password");

        verify(m_passwordEncryptor).encrypt(eq("password"));
    }

    @Test
    public void testHashPassword_SaltedEncrytor() {
        SaltedPasswordEncryptor passwordEncryptor = mock(SaltedPasswordEncryptor.class);
        FTPServerConfigProperties serverConfig = mock(FTPServerConfigProperties.class);
        when(serverConfig.getSalted()).thenReturn("salted");

        m_repo = new FTPUserManagerRepository(m_dao, passwordEncryptor, serverConfig);

        m_repo.hashPassword("password");

        verify(passwordEncryptor).encrypt(eq(MessageDigestAlgorithm.MD5), eq("password"), eq("salted"));
    }


    @Test
    public void testGetUserByName() throws FtpException {
        FTPUser mockUser = FTPUser.builder()
            .username("admin")
            .password("password")
            .isAdmin(true)
            .build();
        when(m_dao.findById(any(FTPUserKey.class)))
            .thenReturn(mockUser);

        User user = m_repo.getUserByName("admin");
        assertTrue(user instanceof FTPUser);
        assertEquals("admin", user.getName());
        assertEquals(3, user.getAuthorities().size());
    }

    @Test
    public void testGetUserByName_DoesNotExist() throws FtpException {
        when(m_dao.findById(any(FTPUserKey.class)))
            .thenReturn(null);

        FtpException exp = assertThrows(FtpException.class, () -> m_repo.getUserByName("admin"));
        assertEquals("User [admin] doesn't exist", exp.getMessage());
    }


    @Test
    public void testDelete() {
        m_repo.delete("Admin");

        verify(m_dao).deleteByID(eq(new FTPUserKey("Admin")));
    }

    @Test
    public void testDoesExist() {
        m_repo.doesExist("Admin");

        verify(m_dao).findById(new FTPUserKey("Admin"));
    }

    @Test
    public void testSave() throws FtpException {
        FTPUser user = FTPUser.builder()
            .username("admin")
            .password("password")
            .workspace("/root/")
            .build();
        m_repo.save(user);

        verify(m_dao).merge(eq(user));
    }

    @Test
    public void testAuthentication_WrongAuthentication() throws Exception {
        assertThrows(AuthenticationFailedException.class, () -> m_repo.authenticate(new AnonymousAuthentication()));
    }

    @Test
    public void testAuthentication_InvalidUserName() throws Exception {
        AuthenticationFailedException exp = assertThrows(AuthenticationFailedException.class, () ->
            m_repo.authenticate(new UsernamePasswordAuthentication("", "password")));

        assertEquals("You must provide a username", exp.getMessage());
    }

    @Test
    public void testAuthentication_InvalidPassword() throws Exception {
        AuthenticationFailedException exp = assertThrows(AuthenticationFailedException.class, () ->
            m_repo.authenticate(new UsernamePasswordAuthentication("admin", "")));

        assertEquals("You must provide a password", exp.getMessage());
    }

    @Test
    public void testAuthentication_ButUserDoesNotExist() {
        when(m_dao.findById(new FTPUserKey("admin"))).thenReturn(null);
        AuthenticationFailedException exp = assertThrows(AuthenticationFailedException.class, () ->
            m_repo.authenticate(new UsernamePasswordAuthentication("admin", "password")));

        assertEquals("User [admin] doesn't exist", exp.getMessage());
    }

    @Test
    public void testAuthentication_ButPasswordIsWrong() throws Exception {
        FTPUser user = FTPUser.builder()
            .username("admin")
            .password("paSsWorDWasHashed")
            .build();
        when(m_dao.findById(new FTPUserKey("admin"))).thenReturn(user);

        when(m_passwordEncryptor.matches("password", "paSsWorDWasHashed")).thenReturn(false);

        AuthenticationFailedException exp = assertThrows(AuthenticationFailedException.class, () ->
            m_repo.authenticate(new UsernamePasswordAuthentication("admin", "password")));

        assertEquals("Authentication failed", exp.getMessage());
    }

    @Test
    public void testAuthentication_Successfully() throws Exception {
        FTPUser user = FTPUser.builder()
            .username("admin")
            .password("paSsWorDWasHashed")
            .build();
        when(m_dao.findById(new FTPUserKey("admin"))).thenReturn(user);

        when(m_passwordEncryptor.matches("password", "paSsWorDWasHashed")).thenReturn(true);

        User userRes = m_repo.authenticate(new UsernamePasswordAuthentication("admin", "password"));

        assertNotNull(userRes);
        assertEquals("admin", userRes.getName());
    }

    @Test
    public void testGetAdminName() {
        when(m_dao.findByNativeQuery("SELECT username FROM FTPUser WHERE isAdmin = 'true'")).thenReturn(
            Collections.singletonList("admin"));

        assertEquals("admin", m_repo.getAdminName());
        verify(m_dao).findByNativeQuery(any());
    }

    @Test
    public void testIsAdmin() {
        FTPUser user = FTPUser.builder()
            .username("admin")
            .password("paSsWorDWasHashed")
            .isAdmin(true)
            .build();
        when(m_dao.findById(new FTPUserKey("admin"))).thenReturn(user);

        assertTrue(m_repo.isAdmin("admin"));
        verify(m_dao).findById(eq(new FTPUserKey("admin")));
    }
}