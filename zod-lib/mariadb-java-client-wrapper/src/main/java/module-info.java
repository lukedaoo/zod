module mariadb.java.client.wrapper {
    requires java.sql;
    requires org.mariadb.jdbc;

    opens com.infamous.framework.jdbc;
}