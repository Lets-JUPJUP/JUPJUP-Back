#!/bin/bash
cd /home/ubuntu/app
DOCKER_APP_NAME=jupjup
# blue를 기준으로 현재 떠있는 컨테이너를 체크한다.
EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

# 컨테이너 스위칭
# blue가 없으면
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
  docker-compose -f docker-compose.${BEFORE_COMPOSE_COLOR}.yml down
  docker rm $(docker ps -q -a -f "name=${BEFORE_COMPOSE_COLOR}")
  echo "$BEFORE_COMPOSE_COLOR down"
fi


echo "Start 기존 이미지 삭제"
docker rmi $(docker images -f "dangling=true" -q)
echo "End 기존 이미지 삭제"
