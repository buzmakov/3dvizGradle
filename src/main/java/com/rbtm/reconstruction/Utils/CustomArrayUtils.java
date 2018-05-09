package com.rbtm.reconstruction.Utils;

import java.util.Arrays;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;

public class CustomArrayUtils {
    public static float getMax(float[] arr) {
        return (float) IntStream.range(0, arr.length).
                mapToDouble(i -> arr[i]).max().getAsDouble();
    }

    public static float getMin(float[] arr) {
        return (float)IntStream.range(0, arr.length).
                mapToDouble(i -> arr[i]).min().getAsDouble();
    }

    public static float getSum(float[] arr) {
        return (float)IntStream.range(0, arr.length).
                mapToDouble(i -> arr[i]).sum();
    }

    public static int getMax(int[] arr) {
        return  Arrays.stream(arr).max().getAsInt();
    }

    public static int getMin(int[] arr) {
        return  Arrays.stream(arr).min().getAsInt();
    }

    public static long getMax(long[] arr) {
        return  Arrays.stream(arr).max().getAsLong();
    }

    public static long getMin(long[] arr) {
        return  Arrays.stream(arr).min().getAsLong();
    }

    public static float[] resize(float[] arr, int step){
        if (step ==1) { return arr; }

        for (int i=0, is=0; is < arr.length; ++i, is+=step){
            arr[i] = arr[is];
        }
        return ArrayUtils.subarray(arr, 0, arr.length/step);
    }
}
