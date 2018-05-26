package com.rbtm.reconstruction.Converters;


import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;

public interface Converter {
    public IMatDatasetObject convert() throws Exception;
}
