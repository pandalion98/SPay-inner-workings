package com.samsung.sensorframework.sda.p030a;

/* renamed from: com.samsung.sensorframework.sda.a.c */
public class SensorConfig extends AbstractConfig implements Cloneable {
    public /* synthetic */ Object clone() {
        return gS();
    }

    public SensorConfig gS() {
        SensorConfig sensorConfig = new SensorConfig();
        for (String str : this.HV.keySet()) {
            sensorConfig.setParameter(str, this.HV.get(str));
        }
        return sensorConfig;
    }
}
