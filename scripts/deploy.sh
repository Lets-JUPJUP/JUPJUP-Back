#!/bin/bash

cd /home/ubuntu/app

DOCKER_APP_NAME=spring

EXIST_BLUE=$(docker compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)
echo "$EXIST_BLUE" >> debug.log
if [ -z "$EXIST_BLUE" ]; then
	echo "blue up" >> debug.log
	docker compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

	sleep 30

	docker compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
	docker image prune -af

else
	echo "green up" >> debug.log
	docker compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

	sleep 30

	docker compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
	docker image prune -af
fi