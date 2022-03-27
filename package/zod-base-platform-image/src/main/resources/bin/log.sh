#!/bin/sh

log() {
    local LEVEL=$1
    shift
    local MESSAGE="[application=$APP_NAME, container_id=$CONTAINER_ID, type=debug, scope=global] $*"
    logger -p $LEVEL $MESSAGE
    if [[ "$DEBUG" == "TRUE" || "$DEBUG" == "true" ]]; then
        echo $MESSAGE
    fi
}

logDebug() {
    log debug $1
}

logInfo() {
    log info $1
}

logError() {
    log error $1
}

logTrace() {
    log notice $1
}
