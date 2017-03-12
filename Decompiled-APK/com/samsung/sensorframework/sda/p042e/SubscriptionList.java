package com.samsung.sensorframework.sda.p042e;

import android.util.SparseArray;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.SDAException;
import java.util.Random;

/* renamed from: com.samsung.sensorframework.sda.e.e */
public class SubscriptionList {
    private static String TAG;
    private final SparseArray<Subscription> KS;
    private final Random KT;

    static {
        TAG = "SubscriptionList";
    }

    public SubscriptionList() {
        this.KS = new SparseArray();
        this.KT = new Random();
    }

    public synchronized int m1646b(Subscription subscription) {
        int hI;
        if (subscription.hH()) {
            hI = hI();
            this.KS.append(hI, subscription);
            Log.m285d(TAG, "registerSubscription() new subscription created for task: " + subscription.hF().getSensorType() + " listener: " + subscription.hG());
            Log.m285d(TAG, "Subscription list size is: " + this.KS.size());
        } else {
            Log.m285d(TAG, "registerSubscription() subscription already exists for task: " + subscription.hF().getSensorType() + " listener: " + subscription.hG());
            int i = 0;
            while (i < this.KS.size()) {
                int keyAt = this.KS.keyAt(i);
                Subscription subscription2 = (Subscription) this.KS.get(keyAt);
                if (subscription2 == null || !subscription2.m1645a(subscription)) {
                    i++;
                } else {
                    hI = keyAt;
                }
            }
            throw new SDAException(8007, "Registered Subscription not found.");
        }
        return hI;
    }

    public synchronized Subscription av(int i) {
        Subscription subscription;
        subscription = (Subscription) this.KS.get(i);
        if (subscription == null) {
            Log.m285d(TAG, "removeSubscription() invalid subscription id: " + i);
            subscription = null;
        } else {
            Log.m285d(TAG, "removeSubscription() deleting subscription created for task: " + subscription.hF().getSensorType() + " listener: " + subscription.hG());
            this.KS.delete(i);
            Log.m285d(TAG, "Subscription list size is: " + this.KS.size());
        }
        return subscription;
    }

    private int hI() {
        int nextInt = this.KT.nextInt();
        int i = 0;
        while (this.KS.get(nextInt) != null) {
            if (i > LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                throw new SDAException(8007, "Listener map >1000 key conflicts.");
            }
            nextInt = this.KT.nextInt();
            i++;
        }
        return nextInt;
    }
}
