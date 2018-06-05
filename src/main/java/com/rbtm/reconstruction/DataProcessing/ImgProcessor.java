package com.rbtm.reconstruction.DataProcessing;

import com.rbtm.reconstruction.DataObjects.FilterEntity;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
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
                    Imgproc.medianBlur(img, img, filter.getValue()*2 + 1);
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


    public static List<Mat> processBlock(List<Mat> block, List<FilterEntity> filters) {
        for (FilterEntity filter: filters) {
            switch (filter.getName()){
                case "Median":
                    block = ImgprocBlocksWraper.blockMedian(block, filter.getValue()*2 + 1);
                    break;

                case "Gaussian":
                    block = ImgprocBlocksWraper.blockGaussian(block, filter.getValue()*2 + 1);
                    break;

                case "Erosion":
                    block = ImgprocBlocksWraper.blockErosion(block, filter.getValue()*2 + 1);
                    break;

                case "Dilatation":
                    block = ImgprocBlocksWraper.blockDilatation(block, filter.getValue()*2 + 1);
                    break;

                case "Opening":
                    block = ImgprocBlocksWraper.blockOpening(block, filter.getValue()*2 + 1);
                    break;

                case "Closing":
                    block = ImgprocBlocksWraper.blockClosing(block, filter.getValue()*2 + 1);
                    break;

                case "Threshold":
                    block = ImgprocBlocksWraper.blockThreshold(block, filter.getValue());
                    break;
                default:
                    System.out.println("Filter " + filter.getName() + " not found in server.");
                    break;
            }
        }

        return block;
    }

}
