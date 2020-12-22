FROM zod-base-image

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-base-platform-image"
LABEL DATE="DEC 21 2020"

ENV BASE_DIR=/zod-app
RUN mkdir -p $BASE_DIR

ADD src/main/resources/start-app.sh $BASE_DIR
RUN chmod 755 /zod-app/start-app.sh

ADD /src/main/resources/bin/* $BASE_DIR/bin/
RUN chmod 755  $BASE_DIR/bin/*

WORKDIR $BASE_DIR/

ENTRYPOINT ["/bin/sh"]