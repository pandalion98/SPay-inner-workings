/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Object
 */
package com.samsung.android.spayfw.interfacelibrary.db;

import android.database.sqlite.SQLiteDatabase;

public interface a {
    public void onConfigure(SQLiteDatabase var1);

    public void onCreate(SQLiteDatabase var1);

    public void onDowngrade(SQLiteDatabase var1, int var2, int var3);

    public void onUpgrade(SQLiteDatabase var1, int var2, int var3);
}

