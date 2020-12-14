package com.infamous.framework.sensitive.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

class SensitiveHashingServiceTest {


    @Test
    public void testHash_CornerCase_WithSourceIsNull() {
        DefaultSensitiveHashingService service = new DefaultSensitiveHashingService();

        String hashed = service.hash(null, MessageDigestAlgorithm.MD5);
        assertNotNull(hashed);
        assertEquals("", hashed);
    }

    @Test
    public void testHash_CornerCase_WithAlgoIsNull() {
        DefaultSensitiveHashingService service = new DefaultSensitiveHashingService();

        String hashed = service.hash("123", null);
        assertNotNull(hashed);
        assertEquals("123", hashed);
    }

    @Test
    public void testHash_CornerCase_WithSourceIsNullAndAlgoIsNull() {
        DefaultSensitiveHashingService service = new DefaultSensitiveHashingService();

        String hashed = service.hash(null, null);
        assertNotNull(hashed);
        assertEquals("", hashed);
    }

    @Test
    public void test() {
        String md5 = checkAndReturn("admin", MessageDigestAlgorithm.MD5);
        String sha1 = checkAndReturn("admin", MessageDigestAlgorithm.SHA_1);
        String sha256 = checkAndReturn("admin", MessageDigestAlgorithm.SHA_256);
        String sha512 = checkAndReturn("admin", MessageDigestAlgorithm.SHA_512);

        Set<String> set = Sets.newSet(md5, sha1, sha256, sha512);
        assertEquals(4, set.size());
        assertTrue(set.contains(md5));
        assertTrue(set.contains(sha1));
        assertTrue(set.contains(sha256));
        assertTrue(set.contains(sha512));
    }

    private String checkAndReturn(String source, MessageDigestAlgorithm algorithm) {
        DefaultSensitiveHashingService service = new DefaultSensitiveHashingService();

        String hashed = service.hash(source, algorithm);
        assertNotNull(hashed);
        assertNotEquals(source, hashed);
        return hashed;
    }
}