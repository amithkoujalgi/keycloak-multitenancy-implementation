#!/usr/bin/env bash

# Begin: Start PostgreSQL service
cd ~/apps/ && sh start-postgresql.sh
# End: Start PostgreSQL service

# Begin: Start Kong service
kong migrations bootstrap
kong start
# End: Start Kong service

# Add services

curl -i -X POST \
  --url http://localhost:8001/services/ \
  --data 'name=IAMServerService' \
  --data "url=http://keycloak-server:8080"


# Add routes

curl -i -X POST \
  --url http://localhost:8001/services/IAMServerService/routes \
  --data 'name=IAMServerServiceRoute' \
  --data "hosts[]=keycloak-server" \
  --data "hosts[]=localhost" \
  --data 'paths[]=/auth' \
  --data 'methods[]=GET' \
  --data 'methods[]=POST' \
  --data 'methods[]=PUT' \
  --data 'methods[]=DELETE' \
  --data 'methods[]=OPTIONS' \
  --data 'methods[]=HEAD' \
  --data 'methods[]=PATCH' \
  --data 'strip_path=false' \
  --data 'preserve_host=true'

tail -f /usr/local/kong/logs/error.log
# End: Add services and routes to Internal Kong Gateway