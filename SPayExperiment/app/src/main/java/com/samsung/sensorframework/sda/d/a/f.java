/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.provider.CallLog
 *  android.provider.CallLog$Calls
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.net.Uri;
import android.provider.CallLog;
import com.samsung.sensorframework.sda.d.a.a;

public class f
extends a {
    private static f JD;
    private static final String[] Jz;

    static {
        Jz = new String[]{"android.permission.READ_CONTACTS", "android.permission.READ_CALL_LOG"};
    }

    private f(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static f aR(Context context) {
        if (JD == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (JD == null) {
                    JD = new f(context);
                }
            }
        }
        return JD;
    }

    @Override
    public void gY() {
        super.gY();
        JD = null;
    }

    @Override
    public int getSensorType() {
        return 5014;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected String he() {
        return "CallContentReaderSensor";
    }

    @Override
    protected String[] hj() {
        String[] arrstring = new String[]{CallLog.Calls.CONTENT_URI.toString()};
        return arrstring;
    }

    @Override
    protected String[] hk() {
        return new String[]{"number", "type", "date", "duration"};
    }
}

