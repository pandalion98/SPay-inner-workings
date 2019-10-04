/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  com.samsung.android.database.sqlite.SemSQLiteSecureOpenHelper
 *  java.lang.String
 */
package com.samsung.android.spayfw.d.a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.database.sqlite.SemSQLiteSecureOpenHelper;
import com.samsung.android.spayfw.interfacelibrary.db.b;

public abstract class a
extends SemSQLiteSecureOpenHelper
implements b {
    private com.samsung.android.spayfw.interfacelibrary.db.a Bo;

    protected a(Context context, String string, SQLiteDatabase.CursorFactory cursorFactory, int n2) {
        super(context, string, cursorFactory, n2);
    }

    @Override
    public void a(com.samsung.android.spayfw.interfacelibrary.db.a a2) {
        this.Bo = a2;
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (this.Bo != null) {
            this.Bo.onConfigure(sQLiteDatabase);
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        if (this.Bo != null) {
            this.Bo.onCreate(sQLiteDatabase);
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        if (this.Bo != null) {
            this.Bo.onUpgrade(sQLiteDatabase, n2, n3);
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        if (this.Bo != null) {
            this.Bo.onUpgrade(sQLiteDatabase, n2, n3);
        }
    }
}

