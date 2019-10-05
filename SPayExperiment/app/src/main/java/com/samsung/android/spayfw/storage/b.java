/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;

import com.samsung.android.spayfw.b.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class b
extends DbAdapter {
    private static b BS;

    private b(Context context) {
        super(context);
        this.execSQL("CREATE TABLE IF NOT EXISTS config (key TEXT PRIMARY KEY, value TEXT NOT NULL)");
        Log.i("ConfigStorage", "Create Config Table If Not Exists");
    }

    public static final b ab(Context context) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (BS == null) {
                BS = new b(context);
            }
            b b2 = BS;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return b2;
        }
    }

    public int bm(String string) {
        Log.d("ConfigStorage", "removeConfig : " + string);
        if (string == null) {
            Log.e("ConfigStorage", "Key is NULL");
            return -1;
        }
        return this.e("config", "key", string);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public Map<String, String> fr() {
        block5 : {
            var1_1 = new HashMap();
            var3_3 = var4_2 = this.a("config", null, null, null, null);
            if (var3_3 == null) break block5;
            try {
                if (var3_3.getCount() <= 0) break block5;
                while (var3_3.moveToNext()) {
                    var5_4 = var3_3.getString(var3_3.getColumnIndex("key"));
                    var6_5 = var3_3.getString(var3_3.getColumnIndex("value"));
                    Log.d("ConfigStorage", "getAllConfigRows: key = " + var5_4 + "; value = " + var6_5);
                    var1_1.put((Object)var5_4, (Object)var6_5);
                }
                break block5;
            }
            catch (Throwable var2_6) {}
            ** GOTO lbl-1000
        }
        b.a(var3_3);
        return var1_1;
        catch (Throwable var2_8) {
            var3_3 = null;
        }
lbl-1000: // 2 sources:
        {
            b.a(var3_3);
            throw var2_7;
        }
    }

    public String getConfig(String string) {
        Log.d("ConfigStorage", "getConfig : " + string);
        if (string == null) {
            Log.e("ConfigStorage", "Key is NULL");
            return null;
        }
        List<String> list = this.a("config", "value", "key", string);
        if (list != null && list.size() > 0) {
            return (String)list.get(0);
        }
        Log.e("ConfigStorage", "Value is NULL");
        return null;
    }

    public int setConfig(String string, String string2) {
        Log.d("ConfigStorage", "setConfig : " + string + " " + string2);
        if (string == null) {
            Log.e("ConfigStorage", "Key is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", string);
        contentValues.put("value", string2);
        return this.a("config", contentValues);
    }
}

