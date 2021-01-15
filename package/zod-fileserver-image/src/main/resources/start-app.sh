#!/bin/sh

#Start Syslog-ng
source /external/start-syslog.sh
source $BASE_DIR/bin/db-utils.sh
source $BASE_DIR/bin/utils.sh

start() {
    title "Setup environment"
    . $BASE_DIR/bin/export-env.sh
    title "Waiting to database"
    waitingToDB $DB_URL $DB_USER $DB_PASS
    title "Setup Java Opts and Start Server"
    . $BASE_DIR/bin/start-server.sh
}

start
