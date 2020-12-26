package com.infamous.framework.persistence.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckStatusMethodTest {

    JPAEntityManagerFactory m_emf;

    public static final String[] ARGUMENTS_SUCCESS = new String[]{
        "checkStatus",
        "org.hsqldb.jdbcDriver",
        "jdbc:hsqldb:file:./target/zoddb",
        "sa",
        "sa",
        "false"
    };

    public static final String[] ARGUMENTS_FAILURE = new String[]{
        "checkStatus",
        "org.hsqldb.jdbcDriver",
        "jdbc:hsqldb:file:./target/zoddb",
        "sa",
        "",
        "false"
    };

    @BeforeEach
    public void setup() {
        m_emf = new JPAEntityManagerFactory("zod_db_test");
    }

    @Test
    public void testCheckStatus() {
        StatusMessage message = SupportedMethod.CHECK_STATUS.getExecuteFunction().apply(new ArgumentFacade(ARGUMENTS_SUCCESS));
        assertEquals("SUCCESS", message.getStatus());
    }

    @Test
    public void testCheckStatus_Failure() {
        StatusMessage message = SupportedMethod.CHECK_STATUS.getExecuteFunction().apply(new ArgumentFacade(ARGUMENTS_FAILURE));
        assertEquals("FAILURE invalid authorization specification: SA", message.getStatus());
    }

}
