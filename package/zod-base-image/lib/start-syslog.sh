#!/bin/sh

syslog-ng
echo $(syslog-ng-ctl config)
logger "Syslog-ng is started"
cat /var/log/messages

