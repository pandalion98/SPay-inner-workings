/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.provider.MediaStore
 *  android.provider.MediaStore$Images
 *  android.provider.MediaStore$Images$Media
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import com.samsung.sensorframework.sda.d.a.a;

public class j
extends a {
    private static j JK;
    private static final String[] Jz;

    static {
        Jz = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
    }

    private j(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static j aU(Context context) {
        if (JK == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (JK == null) {
                    JK = new j(context);
                }
            }
        }
        return JK;
    }

    @Override
    public void gY() {
        super.gY();
        JK = null;
    }

    @Override
    public int getSensorType() {
        return 5019;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected String he() {
        return "ImageContentReaderSensor";
    }

    @Override
    protected String[] hj() {
        String[] arrstring = new String[]{MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString(), MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString()};
        return arrstring;
    }

    @Override
    protected String[] hk() {
        if (Build.VERSION.SDK_INT >= 16) {
            return new String[]{"_display_name", "datetaken", "_size", "height", "width", "orientation"};
        }
        return new String[]{"_display_name", "datetaken", "_size", "orientation"};
    }
}

