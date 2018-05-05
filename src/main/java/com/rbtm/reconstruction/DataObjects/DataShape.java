package com.rbtm.reconstruction.DataObjects;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DataShape {
    private int num;
    private int height;
    private int width;

    @Override
    public String toString() {
        return String.format("num: %d; h: %d; w: %d", num, height, width);

    }
}
