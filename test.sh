#!/usr/bin/env bash

set -e
gradle build

java -cp libraries/ \
-Xms2596M -Xmx2596M \
-Djava.library.path=/home/alop0715/tools/opencv-3.4.0/build/lib \
-jar build/libs/3dviz-1.0-SNAPSHOT.jar