package com.infamous.framework.persistence.utils;

// @formatter:off
/**
 * Example:
 * 1.
 * Command getDatabaseName: java -jar db-utils.jar getDatabaseName jdbc:mariadb://localhost:3306/xyz
 * Result: xyz
 * 2.
 * Command checkStatus: java -jar db-utils.jar checkStatus org.mariadb.jdbc.Driver jdbc:mariadb://localhost:3306/xyz root mysql false
 * Result: SUCCESS
 * Command checkStatus: java -jar db-utils.jar checkStatus org.mariadb.jdbc.Driver jdbc:mariadb://localhost:3306/xyz root mysql123 false
 * Result: FAILURE Access denied for user 'root'@'localhost' (using password: YES)
 * 3.
 * Command createDatabase: java -jar rtz-lib/db-utils/target/db-utils.jar createDatabase org.mariadb.jdbc.Driver jdbc:mariadb://localhost:3306/xyz root mysql false
 * Result SUCCESS
 */
// @formatter:on
public class DBUtils {

    public static void main(String[] args) {
        if (args.length <= 0) {
            printHelper();
            return;
        }

        try {
            ArgumentFacade arguments = new ArgumentFacade(args);

            SupportedMethod method = arguments.getMethod();

            StatusMessage status = method.getExecuteFunction().apply(arguments);
            status.print();
        } catch (UnsupportedOperationException e) {
            printHelper();
        } catch (Exception e) {
            print(e.getMessage());
        }

    }

    private static void printHelper() {
        print("Usage {method} {argument}");
        print("1. checkStatus {DRIVER} {DATABASE_URL}");
        print("2. createDatabase {DRIVER} {DB_URL} {DB_NAME} {DB_PASS} {IS_SECURE} "
            + "{TRUST_STORE_PATH} {TRUST_STORE_PASSWORD} {KEY_STORE_PATH} {KEY_STORE_PASSWORD}");
        print("3. getDatabaseName ${DB_URL}");
    }

    private static void print(String message) {
        System.out.println(message);
    }
}
