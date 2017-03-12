package com.samsung.android.sensorhub;

public class SensorHub {
    public static final int TYPE_ALL = -1;
    public static final int TYPE_CONTEXT = 1;
    public static final int TYPE_GESTURE = 2;
    private int mHandle;
    private int mMinDelay;
    private String mName;
    private int mType;
    private String mVendor;

    SensorHub() {
    }

    public String getName() {
        return this.mName;
    }

    public String getVendor() {
        return this.mVendor;
    }

    public int getType() {
        return this.mType;
    }

    public int getMinDelay() {
        return this.mMinDelay;
    }

    int getHandle() {
        return this.mHandle;
    }
}
