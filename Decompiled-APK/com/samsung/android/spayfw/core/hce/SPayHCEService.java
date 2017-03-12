package com.samsung.android.spayfw.core.hce;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.RemoteException;
import com.samsung.android.spayfw.core.ApduHelper;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.p005a.IPaymentProcessor;
import com.samsung.android.spayfw.core.p005a.PaymentProcessor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;

public class SPayHCEService extends HostApduService {
    public static final boolean DEBUG;
    private static SPayHCEService kl;

    static {
        DEBUG = Utils.DEBUG;
    }

    private IPaymentProcessor aT() {
        return PaymentProcessor.m470q(getApplicationContext());
    }

    public void onCreate() {
        try {
            Log.m285d("SPayHCEService", "Before On create");
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("SPayHCEService", "HCE service: On create :TAException");
                return;
            }
            super.onCreate();
            Log.m285d("SPayHCEService", "HCE service is on");
            kl = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] processCommandApdu(byte[] bArr, Bundle bundle) {
        long currentTimeMillis = System.currentTimeMillis();
        if (DEBUG) {
            Log.m287i("SPayHCEService", "processCommandApdu(): time= " + currentTimeMillis);
        } else {
            Log.m287i("SPayHCEService", "HCE: processCommandApdu()");
        }
        if (DEBUG) {
            Log.m285d("APDU_LOG", ApduHelper.m565a(0, bArr, 0));
        }
        byte[] bArr2 = ApduHelper.iR;
        try {
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("SPayHCEService", "HCE service: processCommandApdu :TAException");
                return ApduHelper.iR;
            }
            bArr2 = aT().processApdu(bArr, bundle);
            if (!DEBUG) {
                return bArr2;
            }
            Log.m285d("APDU_LOG", ApduHelper.m565a(1, bArr2, System.currentTimeMillis() - currentTimeMillis));
            return bArr2;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onDeactivated(int i) {
        if (DEBUG) {
            Log.m285d("SPayHCEService", "onDeactivated(): time= " + System.currentTimeMillis());
        } else {
            Log.m287i("SPayHCEService", "onDeactivated()");
        }
        try {
            if (PaymentFrameworkApp.ay() != 0) {
                Log.m286e("SPayHCEService", "HCE service:onDeactivated :TAException");
            } else {
                aT().m422u(i);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(SPayHCEService.class);
    }

    public static final void disable() {
        PaymentFrameworkApp.m317a(SPayHCEService.class);
    }
}
