#!/bin/sh
source $BASE_DIR/bin/log.sh

MAX_CREATION_DB_RETRY=60
MAX_RETRY_CHECKING_DB=180
DB_DRIVER=org.mariadb.jdbc.Driver

DB_UTILS_JAR=$BASE_DIR/lib/db-utils-$VERSION.jar

findDbNameFromDbUrl() {
    local DB_URL=$1
    local DB_NAME=$(java -jar $DB_UTILS_JAR "getDatabaseName" "$DB_URL")

    if [ "$DB_NAME" == "FAILURE" ]; then
        DB_NAME=exampledb
        logInfo "Using default Database Name exampledb"
    fi
    echo $DB_NAME
}

createDatabase() {
    local DB_URL=$1
    local DB_USER=$2
    local DB_PASS=$3

    local DB_NAME=$(findDbNameFromDbUrl $DB_URL)

    for i in $(seq 1 $MAX_CREATION_DB_RETRY); do
        STATUS=$(java -jar $DB_UTILS_JAR "createDatabase" $DB_DRIVER $DB_URL $DB_USER $DB_PASS "false")
        if [ "$STATUS" == "SUCCESS" ]; then
            logInfo "Database is created..."
            break
        else
            logInfo "Waiting to create Database ${DB_NAME} ..."
            local remains="$(($MAX_CREATION_DB_RETRY - $i))"
            logInfo "$remains attempts remains"
            if [ $remains -eq "0" ]; then
                logInfo "Database ${DB_NAME} is not created"
                exit 1
            fi
            sleep 1
        fi
    done
}

waitingToDB() {
    local DB_URL=$1
    local DB_USER=$2
    local DB_PASS=$3

    local DB_NAME=$(findDbNameFromDbUrl $DB_URL)

    for i in $(seq 1 $MAX_RETRY_CHECKING_DB); do
        STATUS=$(java -jar $DB_UTILS_JAR "checkStatus" $DB_DRIVER $DB_URL $DB_USER $DB_PASS "false")
        if [ "$STATUS" == "SUCCESS" ]; then
            logInfo "Database is started..."
            logInfo "$DB_NAME is already available..."
            break
        elif [ "$STATUS" == "UNKNOWN DATABASE" ]; then
            logInfo "Database is started..."
            logInfo "$DB_NAME is not available..."
            createDatabase $DB_URL $DB_USER $DB_PASS
            break
        else
            logDebug $STATUS
            logInfo "Waiting to Database is started ..."
            local remains="$(($MAX_RETRY_CHECKING_DB - $i))"
            logInfo "$remains attempts remains"
            if [ $remains -eq "0" ]; then
                logInfo "Database is not started"
                exit 1
            fi
            sleep 1
        fi
    done
}
