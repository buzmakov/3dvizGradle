package com.rbtm.reconstruction;


public class DataShape {
    private int num;
    private int height;
    private int width;

    public DataShape(int num, int height, int width){
        this.num = num;
        this.height = height;
        this.width = width;
    }


    public int getNum() {
        return num;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
