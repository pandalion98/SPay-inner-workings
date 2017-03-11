package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.PhoneStateData;
import com.samsung.sensorframework.sda.p036c.CommunicationProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.b.f */
public class PhoneStateProcessor extends CommunicationProcessor {
    public PhoneStateProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public PhoneStateData m1567a(long j, SensorConfig sensorConfig, int i, String str, String str2) {
        PhoneStateData phoneStateData = new PhoneStateData(j, sensorConfig);
        if (this.Je) {
            phoneStateData.setEventType(i);
            phoneStateData.setData(str);
            if (str2 != null) {
                phoneStateData.setNumber(cd(str2));
            }
        }
        return phoneStateData;
    }
}
