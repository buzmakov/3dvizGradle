#!/usr/bin/env bash

set -e
gradle build
dockerTag=opaapao/3dvize:withData
docker build -t $dockerTag ./Dockers/DockerWithData/
docker push $dockerTag
#docker run -it -p 4567:4567 $dockerTag