#!/bin/sh

export TENANTS="org1,org2"
java -jar /apps/appserver.jar --app.keycloak.server.admin.url=${KC_ADMIN_ENDPOINT} --app.keycloak.custom-realm.root-url=http://{realm-name}.${KC_AUTH_HOST}:${KC_AUTH_PORT}