package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p033b.p034a.ImageContentListData;
import com.samsung.sensorframework.sda.p033b.p034a.ImageContentReaderEntry;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: com.samsung.sensorframework.sda.c.a.j */
public class ImageContentReaderProcessor extends ContentReaderProcessor {
    protected /* synthetic */ AbstractContentReaderListData m1549b(long j, SensorConfig sensorConfig) {
        return m1550c(j, sensorConfig);
    }

    public ImageContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    protected ImageContentListData m1550c(long j, SensorConfig sensorConfig) {
        return new ImageContentListData(j, sensorConfig);
    }

    protected AbstractContentReaderEntry m1548b(HashMap<String, String> hashMap) {
        try {
            AbstractContentReaderEntry imageContentReaderEntry = new ImageContentReaderEntry();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = (String) hashMap.get(str);
                if (str2 == null || str2.length() == 0) {
                    str2 = BuildConfig.FLAVOR;
                }
                imageContentReaderEntry.set(str, str2);
                it.remove();
            }
            return imageContentReaderEntry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
