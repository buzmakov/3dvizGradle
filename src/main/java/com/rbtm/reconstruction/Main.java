package com.rbtm.reconstruction;

import com.rbtm.reconstruction.Converters.H5ToImgsConverter;

public class Main {
    public static void main(String[] args) throws Exception {
        /*
        String inputDir = args[0];
        String outputDir = args[1];
        String h5file = args[2];
*/
        long startTime = System.currentTimeMillis();
        /*
        /home/alop0715/diplom/h5_samples \
        /home/alop0715/diplom/tool_result \
        result_hand.hdf5
         */
        H5ToImgsConverter c = new H5ToImgsConverter("/home/alop0715/diplom/h5_samples", "/home/alop0715/diplom/tool_result", "result_hand.hdf5");

        c.convert();

        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime)/1000 + " s");
    }
}
