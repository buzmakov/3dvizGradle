package com.rbtm.reconstruction.Converters;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.DataObjects.FilterEntity;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import com.rbtm.reconstruction.DataProcessing.ImgProcessor;
import com.rbtm.reconstruction.MarchingCubes.MarchingCubes;
import com.rbtm.reconstruction.Utils.Timer;
import org.opencv.core.Mat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DatasetToMarshConverter {
    private IMatDatasetObject sourceDataset;
    private File outputFile;
    List<FilterEntity> filters;
    Timer timer;
    File dumpFile;

    public DatasetToMarshConverter(IMatDatasetObject sourceDataset, String outputFilePath, List<FilterEntity> filters) throws IOException {
        this.sourceDataset = sourceDataset;
        this.filters = filters;
        this.outputFile = new File(outputFilePath);
        this.timer = new Timer();
        this.dumpFile = new File(Constants.TEMP_DIR_PATH, "arrayDump.obj");

        outputFile.delete();
        dumpFile.delete();
        dumpFile.createNewFile();
    }


    private void outputToFile(List<float[]> results) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(dumpFile));

            int idx = 0;
            for(int i = 0; i < results.size(); i++) {
                stream.write(("v " + results.get(i)[0] + " " + results.get(i)[1] + " " + results.get(i)[2] + "\n").getBytes());
                if ((idx+1)% 3 == 0) {
                    stream.write(("f " + (idx - 1 ) + " " + (idx) + " " + (idx + 1 ) + "\n").getBytes());
                }
                idx ++;
            }

            stream.flush();
            stream.close();
        }
        catch (Exception e) {
            System.out.println("Something went wrong while writing to the output file");
            e.printStackTrace();
            return;
        }
    }


    public File convert(final float voxSize[], final float isoValue) throws InterruptedException {
        List<float []> results = new ArrayList<>();


        timer.startStage("Executing marching cubes.");

        ExecutorService executor = Executors.newFixedThreadPool(Constants.THREAD_NUM);
        TreeMap<Integer, List<float []>> mResults = new TreeMap<>();
        for(int i=0; i<sourceDataset.getNumOfBlocks(); i++) {
            int finalI = i;
            executor.submit( () -> {
                List<Mat> block = sourceDataset.getBlockMat(finalI);
                block = ImgProcessor.processBlock(block, filters);
                mResults.put(finalI, MarchingCubes.marchingCubesMat(block, voxSize, isoValue, 1));
            });
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);


        timer.nextStage("PROGRESS: Writing results to output file.");
        for(int i = 0; i< mResults.size(); ++i) {
            results.addAll(mResults.get(i));
        }
        outputToFile(results);
        timer.endStage();
        return dumpFile;
    }
}
