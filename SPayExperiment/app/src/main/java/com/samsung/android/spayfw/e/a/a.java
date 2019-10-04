/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.e.a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.samsung.android.spayfw.c.a.c;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.interfacelibrary.db.b;
import java.util.HashMap;

public class a {
    private static HashMap<DBName, a> Di = new HashMap();
    private static Context mContext;
    private b Dj = null;

    private a(Context context, String string, int n2, DBName dBName) {
        if (com.samsung.android.spayfw.e.b.a.fT()) {
            Log.d((String)"DBHelperWrapper", (String)"Sem Device");
            this.Dj = com.samsung.android.spayfw.d.a.c.a(context, string, n2, dBName);
            return;
        }
        Log.d((String)"DBHelperWrapper", (String)"not Sem Device");
        this.Dj = c.a(context, string, n2, dBName);
    }

    public static a b(Context context, String string, int n2, DBName dBName) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            a a2;
            Log.d((String)"DBHelperWrapper", (String)"getInstance()");
            if (Di.containsKey((Object)dBName) && Di.get((Object)dBName) != null) {
                Log.d((String)"DBHelperWrapper", (String)"Return existing instance");
                a2 = (a)Di.get((Object)dBName);
                return a2;
            }
            Log.d((String)"DBHelperWrapper", (String)"Create new instance");
            mContext = context.getApplicationContext();
            a2 = new a(mContext, string, n2, dBName);
            Di.put((Object)dBName, (Object)a2);
        }
    }

    public void b(com.samsung.android.spayfw.interfacelibrary.db.a a2) {
        this.Dj.a(a2);
    }

    public SQLiteDatabase getReadableDatabase(byte[] arrby) {
        return this.Dj.getReadableDatabase(arrby);
    }

    public SQLiteDatabase getWritableDatabase(byte[] arrby) {
        return this.Dj.getWritableDatabase(arrby);
    }
}

