#!/bin/sh

source $BASE_DIR/bin/utils.sh

setupJavaOptions() {

    local DEBUG_AGENT=$(getDebugAgent)

    local EXTRA_JAVA_OPTS="$DEBUG_AGENT -Dcom.atomikos.icatch.registered=true
        -Dcom.atomikos.icatch.log_base_dir=./transaction-logs/$APP_NAME
        -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "app.full.name" $APP_FULL_NAME)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "app.name" $APP_NAME)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.url" $DB_URL)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.username" $DB_USER)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.password" $DB_PASS)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "db.dataSourceClassName" $(readJavaOptsFromFile "DB_DATASOURCE_CLASS_NAME"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "hibernate.dialect" $(readJavaOptsFromFile "HIBERNATE_DIALECT"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "hibernate.hbm2ddl" $(readJavaOptsFromFile "HIBERNATE_DDL"))"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "fluent.host" $FLUENT_HOST)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "fluent.port" $FLUENT_PORT)"

    local FINAL_JAVA_OPTS="$JAVA_OPTS $EXTRA_JAVA_OPTS"
    export JAVA_OPTS=$FINAL_JAVA_OPTS
}

runJavaApp() {
    java -jar $JAVA_OPTS $BASE_DIR/$APP_NAME-${VERSION}.jar
}

run() {
    setupJavaOptions
    runJavaApp
}

run
