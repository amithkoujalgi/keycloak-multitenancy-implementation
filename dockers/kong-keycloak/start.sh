#!/usr/bin/env bash

docker run \
    -e KEYCLOAK_USER=admin \
    -e KEYCLOAK_PASSWORD=admin \
    -e DB_VENDOR=h2 \
    -p 8000:8000 \
    -p 8001:8001 \
    -p 443:443 \
    -p 8080:8080 \
    -p 8443:8443 \
    -it kong-keycloak:1.0



#
#      DB_ADDR: mysql
#      DB_USER: keycloak
#      DB_PASSWORD: keycloak
#      DB_DATABASE: keycloak
#      DB_PORT: 3306