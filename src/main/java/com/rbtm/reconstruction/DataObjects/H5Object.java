package com.rbtm.reconstruction.DataObjects;

import ch.systemsx.cisd.hdf5.*;
import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;
import lombok.Getter;
import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

import static java.lang.Math.toIntExact;

abstract class H5Object implements IMatDatasetObject {
    @Getter  DataShape shape;
    @Getter  HDF5DataClass type;
    @Getter  String objectName;
    @Getter int numOfBlocks;
    @Getter int blockSize;
    @Getter int currentBlock;
    IHDF5Reader abstractReader;
    int[] blockDimensions;


    private HDF5DataClass getType(HDF5DataSetInformation metaInfo){
        return metaInfo.getTypeInformation().getDataClass();
    }

    private DataShape getDimension(HDF5DataSetInformation metaInfo) throws DimensionMismatchException {

        int dataRank = metaInfo.getRank();
        if (dataRank != 3){
            throw new DimensionMismatchException("data is not 3d matrix");
        }
        long [] demension = metaInfo.getDimensions();
        int num = toIntExact(demension[0]);
        int height = toIntExact(demension[1]);
        int width = toIntExact(demension[2]);
        return new DataShape(num, height, width);
    }

    private HDF5DataSetInformation getMetaInformation(IHDF5Reader reader, String ObjectName) {
        IHDF5ObjectReadOnlyInfoProviderHandler infoReader = reader.object();
        return infoReader.getDataSetInformation(ObjectName);
    }

    H5Object(String h5FilePath, String ObjectName, int numOfBlocks) throws DimensionMismatchException {
        IHDF5Reader abstractReader = HDF5Factory.openForReading(new File(h5FilePath));

        this.objectName = ObjectName;
        HDF5DataSetInformation metaInfo = getMetaInformation(abstractReader, ObjectName);
        this.type = getType(metaInfo);
        this.shape = getDimension(metaInfo);
        this.abstractReader = abstractReader;

        this.numOfBlocks = numOfBlocks;
        this.blockSize = shape.getNum()/numOfBlocks;
        this.blockDimensions = new int[]{blockSize, shape.getHeight(), shape.getWidth()};
        this.currentBlock=0;
        }

    public boolean hasNextBlock(){
        return currentBlock < numOfBlocks;
    }

    public int BlockPassed() {
        return currentBlock -1;
    }

    public abstract List<Mat> getNextBlockMat();
}
