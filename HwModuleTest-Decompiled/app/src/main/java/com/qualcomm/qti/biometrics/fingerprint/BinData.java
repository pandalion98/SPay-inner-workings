package com.qualcomm.qti.biometrics.fingerprint;

public class BinData {
    public int binIndex;
    public int binTemperature;
    public int calibTime;

    public BinData(int calibTime2, int binTemperature2, int binIndex2) {
        this.calibTime = calibTime2;
        this.binTemperature = binTemperature2;
        this.binIndex = binIndex2;
    }

    public int getCalibTime() {
        return this.calibTime;
    }

    public int getBinTemperature() {
        return this.binTemperature;
    }

    public int getBinIndex() {
        return this.binIndex;
    }
}
