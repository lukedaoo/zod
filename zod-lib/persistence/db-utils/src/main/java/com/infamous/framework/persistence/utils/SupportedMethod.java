package com.infamous.framework.persistence.utils;

import java.util.Arrays;
import java.util.function.Function;

public enum SupportedMethod {
    CHECK_STATUS("checkStatus", ExecutionUtils::checkStatus),
    CREATE_DATABASE("createDatabase", ExecutionUtils::createDatabase),
    GET_DATABASE_NAME("getDatabaseName", argumentFacade -> {
        String dbName = ExecutionUtils.getDatabaseName(argumentFacade);
        return new StatusMessage(dbName);
    });

    private String m_method;
    private Function<ArgumentFacade, StatusMessage> m_executeFunction;

    SupportedMethod(String method, Function<ArgumentFacade, StatusMessage> executeFunction) {
        this.m_method = method;
        this.m_executeFunction = executeFunction;
    }

    public String getMethod() {
        return m_method;
    }

    public Function<ArgumentFacade, StatusMessage> getExecuteFunction() {
        return m_executeFunction;
    }

    public static SupportedMethod fromString(String methodAsString) {
        return Arrays.stream(SupportedMethod.values())
            .filter(method -> method.getMethod().equalsIgnoreCase(methodAsString))
            .findAny().orElse(null);
    }
}
