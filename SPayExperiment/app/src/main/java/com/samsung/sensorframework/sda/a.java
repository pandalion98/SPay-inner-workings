/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.SparseArray
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda;

import android.content.Context;
import android.util.SparseArray;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.a.b;
import com.samsung.sensorframework.sda.d.c;
import com.samsung.sensorframework.sda.e.d;
import com.samsung.sensorframework.sda.e.e;

public class a {
    private static a HQ;
    private static final Object lock;
    private final Context HR;
    private final SparseArray<com.samsung.sensorframework.sda.e.a> HS;
    private final e HT;
    private final b HU;

    static {
        lock = new Object();
    }

    private a(Context context) {
        if (context == null) {
            throw new SDAException(8012, "Invalid parameter, context object passed is null");
        }
        this.HR = context;
        this.HS = new SparseArray();
        this.HT = new e();
        this.HU = b.gO();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static a aN(Context context) {
        if (HQ == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (HQ == null) {
                    HQ = new a(context);
                }
            }
        }
        return HQ;
    }

    private com.samsung.sensorframework.sda.e.a ab(int n2) {
        com.samsung.sensorframework.sda.d.b b2 = c.a(n2, this.HR);
        if (c.an(b2.getSensorType())) {
            return new com.samsung.sensorframework.sda.e.b(b2);
        }
        return new com.samsung.sensorframework.sda.e.c(b2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(int n2, com.samsung.sensorframework.sda.b b2) {
        a a2 = this;
        synchronized (a2) {
            try {
                com.samsung.sensorframework.sda.e.a a3 = (com.samsung.sensorframework.sda.e.a)((Object)this.HS.get(n2));
                if (a3 == null) {
                    a3 = this.ab(n2);
                    this.HS.put(n2, (Object)a3);
                }
                com.samsung.android.spayfw.b.c.d("SFSensorManager", "subscribeToSensorData() subscribing listener to sensor: " + c.ap(n2));
                d d2 = new d(a3, b2);
                return this.HT.b(d2);
            }
            catch (Exception exception) {
                throw new SDAException(8001, "Invalid sensor type: " + c.ap(n2) + " (Check permissions? Check hardware availability?)" + " Error message: " + exception.getMessage());
            }
        }
    }

    public void a(int n2, String string, Object object) {
        c.a(n2, this.HR).c(string, object);
        com.samsung.android.spayfw.b.c.d("SFSensorManager", "sensor config updated, sensor: " + c.ap(n2) + " config: " + string + " value: " + object.toString());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void ac(int n2) {
        a a2 = this;
        synchronized (a2) {
            d d2 = this.HT.av(n2);
            if (d2 == null) throw new SDAException(8007, "Un-Mapped subscription id: " + n2);
            com.samsung.sensorframework.sda.e.a a3 = d2.hF();
            d2.unregister();
            if (a3.isStopped()) {
                this.HS.remove(a3.getSensorType());
            }
            com.samsung.android.spayfw.b.c.d("SFSensorManager", "unsubscribeFromSensorData() unsubscribing from sensor: " + c.ap(a3.getSensorType()));
            return;
        }
    }

    public void b(String string, Object object) {
        this.HU.setParameter(string, object);
    }
}

