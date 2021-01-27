package com.infamous.framework.http.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.infamous.framework.converter.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class BodyPartFactoryTest {

    @Test
    public void testForBody() throws FileNotFoundException {
        BodyPart b1 = BodyPartFactory.body("StringBodyPart");
        BodyPart b2 = BodyPartFactory.body(new File("src/test/resources/aot.txt"));
        BodyPart b3 = BodyPartFactory.body(new byte[2]);
        BodyPart b4 = BodyPartFactory.body(new FileInputStream(new File("src/test/resources/aot.txt")));
        BodyPart b5 = BodyPartFactory.body(mock(BodyPart.class));
        BodyPart b6 = BodyPartFactory.body(new DemoObject());

        assertTrue(b1 instanceof BodyAsString);
        assertTrue(b2 instanceof BodyAsFile);
        assertTrue(b3 instanceof BodyAsByteArray);
        assertTrue(b4 instanceof BodyAsInputStream);
        assertTrue(b5 instanceof BodyPart);
        assertNull(b6);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValue(any())).thenReturn("json-string");
        BodyPart b7 = BodyPartFactory.body(new DemoObject(), objectMapper);
        assertTrue(b7 instanceof BodyAsString);
        assertEquals("json-string", b7.getValue());
    }

    @Test
    public void testForBodyWithObject() throws FileNotFoundException {
        Object o1 = "String";
        BodyPart b1 = BodyPartFactory.body(o1);
        assertTrue(b1 instanceof BodyAsString);

        Object o2 = new File("src/test/resources/aot.txt");
        BodyPart b2 = BodyPartFactory.body(o2);
        assertTrue(b2 instanceof BodyAsFile);

        Object o3 = new byte[2];
        BodyPart b3 = BodyPartFactory.body(o3);
        assertTrue(b3 instanceof BodyAsByteArray);

        Object o4 = new FileInputStream(new File("src/test/resources/aot.txt"));
        BodyPart b4 = BodyPartFactory.body(o4);
        assertTrue(b4 instanceof BodyAsInputStream);

        Object o5 = mock(BodyPart.class);
        BodyPart b5 = BodyPartFactory.body(o5);
        assertTrue(b5 instanceof BodyPart);

        Object o6 = new DemoObject();
        BodyPart b6 = BodyPartFactory.body(o6);
        assertNull(b6);
    }

    @Test
    public void testForMultiPart() throws FileNotFoundException {
        BodyPart mp1 = BodyPartFactory.part("name", "LukeD", "text/plain");
        BodyPart mp2 = BodyPartFactory.part("file1", new File("src/test/resources/aot.txt"),
            "application/octet-stream");
        BodyPart mp3 = BodyPartFactory.part("file2", new File("src/test/resources/aot.txt"),
            "application/octet-stream", "newName.txt");
        BodyPart mp4 = BodyPartFactory.part("file3", new byte[2], "application/octet-stream");
        BodyPart mp5 = BodyPartFactory.part("file3", new byte[2], "application/octet-stream", "newName2.txt");
        BodyPart mp6 = BodyPartFactory.part("file4", new FileInputStream(new File("src/test/resources/aot.txt")),
            "application/octet-stream");
        BodyPart mp7 = BodyPartFactory.part("file5", new FileInputStream(new File("src/test/resources/aot.txt")),
            "application/octet-stream", "newName3.txt");
        BodyPart mp8 = BodyPartFactory.part(mock(BodyPart.class));

        assertTrue(mp1 instanceof ParamPart);
        assertTrue(mp2 instanceof FilePart);
        assertEquals("aot.txt", mp2.getFileName());
        assertTrue(mp3 instanceof FilePart);
        assertEquals("newName.txt", mp3.getFileName());
        assertTrue(mp4 instanceof ByteArrayPart);
        assertNotNull(mp4.getFileName());
        assertTrue(mp5 instanceof ByteArrayPart);
        assertEquals("newName2.txt", mp5.getFileName());
        assertTrue(mp6 instanceof InputStreamPart);
        assertNotNull(mp6.getFileName());
        assertTrue(mp7 instanceof InputStreamPart);
        assertEquals("newName3.txt", mp7.getFileName());
        assertTrue(mp8 instanceof BodyPart);
    }

    @Test
    public void testForMultiPartWithObject() throws FileNotFoundException {
        String name = "name";
        String contentType = "application/octet-stream";
        Optional<String> fileName = Optional.of("fileName.txt");

        Object o1 = "String";
        BodyPart mp1 = BodyPartFactory.part(name, o1, contentType);
        BodyPart mp2 = BodyPartFactory.part(name, o1, contentType, fileName);
        assertTrue(mp1 instanceof ParamPart);
        assertTrue(mp2 instanceof ParamPart);
        assertNull(mp1.getFileName());
        assertNull(mp2.getFileName());

        Object o2 = new File("src/test/resources/aot.txt");
        BodyPart mp3 = BodyPartFactory.part(name, o2, contentType);
        BodyPart mp4 = BodyPartFactory.part(name, o2, contentType, fileName);
        assertTrue(mp3 instanceof FilePart);
        assertTrue(mp4 instanceof FilePart);
        assertEquals("aot.txt", mp3.getFileName());
        assertEquals("fileName.txt", mp4.getFileName());

        Object o3 = new byte[2];
        BodyPart mp5 = BodyPartFactory.part(name, o3, contentType);
        BodyPart mp6 = BodyPartFactory.part(name, o3, contentType, fileName);
        assertTrue(mp5 instanceof ByteArrayPart);
        assertTrue(mp6 instanceof ByteArrayPart);
        assertNotNull(mp5.getFileName());
        assertEquals("fileName.txt", mp6.getFileName());

        Object o4 = new FileInputStream(new File("src/test/resources/aot.txt"));
        BodyPart mp7 = BodyPartFactory.part(name, o4, contentType);
        BodyPart mp8 = BodyPartFactory.part(name, o4, contentType, fileName);
        assertTrue(mp7 instanceof InputStreamPart);
        assertTrue(mp8 instanceof InputStreamPart);
        assertNotNull(mp7.getFileName());
        assertEquals("fileName.txt", mp8.getFileName());

        BodyPart bodyPart = mock(BodyPart.class);
        when(bodyPart.getFileName()).thenReturn("newFileName.txt");
        Object o5 = bodyPart;
        BodyPart mp9 = BodyPartFactory.part(name, o5, contentType);
        BodyPart mp10 = BodyPartFactory.part(name, o5, contentType, fileName);
        assertTrue(mp9 instanceof BodyPart);
        assertTrue(mp10 instanceof BodyPart);
        assertEquals("newFileName.txt", mp9.getFileName());
        assertEquals("newFileName.txt", mp10.getFileName());

        Object o6 = new DemoObject();
        BodyPart mp11 = BodyPartFactory.part(name, o6, contentType);
        BodyPart mp12 = BodyPartFactory.part(name, o6, contentType, fileName);
        assertTrue(mp11 instanceof ParamPart);
        assertTrue(mp12 instanceof ParamPart);
        assertNull(mp11.getFileName());
        assertNull(mp12.getFileName());
    }
}