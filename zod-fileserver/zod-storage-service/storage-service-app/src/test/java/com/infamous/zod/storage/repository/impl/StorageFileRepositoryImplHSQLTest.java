package com.infamous.zod.storage.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.file.FileService;
import com.infamous.framework.file.FileServiceImpl;
import com.infamous.framework.file.FileStorageException;
import com.infamous.framework.persistence.DataStore;
import com.infamous.framework.persistence.DataStoreManager;
import com.infamous.framework.persistence.EntityDataStore;
import com.infamous.zod.base.jpa.ZodEntityDataStoreManager;
import com.infamous.zod.ftp.um.FTPDataStore;
import com.infamous.zod.storage.converter.StorageFileConverter;
import com.infamous.zod.storage.model.StorageFile;
import com.infamous.zod.storage.model.StorageFileVO;
import com.infamous.zod.storage.repository.StorageFileDAO;
import com.infamous.zod.storage.repository.StorageFileDataStore;
import com.infamous.zod.base.rest.entity.UploadResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StorageFileRepositoryImplHSQLTest {

    private static final String ROOT = "src/test/resources";

    private StorageFileRepositoryImpl m_repo;
    private StorageFileDAO m_fileDAO;
    private FileService m_fileService;
    private StorageFileConverter m_converter;
    private DataStoreManager m_dsm;

    @BeforeEach
    public void setup() {
        EMF emf = new EMF("sf-hsql-ds");
        DataStore ds = new SFHsqlDataStore(emf.createNewEntityManager());

        m_dsm = new ZodEntityDataStoreManager();
        m_dsm.register(StorageFileDataStore.DS_NAME, ds);

        m_fileDAO = new StorageFileDAOImpl(m_dsm);
        m_fileService = new FileServiceImpl();
        m_fileService.setRootFolder(Path.of(ROOT));
        m_converter = new StorageFileConverter();
        m_repo = new StorageFileRepositoryImpl(m_fileDAO, m_converter, m_fileService);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(ROOT + "/my_war.txt"));
        Files.deleteIfExists(Path.of(ROOT + "/my_war1.txt"));
        Files.deleteIfExists(Path.of(ROOT + "/my_war2.txt"));
        Files.deleteIfExists(Path.of(ROOT + "/my_war3.txt"));
    }

    @Test
    public void testUpload_WithoutContent() {
        StorageFileVO vo = StorageFileVO.builder()
            .content(null)
            .build();

        FileStorageException exp = assertThrows(FileStorageException.class, () -> m_repo.upload(vo));
        assertEquals("Target file name is null or empty", exp.getMessage());
    }

    @Test
    public void testUpload_WithoutFileName() throws FileNotFoundException {
        StorageFileVO vo = StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/my_war.txt"))
            .fileName(null)
            .build();

        FileStorageException exp = assertThrows(FileStorageException.class, () -> m_repo.upload(vo));
        assertEquals("Target file name is null or empty", exp.getMessage());
    }

    @Test
    public void testUpdate_Successfully() throws Exception {
        StorageFileVO vo = StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/my_war.txt"))
            .fileName("my_war.txt")
            .build();

        executeInTx(() -> {
            m_repo.upload(vo);

            assertTrue(Files.exists(Path.of(ROOT + "/my_war.txt")));
            assertTrue(vo.getFileSize() > 0);
            assertNotNull(vo.getId());
        });
    }

    @Test
    public void testUpdate_WhenCanNotSaveToDB_ThrowEx() throws Exception {
        StorageFileVO vo = StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/my_war.txt"))
            .fileName("my_war.txt")
            .build();

        m_fileDAO = mock(StorageFileDAO.class);
        when(m_fileDAO.persist(m_converter.toEntity(vo))).thenThrow(RuntimeException.class);

        m_repo = new StorageFileRepositoryImpl(m_fileDAO, m_converter, m_fileService);
        executeInTx(() -> {
            boolean res = m_repo.upload(vo);
            assertFalse(res);

            assertFalse(Files.exists(Path.of(ROOT + "/my_war.txt")));
        });
    }

    @Test
    public void testUpdate_WhenCanNotSaveToDB_ReturnNullId() throws Exception {
        StorageFileVO vo = StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/my_war.txt"))
            .fileName("my_war.txt")
            .build();

        m_fileDAO = mock(StorageFileDAO.class);
        when(m_fileDAO.persist(m_converter.toEntity(vo))).thenReturn(false);

        m_repo = new StorageFileRepositoryImpl(m_fileDAO, m_converter, m_fileService);
        executeInTx(() -> {
            boolean res = m_repo.upload(vo);
            assertFalse(res);

            assertFalse(Files.exists(Path.of(ROOT + "/my_war.txt")));
        });
    }

    @Test
    public void testMultipleUpload() throws Exception {
        List<StorageFileVO> list =
            Arrays.asList(mockDTO("my_war1.txt"), mockDTO("my_war2.txt"), mockDTO("my_war3.txt"));

        executeInTx(() -> {
            UploadResult res = m_repo.upload(list);

            assertTrue(Files.exists(Path.of(ROOT + "/my_war1.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war2.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war3.txt")));

            assertEquals("success", res.getStatus());
            assertEquals(3, res.getData().size());
        });
    }

    @Test
    public void testMultipleUpload_MultipleTimes_WithSameFiles() throws Exception {
        List<StorageFileVO> list =
            Arrays.asList(mockDTO("my_war1.txt"), mockDTO("my_war2.txt"), mockDTO("my_war3.txt"));

        String[] checksums = new String[2];
        executeInTx(() -> {
            UploadResult res = m_repo.upload(list);

            assertTrue(Files.exists(Path.of(ROOT + "/my_war1.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war2.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war3.txt")));

            assertEquals("success", res.getStatus());
            assertEquals(3, res.getData().size());
            String cs1 = ((StorageFileVO)res.getData().get(0)).getChecksum();
            assertNotNull(cs1);

            checksums[0] = cs1;
        });

        executeInTx(() -> {
            UploadResult res = m_repo.upload(list);

            assertTrue(Files.exists(Path.of(ROOT + "/my_war1.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war2.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war3.txt")));

            assertEquals("success", res.getStatus());
            assertEquals(3, res.getData().size());
            String cs2 = ((StorageFileVO)res.getData().get(0)).getChecksum();
            assertNotNull(cs2);

            checksums[1] = cs2;
        });
        assertEquals(checksums[0], checksums[1]);
    }

    @Test
    public void testMultipleUpload_HaveSomeEx() throws Exception {
        List<StorageFileVO> list =
            Arrays.asList(mockDTO("my_war1.txt"), mockDTO("my_war2.txt"), mockDTO("my_war3.txt"));

        m_fileDAO = mock(StorageFileDAO.class);
        when(m_fileDAO.persist(any(StorageFile.class))).thenReturn(false);

        m_repo = new StorageFileRepositoryImpl(m_fileDAO, m_converter, m_fileService);

        executeInTx(() -> {
            UploadResult res = m_repo.upload(list);

            assertFalse(Files.exists(Path.of(ROOT + "/my_war1.txt")));
            assertFalse(Files.exists(Path.of(ROOT + "/my_war2.txt")));
            assertFalse(Files.exists(Path.of(ROOT + "/my_war3.txt")));

            assertEquals("failure", res.getStatus());
            assertEquals(0, res.getData().size());
        });
    }

    private StorageFileVO mockDTO(String fileName) throws IOException {
        return StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/" + fileName))
            .fileName(fileName)
            .build();
    }

    @Test
    public void testDownload() throws Exception {
        StorageFileVO vo = StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/my_war.txt"))
            .fileName("my_war.txt")
            .build();
        executeInTx(() -> {
            m_repo.upload(vo);
        });

        assertTrue(Files.exists(Paths.get(ROOT + "/my_war.txt")));

        byte[] res = m_repo.download(vo);
        assertTrue(res.length > 0);
    }

    @Test
    public void testFind_NotFound() throws Exception {
        executeInTx(() -> {
            StorageFileVO vo = m_repo.find("file-id");

            assertNull(vo);
        });
    }

    @Test
    public void testFind_Found() throws Exception {
        StorageFileVO vo = StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/my_war.txt"))
            .fileName("my_war.txt")
            .build();
        executeInTx(() -> {
            m_repo.upload(vo);
        });

        assertNotNull(vo.getId());

        executeInTx(() -> {
            StorageFileVO file = m_repo.find(vo.getId());

            assertNotNull(file);
            assertEquals(vo.getId(), file.getId());
        });
    }

    @Test
    public void testFindPhysicalFile_Found() throws Exception {

        // upload file
        StorageFileVO vo = StorageFileVO.builder()
            .content(new FileInputStream(ROOT + "/aot/my_war.txt"))
            .fileName("my_war.txt")
            .build();
        executeInTx(() -> {
            m_repo.upload(vo);
        });

        assertNotNull(vo.getId());

        //
        executeInTx(() -> {
            File file = m_repo.findPhysicalFile(vo.getId());

            assertNotNull(file);
            assertTrue(file.exists());
        });
    }

    @Test
    public void testFindPhysicalFile_NotFound() throws Exception {

        //
        executeInTx(() -> assertThrows(FileStorageException.class, () -> m_repo.findPhysicalFile("file-id")));
    }

    @Test
    public void testMultipleFind() throws Exception {
        List<StorageFileVO> vos = Arrays.asList(mockDTO("my_war1.txt"), mockDTO("my_war2.txt"));

        executeInTx(() -> {
            m_repo.upload(vos);

            assertTrue(Files.exists(Path.of(ROOT + "/my_war1.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war2.txt")));
        });

        List<String> ids = vos.stream().map(StorageFileVO::getId).collect(Collectors.toList());

        executeInTx(() -> {
            List<StorageFileVO> res = m_repo.find(ids);

            assertNotNull(res);
            assertEquals(2, res.size());
        });
    }

    @Test
    public void testFindAll() throws Exception {
        List<StorageFileVO> vos = Arrays.asList(mockDTO("my_war1.txt"), mockDTO("my_war2.txt"), mockDTO("my_war3.txt"));

        executeInTx(() -> m_fileDAO.deleteAll());

        executeInTx(() -> {
            m_repo.upload(vos);

            assertTrue(Files.exists(Path.of(ROOT + "/my_war1.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war2.txt")));
            assertTrue(Files.exists(Path.of(ROOT + "/my_war3.txt")));
        });

        executeInTx(() -> {
            List<StorageFileVO> res = m_repo.findAll();
            assertNotNull(res);
            assertEquals(3, res.size());
        });
    }


    private void executeInTx(Template template) throws Exception {
        m_dsm.getDatastore(StorageFileDataStore.DS_NAME).getEntityManager().getTransaction().begin();
        template.execute();
        m_dsm.getDatastore(StorageFileDataStore.DS_NAME).getEntityManager().getTransaction().commit();
    }

    interface Template {

        void execute() throws Exception;
    }
}

class SFHsqlDataStore extends EntityDataStore {

    private EntityManager m_entityManager;

    public SFHsqlDataStore(EntityManager entityManager) {
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