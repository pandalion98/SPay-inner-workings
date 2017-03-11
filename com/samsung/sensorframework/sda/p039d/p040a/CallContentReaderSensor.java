package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;
import android.provider.CallLog.Calls;

/* renamed from: com.samsung.sensorframework.sda.d.a.f */
public class CallContentReaderSensor extends AbstractContentReaderSensor {
    private static CallContentReaderSensor JD;
    private static final String[] Jz;

    static {
        Jz = new String[]{"android.permission.READ_CONTACTS", "android.permission.READ_CALL_LOG"};
    }

    public static CallContentReaderSensor aR(Context context) {
        if (JD == null) {
            synchronized (lock) {
                if (JD == null) {
                    JD = new CallContentReaderSensor(context);
                }
            }
        }
        return JD;
    }

    private CallContentReaderSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        JD = null;
    }

    protected String[] hb() {
        return Jz;
    }

    public int getSensorType() {
        return 5014;
    }

    protected String[] hj() {
        return new String[]{Calls.CONTENT_URI.toString()};
    }

    protected String[] hk() {
        return new String[]{"number", "type", "date", "duration"};
    }

    protected String he() {
        return "CallContentReaderSensor";
    }
}
