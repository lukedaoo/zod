#!/bin/sh

source $BASE_DIR/bin/utils.sh

export CONTAINER_ID=$(getDockerContainerId)

exportToEnvFromPropertiesFile "$BASE_DIR/env.properties"