/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteOpenHelper
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.contextservice.a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class b
extends SQLiteOpenHelper {
    private static b GB;
    private static Context mContext;

    private b(Context context) {
        super(context, "context.db", null, 4);
    }

    public static b av(Context context) {
        if (GB == null) {
            mContext = context.getApplicationContext();
            GB = new b(mContext);
        }
        return GB;
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isReadOnly()) {
            sQLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
            com.samsung.contextservice.b.b.i("DbOpenHelper", "FOREIGN KEY constraint enabled");
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            if (sQLiteDatabase.isOpen()) {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cacheindex ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT UNIQUE, cachegeohash TEXT UNIQUE NOT NULL, empty BOOL NOT NULL, updateat TEXT, expireat TEXT, data_1 INTEGER, data_2 INTEGER, data_3 DOUBLE, data_4 DOUBLE, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB  ) ");
                com.samsung.contextservice.b.b.d("DbOpenHelper", "Table cacheindex is created");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS caches ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT, cachegeohash TEXT, updatedat INTEGER, expireat INTEGER, geohash TEXT, poiid TEXT, poiname TEXT, poiradius DOUBLE, poipurpose TEXT, poistatus TEXT, lat DOUBLE, lon DOUBLE, alt DOUBLE, wifi TEXT, visitedtimes INTEGER DEFAULT 0, lastvisit INTEGER DEFAULT 0, poi TEXT, other TEXT, data_1 INTEGER, data_2 DOUBLE, data_3 TEXT, data_4 BLOB, UNIQUE(poiid, poipurpose), FOREIGN KEY (cachegeohash) REFERENCES cacheindex(cachegeohash) ON DELETE CASCADE  ) ");
                com.samsung.contextservice.b.b.d("DbOpenHelper", "Table caches is created");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS policies ( _id INTEGER PRIMARY KEY AUTOINCREMENT, policyid TEXT UNIQUE, version TEXT, content BLOB, data_1 INTEGER, data_2 INTEGER, data_3 DOUBLE,  data_4 DOUBLE, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB  ) ");
                com.samsung.contextservice.b.b.d("DbOpenHelper", "Table policies is created");
                com.samsung.contextservice.b.b.i("DbOpenHelper", "Database is created");
            }
            return;
        }
        catch (Exception exception) {
            com.samsung.contextservice.b.b.e("DbOpenHelper", "Database is not fully created, any schema error?");
            com.samsung.contextservice.b.b.c("DbOpenHelper", exception.getMessage(), exception);
            return;
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void onUpgrade(SQLiteDatabase var1_1, int var2_2, int var3_3) {
        block5 : {
            var4_4 = 1;
            com.samsung.contextservice.b.b.i("DbOpenHelper", "upgrade database from version " + var2_2 + " to version " + var3_3);
            if (var2_2 != var3_3) ** GOTO lbl7
            try {
                com.samsung.contextservice.b.b.i("DbOpenHelper", "oldVersion == newVersion, no need to upgrade!");
                return;
lbl7: // 1 sources:
                if (var2_2 > var3_3) {
                    com.samsung.contextservice.b.b.e("DbOpenHelper", "database cannot be downgraded, please update to the latest version");
                    this.onDowngrade(var1_1, var2_2, var3_3);
                    return;
                }
                if (var2_2 < var4_4) break block5;
                var4_4 = var2_2;
            }
            catch (Exception var5_5) {
                com.samsung.contextservice.b.b.d("DbOpenHelper", "cannot upgrade db");
                return;
            }
        }
        if (var4_4 <= 3) {
            var1_1.execSQL("DROP TABLE IF EXISTS caches");
            var1_1.execSQL("DROP TABLE IF EXISTS cacheindex");
            var1_1.execSQL("DROP TABLE IF EXISTS local");
            var1_1.execSQL("CREATE TABLE IF NOT EXISTS cacheindex ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT UNIQUE, cachegeohash TEXT UNIQUE NOT NULL, empty BOOL NOT NULL, updateat TEXT, expireat TEXT, data_1 INTEGER, data_2 INTEGER, data_3 DOUBLE, data_4 DOUBLE, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB  ) ");
            var1_1.execSQL("CREATE TABLE IF NOT EXISTS caches ( _id INTEGER PRIMARY KEY AUTOINCREMENT, cacheid TEXT, cachegeohash TEXT, updatedat INTEGER, expireat INTEGER, geohash TEXT, poiid TEXT, poiname TEXT, poiradius DOUBLE, poipurpose TEXT, poistatus TEXT, lat DOUBLE, lon DOUBLE, alt DOUBLE, wifi TEXT, visitedtimes INTEGER DEFAULT 0, lastvisit INTEGER DEFAULT 0, poi TEXT, other TEXT, data_1 INTEGER, data_2 DOUBLE, data_3 TEXT, data_4 BLOB, UNIQUE(poiid, poipurpose), FOREIGN KEY (cachegeohash) REFERENCES cacheindex(cachegeohash) ON DELETE CASCADE  ) ");
            var6_6 = 4;
            com.samsung.contextservice.b.b.d("DbOpenHelper", "upgrade db " + var4_4 + "->" + var6_6);
        }
        var6_6 = var4_4;
        if (var6_6 != var3_3) return;
        com.samsung.contextservice.b.b.i("DbOpenHelper", "database is upgraded");
    }
}

