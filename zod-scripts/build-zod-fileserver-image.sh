#!/bin/sh

docker build -t zod-base-platform-image ../package/zod-base-platform-image/
docker build -t zod-fileserver-image ../package/zod-fileserver-image/