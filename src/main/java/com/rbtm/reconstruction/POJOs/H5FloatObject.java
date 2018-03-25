package com.rbtm.reconstruction.POJOs;

import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;

import java.io.File;
import ch.systemsx.cisd.base.mdarray.MDFloatArray;
import ch.systemsx.cisd.hdf5.*;
import static java.lang.Math.toIntExact;

import lombok.Getter;

public class H5FloatObject {
    @Getter private DataShape shape;
    @Getter private HDF5DataClass type;
    @Getter private IHDF5FloatReader reader;
    @Getter private String objectName;
    private HDF5DataSetInformation metaInfo;

    private MDFloatArray getMdArray(int i, int[] blockDimensions) {
        long[] offsetArr = {i, 0, 0};
        return reader.readMDArrayBlockWithOffset(this.objectName, blockDimensions, offsetArr);
    }

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


    public H5FloatObject(String h5FilePath, String ObjectName) throws DimensionMismatchException {
        IHDF5Reader reader = HDF5Factory.openForReading(new File(h5FilePath));

        this.objectName = ObjectName;
        this.metaInfo = getMetaInformation(reader, ObjectName);
        this.type = getType(metaInfo);
        this.shape = getDimension(metaInfo);
        this.reader = reader.float32();
    }


    public float[][] getSliceMatrix(int i, int[] blockDimensions){
        MDFloatArray mdArr = getMdArray(i, blockDimensions);
        return mdArr.toMatrix();
    }

    public float[] getSliceArray(int i, int[] blockDimensions){
        MDFloatArray mdArr = getMdArray(i, blockDimensions);
        return mdArr.getAsFlatArray();
    }
}
