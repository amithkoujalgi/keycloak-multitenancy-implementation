#!/usr/bin/env bash

# Add services

curl -i -X POST \
  --url http://kong:8001/services/ \
  --data 'name=IAMServerService' \
  --data "url=http://keycloak:8080"

curl -i -X POST \
  --url http://kong:8001/services/ \
  --data 'name=AuthServerService' \
  --data "url=http://auth-server:9090"

# Add routes

curl -i -X POST \
  --url http://kong:8001/services/IAMServerService/routes \
  --data 'name=IAMServerServiceRoute' \
  --data "hosts[]=kong" \
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
  --url http://kong:8001/services/AuthServerService/routes \
  --data 'name=AuthServerServiceRoute' \
  --data "hosts[]=kong" \
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