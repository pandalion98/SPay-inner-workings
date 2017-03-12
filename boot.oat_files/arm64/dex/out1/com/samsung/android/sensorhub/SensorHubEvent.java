package com.samsung.android.sensorhub;

public class SensorHubEvent {
    public byte[] buffer;
    public int length;
    public SensorHub sensorhub;
    public long timestamp;
    public float[] values = new float[9];

    SensorHubEvent(int size) {
        this.buffer = new byte[size];
    }
}
