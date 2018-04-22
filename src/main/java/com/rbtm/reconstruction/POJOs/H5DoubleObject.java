package com.rbtm.reconstruction.POJOs;

import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;

import ch.systemsx.cisd.base.mdarray.MDDoubleArray;
import ch.systemsx.cisd.hdf5.*;


public class H5DoubleObject extends H5Object {
    private IHDF5DoubleReader reader;

    public H5DoubleObject(String h5FilePath, String ObjectName) throws DimensionMismatchException {
        super(h5FilePath, ObjectName);
        this.reader = abstractReader.float64();
    }

    private MDDoubleArray getMdArray(int i, int[] blockDimensions) {
        long[] offsetArr = {i, 0, 0};
        return reader.readMDArrayBlockWithOffset(this.objectName, blockDimensions, offsetArr);
    }

    public double[] getSliceArray(int i, int[] blockDimensions){
        MDDoubleArray mdArr = getMdArray(i, blockDimensions);
        return mdArr.getAsFlatArray();
    }
}
