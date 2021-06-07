FROM openjdk:8-jre-alpine

RUN mkdir /project

COPY ./target/chatroom-0.0.1-SNAPSHOT.jar /project/

WORKDIR /project

RUN ls

CMD MONGO_USER="madalinoprea" MONGO_PASSWORD="K4a2E!4yfwDaz-F" MONGO_CLUSTER="stepitcluster.ce4kx.mongodb.net" MONGO_DB_NAME="devDB" java -jar chatroom-0.0.1-SNAPSHOT.jar