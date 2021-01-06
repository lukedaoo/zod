FROM zod-base-image

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-base-platform-image"
LABEL DATE="DEC 21 2020"

ENV BASE_DIR=/zod-app
RUN mkdir -p $BASE_DIR

ADD src/main/resources/start-app-example.sh $BASE_DIR
RUN chmod 755 /zod-app/start-app-example.sh

ADD /src/main/resources/bin/* $BASE_DIR/bin/
RUN chmod 755  $BASE_DIR/bin/*

ADD /src/main/resources/lib/* $BASE_DIR/lib/
COPY ./target/lib/* $BASE_DIR/lib/
RUN chmod 755  $BASE_DIR/lib/*

ENTRYPOINT ["/bin/sh", "-c", ". $BASE_DIR/start-app.sh"]