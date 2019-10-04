/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import com.samsung.sensorframework.sda.d.a.a;

public class n
extends a {
    private static final String[] Jz = new String[]{"android.permission.READ_SMS"};
    private static n Kc;

    private n(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static n aX(Context context) {
        if (Kc == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Kc == null) {
                    Kc = new n(context);
                }
            }
        }
        return Kc;
    }

    @Override
    public void gY() {
        super.gY();
        Kc = null;
    }

    @Override
    public int getSensorType() {
        return 5013;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected String he() {
        return "SMSContentReaderSensor";
    }

    @Override
    protected String[] hj() {
        return new String[]{"content://sms"};
    }

    @Override
    protected String[] hk() {
        return new String[]{"address", "type", "date", "body"};
    }
}

