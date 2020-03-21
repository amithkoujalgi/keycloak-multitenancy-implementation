#!/usr/bin/env bash

java -jar /apps/appserver.jar &

# Add services

curl -i -X POST \
  --url http://localhost:8001/services/ \
  --data 'name=IAMServerService' \
  --data "url=http://kong-keycloak:8080"

curl -i -X POST \
  --url http://localhost:8001/services/ \
  --data 'name=AuthServerService' \
  --data "url=http://kong-keycloak:9090"

# Add routes

curl -i -X POST \
  --url http://localhost:8001/services/IAMServerService/routes \
  --data 'name=IAMServerServiceRoute' \
  --data "hosts[]=kong-keycloak" \
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

curl -i -X POST \
  --url http://localhost:8001/services/AuthServerService/routes \
  --data 'name=AuthServerServiceRoute' \
  --data "hosts[]=kong-keycloak" \
  --data "hosts[]=*.localhost" \
  --data 'paths[]=/' \
  --data 'paths[]=/authenticate' \
  --data 'paths[]=/sso/login' \
  --data 'methods[]=GET' \
  --data 'methods[]=POST' \
  --data 'methods[]=PUT' \
  --data 'methods[]=DELETE' \
  --data 'methods[]=OPTIONS' \
  --data 'methods[]=HEAD' \
  --data 'methods[]=PATCH' \
  --data 'strip_path=false' \
  --data 'preserve_host=true'

# End: Add services and routes to Internal Kong Gateway