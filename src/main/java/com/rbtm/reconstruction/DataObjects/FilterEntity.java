package com.rbtm.reconstruction.DataObjects;

import lombok.Getter;
import lombok.Setter;

public class FilterEntity {
    @Getter @Setter private String name;
    @Getter @Setter private int value;

    public String toString(){
        return "{ name: " + name + ", value: " + value + "}";
    }
}
