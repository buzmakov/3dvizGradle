package com.rbtm.reconstruction.DataProcessing.Circle;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.DataObjects.DataShape;

import com.rbtm.reconstruction.Utils.CustomArrayUtils;
import com.rbtm.reconstruction.Utils.Timer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import  java.lang.Math;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class CircleDetector {
    private Mat sourceImg;
    private DataShape shape;
    private Point center;
    private int radius;
    private List<Circle> circleArray;
    private List<CirleDiagramEntity> diagram;
    private float thresh = (float) 0.75;

    private void printInitInfo() {
        System.out.println("center: " + center + ", radius: " + radius + ", datashape: " + shape);
    }


    public CircleDetector(Mat sourceImg) {
        this.sourceImg = sourceImg;
        this.shape = new DataShape(1, sourceImg.rows(), sourceImg.cols());
        this.center = new Point(shape.getWidth()/2, shape.getHeight()/2);
        this.radius = CustomArrayUtils.getMin(new int[]{shape.getWidth() - (int)center.x, shape.getHeight() - (int)center.y});

        printInitInfo();
    }

    private Mat generateCircleImg(int radius) {
        Mat circleImg = new Mat(
                this.shape.getHeight(),
                this.shape.getWidth(),
                16,
                Scalar.all(0));
        Imgproc.circle(circleImg,center,radius,Scalar.all(255), 4);

        return circleImg;
    }

    private float calcSum(Mat img) {
        float[] pixelArray = new float[Math.toIntExact(img.total())];
        //return (float) IntStream.range(0, (int)img.total()).
        //        mapToDouble(i -> img.get(i/img.cols(), i%img.rows())[0]).sum()/255;

        return (float)(Core.sumElems(img).val[0]/255/1.2);

    }

    private float getRelativeSum(int radius){
        Mat workImg = generateCircleImg(radius);
        Core.bitwise_and(sourceImg, workImg, workImg);
        //String buffImgPath = Constants.TEMP_DIR_PATH + "/debugImg_"+radius+"." + Constants.PNG_FORMAT;
        //Imgcodecs.imwrite(buffImgPath, workImg);

        float sum = calcSum(workImg);
        float circleLength = (float) (2*Math.PI*radius)*4;

        return sum/circleLength;
    }

    private void buildDiagram(){
        diagram = new ArrayList<>();
        for(int i = radius/10; i<radius; ++i) {
            diagram.add(new CirleDiagramEntity(i, getRelativeSum(i)));
        }
    }

    private boolean isCircle(int radius, List<CirleDiagramEntity> diagram) {
        return diagram.get(radius).getValue() > this.thresh;
    }

    public List<CirleDiagramEntity> getDiagram() {
        Timer timer = new Timer();
        timer.startStage("Calculate circle diagram");
        if(diagram == null) {
            System.out.println("Building circle diagram not found. Build new");
            buildDiagram();
        }
        timer.endStage();

        return diagram;
    }

    public List<Circle> getCircleArray(){
        if(circleArray != null) {return circleArray;}
        getDiagram();

        circleArray = new ArrayList<>();

        for(int i = 0; i<diagram.size(); ++i){
            if(isCircle(i, diagram)){
                circleArray.add(new Circle(center, i));
            }
        }

        return circleArray;
    }
}
