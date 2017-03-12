package com.samsung.android.spayfw.payprovider;

import com.samsung.android.spayfw.p002b.Log;
import java.util.HashMap;

/* renamed from: com.samsung.android.spayfw.payprovider.g */
public class TACounter {
    private static final HashMap<String, TACounter> pa;

    /* renamed from: com.samsung.android.spayfw.payprovider.g.a */
    public class TACounter {
        private int count;
        final /* synthetic */ TACounter pb;

        public TACounter(TACounter tACounter) {
            this.pb = tACounter;
            reset();
        }

        public int co() {
            this.count++;
            Log.m285d("TACounter", "count increase to" + this.count);
            return this.count;
        }

        public int cp() {
            this.count--;
            Log.m285d("TACounter", "count decrease to" + this.count);
            return this.count;
        }

        public void reset() {
            this.count = 0;
        }

        public int getCount() {
            return this.count;
        }
    }

    static {
        pa = new HashMap();
    }

    public TACounter at(String str) {
        if (!pa.containsKey(str)) {
            pa.put(str, new TACounter(this));
        }
        return (TACounter) pa.get(str);
    }
}
