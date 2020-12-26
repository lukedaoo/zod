package com.infamous.framework.persistence.utils;

public class ArgumentFacade {

    private SupportedMethod m_method;
    private String m_driver;
    private String m_dbUrl;
    private String m_dbUserName;
    private String m_dbPass;
    private Boolean m_isUsedSSL;
    private String m_trustStore;
    private String m_trustStorePass;
    private String m_keyStore;
    private String m_keyStorePass;

    public ArgumentFacade(String[] args) {
        try {
            m_method = SupportedMethod.fromString(get(args, 0));
            if (m_method == null) {
                throw new UnsupportedOperationException("Un-supported method");
            }
            if (m_method == SupportedMethod.GET_DATABASE_NAME) {
                m_dbUrl = get(args, 1);
                return;
            }
            m_driver = get(args, 1);

            m_dbUrl = get(args, 2);
            m_dbUserName = get(args, 3);
            m_dbPass = get(args, 4);

            m_isUsedSSL = Boolean.valueOf(get(args, 5));

            m_trustStore = get(args, 6);
            m_trustStorePass = get(args, 7);
            if (m_trustStore != null && m_trustStorePass == null) {
                throw new NullPointerException("Missing Trust Store Password");
            }

            m_keyStore = get(args, 8);
            m_keyStorePass = get(args, 9);
            if (m_keyStore != null && m_keyStorePass == null) {
                throw new NullPointerException("Missing Key Store Password");
            }
        } catch (IndexOutOfBoundsException e) {
            // Dont Care
        }
    }

    private String get(String[] args, int index) {
        try {
            return args[index];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public SupportedMethod getMethod() {
        return m_method;
    }

    public String getDriver() {
        return m_driver;
    }

    public String getDbUrl() {
        return m_dbUrl;
    }

    public String getDbUserName() {
        return m_dbUserName;
    }

    public String getDbPass() {
        return m_dbPass;
    }

    public Boolean getUsedSSL() {
        return m_isUsedSSL;
    }

    public String getTrustStore() {
        return m_trustStore;
    }

    public String getTrustStorePass() {
        return m_trustStorePass;
    }

    public String getKeyStore() {
        return m_keyStore;
    }

    public String getKeyStorePass() {
        return m_keyStorePass;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ArgumentFacade{");
        sb.append("m_method=").append(m_method);
        sb.append(", m_driver='").append(m_driver).append('\'');
        sb.append(", m_dbUrl='").append(m_dbUrl).append('\'');
        sb.append(", m_dbUserName='").append(m_dbUserName).append('\'');
        sb.append(", m_dbPass='").append(m_dbPass).append('\'');
        sb.append(", m_isUsedSSL=").append(m_isUsedSSL);
        sb.append(", m_trustStore='").append(m_trustStore).append('\'');
        sb.append(", m_trustStorePass='").append(m_trustStorePass).append('\'');
        sb.append(", m_keyStore='").append(m_keyStore).append('\'');
        sb.append(", m_keyStorePass='").append(m_keyStorePass).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void setDbUrl(String dbUrl) {
        this.m_dbUrl = dbUrl;
    }
}
