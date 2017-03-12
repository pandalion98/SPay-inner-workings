package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import java.util.ArrayList;

public class SensorHubSyntax {
    private final double mConversionScale;
    private final DATATYPE mDataType;
    private byte mMessageType;
    private final String mName;
    private final ArrayList<SensorHubSyntax> mRepeatList;
    private byte mSize;

    public enum DATATYPE {
        BOOLEAN,
        BYTE,
        SHORT,
        INTEGER3,
        INTEGER,
        LONG,
        FLOAT2,
        FLOAT3,
        FLOAT4,
        DOUBLE2,
        DOUBLE3,
        DOUBLE4,
        REPEATLIST,
        MESSAGE_TYPE
    }

    public SensorHubSyntax(DATATYPE dType, double scale, String name) {
        this.mSize = (byte) 0;
        this.mMessageType = (byte) -1;
        this.mDataType = dType;
        this.mConversionScale = scale;
        this.mName = name;
        this.mRepeatList = null;
        computeLength();
    }

    public SensorHubSyntax(ArrayList<SensorHubSyntax> list) {
        this.mSize = (byte) 0;
        this.mMessageType = (byte) -1;
        this.mDataType = DATATYPE.REPEATLIST;
        this.mConversionScale = 1.0d;
        this.mName = "";
        this.mRepeatList = list;
        this.mSize = (byte) 0;
    }

    public SensorHubSyntax(byte mType) {
        this.mSize = (byte) 0;
        this.mMessageType = (byte) -1;
        this.mDataType = DATATYPE.MESSAGE_TYPE;
        this.mConversionScale = 1.0d;
        this.mName = "DataType";
        this.mRepeatList = null;
        this.mMessageType = mType;
        this.mSize = (byte) 0;
    }

    byte size() {
        return this.mSize;
    }

    DATATYPE dataType() {
        return this.mDataType;
    }

    double scale() {
        return this.mConversionScale;
    }

    String name() {
        return this.mName;
    }

    ArrayList<SensorHubSyntax> repeatList() {
        return this.mRepeatList;
    }

    byte messageType() {
        return this.mMessageType;
    }

    private void computeLength() {
        if (this.mDataType == DATATYPE.INTEGER || this.mDataType == DATATYPE.FLOAT4 || this.mDataType == DATATYPE.DOUBLE4) {
            this.mSize = (byte) 4;
        } else if (this.mDataType == DATATYPE.BYTE || this.mDataType == DATATYPE.BOOLEAN) {
            this.mSize = (byte) 1;
        } else if (this.mDataType == DATATYPE.LONG) {
            this.mSize = (byte) 8;
        } else if (this.mDataType == DATATYPE.SHORT || this.mDataType == DATATYPE.FLOAT2 || this.mDataType == DATATYPE.DOUBLE2) {
            this.mSize = (byte) 2;
        } else {
            this.mSize = (byte) 3;
        }
    }
}
