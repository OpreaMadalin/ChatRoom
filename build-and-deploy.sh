#!/bin/bash

rm -r ./target/
./mvnw package

ssh root@134.122.73.237 'rm -r /home/Chatroom'
scp -r ~/Downloads/OpreaMadalin_FinalProject_API root@134.122.73.237:/home/Chatroom
ssh root@134.122.73.237 'bash /home/Chatroom/docker-run.sh'

