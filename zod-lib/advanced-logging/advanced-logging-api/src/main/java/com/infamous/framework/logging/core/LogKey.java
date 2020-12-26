package com.infamous.framework.logging.core;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Builder
public class LogKey {

    private @Getter final String m_applicationName;

    private @Getter final String m_logScope;

    private @Getter final String m_logType;

    public LogKey(String applicationName, String logType, String logScope) {
        m_applicationName = applicationName;
        m_logType = logType;
        m_logScope = logScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogKey logKey = (LogKey) o;
        return
            Objects.equals(m_applicationName, logKey.m_applicationName) &&
                Objects.equals(m_logScope, logKey.m_logScope) &&
                Objects.equals(m_logType, logKey.m_logType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_applicationName, m_logScope, m_logType);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("application='").append(m_applicationName).append('\'');
        sb.append(", scope='").append(m_logScope).append('\'');
        sb.append(", type='").append(m_logType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
