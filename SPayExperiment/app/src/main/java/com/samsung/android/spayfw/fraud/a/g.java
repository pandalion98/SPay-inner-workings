/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.fraud.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.fraud.a.a;
import com.samsung.android.spayfw.fraud.a.d;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.utils.h;

public class g {
    private static com.samsung.android.spayfw.e.a.a ns;
    private static g oa;
    private SQLiteDatabase ob;

    private g(Context context) {
        ns = com.samsung.android.spayfw.e.a.a.b(context, null, 0, DBName.ot);
        if (ns == null) {
            throw new Exception("dbHelper is null");
        }
        this.ob = ns.getWritableDatabase(com.samsung.android.spayfw.utils.c.getDbPassword());
        if (this.ob == null) {
            throw new Exception("db is null");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static g z(Context context) {
        try {
            if (oa == null) {
                oa = new g(context);
            }
            do {
                return oa;
                break;
            } while (true);
        }
        catch (Exception exception) {
            c.e("FraudDbAdapter", "FraudDbAdapter: cannot get db adapter");
            oa = null;
            return oa;
        }
    }

    public int a(String string, ContentValues contentValues, String string2, String[] arrstring) {
        if (string == null || contentValues == null) {
            return -1;
        }
        try {
            int n2 = this.ob.update(string, contentValues, string2, arrstring);
            return n2;
        }
        catch (Exception exception) {
            c.c("FraudDbAdapter", exception.getMessage(), exception);
            return -1;
        }
    }

    public long a(a a2) {
        if (a2 == null) {
            return -1L;
        }
        ContentValues contentValues = a2.bC();
        try {
            long l2 = this.ob.replaceOrThrow(a2.bB(), null, contentValues);
            return l2;
        }
        catch (Exception exception) {
            c.c("FraudDbAdapter", exception.getMessage(), exception);
            return -1L;
        }
    }

    public long a(d.b b2) {
        long l2;
        try {
            l2 = this.ob.insert(b2.bB(), null, b2.bC());
        }
        catch (Exception exception) {
            c.c("FraudDbAdapter", exception.getMessage(), exception);
            return -1L;
        }
        c.d("FraudDbAdapter", "addTransactionDetail: rowId = " + l2);
        return l2;
    }

    public Cursor a(String string, String string2, String[] arrstring) {
        if (string == null) {
            return null;
        }
        try {
            Cursor cursor = this.ob.query(string, null, string2, arrstring, null, null, null);
            return cursor;
        }
        catch (Exception exception) {
            c.c("FraudDbAdapter", exception.getMessage(), exception);
            return null;
        }
    }

    public Cursor rawQuery(String string, String[] arrstring) {
        try {
            Cursor cursor = this.ob.rawQuery(string, arrstring);
            return cursor;
        }
        catch (Exception exception) {
            h.a(new RuntimeException((Throwable)exception));
            return null;
        }
    }
}

