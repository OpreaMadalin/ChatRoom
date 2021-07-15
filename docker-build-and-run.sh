#!/bin/bash

rm -r ./target/
./mvnw package

docker build -t chatroom-rest-image .
docker rm chatroom-rest-container
docker run -p 8080:8080 --name chatroom-rest-container chatroom-rest-image