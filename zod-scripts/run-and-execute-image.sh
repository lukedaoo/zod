#!/bin/sh

run() {
  local DOCKER_CONTAINER_ID=$(docker run -d $1)
  docker exec -it $DOCKER_CONTAINER_ID sh
}

run $@