#!/usr/bin/env bash

docker rm `docker ps --no-trunc -aq`

docker-compose down -v && \
    docker-compose rm -f -s && \
    docker-compose build --pull && \
    docker-compose up -d --remove-orphans && \
    docker-compose logs -f