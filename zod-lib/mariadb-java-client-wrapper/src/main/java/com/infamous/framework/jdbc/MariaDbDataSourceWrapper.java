package com.infamous.framework.jdbc;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import org.mariadb.jdbc.HostAddress;
import org.mariadb.jdbc.MariaDbConnection;
import org.mariadb.jdbc.MariaDbPooledConnection;
import org.mariadb.jdbc.MariaXaConnection;
import org.mariadb.jdbc.UrlParser;
import org.mariadb.jdbc.internal.util.exceptions.ExceptionFactory;
import org.mariadb.jdbc.internal.util.pool.GlobalStateInfo;

// Support cachePrepStmts, prepStmtCacheSize, prepStmtCacheSqlLimit, useServerPrepStmts
public class MariaDbDataSourceWrapper implements DataSource, ConnectionPoolDataSource, XADataSource {

    private UrlParser urlParser;
    private String hostname;
    private Integer port = 3306;
    private Integer connectTimeoutInMs;
    private String database;
    private String url;
    private String user;
    private String password;
    private String cachePrepStmts;
    private String prepStmtCacheSize;
    private String prepStmtCacheSqlLimit;
    private String useServerPrepStmts;

    public MariaDbDataSourceWrapper(String hostname, int port, String database) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
    }

    public MariaDbDataSourceWrapper(String url) {
        this.url = url;
    }

    public MariaDbDataSourceWrapper() {
    }

    public String getDatabaseName() {
        if (this.database != null) {
            return this.database;
        } else {
            return this.urlParser != null && this.urlParser.getDatabase() != null ? this.urlParser.getDatabase() : "";
        }
    }

    public void setDatabaseName(String database) throws SQLException {
        this.database = database;
        this.reInitializeIfNeeded();
    }

    public String getUser() {
        if (this.user != null) {
            return this.user;
        } else {
            return this.urlParser != null ? this.urlParser.getUsername() : null;
        }
    }

    public void setUser(String user) throws SQLException {
        this.user = user;
        this.reInitializeIfNeeded();
    }

    public String getUserName() {
        return this.getUser();
    }

    public void setUserName(String userName) throws SQLException {
        this.setUser(userName);
    }

    public void setPassword(String password) throws SQLException {
        this.password = password;
        this.reInitializeIfNeeded();
    }

    public int getPort() {
        if (this.port != 0) {
            return this.port;
        } else {
            return this.urlParser != null ? ((HostAddress) this.urlParser.getHostAddresses().get(0)).port : 3306;
        }
    }

    public void setPort(int port) throws SQLException {
        this.port = port;
        this.reInitializeIfNeeded();
    }

    public int getPortNumber() {
        return this.getPort();
    }

    public void setPortNumber(int port) throws SQLException {
        if (port > 0) {
            this.setPort(port);
        }

    }

    public void setUrl(String url) throws SQLException {
        this.url = url;
        this.reInitializeIfNeeded();
    }

    public String getServerName() {
        if (this.hostname != null) {
            return this.hostname;
        } else {
            boolean hasHost =
                this.urlParser != null && ((HostAddress) this.urlParser.getHostAddresses().get(0)).host != null;
            return hasHost ? ((HostAddress) this.urlParser.getHostAddresses().get(0)).host : "localhost";
        }
    }

    public void setServerName(String serverName) throws SQLException {
        this.hostname = serverName;
        this.reInitializeIfNeeded();
    }

    public void setCachePrepStmts(String cachePrepStmts) throws SQLException {
        this.cachePrepStmts = cachePrepStmts;
        this.reInitializeIfNeeded();
    }

    public void setPrepStmtCacheSize(String prepStmtCacheSize) throws SQLException {
        this.prepStmtCacheSize = prepStmtCacheSize;
        this.reInitializeIfNeeded();
    }

    public void setPrepStmtCacheSqlLimit(String prepStmtCacheSqlLimit) throws SQLException {
        this.prepStmtCacheSqlLimit = prepStmtCacheSqlLimit;
        this.reInitializeIfNeeded();
    }

    public void setUseServerPrepStmts(String useServerPrepStmts) throws SQLException {
        this.useServerPrepStmts = useServerPrepStmts;
        this.reInitializeIfNeeded();
    }

    public Connection getConnection() throws SQLException {
        try {
            if (this.urlParser == null) {
                this.initialize();
            }

            return MariaDbConnection.newConnection(this.urlParser, (GlobalStateInfo) null);
        } catch (SQLException var2) {
            throw ExceptionFactory.INSTANCE.create(var2);
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        try {
            if (this.urlParser == null) {
                this.user = username;
                this.password = password;
                this.initialize();
            }

            UrlParser urlParser = (UrlParser) this.urlParser.clone();
            urlParser.setUsername(username);
            urlParser.setPassword(password);
            return MariaDbConnection.newConnection(urlParser, (GlobalStateInfo) null);
        } catch (SQLException var4) {
            throw ExceptionFactory.INSTANCE.create(var4);
        } catch (CloneNotSupportedException var5) {
            throw ExceptionFactory.INSTANCE.create("Error in configuration");
        }
    }

    public PrintWriter getLogWriter() {
        return null;
    }

    public void setLogWriter(PrintWriter out) {
    }

    public int getLoginTimeout() {
        if (this.connectTimeoutInMs != null) {
            return this.connectTimeoutInMs / 1000;
        } else {
            return this.urlParser != null ? this.urlParser.getOptions().connectTimeout / 1000 : 30;
        }
    }

    public void setLoginTimeout(int seconds) {
        this.connectTimeoutInMs = seconds * 1000;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            if (this.isWrapperFor(iface)) {
                return iface.cast(this);
            } else {
                throw new SQLException("The receiver is not a wrapper and does not implement the interface");
            }
        } catch (Exception var3) {
            throw new SQLException("The receiver is not a wrapper and does not implement the interface");
        }
    }

    public boolean isWrapperFor(Class<?> interfaceOrWrapper) throws SQLException {
        return interfaceOrWrapper.isInstance(this);
    }

    public PooledConnection getPooledConnection() throws SQLException {
        return new MariaDbPooledConnection((MariaDbConnection) this.getConnection());
    }

    public PooledConnection getPooledConnection(String user, String password) throws SQLException {
        return new MariaDbPooledConnection((MariaDbConnection) this.getConnection(user, password));
    }

    public XAConnection getXAConnection() throws SQLException {
        return new MariaXaConnection((MariaDbConnection) this.getConnection());
    }

    public XAConnection getXAConnection(String user, String password) throws SQLException {
        return new MariaXaConnection((MariaDbConnection) this.getConnection(user, password));
    }

    public Logger getParentLogger() {
        return null;
    }

    protected UrlParser getUrlParser() {
        return this.urlParser;
    }

    private void reInitializeIfNeeded() throws SQLException {
        if (this.urlParser != null) {
            this.initialize();
        }

    }

    protected synchronized void initialize() throws SQLException {
        if (this.url == null || this.url.isEmpty()) {
            throw new SQLException("Property 'url' is required");
        }
        Properties props = new Properties();
        if (this.user != null) {
            props.setProperty("user", this.user);
        }

        if (this.password != null) {
            props.setProperty("password", this.password);
        }

        if (this.database != null) {
            props.setProperty("database", this.database);
        }

        if (this.connectTimeoutInMs != null) {
            props.setProperty("connectTimeout", String.valueOf(this.connectTimeoutInMs));
        }

        if (this.cachePrepStmts != null) {
            props.setProperty("cachePrepStmts", this.cachePrepStmts);
        }

        if (this.prepStmtCacheSize != null) {
            props.setProperty("prepStmtCacheSize", this.prepStmtCacheSize);
        }

        if (this.prepStmtCacheSqlLimit != null) {
            props.setProperty("prepStmtCacheSqlLimit", this.prepStmtCacheSqlLimit);
        }

        if (this.useServerPrepStmts != null) {
            props.setProperty("useServerPrepStmts", this.useServerPrepStmts);
        }

        this.urlParser = UrlParser.parse(this.url, props);
    }
}

