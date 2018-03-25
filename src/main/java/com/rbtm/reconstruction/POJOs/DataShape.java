package com.rbtm.reconstruction.POJOs;


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
}
