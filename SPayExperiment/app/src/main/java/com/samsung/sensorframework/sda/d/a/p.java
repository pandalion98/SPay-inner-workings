/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.Uri
 *  android.provider.MediaStore
 *  android.provider.MediaStore$Video
 *  android.provider.MediaStore$Video$Media
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import com.samsung.sensorframework.sda.d.a.a;

public class p
extends a {
    private static final String[] Jz = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
    private static p Ke;

    private p(Context context) {
        super(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static p aZ(Context context) {
        if (Ke == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (Ke == null) {
                    Ke = new p(context);
                }
            }
        }
        return Ke;
    }

    @Override
    public void gY() {
        super.gY();
        Ke = null;
    }

    @Override
    public int getSensorType() {
        return 5020;
    }

    @Override
    protected String[] hb() {
        return Jz;
    }

    @Override
    protected String he() {
        return "VideoContentReaderSensor";
    }

    @Override
    protected String[] hj() {
        String[] arrstring = new String[]{MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString(), MediaStore.Video.Media.INTERNAL_CONTENT_URI.toString()};
        return arrstring;
    }

    @Override
    protected String[] hk() {
        return new String[]{"_display_name", "datetaken", "_size", "height", "width"};
    }
}

