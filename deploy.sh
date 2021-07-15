#!/bin/bash

ssh root@"$REMOTE_SERVER_IP" 'rm -r /home/Chatroom'
scp -r ~/Downloads/OpreaMadalin_FinalProject_API root@"$REMOTE_SERVER_IP":/home/Chatroom
ssh root@"$REMOTE_SERVER_IP" 'bash /home/Chatroom/docker-run.sh'

#134.122.73.237