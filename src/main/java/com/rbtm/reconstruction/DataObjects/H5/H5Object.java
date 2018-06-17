package com.rbtm.reconstruction.DataObjects.H5;

import ch.systemsx.cisd.hdf5.*;
import com.rbtm.reconstruction.DataObjects.DataShape;
import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;
import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;
import lombok.Data;
import lombok.Getter;
import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

import static java.lang.Math.toIntExact;

abstract class H5Object implements IMatDatasetObject {
    @Getter
    DataShape shape;
    @Getter HDF5DataClass type;
    @Getter String objectName;
    @Getter int numOfBlocks;
    @Getter int blockSize;
    @Getter int currentBlock;
    IHDF5Reader abstractReader;
    int[] blockDimensions;
    int step;


    private HDF5DataClass getType(HDF5DataSetInformation metaInfo){
        return metaInfo.getTypeInformation().getDataClass();
    }

    private DataShape getDimension(HDF5DataSetInformation metaInfo, int step) throws DimensionMismatchException {

        int dataRank = metaInfo.getRank();
        if (dataRank != 3){
            throw new DimensionMismatchException("data is not 3d matrix");
        }
        long [] demension = metaInfo.getDimensions();
        int num = toIntExact(demension[0]/step);
        int height = toIntExact(demension[1]/step);
        int width = toIntExact(demension[2]/step);
        return new DataShape(num, height, width);
    }

    private HDF5DataSetInformation getMetaInformation(IHDF5Reader reader, String ObjectName) {
        IHDF5ObjectReadOnlyInfoProviderHandler infoReader = reader.object();
        return infoReader.getDataSetInformation(ObjectName);
    }

    H5Object(String h5FilePath, String ObjectName, int numOfBlocks, int resizeStep) throws DimensionMismatchException {
        IHDF5Reader abstractReader = HDF5Factory.openForReading(new File(h5FilePath));
        this.step = resizeStep;
        this.objectName = ObjectName;
        HDF5DataSetInformation metaInfo = getMetaInformation(abstractReader, ObjectName);
        this.type = getType(metaInfo);
        this.shape = getDimension(metaInfo, step);
        this.abstractReader = abstractReader;

        this.numOfBlocks = numOfBlocks;
        this.blockSize = shape.getNum()/numOfBlocks;
        this.blockDimensions = new int[]{blockSize*step, shape.getHeight()*step, shape.getWidth()*step};
        this.currentBlock=0;
    }

    H5Object(String h5FilePath, String ObjectName, int numOfBlocks) throws DimensionMismatchException {
        this(h5FilePath, ObjectName, numOfBlocks, 1);
    }

    public boolean hasNextBlock(){
        return currentBlock < numOfBlocks;
    }

    public int BlockPassed() {
        return currentBlock -1;
    }

    public abstract List<Mat> getNextBlockMat();
}
