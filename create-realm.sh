#!/usr/bin/env bash

ENDPOINT=http://localhost:8080/auth

RESULT=`curl --data "username=admin&password=admin&grant_type=password&client_id=admin-cli" $ENDPOINT/realms/master/protocol/openid-connect/token`
TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`

echo $TOKEN


# Create realm
curl -v $ENDPOINT/admin/realms/ \
    -H "Content-Type: application/json" \
    -H "Authorization: bearer $TOKEN" \
    --data '{"id":"test", "realm":"test", "enabled":"true"}'

## Create user
#curl -v $ENDPOINT/admin/realms/apiv2/users -H "Content-Type: application/json" -H "Authorization: bearer $TOKEN" \
#    --data '{"firstName":"xyz","lastName":"xyz", "email":"demo2@gmail.com", "enabled":"true"}'