package com.infamous.framework.persistence.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBUtilsTest {

    public static final String[] FULL_ARGUMENTS = new String[]{
        "getDatabaseName",
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

    private static final String[] DB_URL = new String[]{
        "jdbc:mysql://localhost:3306/wanderer",
        "jdbc:mariadb://localhost:3306/wanderer",
        "jdbc:mariadb://localhost:3306/wanderer/",
        "jdbc:mariadb://localhost:3306/wanderer?autoReconnect\\=true&failOverReadOnly\\=false&rewriteBatchedStatements\\=true",

        "jdbc:mariadb://localhost:3306, localhost:3308/wanderer",
        "jdbc:mariadb://localhost:3306, localhost:3308/wanderer/",
        "jdbc:mariadb://localhost:3306, localhost:3308/wanderer?autoReconnect\\=true&failOverReadOnly\\=false&rewriteBatchedStatements\\=true",
    };

    private ArgumentFacade m_argumentFacade;

    @BeforeEach
    public void setup() {
        m_argumentFacade = new ArgumentFacade(FULL_ARGUMENTS);
    }

    @Test
    public void testGetDatabaseName() {
        for (String dbUrl : DB_URL) {
            String actual = null;
            m_argumentFacade.setDbUrl(dbUrl);

            actual = ExecutionUtils.getDatabaseName(m_argumentFacade);

            assertEquals("wanderer", actual);
        }
    }

    @Test
    public void testGetDatabaseUrlWithoutDatabaseName() {
        List<String> actualResult = new ArrayList<>();
        for (String dbUrl : DB_URL) {
            m_argumentFacade.setDbUrl(dbUrl);

            String dbUrlWithoutDatabaseName = ExecutionUtils.getDatabaseUrlWithoutDatabaseName(dbUrl);
            actualResult.add(dbUrlWithoutDatabaseName);
        }

        assertEquals("jdbc:mysql://localhost:3306/", actualResult.get(0));
        assertEquals("jdbc:mariadb://localhost:3306/", actualResult.get(1));
        assertEquals("jdbc:mariadb://localhost:3306/", actualResult.get(2));
        assertEquals(
            "jdbc:mariadb://localhost:3306/?autoReconnect\\=true&failOverReadOnly\\=false&rewriteBatchedStatements\\=true",
            actualResult.get(3));

        assertEquals("jdbc:mariadb://localhost:3306, localhost:3308/", actualResult.get(4));
        assertEquals("jdbc:mariadb://localhost:3306, localhost:3308/", actualResult.get(5));
        assertEquals(
            "jdbc:mariadb://localhost:3306, localhost:3308/?autoReconnect\\=true&failOverReadOnly\\=false&rewriteBatchedStatements\\=true",
            actualResult.get(6));

    }

}