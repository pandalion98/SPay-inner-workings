package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.p009a.DBHelperWrapper;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.utils.DBUtils;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.db.dao.b */
public abstract class DcAbstractDaoImpl<T> implements DcCommonDao<T> {
    protected SQLiteDatabase db;
    private DBHelperWrapper sH;

    protected abstract ContentValues getContentValues(T t);

    protected abstract T getDataValues(Cursor cursor);

    protected abstract String getTableName();

    public DcAbstractDaoImpl(Context context) {
        this.sH = null;
        this.sH = DBHelperWrapper.m689b(context, null, 0, DBName.dc_provider);
        this.db = this.sH.getWritableDatabase(DBUtils.getDbPassword());
    }

    public T getData(long j) {
        Cursor query;
        Throwable th;
        Cursor cursor = null;
        try {
            T dataValues;
            query = this.db.query(getTableName(), null, getQuerySearch(j), null, null, null, getQuerySearch(j) + " DESC");
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        dataValues = getDataValues(query);
                    }
                } catch (NullPointerException e) {
                    try {
                        Log.m286e("DCSDK_DCAbstractDaoImpl<T>", "NPE occured during getData");
                        throw new DcDbException("NP Exception Occurred", 6);
                    } catch (Throwable th2) {
                        cursor = query;
                        th = th2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            }
            if (query != null) {
                query.close();
            }
            return dataValues;
        } catch (NullPointerException e2) {
            query = null;
            Log.m286e("DCSDK_DCAbstractDaoImpl<T>", "NPE occured during getData");
            throw new DcDbException("NP Exception Occurred", 6);
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public long saveData(T t) {
        if (t == null) {
            throw new DcDbException("Invalid Input", 3);
        } else if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        } else {
            try {
                return this.db.insertOrThrow(getTableName(), null, getContentValues(t));
            } catch (SQLException e) {
                Log.m286e("DCSDK_DCAbstractDaoImpl<T>", "SQLException during insert" + e.getMessage());
                e.printStackTrace();
                throw new DcDbException("SQL Exception Occurred", 5);
            }
        }
    }

    public boolean deleteData(long j) {
        if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        } else if (this.db.delete(getTableName(), getQuerySearch(j), null) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateData(T t, long j) {
        if (t == null) {
            throw new DcDbException("Invalid Input", 3);
        } else if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        } else if (this.db.update(getTableName(), getContentValues(t), getQuerySearch(j), null) == 1) {
            return true;
        } else {
            return false;
        }
    }

    protected List<T> m875c(Cursor cursor) {
        List<T> arrayList = new ArrayList();
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(getDataValues(cursor));
            } while (cursor.moveToNext());
            cursor.close();
            return arrayList;
        }
        cursor.close();
        return null;
    }

    protected String getQuerySearch(long j) {
        return "_id=" + j;
    }

    public List<T> m877o(long j) {
        throw new DcDbException("Method Not Supported", 3);
    }

    public void cI() {
        Throwable th;
        Cursor cursor = null;
        Cursor query;
        try {
            query = this.db.query(getTableName(), null, null, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        do {
                            m876d(query);
                        } while (query.moveToNext());
                    } else {
                        query.close();
                        if (query != null) {
                            query.close();
                            return;
                        }
                        return;
                    }
                } catch (NullPointerException e) {
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (NullPointerException e2) {
            query = null;
            try {
                Log.m286e("DCSDK_DCAbstractDaoImpl<T>", "NPE occured during getData");
                throw new DcDbException("NP Exception Occurred", 6);
            } catch (Throwable th2) {
                cursor = query;
                th = th2;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    protected void m876d(Cursor cursor) {
    }
}
