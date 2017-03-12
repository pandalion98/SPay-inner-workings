package com.samsung.android.spayfw.interfacelibrary.db;

import android.database.sqlite.SQLiteDatabase;

/* renamed from: com.samsung.android.spayfw.interfacelibrary.db.b */
public interface DBHelperInterface {
    void m291a(DBHelperCallback dBHelperCallback);

    SQLiteDatabase getReadableDatabase(byte[] bArr);

    SQLiteDatabase getWritableDatabase(byte[] bArr);
}
