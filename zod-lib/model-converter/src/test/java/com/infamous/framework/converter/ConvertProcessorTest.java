package com.infamous.framework.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConvertProcessorTest {


    @Test
    public void testNewConvertProcessor() {
        assertThrows(NullPointerException.class, () -> new ConvertProcessor<>(null));
        assertThrows(NullPointerException.class, () -> new ConvertProcessor<>(e -> new Object())
            .doConvertFunc(null));
    }


    @Test
    public void testConvert() {
        ConvertProcessor<PersonEntity, PersonDto> convertProcessor
            = new ConvertProcessor<>(e -> new PersonDto(e.getName(), e.getAddress()));

        PersonDto dto = convertProcessor.convert(new PersonEntity("admin", "WA, USA"));

        assertEquals("admin", dto.getName());
        assertEquals("WA, USA", dto.getAddress());
    }


    @Test
    public void testConvert_HasAnotherConvertFunc() {
        ConvertProcessor<PersonEntity, PersonDto> convertProcessor
            = new ConvertProcessor<PersonEntity, PersonDto>(e -> new PersonDto(e.getName(), e.getAddress()))
            .doConvertFunc(e -> new PersonDto(e.getName(), e.getAddress() + ", Earth"));

        PersonDto dto = convertProcessor.convert(new PersonEntity("admin", "WA, USA"));

        assertEquals("admin", dto.getName());
        assertEquals("WA, USA, Earth", dto.getAddress());
    }


    @Test
    public void testConvert_HasPreConvertFunction() {
        ConvertProcessor<PersonEntity, PersonDto> convertProcessor
            = new ConvertProcessor<PersonEntity, PersonDto>(e -> new PersonDto(e.getName(), e.getAddress()))
            .preConvertFunc(entity -> {

                return entity.setName("[UserName] " + entity.getName());
            });

        PersonDto dto = convertProcessor.convert(new PersonEntity("admin", "WA, USA"));
        assertEquals("[UserName] admin", dto.getName());
        assertEquals("WA, USA", dto.getAddress());
    }

    @Test
    public void testConvert_HasPostConvertFunction() {
        final boolean[] invoked = new boolean[1];
        ConvertProcessor<PersonEntity, PersonDto> convertProcessor
            = new ConvertProcessor<PersonEntity, PersonDto>(e -> new PersonDto(e.getName(), e.getAddress()))
            .postConvertFunc(dto -> {
                invoked[0] = true;
                return dto;
            });

        assertFalse(invoked[0]);
        PersonDto dto = convertProcessor.convert(new PersonEntity("admin", "WA, USA"));
        assertEquals("admin", dto.getName());
        assertEquals("WA, USA", dto.getAddress());
        assertTrue(invoked[0]);
    }
}


