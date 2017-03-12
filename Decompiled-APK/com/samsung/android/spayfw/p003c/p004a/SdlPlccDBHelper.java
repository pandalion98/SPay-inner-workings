package com.samsung.android.spayfw.p003c.p004a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/* renamed from: com.samsung.android.spayfw.c.a.g */
public class SdlPlccDBHelper extends SdlBaseDBHelper {
    private static SQLiteDatabase Bw;
    private static SdlPlccDBHelper Bx;
    private static final String TAG;

    static {
        TAG = SdlPlccDBHelper.class.getSimpleName();
    }

    private SdlPlccDBHelper(Context context) {
        super(context, "PlccCardData_enc.db", null, 1);
    }

    public static synchronized SdlPlccDBHelper m299V(Context context) {
        SdlPlccDBHelper sdlPlccDBHelper;
        synchronized (SdlPlccDBHelper.class) {
            if (Bx == null) {
                Bx = new SdlPlccDBHelper(context);
            }
            sdlPlccDBHelper = Bx;
        }
        return sdlPlccDBHelper;
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
