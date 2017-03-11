package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import android.content.Intent;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.ScreenData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.b.i */
public class ScreenProcessor extends AbstractProcessor {
    public ScreenProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public ScreenData m1570d(long j, SensorConfig sensorConfig, Intent intent) {
        ScreenData screenData = new ScreenData(j, sensorConfig);
        int i = 2;
        if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            i = 1;
        } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            i = 0;
        }
        screenData.setStatus(i);
        return screenData;
    }
}
