#!/bin/bash

cd /home/ubuntu/app

DOCKER_APP_NAME=server

EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)
echo "$EXIST_BLUE" >> debug.log
if [ -z "$EXIST_BLUE" ]; then
    echo "blue up"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up --build -d
    BEFORE_COMPOSE_COLOR="green"
    AFTER_COMPOSE_COLOR="blue"
# blue가 있으면
else
    echo "green up"
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up --build -d
    BEFORE_COMPOSE_COLOR="blue"
    AFTER_COMPOSE_COLOR="green"
fi

echo "Start sleep"
sleep 10
echo "End sleep"

# 새로운 컨테이너가 제대로 떴있는지 확인
EXIST_AFTER=$(docker-compose -p ${DOCKER_APP_NAME}-${AFTER_COMPOSE_COLOR} -f docker-compose.${AFTER_COMPOSE_COLOR}.yml ps | grep Up)

# {EXIST_AFTER}가 있으면
if [ -n "$EXIST_AFTER" ]; then
  # 이전 컨테이너 종료
  echo "$BEFORE_COMPOSE_COLOR down"
  docker-compose -p ${DOCKER_APP_NAME}-${BEFORE_COMPOSE_COLOR} -f docker-compose.${BEFORE_COMPOSE_COLOR}.yml down
  docker image prune -af
else
    echo "> The new container did not run properly."
    exit 1
fi