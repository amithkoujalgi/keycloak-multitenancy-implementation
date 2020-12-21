#!/bin/bash
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
    print_style "Invalid arguments!" "danger";
    echo ""
    print_style "Usage: " "info";
    print_style "      ./run.sh [ip]" "info";
    echo ""
    print_style "Example:" "info";
    print_style "      ./run.sh 192.168.0.50" "info";
    echo ""
    print_style "Use one of the entries from the detected IP addresses: " "info";
    print_style "------------------------------------------------------" "info";
    ip_list=`ifconfig | grep inet | grep netmask | grep broadcast | cut -d" " -f2`
    print_style $ip_list "info";
    echo ""
    exit 1
}

if [[ $# -eq 0 ]] || [[ $# -ne 1 ]]; then
	usage
fi

export IP_ADDRESS=$1

echo "y" | docker container prune
docker rm `docker ps --no-trunc -aq`
docker-compose down -v && \
    docker-compose rm -f -s && \
    docker-compose build --pull && \
    docker-compose up --remove-orphans && \
    docker-compose logs -f