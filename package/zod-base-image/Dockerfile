ARG OPEN_JDK_VERSION="15-jdk-alpine"

FROM openjdk:$OPEN_JDK_VERSION

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-base-image"
LABEL DATE="DEC 21 2020"

COPY lib/* /external/

#Install Curl
RUN apk add --no-cache curl

ENTRYPOINT ["/bin/sh"]
