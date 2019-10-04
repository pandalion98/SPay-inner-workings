/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.ref.SoftReference
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.LinkedList
 *  java.util.List
 */
package com.samsung.android.spayfw.fraud;

import com.samsung.android.spayfw.fraud.b.a;
import com.samsung.android.spayfw.fraud.d;
import com.samsung.android.spayfw.fraud.g;
import com.samsung.android.spayfw.fraud.h;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class f {
    static List<String> nm = Arrays.asList((Object[])new String[]{"enrollcard.simple-risk-score-1.model"});
    List<String> nn;
    HashMap<String, SoftReference<h>> no = new HashMap();
    private String np;

    f(String string) {
        this.np = string;
        this.nn = g.ac(this.np);
    }

    private h Z(String string) {
        h h2 = a.a(g.ab(string));
        this.no.put((Object)string, (Object)new SoftReference((Object)h2));
        return h2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private h aa(String string) {
        if (!this.no.containsKey((Object)string)) {
            return this.Z(string);
        }
        h h2 = (h)((SoftReference)this.no.get((Object)string)).get();
        if (h2 != null) return h2;
        return this.Z(string);
    }

    public List<d> bx() {
        LinkedList linkedList = new LinkedList();
        Iterator iterator = this.nn.iterator();
        while (iterator.hasNext()) {
            linkedList.add((Object)this.aa((String)iterator.next()).bA());
        }
        if (linkedList.isEmpty()) {
            linkedList.add((Object)a.bJ().bA());
        }
        return linkedList;
    }
}

