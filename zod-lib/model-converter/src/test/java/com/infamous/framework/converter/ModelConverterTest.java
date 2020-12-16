package com.infamous.framework.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModelConverterTest {

    private ConvertProcessor<PersonEntity, PersonDto> m_toDTO =
        new ConvertProcessor<>(entity -> new PersonDto(entity.getName(), entity.getAddress()));

    private ConvertProcessor<PersonDto, PersonEntity> m_toEntity =
        new ConvertProcessor<>(dto -> new PersonEntity(dto.getName(), dto.getAddress()));

    private ModelConverter<PersonEntity, PersonDto> m_converter;

    @BeforeEach
    public void setup() {
        m_converter = new ModelConverter<>(m_toDTO, m_toEntity) {
        };
    }

    @Test
    public void testChangeProcessor_WithNullValue() {
        assertThrows(NullPointerException.class, () -> m_converter.changeDTOProcessor(null));
        assertThrows(NullPointerException.class, () -> m_converter.changeEntityProcessor(null));
    }

    @Test
    public void testChangeProcessor_DTO() {
        m_converter.changeDTOProcessor(new ConvertProcessor<PersonEntity, PersonDto>(
            entity -> new PersonDto(entity.getName(), entity.getAddress()))
            .preConvertFunc(entity -> {
                entity.setName("Entity " + entity.getName());
                entity.setAddress("Entity " + entity.getAddress());
                return entity;
            }));

        PersonDto dto = m_converter.toDTO(new PersonEntity("admin", "WA"));
        assertNotNull(dto);
        assertEquals("Entity admin", dto.getName());
        assertEquals("Entity WA", dto.getAddress());
    }

    @Test
    public void testChangeProcessor_Entity() {
        m_converter.changeEntityProcessor(new ConvertProcessor<PersonDto, PersonEntity>(
            dto -> new PersonEntity(dto.getName(), dto.getAddress()))
            .postConvertFunc(entity -> {
                entity.setName("Entity " + entity.getName());
                entity.setAddress("Entity " + entity.getAddress());
                return entity;
            }));

        PersonEntity entity = m_converter.toEntity(new PersonDto("admin", "WA"));
        assertNotNull(entity);
        assertEquals("Entity admin", entity.getName());
        assertEquals("Entity WA", entity.getAddress());
    }

    @Test
    public void testConvertToDTO() {
        PersonDto dto = m_converter.toDTO(new PersonEntity("admin", "WA"));

        assertNotNull(dto);
        assertEquals("admin", dto.getName());
        assertEquals("WA", dto.getAddress());
    }

    @Test
    public void testConvertToDTOs() {
        Stream<PersonEntity> stream = Stream.of(
            new PersonEntity("admin", "WA"),
            new PersonEntity("admin2", "CA")
        );
        List<PersonDto> dtos = m_converter.toDTO(stream);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        assertEquals("admin", dtos.get(0).getName());
        assertEquals("WA", dtos.get(0).getAddress());
        assertEquals("admin2", dtos.get(1).getName());
        assertEquals("CA", dtos.get(1).getAddress());
    }

    @Test
    public void testConvertToEntity() {
        PersonEntity entity = m_converter.toEntity(new PersonDto("admin", "WA"));

        assertNotNull(entity);
        assertEquals("admin", entity.getName());
        assertEquals("WA", entity.getAddress());
    }


    @Test
    public void testConvertToEntities() {
        Stream<PersonDto> stream = Stream.of(
            new PersonDto("admin", "WA"),
            new PersonDto("admin2", "CA")
        );
        List<PersonEntity> entities = m_converter.toEntity(stream);

        assertNotNull(entities);
        assertEquals(2, entities.size());

        assertEquals("admin", entities.get(0).getName());
        assertEquals("WA", entities.get(0).getAddress());
        assertEquals("admin2", entities.get(1).getName());
        assertEquals("CA", entities.get(1).getAddress());
    }
}