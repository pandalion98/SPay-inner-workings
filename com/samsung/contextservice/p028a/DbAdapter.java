package com.samsung.contextservice.p028a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.contextservice.p029b.CSlog;

/* renamed from: com.samsung.contextservice.a.a */
public class DbAdapter {
    private static DbAdapter GA;
    DbOpenHelper Gz;
    private SQLiteDatabase db;

    public static DbAdapter au(Context context) {
        if (context == null) {
            CSlog.m1409e("DbAdapter", "context is null");
            return null;
        }
        if (GA == null) {
            GA = new DbAdapter(context.getApplicationContext());
        }
        return GA;
    }

    private DbAdapter(Context context) {
        this.Gz = DbOpenHelper.av(context);
        this.db = this.Gz.getWritableDatabase();
    }

    public static final void m1387a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public int update(String str, ContentValues contentValues, String str2, String[] strArr) {
        int update;
        synchronized (DbAdapter.class) {
            update = this.db.update(str, contentValues, str2, strArr);
        }
        return update;
    }

    public long m1388c(String str, ContentValues contentValues) {
        long replace;
        synchronized (DbAdapter.class) {
            replace = this.db.replace(str, null, contentValues);
        }
        return replace;
    }

    public Cursor rawQuery(String str, String[] strArr) {
        Cursor rawQuery;
        synchronized (DbAdapter.class) {
            rawQuery = this.db.rawQuery(str, strArr);
        }
        return rawQuery;
    }

    public void execSQL(String str) {
        synchronized (DbAdapter.class) {
            this.db.execSQL(str);
        }
    }
}
