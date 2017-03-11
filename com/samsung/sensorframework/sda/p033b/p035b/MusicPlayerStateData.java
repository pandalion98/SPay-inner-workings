package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;

/* renamed from: com.samsung.sensorframework.sda.b.b.d */
public class MusicPlayerStateData extends SensorData {
    private static String TAG;
    private int IJ;
    private String IK;
    private String IL;
    private String IM;
    private String IN;
    private long IO;
    private long IP;
    private long IQ;
    private int state;

    static {
        TAG = "MusicPlayerStateData";
    }

    public MusicPlayerStateData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
        this.state = 0;
    }

    public void setState(int i) {
        this.state = i;
        Log.m285d(TAG, String.format("state: %d", new Object[]{Integer.valueOf(this.state)}));
    }

    public void bU(String str) {
        this.IK = str;
    }

    public void bV(String str) {
        this.IL = str;
    }

    public void bW(String str) {
        this.IM = str;
    }

    public void bX(String str) {
        this.IN = str;
    }

    public void setId(long j) {
        this.IO = j;
    }

    public void m1527D(long j) {
        this.IP = j;
    }

    public void m1528E(long j) {
        this.IQ = j;
    }

    public void ak(int i) {
        this.IJ = i;
    }

    public int getSensorType() {
        return 5022;
    }
}
