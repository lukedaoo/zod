package com.infamous.framework.logging.core;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;

public class LogMessage {

    private @Getter final String m_message;
    private @Getter final Object[] m_arguments;

    public LogMessage(String message, Object[] args) {
        this.m_message = message;
        this.m_arguments = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogMessage that = (LogMessage) o;
        return Objects.equals(m_message, that.m_message) &&
            Arrays.equals(m_arguments, that.m_arguments);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(m_message);
        result = 31 * result + Arrays.hashCode(m_arguments);
        return result;
    }
}
