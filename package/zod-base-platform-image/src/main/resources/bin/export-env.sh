#!/bin/sh

source $BASE_DIR/bin/utils.sh

export CONTAINERexpor_ID=$(getDockerContainerId)

exportToEnvFromPropertiesFile "$BASE_DIR/env.properties"