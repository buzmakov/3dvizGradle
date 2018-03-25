gradle build
java -cp libraries/ \
-Djava.library.path=/home/alop0715/tools/opencv-3.4.0/build/lib \
-jar build/libs/3dviz-1.0-SNAPSHOT.jar \
/home/alop0715/diplom/h5_samples \
/home/alop0715/diplom/tool_result \
result_hand.hdf5