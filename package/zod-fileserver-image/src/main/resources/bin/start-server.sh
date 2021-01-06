#!/bin/sh

source $BASE_DIR/bin/utils.sh

title() {
    echo "============ $1 ============"
}

setupJavaOptions() {
    title "Setup Java Options"
    local EXTRA_JAVA_OPTS="-Dcom.atomikos.icatch.registered=true -Dcom.atomikos.icatch.log_base_dir=./transaction-logs/$APP_NAME"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "warehouse.uri" $WAREHOUSE_URI)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "db.url" $DB_URL)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "db.username" $DB_USER)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "db.password" $DB_PASS)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "db.dataSourceClassName" $(readJavaOptsFromFile "DB_DATASOURCE_CLASS_NAME"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "hibernate.dialect" $(readJavaOptsFromFile "HIBERNATE_DIALECT"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "hibernate.hbm2ddl" $(readJavaOptsFromFile "HIBERNATE_DDL"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.port" $(readJavaOptsFromFile "FTP_PORT"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.encryptor-strategy" "salted")"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.salted" $(readJavaOptsFromFile "FTP_SALT_STRING"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.numOfLoginPerIp" $(readJavaOptsFromFile "FTP_NUMBER_OF_LOGIN_PER_IP"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.admin.username" $FTP_USER)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.admin.password" $FTP_PASS)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.admin.workspace" $FTP_USER)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.admin.idleTime" $(readJavaOptsFromFile "FTP_IDLE_TIME"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.admin.maxDownloadRate" $(readJavaOptsFromFile "FTP_MAX_DOWNLOAD_RATE"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.admin.maxUploadRate" $(readJavaOptsFromFile "FTP_MAX_UPLOAD_RATE"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.admin.maxConcurrentLogins" $(readJavaOptsFromFile "FTP_MAX_CONCURRENT_LOGIN"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(appendJavaOpts "ftp.root" $WAREHOUSE_URI)"

    local FINAL_JAVA_OPTS="$JAVA_OPTS $EXTRA_JAVA_OPTS"
    export JAVA_OPTS=$FINAL_JAVA_OPTS
}

readJavaOptsFromFile() {
    local KEY=$1
    getProperty $BASE_DIR/java-opts.properties $KEY
}

runJavaApp() {
    title "Run Java app"
    java -jar $JAVA_OPTS $BASE_DIR/$APP_NAME-${VERSION}.jar
}

run() {
    setupJavaOptions
    runJavaApp
}

run
