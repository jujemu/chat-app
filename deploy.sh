#!/bin/bash
docker_image_name="jujemu/chat-app"

# 그레이들 빌드
chmod +x ./gradlew
./gradlew clean build -x test

# private 네트워크 생성, 있으면 생성하지 않는다.
if ! docker network ls | grep -q chat_network; then
  docker network create --driver bridge chat_network
fi

# mysql 와 redis 실행, 스프링 부트가 시작하자마자 연결상태를 확인하므로 따로 실행한다.
docker run -d \
  --name db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e TZ=Asia/Seoul \
  --expose 3306 \
  -p 3306:3306 \
  --network chat_network \
  mysql

docker run -d \
  --name redis \
  --expose 6379 \
  -p 6379:6379 \
  redis

# nginx 빌드, 설정파일을 바꾸기 위해 따로 빌드한다.
if ! docker images | grep -q web-server:latest; then
  docker image rm -f web-server:latest
fi
docker build -t web-server:latest -f ./nginx/Dockerfile-nginx ./nginx

# 스프링 애플리케이션 빌드, 있으면 지우고 다시 빌드한다.
if ! docker images | grep -q ${docker_image_name}:latest; then
  docker image rm -f ${docker_image_name}:latest
fi
docker build -t "${docker_image_name}:latest" .

if ! docker images | grep -q ${docker_image_name}:redis; then
  docker image rm -f ${docker_image_name}:redis
fi
docker build -t "${docker_image_name}:redis" -f redis-pubsub/Dockerfile redis-pubsub

# mysql 이 완전히 실행될 때까지 기다린다.
sleep 5

# 도커 컴포즈 실행
docker-compose up -d