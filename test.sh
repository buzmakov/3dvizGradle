#!/usr/bin/env bash

set -e
gradle build
#docker build -t $dockerTag .
#docker run -it -v /home/alop0715/tools/opencv-3.4.0/:/app/host-opencv/ -p 3dVize:latest
#docker run -it  -p 4567:4567 $dockerTag


java -cp libraries/ \
-Xms2596M -Xmx2596M \
-Djava.library.path=/home/alop0715/tools/opencv-3.4.0/build/lib \
-jar build/libs/3dviz-1.0-SNAPSHOT.jar