package com.rbtm.reconstruction.MarchingCubes;

import java.util.ArrayList;


public abstract class CallbackMC implements Runnable {
    private ArrayList<float []> vertices;

    void setVertices(ArrayList<float []> vertices) {
        this.vertices = vertices;
    }

    protected ArrayList<float []> getVertices() {
        return this.vertices;
    }
}