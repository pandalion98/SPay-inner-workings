/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.dao;

public interface McCommonDao<T> {
    public long deleteAll();

    public boolean deleteData(long var1);

    public T getData(long var1);

    public long saveData(T var1);

    public long saveData(T var1, long var2);

    public boolean updateData(T var1, long var2);
}

