package com.samsung.contextservice.p028a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.samsung.contextservice.p029b.CSlog;

/* renamed from: com.samsung.contextservice.a.b */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static DbOpenHelper GB;
    private static Context mContext;

    public static DbOpenHelper av(Context context) {
        if (GB == null) {
            mContext = context.getApplicationContext();
            GB = new DbOpenHelper(mContext);
        }
        return GB;
    }

    private DbOpenHelper(Context context) {
        super(context, "context.db", null, 4);
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isReadOnly()) {
            sQLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
            CSlog.m1410i("DbOpenHelper", "FOREIGN KEY constraint enabled");
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            if (sQLiteDatabase.isOpen()) {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cacheindex ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT UNIQUE, cachegeohash TEXT UNIQUE NOT NULL, empty BOOL NOT NULL, updateat TEXT, expireat TEXT, data_1 INTEGER, data_2 INTEGER, data_3 DOUBLE, data_4 DOUBLE, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB  ) ");
                CSlog.m1408d("DbOpenHelper", "Table cacheindex is created");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS caches ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT, cachegeohash TEXT, updatedat INTEGER, expireat INTEGER, geohash TEXT, poiid TEXT, poiname TEXT, poiradius DOUBLE, poipurpose TEXT, poistatus TEXT, lat DOUBLE, lon DOUBLE, alt DOUBLE, wifi TEXT, visitedtimes INTEGER DEFAULT 0, lastvisit INTEGER DEFAULT 0, poi TEXT, other TEXT, data_1 INTEGER, data_2 DOUBLE, data_3 TEXT, data_4 BLOB, UNIQUE(poiid, poipurpose), FOREIGN KEY (cachegeohash) REFERENCES cacheindex(cachegeohash) ON DELETE CASCADE  ) ");
                CSlog.m1408d("DbOpenHelper", "Table caches is created");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS policies ( _id INTEGER PRIMARY KEY AUTOINCREMENT, policyid TEXT UNIQUE, version TEXT, content BLOB, data_1 INTEGER, data_2 INTEGER, data_3 DOUBLE,  data_4 DOUBLE, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB  ) ");
                CSlog.m1408d("DbOpenHelper", "Table policies is created");
                CSlog.m1410i("DbOpenHelper", "Database is created");
            }
        } catch (Throwable e) {
            CSlog.m1409e("DbOpenHelper", "Database is not fully created, any schema error?");
            CSlog.m1406c("DbOpenHelper", e.getMessage(), e);
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        int i3 = 1;
        CSlog.m1410i("DbOpenHelper", "upgrade database from version " + i + " to version " + i2);
        if (i == i2) {
            try {
                CSlog.m1410i("DbOpenHelper", "oldVersion == newVersion, no need to upgrade!");
            } catch (Exception e) {
                CSlog.m1408d("DbOpenHelper", "cannot upgrade db");
            }
        } else if (i > i2) {
            CSlog.m1409e("DbOpenHelper", "database cannot be downgraded, please update to the latest version");
            onDowngrade(sQLiteDatabase, i, i2);
        } else {
            int i4;
            if (i >= 1) {
                i3 = i;
            }
            if (i3 <= 3) {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS caches");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS cacheindex");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS local");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cacheindex ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT UNIQUE, cachegeohash TEXT UNIQUE NOT NULL, empty BOOL NOT NULL, updateat TEXT, expireat TEXT, data_1 INTEGER, data_2 INTEGER, data_3 DOUBLE, data_4 DOUBLE, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB  ) ");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS caches ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT, cachegeohash TEXT, updatedat INTEGER, expireat INTEGER, geohash TEXT, poiid TEXT, poiname TEXT, poiradius DOUBLE, poipurpose TEXT, poistatus TEXT, lat DOUBLE, lon DOUBLE, alt DOUBLE, wifi TEXT, visitedtimes INTEGER DEFAULT 0, lastvisit INTEGER DEFAULT 0, poi TEXT, other TEXT, data_1 INTEGER, data_2 DOUBLE, data_3 TEXT, data_4 BLOB, UNIQUE(poiid, poipurpose), FOREIGN KEY (cachegeohash) REFERENCES cacheindex(cachegeohash) ON DELETE CASCADE  ) ");
                i4 = 4;
                CSlog.m1408d("DbOpenHelper", "upgrade db " + i3 + "->" + 4);
            } else {
                i4 = i3;
            }
            if (i4 == i2) {
                CSlog.m1410i("DbOpenHelper", "database is upgraded");
            }
        }
    }
}
