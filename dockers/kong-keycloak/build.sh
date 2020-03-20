#!/usr/bin/env bash

function print_style() {
  if [ "$2" == "info" ]; then
    COLOR="96m"
  elif [ "$2" == "success" ]; then
    COLOR="92m"
  elif [ "$2" == "warning" ]; then
    COLOR="93m"
  elif [ "$2" == "danger" ]; then
    COLOR="91m"
  else #default color
    COLOR="0m"
  fi
  STARTCOLOR="\e[$COLOR"
  ENDCOLOR="\e[0m"
  printf "$STARTCOLOR%b$ENDCOLOR" "$1\n"
}

function usage() {
  print_style "Invalid arguments!" "danger"
  echo ""
  print_style "Usage: " "info"
  print_style "      ./build.sh [tag]" "info"
  echo ""
  print_style "Example:" "info"
  print_style "      ./build.sh v1.3.2" "info"
  echo ""
  exit 1
}

if [[ $# -eq 0 ]] || [[ $# -ne 1 ]]; then
  usage
fi

TAG=$1

IMAGE_NAME=kong-keycloak:$TAG
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