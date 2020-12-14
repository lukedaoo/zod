package com.infamous.framework.converter;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ModelConverter<ENTITY, DTO> {

    private ConvertProcessor<ENTITY, DTO> m_defaultToDTO;
    private ConvertProcessor<DTO, ENTITY> m_defaultToEntity;

    public ModelConverter(ConvertProcessor<ENTITY, DTO> defaultToDTO,
                          ConvertProcessor<DTO, ENTITY> defaultToEntity) {
        this.m_defaultToDTO = defaultToDTO;
        this.m_defaultToEntity = defaultToEntity;
    }

    public void changeDTOProcessor(ConvertProcessor<ENTITY, DTO> processor) {
        this.m_defaultToDTO = processor;
    }

    public void changeEntityProcessor(ConvertProcessor<DTO, ENTITY> processor) {
        this.m_defaultToEntity = processor;
    }

    //-------------- TO DTO -------------------
    public DTO toDTOWithProcessor(ENTITY entity, ConvertProcessor<ENTITY, DTO> toDTO) {
        return toDTO.convert(entity);
    }

    public Collection<DTO> toDTOWithProcessor(Stream<ENTITY> entityStream, ConvertProcessor<ENTITY, DTO> toDTO) {
        return entityStream.map(entity -> toDTOWithProcessor(entity, toDTO)).collect(Collectors.toList());
    }

    public DTO toDTO(ENTITY entity) {
        return toDTOWithProcessor(entity, m_defaultToDTO);
    }

    public Collection<DTO> toDTO(Stream<ENTITY> entityStream) {
        return toDTOWithProcessor(entityStream, m_defaultToDTO);
    }

    //-------------- TO ENTITY -------------------
    public ENTITY toEntityWithProcessor(DTO dto, ConvertProcessor<DTO, ENTITY> toEntity) {
        return toEntity.convert(dto);
    }

    public Collection<ENTITY> toEntityWithProcessor(Stream<DTO> dtoStream, ConvertProcessor<DTO, ENTITY> toEntity) {
        return dtoStream.map(entity -> toEntityWithProcessor(entity, toEntity)).collect(Collectors.toList());
    }

    public ENTITY toEntity(DTO entity) {
        return toEntityWithProcessor(entity, m_defaultToEntity);
    }

    public Collection<ENTITY> toEntity(Stream<DTO> dtoStream) {
        return toEntityWithProcessor(dtoStream, m_defaultToEntity);
    }
}
