package com.samsung.sensorframework.sdi.p044a;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p033b.p034a.LocationData;
import com.samsung.sensorframework.sdi.p046c.SensingController;
import com.samsung.sensorframework.sdi.p047d.PoIProximityDataHolder;
import com.samsung.sensorframework.sdi.p050g.SFUtils;

/* renamed from: com.samsung.sensorframework.sdi.a.b */
public class DynamicDutyCycling {
    protected SensingController La;
    protected PoIProximityDataHolder Lb;
    protected long Ld;

    public DynamicDutyCycling(Context context) {
        this.La = SensingController.br(context);
        this.Lb = null;
        this.Ld = 1800000;
    }

    public void m1656a(LocationData locationData, PoIProximityDataHolder poIProximityDataHolder) {
        long j = 1800000;
        Log.m285d("DynamicDutyCycling", "Current sleep interval: " + this.Ld);
        if (!SFUtils.m1708f(8, 20)) {
            Log.m285d("DynamicDutyCycling", "PoIProximityDataHolder: current time not in trigger time range");
            j = 3600000;
        } else if (poIProximityDataHolder != null) {
            Log.m285d("DynamicDutyCycling", "PoIProximityDataHolder: " + poIProximityDataHolder.toString());
            if (poIProximityDataHolder.hY() > 8000.0d) {
                Log.m285d("DynamicDutyCycling", "Distance to closest PoI: Over POI_CACHE_DISTANCE_RADIUS");
            } else {
                Log.m285d("DynamicDutyCycling", "Distance to closest PoI: Less than POI_CACHE_DISTANCE_RADIUS");
                j = m1657b(locationData, poIProximityDataHolder);
            }
        } else {
            Log.m285d("DynamicDutyCycling", "PoIProximityDataHolder: null or empty");
        }
        this.Ld = j;
        this.La.m1686K(this.Ld);
        this.Lb = poIProximityDataHolder;
        Log.m285d("DynamicDutyCycling", "New sleep interval: " + this.Ld);
    }

    protected long m1657b(LocationData locationData, PoIProximityDataHolder poIProximityDataHolder) {
        long H;
        Log.m285d("DynamicDutyCycling", "dynamicSensing()");
        if (LocationContextClassifier.m1659b(locationData)) {
            Log.m285d("DynamicDutyCycling", "dynamicSensing() location context has changed");
            if (poIProximityDataHolder == null || this.Lb == null || poIProximityDataHolder.hY() >= this.Lb.hY()) {
                Log.m285d("DynamicDutyCycling", "dynamicSensing() user is moving away from a PoI");
                H = m1653H(this.Ld);
            } else {
                Log.m285d("DynamicDutyCycling", "dynamicSensing() user is moving towards a PoI");
                H = m1654I(this.Ld);
            }
        } else {
            Log.m285d("DynamicDutyCycling", "dynamicSensing() location context has not changed");
            H = m1653H(this.Ld);
        }
        H = m1655J(H);
        Log.m285d("DynamicDutyCycling", "dynamicSensing() updated interval: " + H);
        return H;
    }

    protected long m1653H(long j) {
        return 2 * j;
    }

    protected long m1654I(long j) {
        return j / 2;
    }

    protected long m1655J(long j) {
        if (j < 150000) {
            return 150000;
        }
        if (j > 1200000) {
            return 1200000;
        }
        return j;
    }
}
