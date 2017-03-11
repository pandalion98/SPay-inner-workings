package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.CellData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.c.a.f */
public class CellProcessor extends AbstractProcessor {
    public CellProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public CellData m1543a(long j, ArrayList<HashMap<String, Object>> arrayList, SensorConfig sensorConfig) {
        CellData cellData = new CellData(j, sensorConfig);
        if (this.Je) {
            cellData.m1517j(arrayList);
        }
        return cellData;
    }
}
