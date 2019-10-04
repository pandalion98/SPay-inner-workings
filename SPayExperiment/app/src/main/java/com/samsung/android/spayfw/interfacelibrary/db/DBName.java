/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.interfacelibrary.db;

public final class DBName
extends Enum<DBName> {
    public static final /* enum */ DBName or = new DBName();
    public static final /* enum */ DBName ot = new DBName();
    public static final /* enum */ DBName ov = new DBName();
    public static final /* enum */ DBName ow = new DBName();
    public static final /* enum */ DBName ox = new DBName();
    public static final /* enum */ DBName oy = new DBName();
    private static final /* synthetic */ DBName[] oz;

    static {
        DBName[] arrdBName = new DBName[]{or, ot, ov, ow, ox, oy};
        oz = arrdBName;
    }

    public static DBName valueOf(String string) {
        return (DBName)Enum.valueOf(DBName.class, (String)string);
    }

    public static DBName[] values() {
        return (DBName[])oz.clone();
    }
}

