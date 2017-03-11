package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import android.content.Intent;
import android.support.v4.os.EnvironmentCompat;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.MusicPlayerStateData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.b.d */
public class MusicPlayerStateProcessor extends AbstractProcessor {
    public MusicPlayerStateProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public MusicPlayerStateData m1565b(long j, SensorConfig sensorConfig, Intent intent) {
        MusicPlayerStateData musicPlayerStateData = new MusicPlayerStateData(j, sensorConfig);
        String stringExtra = intent.getStringExtra("artist");
        String stringExtra2 = intent.getStringExtra("album");
        String stringExtra3 = intent.getStringExtra("track");
        String str = EnvironmentCompat.MEDIA_UNKNOWN;
        long longExtra = intent.getLongExtra(PushMessage.JSON_KEY_ID, -1);
        long longExtra2 = intent.getLongExtra("position", -1);
        long longExtra3 = intent.getLongExtra("trackLength", -1);
        int intExtra = intent.getIntExtra("listpos", -1);
        int i = 2;
        if (intent.getBooleanExtra("playing", false)) {
            i = 1;
        }
        musicPlayerStateData.setState(i);
        musicPlayerStateData.bU(stringExtra);
        musicPlayerStateData.bV(stringExtra2);
        musicPlayerStateData.bW(stringExtra3);
        musicPlayerStateData.bX(str);
        musicPlayerStateData.setId(longExtra);
        musicPlayerStateData.m1527D(longExtra2);
        musicPlayerStateData.m1528E(longExtra3);
        musicPlayerStateData.ak(intExtra);
        return musicPlayerStateData;
    }
}
