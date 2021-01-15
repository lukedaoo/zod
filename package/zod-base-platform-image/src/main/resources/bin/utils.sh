#!/bin/sh

source $BASE_DIR/bin/log.sh

title() {
    echo "============ $1 ============"
}


getDockerContainerId() {
    local FULL_CONTAINER_ID=$(head -1 /proc/self/cgroup | cut -d/ -f3)
    echo $FULL_CONTAINER_ID | cut -c1-20
}

getProperty() {
    PROPERTY_FILE=$1
    PROP_KEY=$2
    PROP_VALUE=$(cat $PROPERTY_FILE | grep "$PROP_KEY" | cut -d'=' -f2-)
    echo $PROP_VALUE
}

checkAndExport() {
    PROPERTY_FILE=$1
    PROP_KEY=$2
    DEFAULT_VALUE=$3

    PROP_VALUE=$(getProperty $PROPERTY_FILE $PROP_KEY)

    if [ -z "$PROP_VALUE" ]; then
        PROP_VALUE=$DEFAULT_VALUE
        logInfo "$PROP_KEY is NULL. Exporting $PROP_KEY using default value $DEFAULT_VALUE"
    else
        logInfo "Export $PROP_KEY=$PROP_VALUE"
    fi
    export $PROP_KEY=$PROP_VALUE
}

exportToEnvFromPropertiesFile() {
    FILE=$1
    #ENV_FILE=$2
    #PROPERTIES=$2
    if [ -f "$FILE" ]; then
        logInfo "Properties file [$FILE] found."

        while IFS= read -r line || [ -n "$line" ]; do
            key=$(echo $line | cut -d "=" -f1)
            value=$(echo $line | cut -d "=" -f2)
            if [[ -z "$(echo $key | grep -i pass)" ]]; then
                logInfo "Export ENV $key = $value"
            else
                logInfo "Export ENV $key = ******"
            fi
            export $key=$value
            #PROPERTIES["$key"]=$value
            #echo "export $key=$value" >> $2
        done <$FILE
    else
        logInfo "Properties file [$FILE] does not found."
    fi
}


d() {
    #Append java opts
    local KEY=$1
    local VALUE=$2

    echo " -D$KEY=$VALUE"
}