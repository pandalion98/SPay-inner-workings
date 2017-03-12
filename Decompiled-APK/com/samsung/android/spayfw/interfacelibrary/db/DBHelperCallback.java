package com.samsung.android.spayfw.interfacelibrary.db;

import android.database.sqlite.SQLiteDatabase;

/* renamed from: com.samsung.android.spayfw.interfacelibrary.db.a */
public interface DBHelperCallback {
    void onConfigure(SQLiteDatabase sQLiteDatabase);

    void onCreate(SQLiteDatabase sQLiteDatabase);

    void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);
}
