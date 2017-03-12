package com.samsung.android.spayfw.p003c.p004a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/* renamed from: com.samsung.android.spayfw.c.a.b */
public class SdlCommonDBHelper extends SdlBaseDBHelper {
    private static SdlCommonDBHelper Bp;
    private static Context mContext;
    private SQLiteDatabase Bq;

    static {
        Bp = null;
    }

    public static synchronized SdlCommonDBHelper m293a(Context context, String str, int i) {
        SdlCommonDBHelper sdlCommonDBHelper;
        synchronized (SdlCommonDBHelper.class) {
            Log.i("SdlDbOpenHelper", "getInstance");
            if (Bp == null) {
                mContext = context.getApplicationContext();
                Bp = new SdlCommonDBHelper(mContext, str, i);
            }
            sdlCommonDBHelper = Bp;
        }
        return sdlCommonDBHelper;
    }

    private SdlCommonDBHelper(Context context, String str, int i) {
        super(context, str, null, i);
        Log.i("SdlDbOpenHelper", "super: ");
    }

    public synchronized SQLiteDatabase getWritableDatabase(byte[] bArr) {
        if (this.Bq == null) {
            this.Bq = super.getWritableDatabase(bArr);
        }
        return this.Bq;
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isReadOnly()) {
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.i("SdlDbOpenHelper", "Database is created");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.i("SdlDbOpenHelper", "upgrade database from version " + i + " to version " + i2);
        if (i == i2) {
            Log.i("SdlDbOpenHelper", "oldVersion == newVersion, no need to upgrade!");
        } else if (i > i2) {
            Log.e("SdlDbOpenHelper", "database cannot be downgraded, please update to the latest version");
            onDowngrade(sQLiteDatabase, i, i2);
        } else {
            if (i == 0 || i == 1) {
                i++;
            }
            if (i == i2) {
                Log.i("SdlDbOpenHelper", "database is upgraded");
            }
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
