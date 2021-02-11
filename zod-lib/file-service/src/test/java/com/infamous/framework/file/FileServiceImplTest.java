package com.infamous.framework.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileServiceImplTest {


    private static final String ROOT = "src/test/resources";

    private Path m_root;
    private FileService m_fileService;

    @BeforeEach
    public void setup() {
        m_root = Paths.get(ROOT);
        m_fileService = new FileServiceImpl();
        m_fileService.setRootFolder(m_root);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.walk(m_root)
            .sorted(Comparator.reverseOrder())
            .filter(path -> path.toString().substring(ROOT.length()).contains("test"))
            .forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    //ignore
                }
            });
    }

    @Test
    public void testGetRootFolder() {
        assertEquals(m_root, m_fileService.getRootFolder());
    }

    @Test
    public void testListAll() {
        List<String> actual = m_fileService.listAll();
        assertNotNull(actual);

        assertEquals(2, actual.size());
        assertEquals("existed", actual.get(0));
        assertEquals("existed/Kamehameha.txt", actual.get(1));
    }


    @Test
    public void testCreateDirectory() {
        Path res = m_fileService.createDirectory(m_root.toString() + "/testFolder");

        assertTrue(res.toFile().exists());
        assertTrue(res.toFile().isDirectory());
    }

    @Test
    public void testCreateDirectory_WhenFolderAlreadyExists() {
        assertTrue(Files.exists(m_root.resolve("existed")));
        Path res = m_fileService.createDirectory(m_root.toString() + "/existed");
        assertTrue(res.toFile().exists());
        assertTrue(res.toFile().isDirectory());
    }

    @Test
    public void testDelete() {
        testCreateDirectory();
        m_fileService.delete(m_root, "testFolder");

        assertFalse(new File(m_root.toString() + "/testFolder").exists());
    }

    @Test
    public void testDeleteByFileName() throws Exception {
        assertTrue(Files.exists(m_root.resolve("existed/Kamehameha.txt")));

        m_fileService.createDirectory(m_root.resolve("testFiles").toString());
        assertTrue(Files.exists(m_root.resolve("testFiles")));
        m_fileService.setRootFolder(m_root.resolve("testFiles"));

        m_fileService.store(new FileInputStream(new File(m_root.resolve("existed/Kamehameha.txt").toString())),
            "test_newText.txt");

        assertTrue(Files.exists(m_root.resolve("testFiles/test_newText.txt")));
        m_fileService.delete("test_newText.txt");

        assertFalse(new File(m_root.toString() + "/testFolder/test_newText").exists());
    }

    @Test
    public void testStore() throws IOException {
        assertTrue(Files.exists(m_root.resolve("existed/Kamehameha.txt")));

        m_fileService.createDirectory(m_root.resolve("testFiles").toString());
        assertTrue(Files.exists(m_root.resolve("testFiles")));

        // Copy file from [existed/Kamehameha.txt] to [testFiles/test_newText.txt]
        m_fileService.store(m_root.resolve("testFiles"),
            new FileInputStream(new File(m_root.resolve("existed/Kamehameha.txt").toString())), "test_newText.txt");

        assertTrue(Files.exists(m_root.resolve("testFiles/test_newText.txt")));

        byte[] f1 = Files.readAllBytes(m_root.resolve("existed/Kamehameha.txt"));
        byte[] f2 = Files.readAllBytes(m_root.resolve("testFiles/test_newText.txt"));
        assertTrue(Arrays.equals(f1, f2));
    }

    @Test
    public void testStoreWhenFolderIsNotCreated() throws IOException {
        assertTrue(Files.exists(m_root.resolve("existed/Kamehameha.txt")));
        assertFalse(Files.exists(m_root.resolve("testFiles")));

        assertThrows(FileStorageException.class, () -> m_fileService.store(m_root.resolve("testFiles"),
            new FileInputStream(new File(m_root + "/existed/Kamehameha.txt")), "test_newText.txt"));
    }

    @Test
    public void testStoreCornerCase() {
        // Path is null
        assertThrows(FileStorageException.class, () -> m_fileService.store(null,
            new FileInputStream(new File(m_root + "/existed/Kamehameha.txt")), "test_newText.txt"));

        // IS is null
        assertThrows(FileStorageException.class, () -> m_fileService.store(m_root, null, "test_newText.txt"));

        // fileName is null
        assertThrows(FileStorageException.class, () -> m_fileService.store(m_root.resolve("testFiles"),
            new FileInputStream(new File(m_root + "/existed/Kamehameha.txt")), null));
    }

    @Test
    public void testGetPath() {

        String existed = m_fileService.getFilePath(m_root, "existed/Kamehameha.txt");
        assertTrue(Files.exists(Path.of(existed)));

        String notExist = m_fileService.getFilePath(m_root, "notExist/Kamehameha.txt");
        assertFalse(Files.exists(Path.of(notExist)));
    }

    @Test
    public void testGetFile() {
        InputStream is = m_fileService.getFile(m_root, "existed/Kamehameha.txt");
        assertNotNull(is);
    }

    @Test
    public void testGetFile_WhenFileDoesNotExist() {
        assertThrows(FileStorageException.class, () -> m_fileService.getFile(m_root, "NotExisted/Kamehameha.txt"));
    }

    @Test
    public void testGetFileAsByteArr() throws IOException {
        byte[] arr = m_fileService.getFileAsByteArray(m_root, "existed/Kamehameha.txt");

        assertTrue(Arrays.equals(Files.readAllBytes(m_root.resolve("existed/Kamehameha.txt")), arr));
    }

    @Test
    public void testListFile() {
        String[] res = m_fileService.listAll(m_root).toArray(new String[0]);

        assertEquals(2, res.length);
        assertEquals("existed", res[0]);
        assertEquals("existed/Kamehameha.txt", res[1]);
    }

    @Test
    public void testGetFilePhysical() {
        File file = m_fileService.getFilePhysical("existed/Kamehameha.txt");
        assertTrue(file.exists());
    }

    @Test
    public void isExist() {
        assertTrue(m_fileService.isExist("existed/Kamehameha.txt"));
        assertFalse(m_fileService.isExist(m_root, "abc.xyz"));
    }
}