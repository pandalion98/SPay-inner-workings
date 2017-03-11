package com.samsung.android.spayfw.p003c;

import android.os.IBinder;
import android.util.Log;
import com.sec.enterprise.knox.ccm.IClientCertificateManager;
import com.sec.enterprise.knox.ccm.IClientCertificateManager.Stub;
import java.lang.reflect.InvocationTargetException;

/* renamed from: com.samsung.android.spayfw.c.a */
public class SdlClientCertificateManager {
    private static IClientCertificateManager Bl;

    public static final void fg() {
        Log.e("CcmForSdl", "start ccmSelfCheck");
        try {
            Class cls = Class.forName("android.os.ServiceManager");
            Bl = Stub.asInterface((IBinder) cls.getMethod("getService", new Class[]{String.class}).invoke(cls, new Object[]{"knox_ccm_policy"}));
            Log.d("CcmForSdl", "success to bind CCM service");
        } catch (ClassNotFoundException e) {
            Log.d("CcmForSdl", "ClassNotFoundException : " + e.getMessage());
        } catch (NoSuchMethodException e2) {
            Log.d("CcmForSdl", "NoSuchMethodException : " + e2.getMessage());
        } catch (IllegalAccessException e3) {
            Log.d("CcmForSdl", "IllegalAccessException : " + e3.getMessage());
        } catch (InvocationTargetException e4) {
            Log.d("CcmForSdl", "InvocationTargetException : " + e4.getMessage());
        }
        if (Bl != null) {
            try {
                Log.d("CcmForSdl", "setDefaultCCMProfile : " + Bl.setDefaultCCMProfile());
            } catch (Throwable e5) {
                Log.e("CcmForSdl", e5.getMessage(), e5);
                Log.d("CcmForSdl", "ccmService RemoteException");
            }
        }
    }
}
