/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  java.io.BufferedReader
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Set
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.Context;
import android.content.res.AssetManager;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.b.i;
import com.samsung.sensorframework.sda.c.b;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class h
extends b {
    private final HashMap<String, ArrayList<String>> Jh = this.gX();

    public h(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    private ArrayList<String> ch(String string) {
        ArrayList arrayList = new ArrayList();
        for (String string2 : this.Jh.keySet()) {
            if (!string.matches(string2)) continue;
            arrayList.addAll((Collection)this.Jh.get((Object)string2));
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private HashMap<String, ArrayList<String>> gX() {
        HashMap hashMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader((Reader)new InputStreamReader(this.Jg.getAssets().open("UNIMPLEMENTED")));
            do {
                ArrayList arrayList;
                String string;
                String string2;
                String[] arrstring;
                if ((string2 = bufferedReader.readLine()) != null) {
                    arrstring = string2.split(",");
                    string = arrstring[0];
                    arrayList = new ArrayList();
                } else {
                    bufferedReader.close();
                    return hashMap;
                }
                for (int i2 = 1; i2 < arrstring.length; ++i2) {
                    arrayList.add((Object)arrstring[1]);
                }
                hashMap.put((Object)string, (Object)arrayList);
            } while (true);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            hashMap.clear();
            return hashMap;
        }
    }

    public i a(long l2, c c2, String string, String string2, String string3, String string4) {
        i i2 = new i(l2, c2);
        String[] arrstring = string.split(" ");
        if (this.Je) {
            i2.am(arrstring.length);
            i2.al(string.length());
            i2.setAddress(this.cd(string2));
            i2.cb(string3);
            i2.bZ(this.ce(string));
            i2.ca(string4);
        }
        if (this.Jf) {
            int n2 = arrstring.length;
            for (int i3 = 0; i3 < n2; ++i3) {
                Iterator iterator = this.ch(arrstring[i3]).iterator();
                while (iterator.hasNext()) {
                    i2.addCategory((String)iterator.next());
                }
            }
        }
        return i2;
    }
}

