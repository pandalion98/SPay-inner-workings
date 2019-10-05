/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sdi.a;

import android.content.Context;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.b.a.p;
import com.samsung.sensorframework.sdi.g.a;

public class b {
    protected com.samsung.sensorframework.sdi.c.c La;
    protected com.samsung.sensorframework.sdi.d.b Lb;
    protected long Ld;

    public b(Context context) {
        this.La = com.samsung.sensorframework.sdi.c.c.br(context);
        this.Lb = null;
        this.Ld = 1800000L;
    }

    protected long H(long l2) {
        return 2L * l2;
    }

    protected long I(long l2) {
        return l2 / 2L;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected long J(long l2) {
        if (l2 < 150000L) {
            return 150000L;
        }
        if (l2 <= 1200000L) return l2;
        return 1200000L;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void a(p p2, com.samsung.sensorframework.sdi.d.b b2) {
        long l2 = 1800000L;
        Log.d("DynamicDutyCycling", "Current sleep interval: " + this.Ld);
        if (!a.f(8, 20)) {
            Log.d("DynamicDutyCycling", "PoIProximityDataHolder: current time not in trigger time range");
            l2 = 3600000L;
        } else if (b2 != null) {
            Log.d("DynamicDutyCycling", "PoIProximityDataHolder: " + b2.toString());
            if (b2.hY() > 8000.0) {
                Log.d("DynamicDutyCycling", "Distance to closest PoI: Over POI_CACHE_DISTANCE_RADIUS");
            } else {
                Log.d("DynamicDutyCycling", "Distance to closest PoI: Less than POI_CACHE_DISTANCE_RADIUS");
                l2 = this.b(p2, b2);
            }
        } else {
            Log.d("DynamicDutyCycling", "PoIProximityDataHolder: null or empty");
        }
        this.Ld = l2;
        this.La.K(this.Ld);
        this.Lb = b2;
        Log.d("DynamicDutyCycling", "New sleep interval: " + this.Ld);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected long b(p p2, com.samsung.sensorframework.sdi.d.b b2) {
        long l2;
        Log.d("DynamicDutyCycling", "dynamicSensing()");
        if (c.b(p2)) {
            Log.d("DynamicDutyCycling", "dynamicSensing() location context has changed");
            if (b2 != null && this.Lb != null && b2.hY() < this.Lb.hY()) {
                Log.d("DynamicDutyCycling", "dynamicSensing() user is moving towards a PoI");
                l2 = this.I(this.Ld);
            } else {
                Log.d("DynamicDutyCycling", "dynamicSensing() user is moving away from a PoI");
                l2 = this.H(this.Ld);
            }
        } else {
            Log.d("DynamicDutyCycling", "dynamicSensing() location context has not changed");
            l2 = this.H(this.Ld);
        }
        long l3 = this.J(l2);
        Log.d("DynamicDutyCycling", "dynamicSensing() updated interval: " + l3);
        return l3;
    }
}

