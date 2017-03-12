package com.samsung.android.spayfw.p003c.p004a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteSecureOpenHelper;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperCallback;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperInterface;

/* renamed from: com.samsung.android.spayfw.c.a.a */
public abstract class SdlBaseDBHelper extends SQLiteSecureOpenHelper implements DBHelperInterface {
    private DBHelperCallback Bo;

    public SdlBaseDBHelper(Context context, String str, CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        if (this.Bo != null) {
            this.Bo.onCreate(sQLiteDatabase);
        }
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (this.Bo != null) {
            this.Bo.onConfigure(sQLiteDatabase);
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (this.Bo != null) {
            this.Bo.onDowngrade(sQLiteDatabase, i, i2);
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (this.Bo != null) {
            this.Bo.onUpgrade(sQLiteDatabase, i, i2);
        }
    }

    public void m292a(DBHelperCallback dBHelperCallback) {
        this.Bo = dBHelperCallback;
    }
}
