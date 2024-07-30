#!/bin/bash

IS_DEV1=$(docker ps | grep jupjup-blue)
DEFAULT_CONF=" /etc/nginx/nginx.conf"
MAX_RETRIES=20

check_service() {
  local RETRIES=0
  local URL=$1
  while [ $RETRIES -lt $MAX_RETRIES ]; do
    echo "Checking service at $URL... (attempt: $((RETRIES+1)))"
    sleep 3

    REQUEST=$(curl $URL)
    if [ -n "$REQUEST" ]; then
      echo "health check success"
      return 0
    fi

    RETRIES=$((RETRIES+1))
  done;

  echo "Failed to check service after $MAX_RETRIES attempts."
  return 1
}

if [ -z "$IS_DEV1" ];then
  echo "## DEV2 => DEV1"
  docker-compose up -d dev1

  echo "## health check"
  if ! check_service "http://127.0.0.1:8081"; then
    echo "DEV1 health check 가 실패했습니다."
    exit 1
  fi

  echo "## nginx 재실행"
  sudo cp /etc/nginx/nginx.dev1.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "## DEV2 컨테이너 중지 및 제거"
  docker-compose stop dev2
  docker-compose rm -f dev2

else
  echo "## DEV1 => DEV2"
  docker-compose up -d dev2

  echo "## health check"
  if ! check_service "http://127.0.0.1:8082"; then
      echo "DEV1 health check 가 실패했습니다."
      exit 1
    fi

  echo "## nginx 재실행"
  sudo cp /etc/nginx/nginx.dev2.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "## DEV1 컨테이너 중지 및 제거"
  docker-compose stop dev1
  docker-compose rm -f dev2
fi
