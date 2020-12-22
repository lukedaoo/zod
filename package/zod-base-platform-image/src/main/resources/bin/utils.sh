#!/bin/bash

log() {
    echo "INFO [application=start.up, type=debug, scope=global] $@"
}

getProperty() {
    PROPERTY_FILE=$1
    PROP_KEY=$2
    PROP_VALUE=$(cat $PROPERTY_FILE | grep "$PROP_KEY" | cut -d'=' -f2-)
}

checkAndExport() {
    PROPERTY_FILE=$1
    PROP_KEY=$2
    DEFAULT_VALUE=$3

    PROP_VALUE=$(getProperty $PROPERTY_FILE $PROP_KEY)

    if [ -z "$PROP_VALUE" ]; then
        PROP_VALUE=$DEFAULT_VALUE
        log "$PROP_KEY is NULL. Exporting $PROP_KEY using default value $DEFAULT_VALUE"
    else
        log "Export $PROP_KEY=$PROP_VALUE"
    fi
    export PROP_KEY=PROP_VALUE
}

exportToEnvFromPropertiesFile() {
    file=$1
    if [ -f "$file" ]; then
        log "Properties file [$file] found."

        while IFS='=' read -r key value; do
            if [[ ${key} != *"pass"* ]]; then
                log "Export ENV $key = $value"
            else
                log "Export ENV $key"
            fi
            export $key=$value
        done <$file
    else
        log "Properties file [$file] does not found."
    fi
}
