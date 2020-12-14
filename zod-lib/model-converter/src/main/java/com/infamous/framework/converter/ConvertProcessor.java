package com.infamous.framework.converter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ConvertProcessor<U, T> {

    private Function<U, U> m_preConvert;
    private Function<U, T> m_doConvert;
    private Function<T, T> m_postConvert;

    public ConvertProcessor(Function<U, T> convertFunction) {
        Objects.requireNonNull(convertFunction);
        m_doConvert = convertFunction;
    }

    public ConvertProcessor<U, T> preConvertFunc(Function<U, U> preConvertFunc) {
        m_preConvert = preConvertFunc;
        return this;
    }

    public ConvertProcessor<U, T> doConvertFunc(Function<U, T> doConvert) {
        Objects.requireNonNull(doConvert);
        m_doConvert = doConvert;
        return this;
    }

    public ConvertProcessor<U, T> postConvertFunc(Function<T, T> postConvert) {
        m_postConvert = postConvert;
        return this;
    }

    public T convert(U u) {
        U doPreConvert = doPreConvert(u);
        T converted = doConvert(doPreConvert);
        return doPostConvert(converted);
    }

    private U doPreConvert(U u) {
        return Optional.ofNullable(m_preConvert)
            .map(preFunc -> preFunc.apply(u))
            .orElse(u);
    }

    private T doConvert(U u) {
        return m_doConvert.apply(u);
    }

    private T doPostConvert(T t) {
        return Optional.ofNullable(m_postConvert)
            .map(postFunc -> postFunc.apply(t))
            .orElse(t);
    }
}
