package com.rbtm.reconstruction.MarchingCubes;

import java.util.ArrayList;


abstract class CallbackMC implements Runnable {
    private ArrayList<float []> vertices;

    void setVertices(ArrayList<float []> vertices) {
        this.vertices = vertices;
    }

    ArrayList<float []> getVertices() {
        return this.vertices;
    }
}