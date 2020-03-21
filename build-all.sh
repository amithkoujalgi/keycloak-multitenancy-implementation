#!/usr/bin/env bash

cd ./multi-tenant-auth-server/ && mvn clean install && ./build-image.sh
cd -
cd ./dockers/kong-keycloak/ && ./build.sh