/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.e.a.a;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import java.util.ArrayList;
import java.util.List;

public abstract class DbAdapter {
    private a BT = null;
    private SQLiteDatabase Bq;

    protected DbAdapter(Context context) {
        this.BT = a.b(context, "spayfw_enc.db", 1, DBName.or);
        this.Bq = this.BT.getWritableDatabase(com.samsung.android.spayfw.utils.c.getDbPassword());
    }

    public static final void a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(String string, ContentValues contentValues) {
        if (contentValues == null) {
            return -1;
        }
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            return (int)this.Bq.replaceOrThrow(string, null, contentValues);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(String string, ContentValues contentValues, String string2, String string3) {
        String string4;
        String[] arrstring;
        if (contentValues == null) {
            return -1;
        }
        if (string2 != null) {
            string4 = string2 + " = ?";
            arrstring = null;
            if (string3 != null) {
                arrstring = new String[]{string3};
            }
        } else {
            arrstring = null;
            string4 = null;
        }
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            return this.Bq.update(string, contentValues, string4, arrstring);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int a(String string, String string2, Object object, ColumnType columnType, String string3, String string4) {
        if (string2 == null || string3 == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        switch (columnType) {
            default: {
                return -1;
            }
            case BY: {
                contentValues.put(string2, (String)object);
                do {
                    return this.a(string, contentValues, string3, string4);
                    break;
                } while (true);
            }
            case BV: {
                contentValues.put(string2, (Integer)object);
                return this.a(string, contentValues, string3, string4);
            }
            case BZ: 
        }
        contentValues.put(string2, (byte[])object);
        return this.a(string, contentValues, string3, string4);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Cursor a(String string, String[] arrstring, String string2, String[] arrstring2, String string3) {
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            return this.Bq.query(string, arrstring, string2, arrstring2, null, null, string3);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public List<String> a(String var1_1, String var2_2, String var3_3, String var4_4) {
        block16 : {
            block15 : {
                var5_5 = null;
                if (var2_2 == null) {
                    return null;
                }
                var6_6 = new String[]{var2_2};
                if (var3_3 != null) {
                    var7_7 = var3_3 + " = ?";
                    var8_8 = var4_4 != null ? new String[]{var4_4} : null;
                } else {
                    var7_7 = null;
                    var8_8 = null;
                }
                var15_9 = DbAdapter.class;
                // MONITORENTER : com.samsung.android.spayfw.storage.DbAdapter.class
                var10_11 = var12_10 = this.Bq.query(var1_1, var6_6, var7_7, var8_8, null, null, null);
                if (var10_11 == null) break block15;
                try {
                    if (var10_11.getCount() <= 0) break block15;
                    var13_12 = new ArrayList(var10_11.getCount());
                    while (var10_11.moveToNext()) {
                        var13_12.add((Object)var10_11.getString(var10_11.getColumnIndex(var2_2)));
                    }
                    break block16;
                }
                catch (Throwable var9_13) {}
                ** GOTO lbl-1000
            }
            var13_12 = null;
        }
        DbAdapter.a(var10_11);
        return var13_12;
        catch (Throwable var9_16) {
            var10_11 = null;
        }
lbl-1000: // 2 sources:
        {
            DbAdapter.a(var10_11);
            throw var9_14;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public int d(String var1_1, String var2_2, String var3_3) {
        if (var2_2 != null) {
            var4_4 = var2_2 + " = ?";
            var5_5 = var3_3 != null ? new String[]{var3_3} : null;
        } else {
            var2_2 = "_id";
            var4_4 = null;
            var5_5 = null;
        }
        var11_6 = DbAdapter.class;
        // MONITORENTER : com.samsung.android.spayfw.storage.DbAdapter.class
        try {
            var7_8 = var9_7 = this.Bq.query(var1_1, new String[]{var2_2}, var4_4, var5_5, null, null, null);
            ** if (var7_8 == null) goto lbl19
        }
        catch (Throwable var8_10) {
            var7_8 = null;
            try {
                throw var8_11;
            }
            catch (Throwable var6_13) {}
            ** GOTO lbl-1000
lbl26: // 1 sources:
            DbAdapter.a(var7_8);
            return 0;
            catch (Throwable var6_15) {
                var7_8 = null;
            }
lbl-1000: // 2 sources:
            {
                DbAdapter.a(var7_8);
                throw var6_14;
            }
        }
lbl-1000: // 1 sources:
        {
            Log.d("DbAdapter", "get count by column:" + var7_8.getCount());
            var10_9 = var7_8.getCount();
            DbAdapter.a(var7_8);
            return var10_9;
        }
lbl19: // 1 sources:
        ** GOTO lbl26
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int delete(String string, String string2, String[] arrstring) {
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            return this.Bq.delete(string, string2, arrstring);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int e(String string, String string2, String string3) {
        String[] arrstring;
        String string4;
        if (string2 != null) {
            string4 = string2 + " = ?";
            arrstring = null;
            if (string3 != null) {
                arrstring = new String[]{string3};
            }
        } else {
            arrstring = null;
            string4 = null;
        }
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            return this.Bq.delete(string, string4, arrstring);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void execSQL(String string) {
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            this.Bq.execSQL(string);
            // ** MonitorExit[var3_2] (shouldn't be in output)
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Cursor f(String string, String string2, String string3) {
        if (string2 == null) {
            return null;
        }
        String string4 = string2 + " = ?";
        String[] arrstring = null;
        if (string3 != null) {
            arrstring = new String[]{string3};
        }
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            return this.Bq.query(string, null, string4, arrstring, null, null, null);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Cursor query(String string, String[] arrstring, String string2, String[] arrstring2, String string3, String string4, String string5) {
        Class<DbAdapter> class_ = DbAdapter.class;
        synchronized (DbAdapter.class) {
            return this.Bq.query(string, arrstring, string2, arrstring2, string3, string4, string5);
        }
    }

    public static final class ColumnType
    extends Enum<ColumnType> {
        public static final /* enum */ ColumnType BV = new ColumnType("INTEGER");
        public static final /* enum */ ColumnType BW = new ColumnType("INTEGER");
        public static final /* enum */ ColumnType BX = new ColumnType("REAL");
        public static final /* enum */ ColumnType BY = new ColumnType("TEXT");
        public static final /* enum */ ColumnType BZ = new ColumnType("BLOB");
        public static final /* enum */ ColumnType Ca = new ColumnType("NULL");
        private static final /* synthetic */ ColumnType[] Cb;
        private final String defaultValue;

        static {
            ColumnType[] arrcolumnType = new ColumnType[]{BV, BW, BX, BY, BZ, Ca};
            Cb = arrcolumnType;
        }

        private ColumnType(String string2) {
            this.defaultValue = string2;
        }

        public static ColumnType valueOf(String string) {
            return (ColumnType)Enum.valueOf(ColumnType.class, (String)string);
        }

        public static ColumnType[] values() {
            return (ColumnType[])Cb.clone();
        }

        public final String fs() {
            return this.defaultValue;
        }
    }

}

