package com.rbtm.reconstruction.DataProcessing;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.DataObjects.Circle;
import com.rbtm.reconstruction.DataObjects.DataShape;
import com.rbtm.reconstruction.Utils.ArrayUtils;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class CircleDetector {
    private Mat sourceImg;
    private final Mat background;
    private DataShape shape;
    private Point center;
    private int radius;
    private List<Circle> circleArray;

    public CircleDetector(Mat sourceImg, boolean isPreProcessed) {
        if(isPreProcessed){
            this.sourceImg = sourceImg;
        } else {
            //TODO: need to process source image
            this.sourceImg = sourceImg;
        }
        this.shape = new DataShape(1, sourceImg.rows(), sourceImg.cols());
        this.center = new Point(shape.getWidth()/2, shape.getHeight()/2);
        this.radius = ArrayUtils.getMin(new int[]{shape.getWidth() - center.x, shape.getHeight() - center.y});

        this.background = new Mat(
                this.shape.getHeight(),
                this.shape.getWidth(),
                Constants.DEFAULT_MAT_TYPE,
                Scalar.all(0));

    }

    private generate CircleImg(Point ceneter, )

    private boolean isCircle(Point ceneter, int radius){
    }

    public List<Circle> getCircleArray(){
        if(!circleArray.isEmpty()) {
            return circleArray;
        }

        List<Circle> result = new ArrayList<>();
        for(int i = 0; i<radius; ++i) {
            if(isCircle()) {

            }
        }

    }
}
