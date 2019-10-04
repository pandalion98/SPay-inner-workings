/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.SQLException
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.e.a.a;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCommonDao;

public abstract class MCAbstractDaoImpl<T>
implements McCommonDao<T> {
    public static final long DB_ERROR = -1L;
    private static final String TAG = "MCAbstractDaoImpl<T>";
    protected SQLiteDatabase db;
    private a mMcDBhelperWrapper = null;

    public MCAbstractDaoImpl(Context context) {
        this.mMcDBhelperWrapper = a.b(context, null, 0, DBName.ow);
        this.db = this.mMcDBhelperWrapper.getWritableDatabase(com.samsung.android.spayfw.utils.c.getDbPassword());
    }

    @Override
    public long deleteAll() {
        if (this.db == null) {
            return -1L;
        }
        return this.db.delete(this.getTableName(), "1", null);
    }

    @Override
    public boolean deleteData(long l2) {
        if (this.db == null) {
            return false;
        }
        int n2 = this.db.delete(this.getTableName(), this.getQuerySearch(l2), null);
        if (n2 < 1) {
            c.e(TAG, "deleteData: " + n2);
            return false;
        }
        return true;
    }

    protected abstract ContentValues getContentValues(T var1);

    protected abstract ContentValues getContentValues(T var1, long var2);

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public T getData(long var1_1) {
        block9 : {
            block8 : {
                var3_2 = null;
                var8_3 = this.db.query(this.getTableName(), null, this.getQuerySearch(var1_1), null, null, null, this.getQuerySearch(var1_1) + " DESC");
                if (var8_3 == null) break block8;
                if (!var8_3.moveToFirst()) break block8;
                var11_4 = this.getDataValues(var8_3);
                var9_5 = var11_4;
                break block9;
                catch (NullPointerException var5_6) {
                    block10 : {
                        var6_7 = null;
                        break block10;
                        catch (Throwable var4_8) {}
                        ** GOTO lbl-1000
                        catch (Throwable var4_10) {
                            var3_2 = var8_3;
                            ** GOTO lbl-1000
                        }
                        catch (NullPointerException var10_12) {
                            var6_7 = var8_3;
                        }
                    }
                    try {
                        c.e("MCAbstractDaoImpl<T>", "NPE occured during getData");
                        if (var6_7 == null) return null;
                    }
                    catch (Throwable var7_11) {
                        var3_2 = var6_7;
                        var4_9 = var7_11;
                    }
                    var6_7.close();
                    return null;
                }
lbl-1000: // 3 sources:
                {
                    if (var3_2 == null) throw var4_9;
                    var3_2.close();
                    throw var4_9;
                }
            }
            var9_5 = null;
        }
        if (var8_3 == null) return var9_5;
        var8_3.close();
        return var9_5;
    }

    protected abstract T getDataValues(Cursor var1);

    protected String getQuerySearch(long l2) {
        return "_id=" + l2;
    }

    protected abstract String getTableName();

    @Override
    public long saveData(T t2) {
        if (this.db == null) {
            return -1L;
        }
        ContentValues contentValues = this.getContentValues(t2);
        try {
            long l2 = this.db.insertOrThrow(this.getTableName(), null, contentValues);
            return l2;
        }
        catch (SQLException sQLException) {
            c.e("MCAbstractDaoImpl<T>", "SQLException during insert" + sQLException.getMessage());
            sQLException.printStackTrace();
            return -1L;
        }
    }

    @Override
    public long saveData(T t2, long l2) {
        if (this.db == null) {
            return -1L;
        }
        ContentValues contentValues = this.getContentValues(t2, l2);
        try {
            long l3 = this.db.insertOrThrow(this.getTableName(), null, contentValues);
            return l3;
        }
        catch (SQLException sQLException) {
            c.e("MCAbstractDaoImpl<T>", "SQLException during insert" + sQLException.getMessage());
            sQLException.printStackTrace();
            return -1L;
        }
    }

    @Override
    public boolean updateData(T t2, long l2) {
        if (this.db == null) {
            return false;
        }
        int n2 = this.db.update(this.getTableName(), this.getContentValues(t2), this.getQuerySearch(l2), null);
        if (n2 != 1) {
            c.e("MCAbstractDaoImpl<T>", "updateData: MultipleRows Updated: " + n2);
            return false;
        }
        return true;
    }
}

