package com.rbtm.reconstruction.DataObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterEntity {
    private String name;
    private int value;

    public String toString(){
        return "{ name: " + name + ", value: " + value + "}";
    }
}
