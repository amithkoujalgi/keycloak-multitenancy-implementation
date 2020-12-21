#!/bin/bash

echo "y" | docker container prune
docker rm `docker ps --no-trunc -aq`
docker-compose down -v && \
    docker-compose rm -f -s && \
    docker-compose build --pull && \
    docker-compose up --remove-orphans && \
    docker-compose logs -f