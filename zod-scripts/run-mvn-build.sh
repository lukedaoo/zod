#!/bin/sh

ZOD_FOLDER="$(dirname "$PWD")"
cd $ZOD_FOLDER

source ./zod-scripts/zod-common-script.sh

mvnBuild -DskipTests -T 4