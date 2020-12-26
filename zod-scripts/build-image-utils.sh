#!/bin/sh

packages=("package/zod-base-image" "package/zod-base-platform-image" "package/zod-fileserver-image")
tags=("zod-base-image" "zod-base-platform-image" "zod-fileserver-image")

buildSingleImage() {
    TAG=$1
    LOCATION_DOCKER_FILE=$2

    docker build --no-cache -t $TAG $LOCATION_DOCKER_FILE
}

buildImages() {
    local ZOD_FOLDER=$1

    echo "Zod project folder: $ZOD_FOLDER"
    length=${#packages[@]}

    for ((i = 1; i < length + 1; i++)); do
        TAG=${tags[$i]}
        LOCATION_DOCKER_FILE=$ZOD_FOLDER/${packages[$i]}

        echo "Build Docker Image $TAG at $LOCATION_DOCKER_FILE"
        buildSingleImage $TAG $LOCATION_DOCKER_FILE
    done
}
