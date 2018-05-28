package com.rbtm.reconstruction.Converters;


import com.rbtm.reconstruction.DataObjects.IMatDatasetObject;

public interface Converter {

    /*
        Check if data was converted before
    */
    public IMatDatasetObject convert() throws Exception;

    /*
        Convert without checking. If found data in cash, delete before
    */
    public IMatDatasetObject forceConvert() throws Exception;
}
