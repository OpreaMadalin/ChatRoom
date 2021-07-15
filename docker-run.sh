#!/bin/bash

cd /home/Chatroom || exit
docker build -t chatroom-rest-image .
docker rm -f chatroom-rest-container
docker run -d -p 8080:8080 --name chatroom-rest-container chatroom-rest-image