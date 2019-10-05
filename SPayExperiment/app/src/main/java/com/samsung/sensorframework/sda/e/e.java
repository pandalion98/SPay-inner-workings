/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Random
 */
package com.samsung.sensorframework.sda.e;

import android.util.SparseArray;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.SDAException;

import java.util.Random;

public class e {
    private static String TAG = "SubscriptionList";
    private final SparseArray<d> KS = new SparseArray();
    private final Random KT = new Random();

    private int hI() {
        int n2 = this.KT.nextInt();
        int n3 = 0;
        while (this.KS.get(n2) != null) {
            if (n3 > 1000) {
                throw new SDAException(8007, "Listener map >1000 key conflicts.");
            }
            n2 = this.KT.nextInt();
            ++n3;
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public d av(int n2) {
        e e2 = this;
        synchronized (e2) {
            d d2;
            block7 : {
                d2 = (d)this.KS.get(n2);
                if (d2 != null) break block7;
                Log.d(TAG, "removeSubscription() invalid subscription id: " + n2);
                return null;
            }
            Log.d(TAG, "removeSubscription() deleting subscription created for task: " + d2.hF().getSensorType() + " listener: " + d2.hG());
            this.KS.delete(n2);
            Log.d(TAG, "Subscription list size is: " + this.KS.size());
            return d2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int b(d d2) {
        e e2 = this;
        synchronized (e2) {
            if (!d2.hH()) {
                Log.d(TAG, "registerSubscription() subscription already exists for task: " + d2.hF().getSensorType() + " listener: " + d2.hG());
                int n2 = 0;
                do {
                    boolean bl;
                    if (n2 >= this.KS.size()) {
                        throw new SDAException(8007, "Registered Subscription not found.");
                    }
                    int n3 = this.KS.keyAt(n2);
                    d d3 = (d)this.KS.get(n3);
                    if (d3 != null && (bl = d3.a(d2))) {
                        return n3;
                    }
                    ++n2;
                } while (true);
            }
            int n4 = this.hI();
            this.KS.append(n4, (Object)d2);
            Log.d(TAG, "registerSubscription() new subscription created for task: " + d2.hF().getSensorType() + " listener: " + d2.hG());
            Log.d(TAG, "Subscription list size is: " + this.KS.size());
            return n4;
        }
    }
}

