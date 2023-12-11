#!/bin/bash
mvn clean install

docker build -t my-app .

docker-compose down
docker-compose up -d

