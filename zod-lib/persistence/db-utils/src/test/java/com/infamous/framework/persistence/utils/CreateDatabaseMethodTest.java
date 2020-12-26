package com.infamous.framework.persistence.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateDatabaseMethodTest {

    JPAEntityManagerFactory m_emf;

    public static final String[] ARGUMENTS_SUCCESS = new String[]{
        "createDatabase",
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
    public void testCreateDatabase_Success() {
        StatusMessage message = SupportedMethod.CREATE_DATABASE.getExecuteFunction()
            .apply(new ArgumentFacade(ARGUMENTS_SUCCESS));
        assertNotEquals("FAILURE", message.getStatus());
    }

    @Test
    public void testCreateDatabase_Failure() {
        StatusMessage message = SupportedMethod.CREATE_DATABASE.getExecuteFunction()
            .apply(new ArgumentFacade(ARGUMENTS_FAILURE));
        assertEquals("FAILURE invalid authorization specification: SA", message.getStatus());
    }

}
