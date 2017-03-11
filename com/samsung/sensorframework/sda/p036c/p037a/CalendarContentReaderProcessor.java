package com.samsung.sensorframework.sda.p036c.p037a;

import android.content.Context;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderEntry;
import com.samsung.sensorframework.sda.p033b.p034a.AbstractContentReaderListData;
import com.samsung.sensorframework.sda.p033b.p034a.CalendarContentListData;
import com.samsung.sensorframework.sda.p033b.p034a.CalendarContentReaderEntry;
import java.util.HashMap;
import java.util.Iterator;
import org.bouncycastle.i18n.MessageBundle;

/* renamed from: com.samsung.sensorframework.sda.c.a.d */
public class CalendarContentReaderProcessor extends ContentReaderProcessor {
    protected /* synthetic */ AbstractContentReaderListData m1540b(long j, SensorConfig sensorConfig) {
        return m1538a(j, sensorConfig);
    }

    public CalendarContentReaderProcessor(Context context, boolean z, boolean z2) {
        super(context, z, z2);
    }

    protected CalendarContentListData m1538a(long j, SensorConfig sensorConfig) {
        return new CalendarContentListData(j, sensorConfig);
    }

    protected AbstractContentReaderEntry m1539b(HashMap<String, String> hashMap) {
        try {
            AbstractContentReaderEntry calendarContentReaderEntry = new CalendarContentReaderEntry();
            Iterator it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = (String) hashMap.get(str);
                if (str2 == null || str2.length() == 0) {
                    str2 = BuildConfig.FLAVOR;
                }
                if (str.equals(MessageBundle.TITLE_ENTRY) || str.equals("description")) {
                    str2 = ce(str2);
                }
                calendarContentReaderEntry.set(str, str2);
                it.remove();
            }
            return calendarContentReaderEntry;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
