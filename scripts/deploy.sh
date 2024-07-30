#!/bin/bash
cd /home/ubuntu/app
DOCKER_APP_NAME=jupjup

# 현재 실행 중인 컨테이너 확인
EXIST_BLUE=$(docker compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps -q)
EXIST_GREEN=$(docker compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml ps -q)

# 컨테이너 스위칭
if [ -z "$EXIST_BLUE" ]; then
    echo "blue up"
    docker compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up --build -d
    BEFORE_COMPOSE_COLOR="green"
    AFTER_COMPOSE_COLOR="blue"

    # 이전 컨테이너가 존재하면 종료 및 제거
    if [ -n "$EXIST_GREEN" ]; then
        echo "$BEFORE_COMPOSE_COLOR down"
        docker compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
    fi

else
    echo "green up"
    docker compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up --build -d
    BEFORE_COMPOSE_COLOR="blue"
    AFTER_COMPOSE_COLOR="green"

    # 이전 컨테이너가 존재하면 종료 및 제거
    if [ -n "$EXIST_BLUE" ]; then
        echo "$BEFORE_COMPOSE_COLOR down"
        docker compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
    fi
fi

echo "Start sleep"
sleep 10
echo "End sleep"

# 새로운 컨테이너가 제대로 떴는지 확인
EXIST_AFTER=$(docker compose -p ${DOCKER_APP_NAME}-${AFTER_COMPOSE_COLOR} -f docker-compose.${AFTER_COMPOSE_COLOR}.yml ps -q)

if [ -n "$EXIST_AFTER" ]; then
  echo "Deployment successful."
else
    echo "> The new container did not run properly."
    exit 1  
fi
