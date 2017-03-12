package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

public abstract class TypeParser extends SensorHubParserBean implements ISensorHubParser {
    public abstract int parse(byte[] bArr, int i);
}
