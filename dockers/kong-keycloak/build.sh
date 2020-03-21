#!/usr/bin/env bash

IMAGE_NAME=kong-keycloak:1.0
docker build -t $IMAGE_NAME .
if [[ "$(docker images -f "dangling=true" -q)" == "" ]]; then
    echo ""
    echo "No dangling images to be cleand up. Docker image [$IMAGE_NAME] is now ready for use."
    echo ""
else
    echo ""
    echo "Cleaning up dangling images..."
    docker rmi -f $(docker images -f "dangling=true" -q)
    echo "Clean up completed. Docker image [$IMAGE_NAME] is now ready for use."
    echo ""
fi