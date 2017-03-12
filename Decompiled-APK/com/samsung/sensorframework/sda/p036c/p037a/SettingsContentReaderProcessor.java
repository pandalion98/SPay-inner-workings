package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p033b.p034a.SettingsContentListData;
import com.samsung.sensorframework.sda.p033b.p034a.SettingsContentReaderEntry;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: com.samsung.sensorframework.sda.c.a.m */
public class SettingsContentReaderProcessor extends ContentReaderProcessor {
    protected /* synthetic */ AbstractContentReaderListData m1555b(long j, SensorConfig sensorConfig) {
        return m1556d(j, sensorConfig);
    }

    public SettingsContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    protected SettingsContentListData m1556d(long j, SensorConfig sensorConfig) {
        return new SettingsContentListData(j, sensorConfig);
    }

    protected AbstractContentReaderEntry m1554b(HashMap<String, String> hashMap) {
        try {
            AbstractContentReaderEntry settingsContentReaderEntry = new SettingsContentReaderEntry();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = (String) hashMap.get(str);
                if (str2 == null || str2.length() == 0) {
                    str2 = BuildConfig.FLAVOR;
                }
                settingsContentReaderEntry.set(str, str2);
                it.remove();
            }
            return settingsContentReaderEntry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
