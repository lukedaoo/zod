package com.infamous.framework.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class HttpMethodTest {


    @Test
    public void testFrom() {
        assertHttpMethod(HttpMethod.GET, new String[]{"get", "GET", "geT"});
        assertHttpMethod(HttpMethod.POST, new String[]{"post", "POST", "pOst"});
        assertHttpMethod(HttpMethod.PUT, new String[]{"put", "PUT", "PuT"});
        assertHttpMethod(HttpMethod.DELETE, new String[]{"delete", "DELETE", "DElete"});
        assertHttpMethod(HttpMethod.PATCH, new String[]{"patch", "PATCH", "PATch"});
        assertHttpMethod(HttpMethod.OPTIONS, new String[]{"options", "OPTIONS", "OPTIONs"});
        assertHttpMethod(HttpMethod.HEAD, new String[]{"head", "HEAD", "HeaD"});
        assertHttpMethod(HttpMethod.TRACE, new String[]{"trace", "TRACE", "TraCE"});
    }



    @Test
    public void testFrom_UnsupportMethod() {
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.from("ABC"));
    }


    void assertHttpMethod(HttpMethod expected, String[] args) {
        for (String s : args) {
            assertEquals(expected, HttpMethod.from(s));
        }
    }


}