package com.samsung.android.spayfw.payprovider.mastercard.dao;

public interface McCommonDao<T> {
    long deleteAll();

    boolean deleteData(long j);

    T getData(long j);

    long saveData(T t);

    long saveData(T t, long j);

    boolean updateData(T t, long j);
}
