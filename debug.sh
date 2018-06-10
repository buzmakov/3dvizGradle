#!/usr/bin/env bash
gradle build
java -cp libraries/ \
-Xms2096M -Xmx2096M \
-Djava.library.path=/home/alop0715/tools/opencv-3.4.0/build/lib \
-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 \
-jar build/libs/3dviz-1.0-SNAPSHOT.jar