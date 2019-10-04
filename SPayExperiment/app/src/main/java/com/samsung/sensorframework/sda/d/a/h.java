/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.provider.ContactsContract
 *  android.provider.ContactsContract$Data
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import com.samsung.sensorframework.sda.d.a.a;

public class h
extends a {
    private static h JG;
    private static final String[] Jz;

    static {
        Jz = new String[]{"android.permission.READ_CONTACTS"};
    }

    private h(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static h aT(Context context) {
        if (JG == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (JG == null) {
                    JG = new h(context);
                }
            }
        }
        return JG;
    }

    @Override
    public void gY() {
        super.gY();
        JG = null;
    }

    @Override
    public int getSensorType() {
        return 5016;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected String he() {
        return "ContactsContentReaderSensor";
    }

    @Override
    protected String[] hj() {
        String[] arrstring = new String[]{ContactsContract.Data.CONTENT_URI.toString()};
        return arrstring;
    }

    @Override
    protected String[] hk() {
        if (Build.VERSION.SDK_INT >= 18) {
            return new String[]{"_id", "display_name", "data1", "data1", "mimetype", "contact_last_updated_timestamp"};
        }
        return new String[]{"_id", "display_name", "data1", "data1", "mimetype"};
    }
}

