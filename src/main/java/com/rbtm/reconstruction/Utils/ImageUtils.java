package com.rbtm.reconstruction.Utils;

import org.opencv.core.Mat;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageUtils {
    public static BufferedImage Mat2BufferedImage(Mat m)
    {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        System.out.println(
                "img channels: " + m.channels() + "\n" +
                "img cols: " + m.cols() + "\n" +
                "img rows: " + m.rows()
        );

        int bufferSize = m.channels()*m.cols()*m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}
