package com.infamous.framework.persistence.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecutionUtils {

    private static final String SLASH = "/";
    private static final String EMPTY = "";
    private static final Pattern DB_URL_PATTERN_1 = Pattern.compile("(.*://)([^/]*)(/.*)(\\?.*)");
    private static final Pattern DB_URL_PATTERN_2 = Pattern.compile("(.*://)([^/]*)(/.*)");

    private ExecutionUtils() {

    }

    public static String getDatabaseName(ArgumentFacade arguments) {
        String dbUrl = arguments.getDbUrl();

        return getDatabaseNameFromDbUrl(dbUrl);
    }

    private static String getDatabaseNameFromDbUrl(String dbUrl) {
        // Default DB name is testing
        return getJDBCMatcher(dbUrl)
            .map(m -> m.group(3).replace(SLASH, EMPTY))
            .orElse("testing");
    }

    private static Optional<Matcher> getJDBCMatcher(String dbUrl) {
        Matcher matcher = DB_URL_PATTERN_1.matcher(dbUrl);
        if (!matcher.find()) {
            matcher = DB_URL_PATTERN_2.matcher(dbUrl);
            return matcher.find() ? Optional.of(matcher) : Optional.empty();
        }
        return Optional.of(matcher);
    }


    protected static String getDatabaseUrlWithoutDatabaseName(ArgumentFacade argumentFacade) {
        return getDatabaseUrlWithoutDatabaseName(argumentFacade.getDbUrl());
    }

    protected static String getDatabaseUrlWithoutDatabaseName(String dbUrl) {
        return getJDBCMatcher(dbUrl)
            .map(m -> {
                String protocol = m.group(1);
                String hostAndPort = m.group(2);
                String extraProperties;
                try {
                    extraProperties = m.group(4);
                } catch (Exception e) {
                    extraProperties = EMPTY;
                }

                return protocol + hostAndPort + SLASH + extraProperties;
            })
            .orElse(dbUrl);
    }

    public static StatusMessage checkStatus(ArgumentFacade arguments) {
        StatusMessage statusMessage = new StatusMessage("");
        Connection connection = null;
        try {
            connection = getConnection(arguments);
            return StatusMessage.SUCCESS;
        } catch (Exception e) {
            if (e.getMessage().toUpperCase().contains("UNKNOWN DATABASE")) {
                statusMessage.setStatus("UNKNOWN DATABASE");
            } else {
                statusMessage.setStatus("FAILURE " + e.getMessage());
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
        return statusMessage;
    }


    public static StatusMessage createDatabase(ArgumentFacade arguments) {
        String databaseName = getDatabaseName(arguments);
        String dbUrlWithoutDatabaseName = getDatabaseUrlWithoutDatabaseName(arguments);
        Connection connection = null;
        try {
            connection = getConnection(arguments, dbUrlWithoutDatabaseName);
            Class.forName(arguments.getDriver());

            Statement statement = connection.createStatement();
            int result = 0;
            if (!arguments.getDriver().contains("hsqldb")) {
                result = statement.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + databaseName + " CHARACTER SET = 'utf8' COLLATE = 'utf8_bin'");
                statement.execute("USE " + databaseName);
            } else {
                result = statement.executeUpdate(
                    "CREATE SCHEMA IF NOT EXISTS " + databaseName);
            }

            StatusMessage statusMessage = new StatusMessage("");
            if (result == 0) {
                statusMessage.setStatus("DATABASE EXISTS");
            } else {
                statusMessage.setStatus("SUCCESS");
            }
            return statusMessage;
        } catch (Exception e) {

            return new StatusMessage("FAILURE " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
    }

    private static Connection getConnection(String url, String dbUserName, String dbPassword, boolean isSSL,
                                            String trustStorePath, String trustStorePass, String keyStorePath,
                                            String keyStorePass) throws SQLException {

        if (isSSL) {
            url += "&useSSL=true";
            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePass);
            System.setProperty("javax.net.ssl.keyStore", keyStorePath);
            System.setProperty("javax.net.ssl.keyStorePassword", keyStorePass);
        }

        return DriverManager.getConnection(url, dbUserName, dbPassword);
    }

    private static Connection getConnection(ArgumentFacade arguments) throws SQLException {
        return getConnection(arguments.getDbUrl(), arguments.getDbUserName(), arguments.getDbPass(),
            arguments.getUsedSSL(), arguments.getTrustStore(), arguments.getTrustStorePass(),
            arguments.getKeyStore(), arguments.getKeyStorePass());
    }

    private static Connection getConnection(ArgumentFacade arguments, String dbUrl)
        throws SQLException {
        return getConnection(dbUrl, arguments.getDbUserName(), arguments.getDbPass(),
            arguments.getUsedSSL(), arguments.getTrustStore(), arguments.getTrustStorePass(),
            arguments.getKeyStore(), arguments.getKeyStorePass());
    }

}
