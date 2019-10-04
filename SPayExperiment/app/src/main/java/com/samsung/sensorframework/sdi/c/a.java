/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sdi.c;

import android.content.Context;
import com.samsung.android.spayfw.b.c;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sdi.c.b;
import com.samsung.sensorframework.sdi.exception.SDIException;
import com.samsung.sensorframework.sdm.datahandler.except.DataHandlerException;
import java.util.HashMap;

public class a {
    private static final Object Ho = new Object();
    private static a Lf;
    private com.samsung.sensorframework.sda.a HQ;
    private com.samsung.sensorframework.sda.b Kl;
    private HashMap<Integer, Integer> Lg;
    private final int[] Lh = new int[]{5002, 5011, 5004, 5038};
    private final int[] Li = new int[]{5002, 5011};
    private final int[] Lj = new int[]{5004, 5038};
    private Context context;

    private a(Context context) {
        if (context == null) {
            throw new SDIException(9000, "context can not be null");
        }
        this.context = context;
        this.HQ = com.samsung.sensorframework.sda.a.aN(context);
        if (this.HQ == null) {
            throw new SDIException(9001, "sensorManager cannot be null.");
        }
        c.d("SDISensorHandler", "instance created.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(int[] arrn) {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "enableSensors()");
            if (this.c(arrn)) {
                c.e("SDISensorHandler", "enableSensors() already enabled.");
            } else {
                this.hO();
                for (int n2 : arrn) {
                    if (this.ax(n2)) continue;
                    this.aw(n2);
                    this.ay(n2);
                }
            }
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void aw(int var1_1) {
        var4_2 = this;
        // MONITORENTER : var4_2
        if (var1_1 != 5002) ** GOTO lbl7
        try {
            this.HQ.a(5002, "BATTERY_INTENT_FILTERS", "BATTERY_INTENT_FILTER_LOW_OK");
            return;
lbl7: // 1 sources:
            if (var1_1 == 5004) {
                this.HQ.a(5004, "LOCATION_ACCURACY", "LOCATION_ACCURACY_COARSE");
                this.HQ.a(5004, "SENSE_WINDOW_LENGTH_MILLIS", 20000L);
                this.c(5004, 1800000L);
                return;
            }
            if (var1_1 != 5010) {
                // MONITOREXIT : var4_2
                return;
            }
            this.c(5010, 1200000L);
            return;
        }
        catch (SDAException var3_3) {
            var3_3.printStackTrace();
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void ay(int var1_1) {
        block10 : {
            var8_2 = this;
            // MONITORENTER : var8_2
            c.d("SDISensorHandler", "subscribeToSensor()");
            if (this.Lg == null) {
                this.Lg = new HashMap();
            }
            if ((var3_3 = this.Kl) != null) break block10;
            try {
                this.Kl = new b(this.context);
            }
            catch (SDAException var7_5) {}
            ** GOTO lbl-1000
            catch (DataHandlerException var7_7) {}
lbl-1000: // 2 sources:
            {
                var7_6.printStackTrace();
            }
        }
        try {
            if (!this.ax(var1_1)) {
                var5_4 = this.HQ.a(var1_1, this.Kl);
                this.Lg.put((Object)var1_1, (Object)var5_4);
                c.d("SDISensorHandler", "subscribeToSensor() subscribed to sensor: " + com.samsung.sensorframework.sda.d.c.ap(var1_1));
            }
            // MONITOREXIT : var8_2
            return;
        }
        catch (SDAException var4_8) {
            var4_8.printStackTrace();
            return;
        }
    }

    private void az(int n2) {
        a a2 = this;
        synchronized (a2) {
            boolean bl = this.ax(n2);
            if (bl) {
                this.HQ.ac((Integer)this.Lg.get((Object)n2));
                this.Lg.remove((Object)n2);
                c.d("SDISensorHandler", "unsubscribeFromSensor() unsubscribed from sensor: " + com.samsung.sensorframework.sda.d.c.ap(n2));
            }
            return;
        }
    }

    private void b(int[] arrn) {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "disableSensors()");
            this.d(arrn);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static a bq(Context context) {
        if (Lf == null) {
            Object object;
            Object object2 = object = Ho;
            synchronized (object2) {
                if (Lf == null) {
                    Lf = new a(context);
                }
            }
        }
        return Lf;
    }

    private void d(int[] arrn) {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "unsubscribeFromSensors()");
            int n2 = arrn.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                this.az(arrn[i2]);
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void hO() {
        a a2 = this;
        synchronized (a2) {
            try {
                c.d("SDISensorHandler", "setGlobalConfig()");
                this.HQ.b("INTENT_BROADCASTER_PERMISSION", "com.samsung.android.spayfw.permission.ACCESS_PF");
                do {
                    return;
                    break;
                } while (true);
            }
            catch (SDAException sDAException) {
                sDAException.printStackTrace();
                return;
            }
            finally {
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean ax(int n2) {
        a a2 = this;
        synchronized (a2) {
            block4 : {
                if (this.Lg == null || this.Lg.size() <= 0 || !this.Lg.containsKey((Object)n2)) break block4;
                c.d("SDISensorHandler", "isSubscribedToSensor() true");
                return true;
            }
            c.d("SDISensorHandler", "isSubscribedToSensor() false");
            return false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void c(int n2, long l2) {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "setDutyCyclingInterval(), interval: " + l2 + " Sensor: " + com.samsung.sensorframework.sda.f.a.ap(n2));
            try {
                long l3 = l2 + (long)com.samsung.sensorframework.sdi.g.a.aA(1000);
                c.d("SDISensorHandler", "setDutyCyclingInterval(), updated interval after adding additional random time: " + l3);
                this.HQ.a(n2, "POST_SENSE_SLEEP_LENGTH_MILLIS", l3);
                do {
                    return;
                    break;
                } while (true);
            }
            catch (Exception exception) {
                exception.printStackTrace();
                try {
                    this.HQ.a(n2, "POST_SENSE_SLEEP_LENGTH_MILLIS", 1800000L);
                    return;
                }
                catch (Exception exception2) {
                    exception2.printStackTrace();
                    return;
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean c(int[] arrn) {
        a a2 = this;
        synchronized (a2) {
            int n2 = arrn.length;
            int n3 = 0;
            while (n3 < n2) {
                block5 : {
                    boolean bl = this.ax(arrn[n3]);
                    boolean bl2 = false;
                    if (bl) break block5;
                    return bl2;
                }
                ++n3;
            }
            return true;
        }
    }

    public void hK() {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "enableRadioSensors()");
            this.a(this.Lj);
            return;
        }
    }

    public void hL() {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "enableBatteryAndConnectionSensors()");
            this.a(this.Li);
            return;
        }
    }

    public void hM() {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "disableAllSensors()");
            this.b(this.Lh);
            if (this.Lg != null) {
                this.Lg.clear();
                this.Lg = null;
            }
            if (this.Kl != null) {
                if (this.Kl instanceof b) {
                    ((b)this.Kl).hP();
                }
                this.Kl = null;
            }
            return;
        }
    }

    public void hN() {
        a a2 = this;
        synchronized (a2) {
            c.d("SDISensorHandler", "disableRadioSensors()");
            this.d(this.Lj);
            return;
        }
    }
}

