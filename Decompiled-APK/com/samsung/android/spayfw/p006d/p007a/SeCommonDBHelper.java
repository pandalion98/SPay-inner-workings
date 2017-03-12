package com.samsung.android.spayfw.p006d.p007a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/* renamed from: com.samsung.android.spayfw.d.a.b */
public class SeCommonDBHelper extends SeBaseDBHelper {
    private static SeCommonDBHelper BB;
    private static Context mContext;
    private SQLiteDatabase Bq;

    static {
        BB = null;
    }

    public static synchronized SeCommonDBHelper m681c(Context context, String str, int i) {
        SeCommonDBHelper seCommonDBHelper;
        synchronized (SeCommonDBHelper.class) {
            Log.i("SeDbOpenHelper", "getInstance");
            if (BB == null) {
                mContext = context.getApplicationContext();
                BB = new SeCommonDBHelper(mContext, str, i);
            }
            seCommonDBHelper = BB;
        }
        return seCommonDBHelper;
    }

    private SeCommonDBHelper(Context context, String str, int i) {
        super(context, str, null, i);
        Log.i("SeDbOpenHelper", "super: ");
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
        Log.i("SeDbOpenHelper", "Database is created");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.i("SeDbOpenHelper", "upgrade database from version " + i + " to version " + i2);
        if (i == i2) {
            Log.i("SeDbOpenHelper", "oldVersion == newVersion, no need to upgrade!");
        } else if (i > i2) {
            Log.e("SeDbOpenHelper", "database cannot be downgraded, please update to the latest version");
            onDowngrade(sQLiteDatabase, i, i2);
        } else {
            if (i == 0 || i == 1) {
                i++;
            }
            if (i == i2) {
                Log.i("SeDbOpenHelper", "database is upgraded");
            }
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
