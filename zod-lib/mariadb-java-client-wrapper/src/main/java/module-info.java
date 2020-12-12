module zod.jdbc.mariadb.client {
    requires java.sql;
    requires org.mariadb.jdbc;

    opens com.infamous.framework.jdbc;
}