package com.rbtm.reconstruction.DataProcessing;

import com.rbtm.reconstruction.DataObjects.FilterEntity;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Map;

public class ImgProcessor {

    public static Mat process(Mat img, List<FilterEntity> filters) {
        Mat element;
        for (FilterEntity filter: filters) {
            switch (filter.getName()){
                case "Median":
                    Imgproc.medianBlur(img, img, filter.getValue());
                    break;

                case "Gaussian":
                    Imgproc.GaussianBlur(img, img, new Size(2*filter.getValue() + 1, 2*filter.getValue()+1), 0, 0);
                    break;

                case "Erosion":
                    element = Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT,
                            new Size(2*filter.getValue() + 1, 2*filter.getValue()+1));

                    Imgproc.erode(img, img, element);
                    break;

                case "Dilatation":
                    element = Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT,
                            new Size(2*filter.getValue() + 1, 2*filter.getValue()+1));
                    Imgproc.dilate(img, img, element);
                    break;

                case "Opening":
                    element = Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT,
                            new Size(2*filter.getValue() + 1, 2*filter.getValue()+1));
                    Imgproc.morphologyEx(img, img, Imgproc.MORPH_OPEN, element);
                    break;

                case "Closing":
                    element = Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT,
                            new Size(2*filter.getValue() + 1, 2*filter.getValue()+1));
                    Imgproc.morphologyEx(img, img, Imgproc.MORPH_CLOSE, element);
                    break;

                case "Threshold":
                    Imgproc.threshold(img, img, filter.getValue(), 255, Imgproc.THRESH_BINARY);
                    break;
                default:
                    System.out.println("Filter " + filter.getName() + " not found in server.");
                    break;
            }
        }

        return img;
    }
}
