#!/bin/sh

docker build --no-cache -t zod-base-image ../package/zod-base-image/
docker build --no-cache -t zod-base-platform-image ../package/zod-base-platform-image/
docker build --no-cache -t zod-fileserver-image ../package/zod-fileserver-image/