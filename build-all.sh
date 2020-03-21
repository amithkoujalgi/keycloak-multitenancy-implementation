#!/usr/bin/env bash

cd ./multi-tenant-auth-server/ && mvn clean install && ./build-image.sh
cd -
cd ./dockers/kong-keycloak/ && ./build.sh
cd -
mkdir -p ./docker-compose/resources/tmp
cp ./multi-tenant-auth-server/target/multi-tenant-auth-server-0.0.1-SNAPSHOT.jar ./docker-compose/resources/tmp/auth-server.jar