package com.infamous.zod.ftp.um.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.file.FileService;
import com.infamous.framework.file.FileServiceImpl;
import com.infamous.framework.persistence.DataStore;
import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.sensitive.service.ClearTextPasswordEncryptor;
import com.infamous.framework.sensitive.service.DefaultPasswordEncryptor;
import com.infamous.framework.sensitive.service.PasswordEncryptor;
import com.infamous.framework.sensitive.service.SaltedPasswordEncryptor;
import com.infamous.zod.base.jpa.ZodEntityDataStoreManager;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.um.FTPUserDAO;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

class FTPDataStoreConfigTest {

    private static final String ROOT = "src/test/resources";

    private FTPDataStoreConfig m_ftpDataStoreConfig;
    private DataStoreManager m_dataStoreManager;
    private FTPServerConfigProperties m_serverConfigProperties;
    private FileService m_fileService;

    @BeforeEach
    public void setup() {
        m_ftpDataStoreConfig = new FTPDataStoreConfig();
        m_dataStoreManager = new ZodEntityDataStoreManager();
        m_serverConfigProperties = mock(FTPServerConfigProperties.class);
        m_fileService = new FileServiceImpl();
    }

    @AfterEach
    public void tearDown() {
        m_dataStoreManager.destroy();
        m_fileService.delete(ROOT + "/ftp");
    }

    @Test
    public void testCreateEMF() {
        LocalContainerEntityManagerFactoryBean emf = m_ftpDataStoreConfig.createEmf();
        assertNotNull(emf);
        assertEquals("ftp-ds", emf.getPersistenceUnitName());
    }

    @Test
    public void testCreateDataStore() {
        DataStore dataStore = m_ftpDataStoreConfig.createFTPDataStore(m_dataStoreManager);
        assertNotNull(dataStore);
        assertTrue(m_dataStoreManager.isRegistered("FTP_DS"));
    }

    @Test
    public void testCreateFTPUserDAO() {
        assertNotNull(m_ftpDataStoreConfig.createFTPUserDAO(m_dataStoreManager));
    }

    @Test
    public void testCreatePasswordEncryptor() {
        checkCreatePasswordEncryptor("clear", ClearTextPasswordEncryptor.class);
        checkCreatePasswordEncryptor("md5", DefaultPasswordEncryptor.class);
        checkCreatePasswordEncryptor("salted", SaltedPasswordEncryptor.class);

        when(m_serverConfigProperties.getEncryptorStrategy()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
            () -> m_ftpDataStoreConfig.createPasswordEncryptor(m_serverConfigProperties));

        when(m_serverConfigProperties.getEncryptorStrategy()).thenReturn("");
        assertThrows(IllegalArgumentException.class,
            () -> m_ftpDataStoreConfig.createPasswordEncryptor(m_serverConfigProperties));

        when(m_serverConfigProperties.getEncryptorStrategy()).thenReturn("ABC");
        assertThrows(IllegalArgumentException.class,
            () -> m_ftpDataStoreConfig.createPasswordEncryptor(m_serverConfigProperties));
    }


    private void checkCreatePasswordEncryptor(String strategy, Class<?> expectedObject) {
        when(m_serverConfigProperties.getEncryptorStrategy()).thenReturn(strategy);
        PasswordEncryptor pe = m_ftpDataStoreConfig.createPasswordEncryptor(m_serverConfigProperties);
        assertTrue(pe.getClass().isAssignableFrom(expectedObject));
    }

    @Test
    public void testCreateUserManger() {
        when(m_serverConfigProperties.getEncryptorStrategy()).thenReturn("clear");
        when(m_serverConfigProperties.getRootFolder()).thenReturn(ROOT + "/ftp");
        FTPUserDAO dao = m_ftpDataStoreConfig.createFTPUserDAO(m_dataStoreManager);
        PasswordEncryptor pe = m_ftpDataStoreConfig.createPasswordEncryptor(m_serverConfigProperties);

        assertNotNull(m_ftpDataStoreConfig.createUserManger(dao, pe, m_fileService, m_serverConfigProperties));
        assertTrue(Files.exists(Path.of(ROOT + "/ftp")));
    }
}