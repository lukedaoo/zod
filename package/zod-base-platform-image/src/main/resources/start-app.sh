#!/bin/sh

source $BASE_DIR/bin/utils.sh

exportToEnvFromPropertiesFile "$BASE_DIR/env.properties"

java -jar $BASE_DIR/$APP_NAME-${VERSION}.jar