#!/usr/bin/env bash

PORT_TO_CHECK=8080

ACTIVE=0

while [ $ACTIVE -eq 0 ]
do
    sleep 1
    (echo >/dev/tcp/localhost/$PORT_TO_CHECK) &>/dev/null && ACTIVE=1 || echo "TCP port $PORT_TO_CHECK is not up yet. Trying again..."
done

sleep 5

echo "Starting auth server..."

java -jar /apps/appserver.jar &