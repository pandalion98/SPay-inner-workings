/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.SQLException
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.c.a;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.samsung.android.spayfw.c.a.a;

public class g
extends a {
    private static SQLiteDatabase Bw;
    private static g Bx;
    private static final String TAG;

    static {
        TAG = g.class.getSimpleName();
    }

    private g(Context context) {
        super(context, "PlccCardData_enc.db", null, 1);
    }

    public static g V(Context context) {
        Class<g> class_ = g.class;
        synchronized (g.class) {
            if (Bx == null) {
                Bx = new g(context);
            }
            g g2 = Bx;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return g2;
        }
    }

    public void close() {
        g g2 = this;
        synchronized (g2) {
            super.close();
            if (Bw != null && Bw.isOpen()) {
                Bw.close();
            }
            return;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE entry (_id INTEGER PRIMARY KEY AUTOINCREMENT, providerKey VARCHAR UNIQUE,tokenStatus VARCHAR,merchantID VARCHAR,defaultSequenceConfig VARCHAR,trTokenId VARCHAR,timeStamp VARCHAR,tzData VARCHAR)");
            return;
        }
        catch (SQLException sQLException) {
            Log.e((String)TAG, (String)("onCreate: SqlException: " + sQLException.getMessage()));
            Log.e((String)TAG, (String)sQLException.getMessage(), (Throwable)sQLException);
            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        Log.e((String)TAG, (String)("upgrade database from version " + n2 + " to version " + n3));
        if (n2 > n3) {
            Log.e((String)TAG, (String)"database cannot be downgraded, please update to the latest version");
        }
    }
}

