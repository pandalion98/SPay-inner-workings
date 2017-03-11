package com.samsung.sensorframework.sdi.p049f;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytui.SPayTUIException;
import com.samsung.sensorframework.sdi.exception.SDIException;
import com.samsung.sensorframework.sdi.p050g.SFUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* renamed from: com.samsung.sensorframework.sdi.f.a */
public class PersistentStorage {
    private static final Object Ho;
    private static PersistentStorage LI;
    private SharedPreferences LJ;

    static {
        Ho = new Object();
    }

    public static PersistentStorage bu(Context context) {
        if (LI == null) {
            synchronized (Ho) {
                if (LI == null) {
                    LI = new PersistentStorage(context);
                }
            }
        }
        return LI;
    }

    private PersistentStorage(Context context) {
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.LJ = context.getSharedPreferences("spayfw.sensorframework.PersistentStorage", 0);
        if (this.LJ == null) {
            throw new SDIException(SPayTUIException.ERR_UNKNOWN, "sharedPreferences can not be null.");
        }
        Log.m285d("PersistentStorage", "instance created.");
    }

    public void removeAll() {
        Log.m285d("PersistentStorage", "removeAll()");
        ig();
        Set stringSet = this.LJ.getStringSet("poiEnterGeofenceSet", new HashSet());
        Editor edit = this.LJ.edit();
        edit.clear();
        edit.putStringSet("poiEnterGeofenceSet", stringSet);
        edit.commit();
        Log.m285d("PersistentStorage", "removeAll() - cleared");
        ig();
    }

    private void ig() {
        Map all = this.LJ.getAll();
        if (all != null) {
            for (String str : all.keySet()) {
                if (all.get(str) != null) {
                    Log.m285d("PersistentStorage", "printAll() key: " + str + " value: " + all.get(str).toString());
                } else {
                    Log.m285d("PersistentStorage", "printAll() key: " + str + " value: null");
                }
            }
            return;
        }
        Log.m285d("PersistentStorage", "printAll() no keys stored in persistent storage");
    }

    public boolean contains(String str) {
        return this.LJ.contains(str);
    }

    public void set(String str, int i) {
        Log.m285d("PersistentStorage", "set() key: " + str + " value: " + i);
        Editor edit = this.LJ.edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public int get(String str, int i) {
        int i2 = this.LJ.getInt(str, i);
        Log.m285d("PersistentStorage", "get() key: " + str + " defaultValue: " + i + " returning: " + i2);
        return i2;
    }

    public void m1706a(String str, HashSet<String> hashSet) {
        Log.m285d("PersistentStorage", "setHashSet() key: " + str + " values: " + SFUtils.m1707a(hashSet));
        Editor edit = this.LJ.edit();
        edit.putStringSet(str, hashSet);
        edit.commit();
    }

    public HashSet<String> m1705a(String str, Set<String> set) {
        Collection stringSet = this.LJ.getStringSet(str, set);
        Object hashSet = new HashSet();
        if (stringSet != null && stringSet.size() > 0) {
            hashSet.addAll(stringSet);
        }
        Log.m285d("PersistentStorage", "getHashSet() key: " + str + " defaultValue: " + SFUtils.m1707a(set) + " returning: " + SFUtils.m1707a(hashSet));
        return hashSet;
    }

    public HashMap<String, Integer> cy(String str) {
        Log.m285d("PersistentStorage", "getAllMatchingKeySubstring()");
        HashMap<String, Integer> hashMap = new HashMap();
        Map all = this.LJ.getAll();
        if (all != null) {
            for (String str2 : all.keySet()) {
                if (str2.contains(str)) {
                    int intValue = ((Integer) all.get(str2)).intValue();
                    Log.m285d("PersistentStorage", "getAllMatchingKeySubstring() keySubstring: " + str + " key: " + str2 + " value: " + intValue);
                    hashMap.put(str2, Integer.valueOf(intValue));
                }
            }
        }
        return hashMap;
    }
}
