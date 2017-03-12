package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p033b.p034a.VideoContentListData;
import com.samsung.sensorframework.sda.p033b.p034a.VideoContentReaderEntry;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: com.samsung.sensorframework.sda.c.a.n */
public class VideoContentReaderProcessor extends ContentReaderProcessor {
    protected /* synthetic */ AbstractContentReaderListData m1558b(long j, SensorConfig sensorConfig) {
        return m1559e(j, sensorConfig);
    }

    public VideoContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    protected VideoContentListData m1559e(long j, SensorConfig sensorConfig) {
        return new VideoContentListData(j, sensorConfig);
    }

    protected AbstractContentReaderEntry m1557b(HashMap<String, String> hashMap) {
        try {
            AbstractContentReaderEntry videoContentReaderEntry = new VideoContentReaderEntry();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = (String) hashMap.get(str);
                if (str2 == null || str2.length() == 0) {
                    str2 = BuildConfig.FLAVOR;
                }
                videoContentReaderEntry.set(str, str2);
                it.remove();
            }
            return videoContentReaderEntry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
