package com.infamous.framework.persistence.tx;

@FunctionalInterface
public interface TxTemplate<T> {

    T execute();
}
