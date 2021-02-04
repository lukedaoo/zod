package com.infamous.framework.logging.appender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StaticFieldTest {


    @Test
    public void test() {
        StaticField field = StaticField.createStaticField("name", "${env:app.name}");

        assertNotNull(field);
        assertEquals("name", field.getName());
        assertEquals("${env:app.name}", field.getValue());
        assertTrue(field.isValueNeedsLookup());
    }

    @Test
    public void testInvalid_Input() {
        assertThrows(IllegalArgumentException.class, () -> StaticField.createStaticField(null, "Luke"));
        assertThrows(IllegalArgumentException.class, () -> StaticField.createStaticField("", "Luke"));

        assertThrows(IllegalArgumentException.class, () -> StaticField.createStaticField("name", null));
    }

}