package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class HeaderMapTest {


    @Test
    public void testHeaderMap() {
        HeaderMap headerMap = new HeaderMap();

        headerMap.add("Content-Type", "application/json");
        headerMap.add(new HashMap<>() {{
            put("ClientId", "client-id-123");
        }});

        Map<String, String> map = headerMap.get();
        assertEquals(2, map.size());
        assertEquals("application/json", headerMap.get("Content-Type"));
        assertEquals("client-id-123", headerMap.get("ClientId"));

        headerMap.remove("ClientId");
        assertEquals(1, headerMap.get().size());
    }

    @Test
    public void testEqualsHashCode() {
        HeaderMap headerMap1 = new HeaderMap();
        HeaderMap headerMap2 = new HeaderMap();

        assertTrue(headerMap1.equals(headerMap2));
        assertEquals(headerMap1.hashCode(), headerMap2.hashCode());
    }
}