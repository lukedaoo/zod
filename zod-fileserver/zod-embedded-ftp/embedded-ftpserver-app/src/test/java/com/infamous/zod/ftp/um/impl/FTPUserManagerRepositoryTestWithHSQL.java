package com.infamous.zod.ftp.um.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.EntityDataStore;
import com.infamous.framework.sensitive.service.SaltedPasswordEncryptor;
import com.infamous.zod.base.jpa.ZodEntityDataStoreManager;
import com.infamous.zod.ftp.FTPServerConfigProperties;
import com.infamous.zod.ftp.model.FTPUser;
import com.infamous.zod.ftp.um.FTPDataStore;
import com.infamous.zod.ftp.um.FTPUserDAO;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FTPUserManagerRepositoryTestWithHSQL {

    private DataStoreManager m_dsm;
    private FTPUserDAO m_dao;
    private FTPUserManagerRepository m_repo = new FTPUserManagerRepository();
    private SaltedPasswordEncryptor m_pe;
    private FTPServerConfigProperties m_serverConfig;

    @BeforeEach
    public void setup() throws Exception {
        m_dsm = new ZodEntityDataStoreManager();
        EMF emf = new EMF("ftp-hsql-ds");
        FTPHsqlDataStore dataStore = new FTPHsqlDataStore(emf.createNewEntityManager());
        m_dsm.register(FTPDataStore.DS_NAME, dataStore);
        m_dao = new FTPUserDAOImpl(m_dsm);

        m_serverConfig = mock(FTPServerConfigProperties.class);
        when(m_serverConfig.getSalted()).thenReturn("salted");
        m_pe = new SaltedPasswordEncryptor();
        m_repo = new FTPUserManagerRepository(m_dao, m_pe, m_serverConfig);

        executeInTx(() -> {
            m_dao.deleteAll();
        });
    }


    @Test
    public void testSave() throws Exception {
        FTPUser user = FTPUser.builder()
            .username("admin")
            .password("password")
            .workspace("/root/")
            .isAdmin(true)
            .build();

        executeInTx(() -> {
            m_repo.save(user);
        });

        executeInTx(() -> {
            String admin = m_repo.getAdminName();
            assertEquals("admin", admin);
        });
    }


    @Test
    void getAllUserNames() throws Exception {
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password("password")
                .workspace("/root/")
                .isAdmin(true)
                .build();

            FTPUser user2 = FTPUser.builder()
                .username("luke")
                .password("password")
                .workspace("/root/")
                .isAdmin(false)
                .build();

            m_repo.save(user1);
            m_repo.save(user2);
        });

        executeInTx(() -> {
            String[] res = m_repo.getAllUserNames();
            assertEquals(2, res.length);
            assertEquals("admin", res[0]);
            assertEquals("luke", res[1]);
        });
    }

    @Test
    void getAllUserNames_WhenDAOReturnsNull() throws Exception {
        executeInTx(() -> {
            String[] res = m_repo.getAllUserNames();
            assertEquals(0, res.length);
        });
    }

    @Test
    public void testHashPassword() {

        String hashed = m_repo.hashPassword("password");

        assertTrue(hashed.contains("salted"));
    }

    @Test
    public void testGetUserByName() throws Exception {
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password("password")
                .workspace("/root/")
                .isAdmin(true)
                .build();
            m_repo.save(user1);
        });

        executeInTx(() -> {
            User res = m_repo.getUserByName("admin");
            assertEquals("admin", res.getName());
        });
    }

    @Test
    public void testDelete() throws Exception {
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password("password")
                .workspace("/root/")
                .isAdmin(true)
                .build();
            m_repo.save(user1);
        });

        executeInTx(() -> {
            m_repo.delete("admin");
        });

        executeInTx(() -> {
            FtpException exp = assertThrows(FtpException.class, () -> m_repo.getUserByName("admin"));
            assertEquals("User [admin] doesn't exist", exp.getMessage());
        });
    }

    @Test
    public void testDoesExist() throws Exception {
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password("password")
                .workspace("/root/")
                .isAdmin(true)
                .build();
            m_repo.save(user1);
        });

        executeInTx(() -> {
            User res = m_repo.getUserByName("admin");
            assertEquals("admin", res.getName());
        });

        executeInTx(() -> {
            assertTrue(m_repo.doesExist("admin"));
        });
    }

    @Test
    public void testAuthentication_ButUserDoesNotExist() throws Exception {
        executeInTx(() -> {
            assertFalse(m_repo.doesExist("admin"));
        });
        executeInTx(() -> {
            AuthenticationFailedException exp = assertThrows(AuthenticationFailedException.class, () ->
                m_repo.authenticate(new UsernamePasswordAuthentication("admin", "password")));
            assertEquals("User [admin] doesn't exist", exp.getMessage());
        });
    }

    @Test
    public void testAuthentication_ButPasswordIsWrong() throws Exception {
        final String hashedPassword = m_repo.hashPassword("password");
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password(hashedPassword)
                .workspace("/root/")
                .isAdmin(true)
                .build();
            m_repo.save(user1);
        });

        executeInTx(() -> {
            User res = m_repo.getUserByName("admin");
            assertEquals("admin", res.getName());
            assertTrue(res instanceof FTPUser);
            assertNotEquals("password", res.getPassword());
        });

        executeInTx(() -> {
            AuthenticationFailedException exp = assertThrows(AuthenticationFailedException.class, () ->
                m_repo.authenticate(new UsernamePasswordAuthentication("admin", "password1")));
            assertEquals("Authentication failed", exp.getMessage());
        });
    }

    @Test
    public void testAuthentication_Successfully() throws Exception {
        final String hashedPassword = m_repo.hashPassword("password");
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password(hashedPassword)
                .workspace("/root/")
                .isAdmin(true)
                .build();
            m_repo.save(user1);
        });

        executeInTx(() -> {
            User res = m_repo.getUserByName("admin");
            assertEquals("admin", res.getName());
            assertTrue(res instanceof FTPUser);
            assertNotEquals("password", res.getPassword());
        });

        executeInTx(() -> {
            User user = m_repo.authenticate(new UsernamePasswordAuthentication("admin", "password"));
            assertEquals("admin", user.getName());
        });
    }

    @Test
    public void testGetAdminName_WhenUserIsNotAdmin() throws Exception {
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password("password")
                .workspace("/root/")
                .isAdmin(false)
                .build();
            m_repo.save(user1);
        });
        executeInTx(() -> {
            String admin = m_repo.getAdminName();
            assertNull(admin);
        });
    }

    @Test
    public void testIsAdmin() throws Exception {
        executeInTx(() -> {
            FTPUser user1 = FTPUser.builder()
                .username("admin")
                .password("password")
                .workspace("/root/")
                .isAdmin(true)
                .build();
            m_repo.save(user1);
        });
        executeInTx(() -> {
            assertTrue(m_repo.isAdmin("admin"));
        });
    }

    private void executeInTx(Template template) throws Exception {
        m_dsm.getDatastore(FTPDataStore.DS_NAME).getEntityManager().getTransaction().begin();
        template.execute();
        m_dsm.getDatastore(FTPDataStore.DS_NAME).getEntityManager().getTransaction().commit();
    }

    interface Template {

        void execute() throws Exception;
    }
}

class FTPHsqlDataStore extends EntityDataStore {

    private EntityManager m_entityManager;

    public FTPHsqlDataStore(EntityManager entityManager) {
        m_entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return m_entityManager;
    }

    @Override
    public String getDataStoreName() {
        return FTPDataStore.DS_NAME;
    }
}

class EMF {

    private EntityManagerFactory m_factory;

    public EMF(String persistenceUnitName, Map<String, String> properties) {
        initEntityMgr(persistenceUnitName, properties);
    }

    public EMF(String persistenceUnitName) {
        initEntityMgr(persistenceUnitName, null);
    }

    private void initEntityMgr(String persistenceUnitName, Map<String, String> properties) {
        HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
        m_factory = persistenceProvider.createEntityManagerFactory(persistenceUnitName, properties);
    }

    public EntityManager createNewEntityManager() {
        return m_factory.createEntityManager();
    }
}