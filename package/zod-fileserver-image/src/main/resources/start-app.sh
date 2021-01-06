#!/bin/sh

#Start Syslog-ng
source /external/start-syslog.sh
source $BASE_DIR/bin/db-utils.sh

. $BASE_DIR/bin/export-env.sh

start() {
    waitingToDB $DB_URL $DB_USER $DB_PASS
    . $BASE_DIR/bin/start-server.sh
}

start