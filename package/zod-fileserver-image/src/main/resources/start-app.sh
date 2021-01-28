#!/bin/sh

#Start Syslog-ng
source $BASE_DIR/bin/db-utils.sh
source $BASE_DIR/bin/utils.sh

start() {
#    echo "@define FLUENT_HOST \"$FLUENT_HOST\"" >> /etc/syslog-ng/env.conf
#    echo "@define FLUENT_PORT \"$FLUENT_PORT\"" >> /etc/syslog-ng/env.conf
    source /external/start-syslog.sh
    title "Setup environment"
    . $BASE_DIR/bin/export-env.sh
    title "Waiting to database"
    waitingToDB $DB_URL $DB_USER $DB_PASS
    title "Setup Java Opts and Start Server"
    . $BASE_DIR/bin/start-server.sh
}

start
