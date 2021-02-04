ARG FLUTENT_BIT_VERSION="1.6.10-debug"

FROM fluent/fluent-bit:$FLUTENT_BIT_VERSION

LABEL AUTHOR="Luke D <https://github.com/infamousSs>"
LABEL IMAGE_NAME="zod-base-image"
LABEL DATE="JAN 27 2020"

ADD /container-files/fluent-forward.conf /fluent-bit/etc/

ENTRYPOINT ["/fluent-bit/bin/fluent-bit", "-c", "/fluent-bit/etc/fluent-forward.conf"]
