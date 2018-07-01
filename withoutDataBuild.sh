#!/usr/bin/env bash

set -e
gradle build
dockerTag=opaapao/3dvize:withoutData
docker build -t $dockerTag ./Dockers/Docker/
docker push $dockerTag
#docker run -it -v /home/alop0715/diplom/:/app/data/ -p 4567:4567 $dockerTag