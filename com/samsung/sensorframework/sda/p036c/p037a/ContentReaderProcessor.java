package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p036c.CommunicationProcessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: com.samsung.sensorframework.sda.c.a.h */
public abstract class ContentReaderProcessor extends CommunicationProcessor {
    protected abstract AbstractContentReaderEntry m1536b(HashMap<String, String> hashMap);

    protected abstract AbstractContentReaderListData m1537b(long j, SensorConfig sensorConfig);

    public ContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    public AbstractContentReaderListData m1535a(long j, int i, ArrayList<HashMap<String, String>> arrayList, SensorConfig sensorConfig) {
        AbstractContentReaderListData b = m1537b(j, sensorConfig);
        if (this.Je) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                AbstractContentReaderEntry b2 = m1536b((HashMap) it.next());
                if (b2 != null) {
                    b.m1512a(b2);
                }
            }
        }
        return b;
    }
}
