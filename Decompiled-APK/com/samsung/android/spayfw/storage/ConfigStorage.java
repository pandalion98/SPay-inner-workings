package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.p002b.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: com.samsung.android.spayfw.storage.b */
public class ConfigStorage extends DbAdapter {
    private static ConfigStorage BS;

    public static final synchronized ConfigStorage ab(Context context) {
        ConfigStorage configStorage;
        synchronized (ConfigStorage.class) {
            if (BS == null) {
                BS = new ConfigStorage(context);
            }
            configStorage = BS;
        }
        return configStorage;
    }

    private ConfigStorage(Context context) {
        super(context);
        execSQL("CREATE TABLE IF NOT EXISTS config (key TEXT PRIMARY KEY, value TEXT NOT NULL)");
        Log.m287i("ConfigStorage", "Create Config Table If Not Exists");
    }

    public Map<String, String> fr() {
        Cursor a;
        Throwable th;
        Map<String, String> hashMap = new HashMap();
        try {
            a = m1110a("config", null, null, null, null);
            if (a != null) {
                try {
                    if (a.getCount() > 0) {
                        while (a.moveToNext()) {
                            String string = a.getString(a.getColumnIndex("key"));
                            String string2 = a.getString(a.getColumnIndex("value"));
                            Log.m285d("ConfigStorage", "getAllConfigRows: key = " + string + "; value = " + string2);
                            hashMap.put(string, string2);
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    DbAdapter.m1106a(a);
                    throw th;
                }
            }
            DbAdapter.m1106a(a);
            return hashMap;
        } catch (Throwable th3) {
            th = th3;
            a = null;
            DbAdapter.m1106a(a);
            throw th;
        }
    }

    public int setConfig(String str, String str2) {
        Log.m285d("ConfigStorage", "setConfig : " + str + " " + str2);
        if (str == null) {
            Log.m286e("ConfigStorage", "Key is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", str);
        contentValues.put("value", str2);
        return m1107a("config", contentValues);
    }

    public String getConfig(String str) {
        Log.m285d("ConfigStorage", "getConfig : " + str);
        if (str == null) {
            Log.m286e("ConfigStorage", "Key is NULL");
            return null;
        }
        List a = m1111a("config", "value", "key", str);
        if (a != null && a.size() > 0) {
            return (String) a.get(0);
        }
        Log.m286e("ConfigStorage", "Value is NULL");
        return null;
    }

    public int bm(String str) {
        Log.m285d("ConfigStorage", "removeConfig : " + str);
        if (str != null) {
            return m1113e("config", "key", str);
        }
        Log.m286e("ConfigStorage", "Key is NULL");
        return -1;
    }
}
