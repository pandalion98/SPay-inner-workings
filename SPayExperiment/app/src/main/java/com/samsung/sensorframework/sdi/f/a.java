/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Map
 *  java.util.Set
 */
package com.samsung.sensorframework.sdi.f;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sdi.exception.SDIException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class a {
    private static final Object Ho = new Object();
    private static a LI;
    private SharedPreferences LJ;

    private a(Context context) {
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.LJ = context.getSharedPreferences("spayfw.sensorframework.PersistentStorage", 0);
        if (this.LJ == null) {
            throw new SDIException(9001, "sharedPreferences can not be null.");
        }
        c.d("PersistentStorage", "instance created.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static a bu(Context context) {
        if (LI == null) {
            Object object;
            Object object2 = object = Ho;
            synchronized (object2) {
                if (LI == null) {
                    LI = new a(context);
                }
            }
        }
        return LI;
    }

    private void ig() {
        Map map = this.LJ.getAll();
        if (map != null) {
            for (String string : map.keySet()) {
                if (map.get((Object)string) != null) {
                    c.d("PersistentStorage", "printAll() key: " + string + " value: " + map.get((Object)string).toString());
                    continue;
                }
                c.d("PersistentStorage", "printAll() key: " + string + " value: null");
            }
        } else {
            c.d("PersistentStorage", "printAll() no keys stored in persistent storage");
        }
    }

    public HashSet<String> a(String string, Set<String> set) {
        Set set2 = this.LJ.getStringSet(string, set);
        HashSet hashSet = new HashSet();
        if (set2 != null && set2.size() > 0) {
            hashSet.addAll((Collection)set2);
        }
        c.d("PersistentStorage", "getHashSet() key: " + string + " defaultValue: " + com.samsung.sensorframework.sdi.g.a.a(set) + " returning: " + com.samsung.sensorframework.sdi.g.a.a((Collection<String>)hashSet));
        return hashSet;
    }

    public void a(String string, HashSet<String> hashSet) {
        c.d("PersistentStorage", "setHashSet() key: " + string + " values: " + com.samsung.sensorframework.sdi.g.a.a(hashSet));
        SharedPreferences.Editor editor = this.LJ.edit();
        editor.putStringSet(string, hashSet);
        editor.commit();
    }

    public boolean contains(String string) {
        return this.LJ.contains(string);
    }

    public HashMap<String, Integer> cy(String string) {
        c.d("PersistentStorage", "getAllMatchingKeySubstring()");
        HashMap hashMap = new HashMap();
        Map map = this.LJ.getAll();
        if (map != null) {
            for (String string2 : map.keySet()) {
                if (!string2.contains((CharSequence)string)) continue;
                int n2 = (Integer)map.get((Object)string2);
                c.d("PersistentStorage", "getAllMatchingKeySubstring() keySubstring: " + string + " key: " + string2 + " value: " + n2);
                hashMap.put((Object)string2, (Object)n2);
            }
        }
        return hashMap;
    }

    public int get(String string, int n2) {
        int n3 = this.LJ.getInt(string, n2);
        c.d("PersistentStorage", "get() key: " + string + " defaultValue: " + n2 + " returning: " + n3);
        return n3;
    }

    public void removeAll() {
        c.d("PersistentStorage", "removeAll()");
        this.ig();
        Set set = this.LJ.getStringSet("poiEnterGeofenceSet", (Set)new HashSet());
        SharedPreferences.Editor editor = this.LJ.edit();
        editor.clear();
        editor.putStringSet("poiEnterGeofenceSet", set);
        editor.commit();
        c.d("PersistentStorage", "removeAll() - cleared");
        this.ig();
    }

    public void set(String string, int n2) {
        c.d("PersistentStorage", "set() key: " + string + " value: " + n2);
        SharedPreferences.Editor editor = this.LJ.edit();
        editor.putInt(string, n2);
        editor.commit();
    }
}

