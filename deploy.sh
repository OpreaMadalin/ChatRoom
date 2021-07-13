#!/bin/bash

ssh root@134.122.73.237 'rm -r /home/Chatroom'
scp -r ~/Downloads/Chatroom root@134.122.73.237:/home/Chatroom
ssh root@134.122.73.237 'bash /home/Chatroom/docker-run.sh'

