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
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.e.a.a;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;

import java.util.ArrayList;
import java.util.List;

public abstract class b<T>
implements DcCommonDao<T> {
    protected SQLiteDatabase db;
    private a sH = null;

    public b(Context context) {
        this.sH = a.b(context, null, 0, DBName.oy);
        this.db = this.sH.getWritableDatabase(com.samsung.android.spayfw.utils.c.getDbPassword());
    }

    protected List<T> c(Cursor cursor) {
        ArrayList arrayList = new ArrayList();
        if (cursor == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        do {
            arrayList.add(this.getDataValues(cursor));
        } while (cursor.moveToNext());
        cursor.close();
        return arrayList;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public void cI() {
        block13 : {
            block14 : {
                block15 : {
                    var4_2 = var6_1 = this.db.query(this.getTableName(), null, null, null, null, null, null);
                    if (var4_2 == null) break block13;
                    try {
                        if (var4_2.moveToFirst()) break block14;
                        var4_2.close();
                        if (var4_2 == null) break block15;
                    }
                    catch (NullPointerException var7_9) {
                        ** continue;
                    }
                    var4_2.close();
                }
lbl10: // 2 sources:
                do {
                    return;
                    break;
                } while (true);
            }
            do {
                this.d(var4_2);
                if (var8_3 = var4_2.moveToNext()) continue;
                break;
            } while (true);
        }
        ** while (var4_2 == null)
lbl19: // 1 sources:
        var4_2.close();
        return;
        catch (NullPointerException var3_4) {
            var4_2 = null;
lbl23: // 2 sources:
            do {
                try {
                    Log.e("DCSDK_DCAbstractDaoImpl<T>", "NPE occured during getData");
                    throw new DcDbException("NP Exception Occurred", 6);
                }
                catch (Throwable var5_5) {
                    var2_6 = var4_2;
                    var1_7 = var5_5;
lbl30: // 2 sources:
                    do {
                        if (var2_6 != null) {
                            var2_6.close();
                        }
                        throw var1_7;
                        break;
                    } while (true);
                }
                break;
            } while (true);
        }
        catch (Throwable var1_8) {
            var2_6 = null;
            ** continue;
        }
    }

    protected void d(Cursor cursor) {
    }

    public boolean deleteData(long l2) {
        if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        }
        return this.db.delete(this.getTableName(), this.getQuerySearch(l2), null) > 0;
    }

    protected abstract ContentValues getContentValues(T var1);

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public T getData(long var1_1) {
        block8 : {
            var6_3 = var8_2 = this.db.query(this.getTableName(), null, this.getQuerySearch(var1_1), null, null, null, this.getQuerySearch(var1_1) + " DESC");
            var9_4 = null;
            if (var6_3 == null) break block8;
            try {
                var11_5 = var6_3.moveToFirst();
                var9_4 = null;
                if (!var11_5) break block8;
                var12_6 = this.getDataValues(var6_3);
            }
            catch (NullPointerException var10_12) {
                ** continue;
            }
            var9_4 = var12_6;
        }
        if (var6_3 == null) return var9_4;
        var6_3.close();
        return var9_4;
        catch (NullPointerException var5_7) {
            block9 : {
                var6_3 = null;
                break block9;
                catch (Throwable var3_11) {
                    var4_9 = null;
                    ** GOTO lbl29
                }
            }
lbl22: // 2 sources:
            do {
                try {
                    Log.e("DCSDK_DCAbstractDaoImpl<T>", "NPE occured during getData");
                    throw new DcDbException("NP Exception Occurred", 6);
                }
                catch (Throwable var7_8) {
                    var4_9 = var6_3;
                    var3_10 = var7_8;
lbl29: // 2 sources:
                    if (var4_9 == null) throw var3_10;
                    var4_9.close();
                    throw var3_10;
                }
                break;
            } while (true);
        }
    }

    protected abstract T getDataValues(Cursor var1);

    protected String getQuerySearch(long l2) {
        return "_id=" + l2;
    }

    protected abstract String getTableName();

    public List<T> o(long l2) {
        throw new DcDbException("Method Not Supported", 3);
    }

    public long saveData(T t2) {
        if (t2 == null) {
            throw new DcDbException("Invalid Input", 3);
        }
        if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        }
        ContentValues contentValues = this.getContentValues(t2);
        try {
            long l2 = this.db.insertOrThrow(this.getTableName(), null, contentValues);
            return l2;
        }
        catch (SQLException sQLException) {
            Log.e("DCSDK_DCAbstractDaoImpl<T>", "SQLException during insert" + sQLException.getMessage());
            sQLException.printStackTrace();
            throw new DcDbException("SQL Exception Occurred", 5);
        }
    }

    public boolean updateData(T t2, long l2) {
        if (t2 == null) {
            throw new DcDbException("Invalid Input", 3);
        }
        if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        }
        return this.db.update(this.getTableName(), this.getContentValues(t2), this.getQuerySearch(l2), null) == 1;
    }
}

