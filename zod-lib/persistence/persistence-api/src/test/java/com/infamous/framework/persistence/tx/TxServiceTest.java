package com.infamous.framework.persistence.tx;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.junit.jupiter.api.Test;

class TxServiceTest {


    @Test
    public void testTxService_shouldRunWithTx_Required()
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        testMethod("executeWithTxRequired", TxType.REQUIRED);
    }

    @Test
    public void testTxService_shouldRunWithTx_RequiredNew()
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        testMethod("executeWithTxRequiresNew", TxType.REQUIRES_NEW);
    }

    @Test
    public void testTxService_shouldRunWithTx_Supports()
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        testMethod("executeWithTxSupports", TxType.SUPPORTS);
    }

    @Test
    public void testTxService_shouldRunWithTx_Mandatory()
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        testMethod("executeWithTxMandatory", TxType.MANDATORY);
    }


    public void testMethod(String methodName, Transactional.TxType txType)
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TxService txService = new TxService();

        Method method = txService.getClass().getDeclaredMethod(methodName, TxTemplate.class);
        Transactional transactional = method.getAnnotation(Transactional.class);
        assertEquals(transactional.value(), txType);
        assertEquals(transactional.rollbackOn()[0], RuntimeException.class);
        assertEquals(transactional.rollbackOn()[1], Exception.class);

        boolean val = (boolean) method.invoke(txService, (TxTemplate<Boolean>) () -> true);
        assertTrue(val);
    }
}