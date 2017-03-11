package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.p009a.DBHelperWrapper;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.utils.DBUtils;

public abstract class MCAbstractDaoImpl<T> implements McCommonDao<T> {
    public static final long DB_ERROR = -1;
    private static final String TAG = "MCAbstractDaoImpl<T>";
    protected SQLiteDatabase db;
    private DBHelperWrapper mMcDBhelperWrapper;

    protected abstract ContentValues getContentValues(T t);

    protected abstract ContentValues getContentValues(T t, long j);

    protected abstract T getDataValues(Cursor cursor);

    protected abstract String getTableName();

    public MCAbstractDaoImpl(Context context) {
        this.mMcDBhelperWrapper = null;
        this.mMcDBhelperWrapper = DBHelperWrapper.m689b(context, null, 0, DBName.mc_enc);
        this.db = this.mMcDBhelperWrapper.getWritableDatabase(DBUtils.getDbPassword());
    }

    public T getData(long j) {
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        try {
            T dataValues;
            Cursor query = this.db.query(getTableName(), null, getQuerySearch(j), null, null, null, getQuerySearch(j) + " DESC");
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        dataValues = getDataValues(query);
                        if (query != null) {
                            return dataValues;
                        }
                        query.close();
                        return dataValues;
                    }
                } catch (NullPointerException e) {
                    cursor = query;
                    try {
                        Log.m286e(TAG, "NPE occured during getData");
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    } catch (Throwable th2) {
                        cursor2 = cursor;
                        th = th2;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    cursor2 = query;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            dataValues = null;
            if (query != null) {
                return dataValues;
            }
            query.close();
            return dataValues;
        } catch (NullPointerException e2) {
            cursor = null;
            Log.m286e(TAG, "NPE occured during getData");
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } catch (Throwable th4) {
            th = th4;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    public long saveData(T t) {
        long j = DB_ERROR;
        if (this.db != null) {
            try {
                j = this.db.insertOrThrow(getTableName(), null, getContentValues(t));
            } catch (SQLException e) {
                Log.m286e(TAG, "SQLException during insert" + e.getMessage());
                e.printStackTrace();
            }
        }
        return j;
    }

    public long saveData(T t, long j) {
        long j2 = DB_ERROR;
        if (this.db != null) {
            try {
                j2 = this.db.insertOrThrow(getTableName(), null, getContentValues(t, j));
            } catch (SQLException e) {
                Log.m286e(TAG, "SQLException during insert" + e.getMessage());
                e.printStackTrace();
            }
        }
        return j2;
    }

    public boolean deleteData(long j) {
        if (this.db == null) {
            return false;
        }
        int delete = this.db.delete(getTableName(), getQuerySearch(j), null);
        if (delete >= 1) {
            return true;
        }
        Log.m286e(TAG, "deleteData: " + delete);
        return false;
    }

    public boolean updateData(T t, long j) {
        if (this.db == null) {
            return false;
        }
        int update = this.db.update(getTableName(), getContentValues(t), getQuerySearch(j), null);
        if (update == 1) {
            return true;
        }
        Log.m286e(TAG, "updateData: MultipleRows Updated: " + update);
        return false;
    }

    public long deleteAll() {
        if (this.db == null) {
            return DB_ERROR;
        }
        return (long) this.db.delete(getTableName(), TransactionInfo.VISA_TRANSACTIONTYPE_REFUND, null);
    }

    protected String getQuerySearch(long j) {
        return "_id=" + j;
    }
}
