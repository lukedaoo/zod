#!/bin/sh

source $BASE_DIR/bin/utils.sh

setupJavaOptions() {

    local DEBUG_AGENT=$(getDebugAgent)

    local EXTRA_JAVA_OPTS="$DEBUG_AGENT"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "app.full.name" $APP_FULL_NAME)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "app.name" $APP_NAME)"

    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "fluent.host" $FLUENT_HOST)"
    EXTRA_JAVA_OPTS="$EXTRA_JAVA_OPTS $(d "fluent.port" $FLUENT_PORT)"

    local FINAL_JAVA_OPTS="$JAVA_OPTS $EXTRA_JAVA_OPTS"
    export JAVA_OPTS=$FINAL_JAVA_OPTS
}

runJavaApp() {
    java -jar $JAVA_OPTS $BASE_DIR/$APP_NAME-${VERSION}.jar
}

run() {
    setupJavaOptions
    runJavaApp
}

run
