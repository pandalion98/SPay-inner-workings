/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Set
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.a;
import com.samsung.sensorframework.sda.b.a.b;
import com.samsung.sensorframework.sda.b.a.j;
import com.samsung.sensorframework.sda.b.a.k;
import com.samsung.sensorframework.sda.b.a.l;
import com.samsung.sensorframework.sda.c.a.h;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class g
extends h {
    public g(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public b a(long l2, int n2, ArrayList<HashMap<String, String>> arrayList, c c2) {
        k k2 = (k)super.a(l2, n2, arrayList, c2);
        HashMap hashMap = new HashMap();
        Iterator iterator = k2.gU().iterator();
        do {
            String string;
            String string2;
            String string3;
            j j2;
            if (!iterator.hasNext()) {
                k2.a((HashMap<String, j>)hashMap);
                return k2;
            }
            a a2 = (a)iterator.next();
            String string4 = a2.get("display_name");
            if (string4 == null || (string = a2.get("mimetype")) == null) continue;
            if (string.equals((Object)"vnd.android.cursor.item/phone_v2")) {
                String string5 = a2.get("data1");
                string3 = null;
                string2 = string5;
            } else if (string.equals((Object)"vnd.android.cursor.item/email_v2")) {
                string3 = a2.get("data1");
                string2 = null;
            } else {
                string3 = null;
                string2 = null;
            }
            if (string2 == null && string3 == null) continue;
            if (hashMap.containsKey((Object)string4)) {
                j2 = (j)hashMap.get((Object)string4);
            } else {
                j2 = new j(string4);
                hashMap.put((Object)string4, (Object)j2);
            }
            if (string2 != null) {
                j2.setPhoneNumber(string2);
            }
            if (string3 == null) continue;
            j2.setEmail(string3);
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected a b(HashMap<String, String> hashMap) {
        try {
            l l2 = new l();
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String string = (String)iterator.next();
                String string2 = (String)hashMap.get((Object)string);
                if (string2 == null || string2.length() == 0) {
                    string2 = "";
                }
                if (string.equals((Object)"data1") || string.equals((Object)"data1")) {
                    String string3 = l2.get("mimetype");
                    string2 = string3 != null && string3.equals((Object)"vnd.android.cursor.item/phone_v2") ? this.cd(string2) : this.ce(string2);
                } else if (string.equals((Object)"display_name")) {
                    String string4;
                    string2 = string4 = this.ce(string2);
                }
                l2.set(string, string2);
                iterator.remove();
            }
            return l2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected b b(long l2, c c2) {
        return new k(l2, c2);
    }
}

