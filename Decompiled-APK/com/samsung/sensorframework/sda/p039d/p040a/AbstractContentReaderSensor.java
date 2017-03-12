package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p033b.SensorData;
import com.samsung.sensorframework.sda.p036c.p037a.ContentReaderProcessor;
import com.samsung.sensorframework.sda.p039d.SensorUtils;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.d.a.a */
public abstract class AbstractContentReaderSensor extends AbstractPullSensor {
    protected static final Object lock;
    protected ArrayList<HashMap<String, String>> If;

    /* renamed from: com.samsung.sensorframework.sda.d.a.a.1 */
    class AbstractContentReaderSensor extends Thread {
        final /* synthetic */ AbstractContentReaderSensor Jm;

        AbstractContentReaderSensor(AbstractContentReaderSensor abstractContentReaderSensor) {
            this.Jm = abstractContentReaderSensor;
        }

        public void run() {
            this.Jm.If = new ArrayList();
            try {
                for (String parse : this.Jm.hj()) {
                    Uri parse2 = Uri.parse(parse);
                    String[] hk = this.Jm.hk();
                    Log.m285d(this.Jm.he(), String.format("Sensing content reader: %s", new Object[]{SensorUtils.ap(this.Jm.getSensorType())}));
                    Cursor query = this.Jm.HR.getContentResolver().query(parse2, hk, null, null, null);
                    if (query != null) {
                        query.moveToFirst();
                        Log.m285d(this.Jm.he(), "Total entries in the cursor: " + query.getCount());
                        while (!query.isAfterLast()) {
                            HashMap hashMap = new HashMap();
                            for (String str : hk) {
                                hashMap.put(str, query.getString(query.getColumnIndex(str)));
                            }
                            this.Jm.If.add(hashMap);
                            query.moveToNext();
                        }
                        query.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.Jm.ho();
            }
        }
    }

    protected abstract String[] hj();

    protected abstract String[] hk();

    static {
        lock = new Object();
    }

    public AbstractContentReaderSensor(Context context) {
        super(context);
    }

    protected boolean hc() {
        new AbstractContentReaderSensor(this).start();
        return true;
    }

    protected void hd() {
    }

    protected SensorData hl() {
        return ((ContentReaderProcessor) super.hi()).m1535a(this.Jn, getSensorType(), this.If, this.Id);
    }

    protected void hm() {
    }
}
