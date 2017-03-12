package com.samsung.android.spayfw.fraud.p011a.p012a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.samsung.android.spayfw.fraud.p011a.FBaseRecord;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.TAController;

/* renamed from: com.samsung.android.spayfw.fraud.a.a.b */
public class FraudEfsAdapter {
    private static FraudEfsAdapter of;
    private static FraudEfsAdapter og;
    private static SQLiteDatabase oh;
    private static SQLiteDatabase oj;

    /* renamed from: com.samsung.android.spayfw.fraud.a.a.b.a */
    static class FraudEfsAdapter extends SQLiteOpenHelper {
        private static final String ok;
        private static FraudEfsAdapter ol;

        static {
            ok = TAController.getEfsDirectory() + "/statistics.db";
        }

        public static FraudEfsAdapter m692B(Context context) {
            if (ol == null) {
                ol = new FraudEfsAdapter(context.getApplicationContext());
            }
            return ol;
        }

        private FraudEfsAdapter(Context context) {
            super(context, ok, null, 1);
        }

        public void onConfigure(SQLiteDatabase sQLiteDatabase) {
            if (!sQLiteDatabase.isReadOnly()) {
            }
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            try {
                if (sQLiteDatabase.isOpen()) {
                    sQLiteDatabase.execSQL("CREATE TABLE fdevice_info (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,time integer NOT NULL,reason varchar(255) NOT NULL,external_id varchar(255),extras varchar(255),overflow integer NOT NULL DEFAULT 0)");
                    sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  fcount (id integer NOT NULL  PRIMARY KEY,username_count integer NOT NULL DEFAULT 0,token_count integer NOT NULL DEFAULT 0,zip_count integer NOT NULL DEFAULT 0,brand_count integer NOT NULL DEFAULT 0,fraud_count integer NOT NULL DEFAULT 0)");
                    Log.m287i("FraudEfsHelper", "third database is created");
                }
            } catch (Throwable e) {
                Log.m286e("FraudEfsHelper", "Database is not fully created, any schema error?");
                Log.m284c("FraudEfsHelper", e.getMessage(), e);
            }
        }

        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.m286e("FraudEfsHelper", "upgrade database from version " + i + " to version " + i2);
            if (i > i2) {
                Log.m286e("FraudEfsHelper", "database cannot be downgraded, please update to the latest version");
                return;
            }
            if (i == 0 || i == 1) {
                i++;
            }
            if (i == i2) {
                Log.m287i("FraudEfsHelper", "database is upgraded");
            }
        }
    }

    private FraudEfsAdapter(Context context) {
        og = FraudEfsAdapter.m692B(context);
        oh = og.getReadableDatabase();
        oj = og.getWritableDatabase();
        if (oh == null || oj == null) {
            throw new Exception("db is null");
        }
    }

    public static FraudEfsAdapter m693A(Context context) {
        try {
            if (of == null) {
                of = new FraudEfsAdapter(context);
            }
        } catch (Exception e) {
            Log.m286e("FraudEfsAdapter", "cannot get third db");
            of = null;
        }
        return of;
    }

    public Cursor rawQuery(String str, String[] strArr) {
        try {
            return oj.rawQuery(str, strArr);
        } catch (Throwable e) {
            Log.m284c("FraudEfsAdapter", e.getMessage(), e);
            return null;
        }
    }

    public long m694a(FBaseRecord fBaseRecord) {
        long j = -1;
        if (fBaseRecord != null) {
            ContentValues bC = fBaseRecord.bC();
            Log.m285d("FraudEfsAdapter", bC.toString());
            try {
                j = oj.replaceOrThrow(fBaseRecord.bB(), null, bC);
            } catch (Throwable e) {
                Log.m284c("FraudEfsAdapter", e.getMessage(), e);
            }
        }
        return j;
    }

    public int m695b(String str, String str2, String[] strArr) {
        int i = -1;
        if (str != null) {
            try {
                i = oj.delete(str, str2, strArr);
            } catch (Throwable e) {
                Log.m284c("FraudEfsAdapter", e.getMessage(), e);
            }
        }
        return i;
    }
}
