package com.samsung.android.contextaware.aggregator.lpp;

public class ApdrData {
    public double StepCount;
    public long TimeNoMove;
    public double[] acc;
    public int apdrDevicePosition;
    public double apdrForwardingVector;
    public double apdrHeading;
    public double[] att;
    public double bearing;
    public int carryPos;
    public double[] gyro;
    public double[] mag;
    public int movingStatus;
    public double stepFlag;
    public double stepLength;
    public long systemtime;
    public long utctime;

    public ApdrData() {
        this.acc = new double[3];
        this.gyro = new double[3];
        this.mag = new double[4];
        this.att = new double[3];
        this.movingStatus = 2;
        this.bearing = 0.0d;
        this.StepCount = 0.0d;
        this.TimeNoMove = 0;
        this.stepFlag = 0.0d;
        this.stepLength = 0.0d;
        this.systemtime = 0;
        this.utctime = 0;
        for (int inx = 0; inx < 3; inx++) {
            this.acc[inx] = 0.0d;
            this.gyro[inx] = 0.0d;
            this.mag[inx] = 0.0d;
            this.att[inx] = 0.0d;
        }
        this.mag[3] = 0.0d;
        this.apdrForwardingVector = 0.0d;
        this.apdrHeading = 0.0d;
        this.apdrDevicePosition = 1;
    }

    public ApdrData(ApdrData aPDR_data) {
        this.acc = new double[3];
        this.gyro = new double[3];
        this.mag = new double[4];
        this.att = new double[3];
        this.movingStatus = 2;
        set(aPDR_data);
    }

    public void set(ApdrData aPDR_data) {
        this.bearing = aPDR_data.bearing;
        this.StepCount = aPDR_data.StepCount;
        this.TimeNoMove = aPDR_data.TimeNoMove;
        this.stepFlag = aPDR_data.stepFlag;
        this.stepLength = aPDR_data.stepLength;
        this.systemtime = aPDR_data.systemtime;
        this.utctime = aPDR_data.utctime;
        for (int inx = 0; inx < 3; inx++) {
            this.acc[inx] = aPDR_data.acc[inx];
            this.gyro[inx] = aPDR_data.gyro[inx];
            this.mag[inx] = aPDR_data.mag[inx];
            this.att[inx] = aPDR_data.att[inx];
        }
        this.mag[3] = aPDR_data.mag[3];
        this.movingStatus = aPDR_data.movingStatus;
        this.apdrForwardingVector = aPDR_data.apdrForwardingVector;
        this.apdrHeading = aPDR_data.apdrHeading;
        this.apdrDevicePosition = aPDR_data.apdrDevicePosition;
    }
}
