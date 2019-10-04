/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextservice.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.contextservice.a.b;

public class a {
    private static a GA;
    b Gz;
    private SQLiteDatabase db;

    private a(Context context) {
        this.Gz = b.av(context);
        this.db = this.Gz.getWritableDatabase();
    }

    public static final void a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public static a au(Context context) {
        if (context == null) {
            com.samsung.contextservice.b.b.e("DbAdapter", "context is null");
            return null;
        }
        if (GA == null) {
            GA = new a(context.getApplicationContext());
        }
        return GA;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long c(String string, ContentValues contentValues) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return this.db.replace(string, null, contentValues);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void execSQL(String string) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            this.db.execSQL(string);
            // ** MonitorExit[var3_2] (shouldn't be in output)
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Cursor rawQuery(String string, String[] arrstring) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return this.db.rawQuery(string, arrstring);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int update(String string, ContentValues contentValues, String string2, String[] arrstring) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return this.db.update(string, contentValues, string2, arrstring);
        }
    }
}

