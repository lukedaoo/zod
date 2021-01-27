#!/bin/sh

source $BASE_DIR/bin/utils.sh

setupJavaOptions() {
    local EXTRA_JAVA_OPTS="-Dcom.atomikos.icatch.registered=true -Dcom.atomikos.icatch.log_base_dir=./transaction-logs/$APP_NAME"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "app.full.name" $APP_FULL_NAME)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "app.name" $APP_NAME)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "warehouse.uri" $WAREHOUSE_URI)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.url" $DB_URL)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.username" $DB_USER)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.password" $DB_PASS)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.dataSourceClassName" $(readJavaOptsFromFile "DB_DATASOURCE_CLASS_NAME"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "hibernate.dialect" $(readJavaOptsFromFile "HIBERNATE_DIALECT"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "hibernate.hbm2ddl" $(readJavaOptsFromFile "HIBERNATE_DDL"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.port" $(readJavaOptsFromFile "FTP_PORT"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.encryptor-strategy" "salted")"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.salted" $(readJavaOptsFromFile "FTP_SALT_STRING"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.numOfLoginPerIp" $(readJavaOptsFromFile "FTP_NUMBER_OF_LOGIN_PER_IP"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.admin.username" $FTP_USER)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.admin.password" $FTP_PASS)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.admin.workspace" $FTP_USER)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.admin.idleTime" $(readJavaOptsFromFile "FTP_IDLE_TIME"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.admin.maxDownloadRate" $(readJavaOptsFromFile "FTP_MAX_DOWNLOAD_RATE"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.admin.maxUploadRate" $(readJavaOptsFromFile "FTP_MAX_UPLOAD_RATE"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.admin.maxConcurrentLogins" $(readJavaOptsFromFile "FTP_MAX_CONCURRENT_LOGIN"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "ftp.root" $WAREHOUSE_URI)"

    local FINAL_JAVA_OPTS="$JAVA_OPTS $EXTRA_JAVA_OPTS"
    export JAVA_OPTS=$FINAL_JAVA_OPTS
}

readJavaOptsFromFile() {
    local KEY=$1
    getProperty $BASE_DIR/java-opts.properties $KEY
}

runJavaApp() {
    java -jar $JAVA_OPTS $BASE_DIR/$APP_NAME-${VERSION}.jar
}

run() {
    setupJavaOptions
    runJavaApp
}

run
