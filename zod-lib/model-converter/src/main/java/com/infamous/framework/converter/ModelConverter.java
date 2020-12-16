package com.infamous.framework.converter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ModelConverter<ENTITY, DTO> {

    private ConvertProcessor<ENTITY, DTO> m_toDTODefaultProcessor;
    private ConvertProcessor<DTO, ENTITY> m_toEntityDefaultProcessor;

    public ModelConverter(ConvertProcessor<ENTITY, DTO> toDTODefaultProcessor,
                          ConvertProcessor<DTO, ENTITY> toEntityDefaultProcessor) {
        this.m_toDTODefaultProcessor = toDTODefaultProcessor;
        this.m_toEntityDefaultProcessor = toEntityDefaultProcessor;
    }

    public void changeDTOProcessor(ConvertProcessor<ENTITY, DTO> processor) {
        Objects.requireNonNull(processor);
        this.m_toDTODefaultProcessor = processor;
    }

    public void changeEntityProcessor(ConvertProcessor<DTO, ENTITY> processor) {
        Objects.requireNonNull(processor);
        this.m_toEntityDefaultProcessor = processor;
    }

    //-------------- TO DTO -------------------
    public DTO toDTOWithProcessor(ENTITY entity, ConvertProcessor<ENTITY, DTO> toDTO) {
        return toDTO.convert(entity);
    }

    public List<DTO> toDTOWithProcessor(Stream<ENTITY> entityStream, ConvertProcessor<ENTITY, DTO> toDTO) {
        return entityStream.map(entity -> toDTOWithProcessor(entity, toDTO)).collect(Collectors.toList());
    }

    public DTO toDTO(ENTITY entity) {
        return toDTOWithProcessor(entity, m_toDTODefaultProcessor);
    }

    public List<DTO> toDTO(Stream<ENTITY> entityStream) {
        return toDTOWithProcessor(entityStream, m_toDTODefaultProcessor);
    }

    //-------------- TO ENTITY -------------------
    public ENTITY toEntityWithProcessor(DTO dto, ConvertProcessor<DTO, ENTITY> toEntity) {
        return toEntity.convert(dto);
    }

    public List<ENTITY> toEntityWithProcessor(Stream<DTO> dtoStream, ConvertProcessor<DTO, ENTITY> toEntity) {
        return dtoStream.map(entity -> toEntityWithProcessor(entity, toEntity)).collect(Collectors.toList());
    }

    public ENTITY toEntity(DTO entity) {
        return toEntityWithProcessor(entity, m_toEntityDefaultProcessor);
    }

    public List<ENTITY> toEntity(Stream<DTO> dtoStream) {
        return toEntityWithProcessor(dtoStream, m_toEntityDefaultProcessor);
    }
}
