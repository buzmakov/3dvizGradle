package com.rbtm.reconstruction.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.Point;

@Setter
@Getter
@AllArgsConstructor
public class Circle {
    private Point center;
    private int radius;
}
