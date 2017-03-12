package com.samsung.android.spayfw.p006d.p007a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/* renamed from: com.samsung.android.spayfw.d.a.g */
public class SePlccDBHelper extends SeBaseDBHelper {
    private static SePlccDBHelper BG;
    private static SQLiteDatabase Bw;
    private static final String TAG;

    static {
        TAG = SePlccDBHelper.class.getSimpleName();
    }

    private SePlccDBHelper(Context context) {
        super(context, "PlccCardData_enc.db", null, 1);
    }

    public static synchronized SePlccDBHelper m687Z(Context context) {
        SePlccDBHelper sePlccDBHelper;
        synchronized (SePlccDBHelper.class) {
            if (BG == null) {
                BG = new SePlccDBHelper(context);
            }
            sePlccDBHelper = BG;
        }
        return sePlccDBHelper;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL("CREATE TABLE entry (_id INTEGER PRIMARY KEY AUTOINCREMENT, providerKey VARCHAR UNIQUE,tokenStatus VARCHAR,merchantID VARCHAR,defaultSequenceConfig VARCHAR,trTokenId VARCHAR,timeStamp VARCHAR,tzData VARCHAR)");
        } catch (Throwable e) {
            Log.e(TAG, "onCreate: SqlException: " + e.getMessage());
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public synchronized void close() {
        super.close();
        if (Bw != null && Bw.isOpen()) {
            Bw.close();
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.e(TAG, "upgrade database from version " + i + " to version " + i2);
        if (i > i2) {
            Log.e(TAG, "database cannot be downgraded, please update to the latest version");
        }
    }
}
