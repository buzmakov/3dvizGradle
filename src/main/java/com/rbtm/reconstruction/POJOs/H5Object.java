package com.rbtm.reconstruction.POJOs;

import ch.systemsx.cisd.hdf5.*;
import com.rbtm.reconstruction.Exceptions.DimensionMismatchException;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

import static java.lang.Math.toIntExact;

class H5Object {
    @Getter  DataShape shape;
    @Getter  HDF5DataClass type;
    @Getter  String objectName;
    IHDF5Reader abstractReader;
    private HDF5DataSetInformation metaInfo;


    private HDF5DataClass getType(HDF5DataSetInformation metaInfo){
        return metaInfo.getTypeInformation().getDataClass();
    }

    private DataShape getDimension(HDF5DataSetInformation metaInfo) throws DimensionMismatchException {

        int dataRank = metaInfo.getRank();
        if (dataRank != 3){
            throw new DimensionMismatchException("data is not 3d matrix");
        }
        long [] demension = metaInfo.getDimensions();
        int num = toIntExact(demension[0]);
        int height = toIntExact(demension[1]);
        int width = toIntExact(demension[2]);
        return new DataShape(num, height, width);
    }

    private HDF5DataSetInformation getMetaInformation(IHDF5Reader reader, String ObjectName) {
        IHDF5ObjectReadOnlyInfoProviderHandler infoReader = reader.object();
        return infoReader.getDataSetInformation(ObjectName);
    }

    public H5Object(String h5FilePath, String ObjectName) throws DimensionMismatchException {
        IHDF5Reader abstractReader = HDF5Factory.openForReading(new File(h5FilePath));

        this.objectName = ObjectName;
        this.metaInfo = getMetaInformation(abstractReader, ObjectName);
        this.type = getType(metaInfo);
        this.shape = getDimension(metaInfo);
        this.abstractReader = abstractReader;
        }
}
