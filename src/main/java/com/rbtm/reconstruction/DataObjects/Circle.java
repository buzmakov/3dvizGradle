package com.rbtm.reconstruction.DataObjects;

import org.opencv.core.Point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Circle {
    private Point center;
    private int radius;
}
