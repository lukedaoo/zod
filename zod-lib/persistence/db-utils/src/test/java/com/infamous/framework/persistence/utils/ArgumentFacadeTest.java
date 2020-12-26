package com.infamous.framework.persistence.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArgumentFacadeTest {

    public static final String[] FULL_ARGUMENTS = new String[]{
        "createDatabase",
        "org.mariadb.jdbc.Driver",
        "jdbc:mariadb://localhost:3306/wanderer?autoReconnect\\=true&failOverReadOnly\\=false&rewriteBatchedStatements\\=true",
        "root",
        "mysql",
        "true",
        "/trust-store/db",
        "trust_store_password",
        "/key-store/db",
        "key_store_password"
    };

    private ArgumentFacade m_argumentFacade;

    @BeforeEach
    public void setup() {
        m_argumentFacade = new ArgumentFacade(FULL_ARGUMENTS);
    }

    @Test
    public void testGetArguments() {
        assertEquals(SupportedMethod.CREATE_DATABASE, m_argumentFacade.getMethod());
        assertEquals(FULL_ARGUMENTS[1], m_argumentFacade.getDriver());
        assertEquals(FULL_ARGUMENTS[2], m_argumentFacade.getDbUrl());
        assertEquals(FULL_ARGUMENTS[3], m_argumentFacade.getDbUserName());
        assertEquals(FULL_ARGUMENTS[4], m_argumentFacade.getDbPass());
        assertEquals(FULL_ARGUMENTS[5], m_argumentFacade.getUsedSSL().toString());
        assertEquals(FULL_ARGUMENTS[6], m_argumentFacade.getTrustStore());
        assertEquals(FULL_ARGUMENTS[7], m_argumentFacade.getTrustStorePass());
        assertEquals(FULL_ARGUMENTS[8], m_argumentFacade.getKeyStore());
        assertEquals(FULL_ARGUMENTS[9], m_argumentFacade.getKeyStorePass());
    }
}