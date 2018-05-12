package com.rbtm.reconstruction.DataObjects.NDArray;

import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;
import lombok.Getter;

public class NDAarrayFloat {
    private float [] flatArr;
    private int[] strides;
    @Getter private int[] dims;

    NDAarrayFloat(float[] arr, int[] dims) throws DimensionMismatchException {
        flatArr = arr;
        this.dims = dims;

        int dimsSize = dims[0];

        strides = new int[dims.length];
        strides[0] = 1;
        for(int i=1;i<this.dims.length;++i) {
            this.strides[i] = this.strides[i - 1] * this.dims[i - 1];
            dimsSize*=dims[i];
        }

        if (arr.length != dimsSize) {
            throw new DimensionMismatchException("arr length not equals dimensions");
        }
    }

    public float get(int inx){
        return flatArr[inx];
    }

    public float get(int[] coords) throws DimensionMismatchException {
        if(coords.length != dims.length) {
            throw new DimensionMismatchException("Coords and dims are not equal");
        }

        int idx = coords[0];
        for(int i=1;i<coords.length;++i)
            idx += coords[i] * strides[i];
        return idx;
    }

    public int length() {
        return flatArr.length;
    }
}
