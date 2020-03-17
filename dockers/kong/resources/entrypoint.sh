#!/usr/bin/env bash

# Begin: Start PostgreSQL service
cd ~/apps/ && sh start-postgresql.sh
# End: Start PostgreSQL service

# Begin: Start Kong service
kong migrations bootstrap
kong start --nginx-conf /root/apps/custom_nginx.template
# End: Start Kong service

tail -f /usr/local/kong/logs/error.log
