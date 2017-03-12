package com.samsung.sensorframework.sda.p033b.p035b;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.b.b.i */
public class SmsData extends SensorData {
    private int IY;
    private int IZ;
    private String Ja;
    private String Jb;
    private String Jc;
    private final HashMap<String, Integer> Jd;
    private String address;

    public SmsData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
        this.Jd = new HashMap();
    }

    public void addCategory(String str) {
        Integer num = (Integer) this.Jd.get(str);
        if (num == null) {
            num = Integer.valueOf(0);
        }
        this.Jd.put(str, Integer.valueOf(num.intValue() + 1));
    }

    public void al(int i) {
        this.IY = i;
    }

    public void bZ(String str) {
        this.Jb = str;
    }

    public void am(int i) {
        this.IZ = i;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public void ca(String str) {
        this.Jc = str;
    }

    public void cb(String str) {
        this.Ja = str;
    }

    public int getSensorType() {
        return 5009;
    }
}
