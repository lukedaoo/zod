package com.infamous.framework.converter;

public class Converter<U, T> {

    private ConvertProcessor<U, T> m_convertProcessor;

    public Converter(ConvertProcessor<U, T> convertProcessor) {
        m_convertProcessor = convertProcessor;
    }

    public T converter(U value) {
        return m_convertProcessor.convert(value);
    }

    public T converter(U value, ConvertProcessor<U, T> convertProcessor) {
        return convertProcessor.convert(value);
    }
}
