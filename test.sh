#!/usr/bin/env bash

set -e
gradle build
dockerTag=3dvize:latest
docker build -t $dockerTag .
docker run -it -v /home/alop0715/diplom/:/app/data/ -p 4567:4567 $dockerTag

#docker run -it -v /home/alop0715/diplom/:/app/data/ -p 4567:4567 3dvize:latest


#java -cp libraries/ \
#-Xms2596M -Xmx2596M \
#-Djava.library.path=/home/alop0715/tools/opencv-3.4.0/build/lib \
#-jar build/libs/3dviz-1.0-SNAPSHOT.jar