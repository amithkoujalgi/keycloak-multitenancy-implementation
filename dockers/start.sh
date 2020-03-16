#!/usr/bin/env bash

HOST_MACHINE_IP=$(ifconfig | grep "inet " | grep -v 127.0.0.1 | cut -d\  -f2 | head -n1)

docker run \
    -p 8000:8000 \
    -p 443:8443 \
    -p 8001:8001 \
    -p 5000:5000 \
    --env HOST_MACHINE_IP=$HOST_MACHINE_IP \
    --env MYSQL_HOST_IP=192.168.50.237 \
    --env KEYCLOAK_AUTH_SERVER_URL=http://localhost:8000/auth \
    -it custom-kong:1.0
