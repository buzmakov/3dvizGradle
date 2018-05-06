package com.rbtm.reconstruction.DataProcessing;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.DataObjects.Circle;
import com.rbtm.reconstruction.DataObjects.DataShape;
import com.rbtm.reconstruction.Utils.ArrayUtils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import  java.lang.Math;


public class CircleDetector {
    private Mat sourceImg;
    private DataShape shape;
    private Point center;
    private int radius;
    private List<Circle> circleArray;
    private List<Float> diagram;

    public CircleDetector(Mat sourceImg, boolean isPreProcessed) {
        if(isPreProcessed){
            this.sourceImg = sourceImg;
        } else {
            //TODO: need to process source image
            this.sourceImg = sourceImg;
        }
        this.shape = new DataShape(1, sourceImg.rows(), sourceImg.cols());
        this.center = new Point(shape.getWidth()/2, shape.getHeight()/2);
        this.radius = ArrayUtils.getMin(new int[]{shape.getWidth() - (int)center.x, shape.getHeight() - (int)center.y});
    }

    private Mat generateCircleImg(int radius) {
        Mat circleImg = new Mat(
                this.shape.getHeight(),
                this.shape.getWidth(),
                Constants.DEFAULT_MAT_TYPE,
                Scalar.all(0));
        Imgproc.circle(circleImg,center,radius,Scalar.all(0), 2);
        return circleImg;
    }

    private float getRelativeSum(int radius){
        Mat workImg = generateCircleImg(radius);
        Core.bitwise_and(sourceImg, workImg, workImg);
        float[] pixelArray = new float[Math.toIntExact(workImg.total())];
        workImg.get(0,0, pixelArray);

        float sum = ArrayUtils.getSum(pixelArray)/255;
        float circleLength = (float) (2*Math.PI*radius);

        return sum/circleLength;
    }

    private void buildDiagram(){
        diagram = new ArrayList<>();
        for(int i = radius/10; i<radius; ++i) {
            diagram.add(getRelativeSum(i));
        }
    }

    private static boolean isCircle(int radius, List<Float> diagram) {
        //TODO
        return true;
    }

    public List<Float> getDiagram() {
        if(diagram == null) { buildDiagram();}
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
