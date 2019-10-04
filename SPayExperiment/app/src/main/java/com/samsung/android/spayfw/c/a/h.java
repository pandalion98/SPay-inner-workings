/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  java.lang.String
 */
package com.samsung.android.spayfw.c.a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.c.a.a;

public class h
extends a {
    private static h By;
    private static boolean Bz;
    private static Context mContext;

    static {
        Bz = false;
    }

    private h(Context context, String string, int n2) {
        super(context, string, null, n2);
    }

    public static h b(Context context, String string, int n2) {
        if (By == null) {
            mContext = context.getApplicationContext();
            By = new h(mContext, string, n2);
        }
        return By;
    }
}

