package com.samsung.sensorframework.sda.p039d.p040a;

import android.content.Context;

/* renamed from: com.samsung.sensorframework.sda.d.a.n */
public class SMSContentReaderSensor extends AbstractContentReaderSensor {
    private static final String[] Jz;
    private static SMSContentReaderSensor Kc;

    static {
        Jz = new String[]{"android.permission.READ_SMS"};
    }

    public static SMSContentReaderSensor aX(Context context) {
        if (Kc == null) {
            synchronized (lock) {
                if (Kc == null) {
                    Kc = new SMSContentReaderSensor(context);
                }
            }
        }
        return Kc;
    }

    private SMSContentReaderSensor(Context context) {
        super(context);
    }

    public void gY() {
        super.gY();
        Kc = null;
    }

    public int getSensorType() {
        return 5013;
    }

    protected String[] hj() {
        return new String[]{"content://sms"};
    }

    protected String[] hk() {
        return new String[]{"address", "type", "date", "body"};
    }

    protected String[] hb() {
        return Jz;
    }

    protected String he() {
        return "SMSContentReaderSensor";
    }
}
