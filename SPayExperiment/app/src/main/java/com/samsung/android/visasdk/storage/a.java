/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.utils.c;
import com.samsung.android.visasdk.facade.VisaPaymentSDKImpl;
import com.samsung.android.visasdk.storage.d;
import java.util.ArrayList;
import java.util.List;

public class a
extends d {
    private static a Gt;
    private com.samsung.android.spayfw.e.a.a Gs = null;
    private SQLiteDatabase db;

    private a(Context context) {
        super(context);
        this.Gs = com.samsung.android.spayfw.e.a.a.b(context, "cbp_jan_enc.db", 3, DBName.ov);
        this.Gs.b(this);
        this.db = this.Gs.getWritableDatabase(c.getDbPassword());
        VisaPaymentSDKImpl.resetDbPassword();
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
    public static a as(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (Gt != null) return Gt;
            if (context == null) {
                com.samsung.android.visasdk.c.a.e("DbAdapter", "context is null");
                return null;
            }
            Gt = new a(context.getApplicationContext());
            return Gt;
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
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return this.db.update(string, contentValues, string4, arrstring);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Cursor a(String string, String[] arrstring, String string2, String[] arrstring2, String string3) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return this.db.query(string, arrstring, string2, arrstring2, null, null, string3);
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
                var15_9 = a.class;
                // MONITORENTER : com.samsung.android.visasdk.storage.a.class
                var10_11 = var12_10 = this.db.query(var1_1, var6_6, var7_7, var8_8, null, null, null);
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
        a.a(var10_11);
        return var13_12;
        catch (Throwable var9_16) {
            var10_11 = null;
        }
lbl-1000: // 2 sources:
        {
            a.a(var10_11);
            throw var9_14;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int b(String string, ContentValues contentValues) {
        if (contentValues == null) {
            return -1;
        }
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return (int)this.db.insertOrThrow(string, null, contentValues);
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
    public List<byte[]> b(String var1_1, String var2_2, String var3_3, String var4_4) {
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
                var15_9 = a.class;
                // MONITORENTER : com.samsung.android.visasdk.storage.a.class
                var10_11 = var12_10 = this.db.query(var1_1, var6_6, var7_7, var8_8, null, null, null);
                if (var10_11 == null) break block15;
                try {
                    if (var10_11.getCount() <= 0) break block15;
                    var13_12 = new ArrayList(var10_11.getCount());
                    while (var10_11.moveToNext()) {
                        var13_12.add((Object)var10_11.getBlob(var10_11.getColumnIndex(var2_2)));
                    }
                    break block16;
                }
                catch (Throwable var9_13) {}
                ** GOTO lbl-1000
            }
            var13_12 = null;
        }
        a.a(var10_11);
        return var13_12;
        catch (Throwable var9_16) {
            var10_11 = null;
        }
lbl-1000: // 2 sources:
        {
            a.a(var10_11);
            throw var9_14;
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
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return this.db.delete(string, string4, arrstring);
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
        Class<a> class_ = a.class;
        synchronized (a.class) {
            return this.db.query(string, null, string4, arrstring, null, null, null);
        }
    }
}

