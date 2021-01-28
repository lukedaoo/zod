ARG FLUTENT_BIT_VERSION="1.6.10-debug"

FROM fluent/fluent-bit:$FLUTENT_BIT_VERSION

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-base-image"
LABEL DATE="JAN 27 2020"

# CMD ["echo $(syslog-ng-ctl reload)"]

ADD /container-files/fluent-tcp.conf /fluent-bit/etc/

ENTRYPOINT ["/fluent-bit/bin/fluent-bit", "-c", "/fluent-bit/etc/fluent-tcp.conf"]
