#!/usr/bin/env bash


cat ./Dockers/DockerWithData/Dockerfile > Dockerfile

set -e
gradle build
dockerTag=opaapao/3dvize:withData
docker build -t $dockerTag .
docker push $dockerTag
#docker run -it -p 4567:4567 $dockerTag