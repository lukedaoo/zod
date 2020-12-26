ARG OPEN_JDK_VERSION="15-jdk-alpine"

FROM openjdk:$OPEN_JDK_VERSION

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-base-image"
LABEL DATE="DEC 21 2020"

COPY lib/* /external/
RUN chmod 755 /external/

#Install Curl
RUN apk add --no-cache curl

#Install Syslog
RUN apk add --no-cache syslog-ng

ADD /container-files/etc/syslog-ng /etc/syslog-ng
RUN chmod 755 /etc/syslog-ng/*

# CMD ["echo $(syslog-ng-ctl reload)"]
