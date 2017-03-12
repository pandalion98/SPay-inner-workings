package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.MicrophoneData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.a.b */
public class AudioProcessor extends AbstractProcessor {
    public AudioProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public MicrophoneData m1533a(long j, double[] dArr, long[] jArr, String str, SensorConfig sensorConfig) {
        MicrophoneData microphoneData = new MicrophoneData(j, sensorConfig);
        if (this.Je) {
            microphoneData.m1522a(dArr);
            microphoneData.m1523a(jArr);
            microphoneData.bS(str);
        }
        return microphoneData;
    }
}
