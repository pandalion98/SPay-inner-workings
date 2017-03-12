package com.samsung.android.spayfw.p006d.p007a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.samsung.android.database.sqlite.SemSQLiteSecureOpenHelper;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperCallback;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperInterface;

/* renamed from: com.samsung.android.spayfw.d.a.a */
public abstract class SeBaseDBHelper extends SemSQLiteSecureOpenHelper implements DBHelperInterface {
    private DBHelperCallback Bo;

    protected SeBaseDBHelper(Context context, String str, CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (this.Bo != null) {
            this.Bo.onUpgrade(sQLiteDatabase, i, i2);
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        if (this.Bo != null) {
            this.Bo.onCreate(sQLiteDatabase);
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (this.Bo != null) {
            this.Bo.onUpgrade(sQLiteDatabase, i, i2);
        }
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (this.Bo != null) {
            this.Bo.onConfigure(sQLiteDatabase);
        }
    }

    public void m680a(DBHelperCallback dBHelperCallback) {
        this.Bo = dBHelperCallback;
    }
}
