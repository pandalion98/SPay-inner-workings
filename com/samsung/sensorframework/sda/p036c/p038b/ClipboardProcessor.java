package com.samsung.sensorframework.sda.p036c.p038b;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p035b.ClipboardData;
import com.samsung.sensorframework.sda.p036c.AbstractProcessor;

/* renamed from: com.samsung.sensorframework.sda.c.b.b */
public class ClipboardProcessor extends AbstractProcessor {
    public ClipboardProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public ClipboardData m1562a(long j, SensorConfig sensorConfig, ClipData clipData) {
        int i;
        int i2 = 0;
        ClipboardData clipboardData = new ClipboardData(j, sensorConfig);
        if (clipData != null) {
            i = 0;
            while (i2 < clipData.getItemCount()) {
                Item itemAt = clipData.getItemAt(i2);
                if (itemAt != null) {
                    i += itemAt.getText().length();
                }
                i2++;
            }
        } else {
            i = 0;
        }
        clipboardData.aj(i);
        return clipboardData;
    }
}
