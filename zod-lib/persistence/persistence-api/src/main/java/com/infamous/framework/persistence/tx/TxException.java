package com.infamous.framework.persistence.tx;

public class TxException extends RuntimeException {

    public TxException(String mess) {
        super(mess);
    }

    public TxException(Throwable e) {
        super(e);
    }

    public TxException(String mess, Throwable e) {
        super(mess, e);
    }
}
