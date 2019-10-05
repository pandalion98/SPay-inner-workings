/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteOpenHelper
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.fraud.a.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spaytzsvc.api.TAController;

public class b {
    private static b of;
    private static a og;
    private static SQLiteDatabase oh;
    private static SQLiteDatabase oj;

    private b(Context context) {
        og = a.B(context);
        oh = og.getReadableDatabase();
        oj = og.getWritableDatabase();
        if (oh == null || oj == null) {
            throw new Exception("db is null");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static b A(Context context) {
        try {
            if (of == null) {
                of = new b(context);
            }
            do {
                return of;
                break;
            } while (true);
        }
        catch (Exception exception) {
            Log.e("FraudEfsAdapter", "cannot get third db");
            of = null;
            return of;
        }
    }

    public long a(com.samsung.android.spayfw.fraud.a.a a2) {
        if (a2 == null) {
            return -1L;
        }
        ContentValues contentValues = a2.bC();
        Log.d("FraudEfsAdapter", contentValues.toString());
        try {
            long l2 = oj.replaceOrThrow(a2.bB(), null, contentValues);
            return l2;
        }
        catch (Exception exception) {
            Log.c("FraudEfsAdapter", exception.getMessage(), exception);
            return -1L;
        }
    }

    public int b(String string, String string2, String[] arrstring) {
        if (string == null) {
            return -1;
        }
        try {
            int n2 = oj.delete(string, string2, arrstring);
            return n2;
        }
        catch (Exception exception) {
            Log.c("FraudEfsAdapter", exception.getMessage(), exception);
            return -1;
        }
    }

    public Cursor rawQuery(String string, String[] arrstring) {
        try {
            Cursor cursor = oj.rawQuery(string, arrstring);
            return cursor;
        }
        catch (Exception exception) {
            Log.c("FraudEfsAdapter", exception.getMessage(), exception);
            return null;
        }
    }

    static class a
    extends SQLiteOpenHelper {
        private static final String ok = TAController.getEfsDirectory() + "/statistics.db";
        private static a ol;

        private a(Context context) {
            super(context, ok, null, 1);
        }

        public static a B(Context context) {
            if (ol == null) {
                ol = new a(context.getApplicationContext());
            }
            return ol;
        }

        public void onConfigure(SQLiteDatabase sQLiteDatabase) {
            if (!sQLiteDatabase.isReadOnly()) {
                // empty if block
            }
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            try {
                if (sQLiteDatabase.isOpen()) {
                    sQLiteDatabase.execSQL("CREATE TABLE fdevice_info (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,time integer NOT NULL,reason varchar(255) NOT NULL,external_id varchar(255),extras varchar(255),overflow integer NOT NULL DEFAULT 0)");
                    sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  fcount (id integer NOT NULL  PRIMARY KEY,username_count integer NOT NULL DEFAULT 0,token_count integer NOT NULL DEFAULT 0,zip_count integer NOT NULL DEFAULT 0,brand_count integer NOT NULL DEFAULT 0,fraud_count integer NOT NULL DEFAULT 0)");
                    Log.i("FraudEfsHelper", "third database is created");
                }
                return;
            }
            catch (Exception exception) {
                Log.e("FraudEfsHelper", "Database is not fully created, any schema error?");
                Log.c("FraudEfsHelper", exception.getMessage(), exception);
                return;
            }
        }

        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        }

        /*
         * Enabled aggressive block sorting
         */
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
            Log.e("FraudEfsHelper", "upgrade database from version " + n2 + " to version " + n3);
            if (n2 > n3) {
                Log.e("FraudEfsHelper", "database cannot be downgraded, please update to the latest version");
                return;
            } else {
                if (n2 == 0 || n2 == 1) {
                    ++n2;
                }
                if (n2 != n3) return;
                {
                    Log.i("FraudEfsHelper", "database is upgraded");
                    return;
                }
            }
        }
    }

}

