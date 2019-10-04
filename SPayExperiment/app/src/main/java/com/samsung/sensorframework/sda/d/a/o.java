/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.provider.Settings
 *  android.provider.Settings$Global
 *  android.provider.Settings$System
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.samsung.sensorframework.sda.d.a.a;

public class o
extends a {
    private static o Kd;

    private o(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static o aY(Context context) {
        if (Kd == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Kd == null) {
                    Kd = new o(context);
                }
            }
        }
        return Kd;
    }

    @Override
    public void gY() {
        super.gY();
        Kd = null;
    }

    @Override
    public int getSensorType() {
        return 5025;
    }

    @Override
    protected String he() {
        return "SettingsContentReaderSensor";
    }

    @Override
    protected String[] hj() {
        if (Build.VERSION.SDK_INT >= 17) {
            String[] arrstring = new String[]{Settings.System.CONTENT_URI.toString(), Settings.Global.CONTENT_URI.toString()};
            return arrstring;
        }
        String[] arrstring = new String[]{Settings.System.CONTENT_URI.toString()};
        return arrstring;
    }

    @Override
    protected String[] hk() {
        return new String[]{"name", "value"};
    }
}

