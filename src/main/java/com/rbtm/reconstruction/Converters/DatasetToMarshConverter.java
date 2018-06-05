package com.rbtm.reconstruction.Converters;

import com.rbtm.reconstruction.Constants;
import com.rbtm.reconstruction.DataObjects.FilterEntity;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import com.rbtm.reconstruction.DataProcessing.ImgProcessor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.opencv.core.Mat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DatasetToMarshConverter {
    private IMatDatasetObject sourceDataset;
    private String outputFilePath;
    List<FilterEntity> filters;

    private void dump(List<Mat> block, BufferedWriter buffWriter) throws IOException {
        for (Mat m: block) {
            for (int i = 0; i<m.rows(); ++i) {
                for(int j = 0; j<m.cols(); ++i) {
                    buffWriter.write(m.get(i,j)[0]+" ");
                }
            }
        }
    }

    private void filterAndDump(File dumpFile) throws IOException {
        dumpFile.delete();
        List<Mat> block;

        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(dumpFile));
        while(sourceDataset.hasNextBlock()) {
            block = sourceDataset.getNextBlockMat();
            block = ImgProcessor.processBlock(block, filters);
            dump(block, outputWriter);
        }

        outputWriter.flush();
        outputWriter.close();
    }

    public void convert() throws IOException {
        File outPutFile = new File(outputFilePath);
        File arrayDumpFile = new File(Constants.TEMP_DIR_PATH, "arrayDump.txt");
        outPutFile.delete();
        filterAndDump(arrayDumpFile);
    }
}
