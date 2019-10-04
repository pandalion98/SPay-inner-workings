/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.util.Log
 *  com.sec.enterprise.knox.ccm.IClientCertificateManager
 *  com.sec.enterprise.knox.ccm.IClientCertificateManager$Stub
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.IllegalAccessException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 */
package com.samsung.android.spayfw.c;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.sec.enterprise.knox.ccm.IClientCertificateManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class a {
    private static IClientCertificateManager Bl;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static final void fg() {
        Log.e((String)"CcmForSdl", (String)"start ccmSelfCheck");
        try {
            Class class_ = Class.forName((String)"android.os.ServiceManager");
            Bl = IClientCertificateManager.Stub.asInterface((IBinder)((IBinder)class_.getMethod("getService", new Class[]{String.class}).invoke((Object)class_, new Object[]{"knox_ccm_policy"})));
            Log.d((String)"CcmForSdl", (String)"success to bind CCM service");
        }
        catch (ClassNotFoundException classNotFoundException) {
            Log.d((String)"CcmForSdl", (String)("ClassNotFoundException : " + classNotFoundException.getMessage()));
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.d((String)"CcmForSdl", (String)("NoSuchMethodException : " + noSuchMethodException.getMessage()));
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.d((String)"CcmForSdl", (String)("IllegalAccessException : " + illegalAccessException.getMessage()));
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.d((String)"CcmForSdl", (String)("InvocationTargetException : " + invocationTargetException.getMessage()));
        }
        if (Bl == null) return;
        try {
            boolean bl = Bl.setDefaultCCMProfile();
            Log.d((String)"CcmForSdl", (String)("setDefaultCCMProfile : " + bl));
            return;
        }
        catch (RemoteException remoteException) {
            Log.e((String)"CcmForSdl", (String)remoteException.getMessage(), (Throwable)remoteException);
            Log.d((String)"CcmForSdl", (String)"ccmService RemoteException");
            return;
        }
    }
}

