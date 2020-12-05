package com.infamous.framework.persistence.tx;

import static javax.transaction.Transactional.TxType.MANDATORY;
import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import javax.transaction.Transactional;

public class TxService {

    @Transactional(value = REQUIRED, rollbackOn = {RuntimeException.class, Exception.class})
    public <T> T executeWithTxRequired(TxTemplate<T> template) throws TxException {
        return template.execute();
    }

    @Transactional(value = REQUIRES_NEW, rollbackOn = {RuntimeException.class, Exception.class})
    public <T> T executeWithTxRequiresNew(TxTemplate<T> template) throws TxException {
        return template.execute();
    }

    @Transactional(value = SUPPORTS, rollbackOn = {RuntimeException.class, Exception.class})
    public <T> T executeWithTxSupports(TxTemplate<T> template) throws TxException {
        return template.execute();
    }

    @Transactional(value = MANDATORY, rollbackOn = {RuntimeException.class, Exception.class})
    public <T> T executeWithTxMandatory(TxTemplate<T> template) throws TxException {
        return template.execute();
    }
}
