package com.rbtm.reconstruction.POJOs;

import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;


import ch.systemsx.cisd.base.mdarray.MDFloatArray;
import ch.systemsx.cisd.hdf5.*;


public class H5FloatObject extends H5Object{
    private IHDF5FloatReader reader;

    public H5FloatObject(String h5FilePath, String ObjectName) throws DimensionMismatchException {
        super(h5FilePath, ObjectName);
        this.reader = abstractReader.float32();
    }

    private MDFloatArray getMdArray(int i, int[] blockDimensions) {
        long[] offsetArr = {i, 0, 0};
        return reader.readMDArrayBlockWithOffset(this.objectName, blockDimensions, offsetArr);
    }

    public float[] getSliceArray(int i, int[] blockDimensions){
        MDFloatArray mdArr = getMdArray(i, blockDimensions);
        return mdArr.getAsFlatArray();
    }
}
