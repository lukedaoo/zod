FROM zod-base-platform-image

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-base-platform-image"
LABEL DATE="DEC 21 2020"

ENV APP_FULL_NAME="Zod File Server"
ENV APP_NAME=zod-fileserver
ENV VERSION=1.0.0-SNAPSHOT

COPY ./target/$APP_NAME-$VERSION.jar $BASE_DIR/

ADD src/main/resources/bin/start-server.sh $BASE_DIR/bin/start-server.sh
RUN chmod 755 $BASE_DIR/bin/start-server.sh

ADD /src/main/resources/* $BASE_DIR/
RUN chmod 755  $BASE_DIR/*

WORKDIR $BASE_DIR/