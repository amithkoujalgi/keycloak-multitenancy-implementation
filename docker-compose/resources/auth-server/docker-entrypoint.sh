#!/bin/sh

echo "Adding external Kong services and routes..."

# -- SERVICES --
curl -i -X POST \
  --url http://kong:8001/services/ \
  --data 'name=AuthServerService' \
  --data "url=http://auth-server:9090"\
  --data 'connect_timeout=3600000'\
  --data 'read_timeout=3600000'\
  --data 'write_timeout=3600000'

curl -i -X POST \
  --url http://kong:8001/services/ \
  --data 'name=KeycloakService' \
  --data "url=http://keycloak:8080"\
  --data 'connect_timeout=3600000'\
  --data 'read_timeout=3600000'\
  --data 'write_timeout=3600000'

# -- ROUTES --

curl -i -X POST \
  --url http://kong:8001/services/AuthServerService/routes \
  --data 'name=AuthServerServiceRoute' \
  --data "hosts[]=*.localhost" \
  --data "hosts[]=kong" \
  --data "hosts[]=keycloak" \
  --data 'paths[]=/authenticate' \
  --data 'paths[]=/sso/login' \
  --data 'paths[]=/' \
  --data 'methods[]=GET' \
  --data 'methods[]=POST' \
  --data 'methods[]=PUT' \
  --data 'methods[]=DELETE' \
  --data 'methods[]=OPTIONS' \
  --data 'methods[]=HEAD' \
  --data 'methods[]=PATCH' \
  --data 'protocols[]=http' \
  --data 'protocols[]=https' \
  --data 'strip_path=false' \
  --data 'preserve_host=true'

curl -i -X POST \
  --url http://kong:8001/services/KeycloakService/routes \
  --data 'name=KeycloakServiceRoute' \
  --data "hosts[]=*.localhost" \
  --data "hosts[]=kong" \
  --data "hosts[]=auth-server" \
  --data 'paths[]=/auth' \
  --data 'methods[]=GET' \
  --data 'methods[]=POST' \
  --data 'methods[]=PUT' \
  --data 'methods[]=DELETE' \
  --data 'methods[]=OPTIONS' \
  --data 'methods[]=HEAD' \
  --data 'methods[]=PATCH' \
  --data 'protocols[]=http' \
  --data 'protocols[]=https' \
  --data 'strip_path=false' \
  --data 'preserve_host=true'

# export TENANTS="org1,org2,org3"

java -jar /apps/appserver.jar \
  --app.keycloak.server.admin.url=${KC_ADMIN_ENDPOINT} \
  --app.keycloak.custom-realm.root-url=http://{realm-name}.${KC_AUTH_DOMAIN} \
  --app.keycloak.server.auth-domain=${KC_AUTH_DOMAIN}