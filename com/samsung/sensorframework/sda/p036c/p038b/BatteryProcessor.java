package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.BatteryData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.b.a */
public class BatteryProcessor extends AbstractProcessor {
    public BatteryProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public BatteryData m1561a(long j, SensorConfig sensorConfig, Intent intent) {
        BatteryData batteryData = new BatteryData(j, sensorConfig);
        if (this.Je) {
            batteryData.bT(intent.getAction());
            batteryData.ad(intent.getIntExtra("level", -1));
            batteryData.af(intent.getIntExtra("scale", -1));
            batteryData.ag(intent.getIntExtra("temperature", -1));
            batteryData.ah(intent.getIntExtra("voltage", -1));
            batteryData.ae(intent.getIntExtra("plugged", -1));
            batteryData.setStatus(intent.getIntExtra(CardMaster.COL_STATUS, -1));
            batteryData.ai(intent.getIntExtra("health", -1));
        }
        return batteryData;
    }
}
