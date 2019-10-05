/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.payprovider;

import com.samsung.android.spayfw.b.Log;

import java.util.HashMap;

public class g {
    private static final HashMap<String, a> pa = new HashMap();

    public a at(String string) {
        if (!pa.containsKey((Object)string)) {
            pa.put((Object)string, (Object)new a());
        }
        return (a)pa.get((Object)string);
    }

    public class a {
        private int count;

        public a() {
            this.reset();
        }

        public int co() {
            this.count = 1 + this.count;
            Log.d("TACounter", "count increase to" + this.count);
            return this.count;
        }

        public int cp() {
            this.count = -1 + this.count;
            Log.d("TACounter", "count decrease to" + this.count);
            return this.count;
        }

        public int getCount() {
            return this.count;
        }

        public void reset() {
            this.count = 0;
        }
    }

}

