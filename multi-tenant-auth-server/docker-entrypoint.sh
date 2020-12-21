#!/bin/sh

export TENANTS="org1,org2"

export KC_ADMIN_ENDPOINT=http://kong:8000/auth
export KC_AUTH_DOMAIN=localhost:8000

java -jar /apps/appserver.jar \
  --app.keycloak.server.admin.url=${KC_ADMIN_ENDPOINT} \
  --app.keycloak.custom-realm.root-url=http://{realm-name}.${KC_AUTH_DOMAIN} \
  --app.keycloak.server.auth-domain=${KC_AUTH_DOMAIN}