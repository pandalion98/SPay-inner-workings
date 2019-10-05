/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.nfc.cardemulation.HostApduService
 *  android.os.Bundle
 *  android.os.RemoteException
 *  java.lang.Exception
 *  java.lang.System
 */
package com.samsung.android.spayfw.core.hce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.RemoteException;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a.n;
import com.samsung.android.spayfw.core.b;
import com.samsung.android.spayfw.utils.h;

public class SPayHCEService
extends HostApduService {
    public static final boolean DEBUG = h.DEBUG;
    private static SPayHCEService kl;

    private com.samsung.android.spayfw.core.a.h aT() {
        return n.q(this.getApplicationContext());
    }

    public static final void disable() {
        PaymentFrameworkApp.a(SPayHCEService.class);
    }

    public static final void enable() {
        PaymentFrameworkApp.b(SPayHCEService.class);
    }

    public void onCreate() {
        try {
            Log.d("SPayHCEService", "Before On create");
            if (PaymentFrameworkApp.ay() != 0) {
                Log.e("SPayHCEService", "HCE service: On create :TAException");
                return;
            }
            super.onCreate();
            Log.d("SPayHCEService", "HCE service is on");
            kl = this;
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onDeactivated(int n2) {
        if (DEBUG) {
            Log.d("SPayHCEService", "onDeactivated(): time= " + System.currentTimeMillis());
        } else {
            Log.i("SPayHCEService", "onDeactivated()");
        }
        try {
            if (PaymentFrameworkApp.ay() != 0) {
                Log.e("SPayHCEService", "HCE service:onDeactivated :TAException");
                return;
            }
            this.aT().u(n2);
            return;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] processCommandApdu(byte[] arrby, Bundle bundle) {
        long l2 = System.currentTimeMillis();
        if (DEBUG) {
            Log.i("SPayHCEService", "processCommandApdu(): time= " + l2);
        } else {
            Log.i("SPayHCEService", "HCE: processCommandApdu()");
        }
        if (DEBUG) {
            Log.d("APDU_LOG", b.a(0, arrby, 0L));
        }
        byte[] arrby2 = b.iR;
        try {
            if (PaymentFrameworkApp.ay() != 0) {
                Log.e("SPayHCEService", "HCE service: processCommandApdu :TAException");
                return b.iR;
            }
            byte[] arrby3 = this.aT().processApdu(arrby, bundle);
            arrby2 = arrby3;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
        if (!DEBUG) {
            return arrby2;
        }
        Log.d("APDU_LOG", b.a(1, arrby2, System.currentTimeMillis() - l2));
        return arrby2;
    }
}

