#!/bin/bash
docker_image_name="jujemu/chat-app"

chmod +x ./gradlew
./gradlew clean build -x test

if ! docker network ls | grep -q chat_network; then
  docker network create --driver bridge chat_network
fi

docker run -d \
  --name db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e TZ=Asia/Seoul \
  --expose 3306 \
  -p 3306:3306 \
  --network chat_network \
  mysql

if ! docker images | grep -q web-server:latest; then
  docker image rm -f web-server:latest
fi
docker build -t web-server:latest -f ./nginx/Dockerfile-nginx ./nginx

if ! docker images | grep -q ${docker_image_name}:latest; then
  docker image rm -f ${docker_image_name}:latest
fi
docker build -t "${docker_image_name}:latest" .

sleep 5

docker-compose up -d