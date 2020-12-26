#!/bin/sh

ZOD_FOLDER="$(dirname "$PWD")"
cd $ZOD_FOLDER

source ./zod-scripts/build-image-utils.sh

buildImages $ZOD_FOLDER