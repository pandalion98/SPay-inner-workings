/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.ActivityThread
 *  android.content.Context
 *  android.content.pm.IPackageManager
 *  android.nfc.INfcAdapter
 *  android.nfc.INfcAdapter$Stub
 *  android.nfc.NfcAdapter
 *  android.os.IBinder
 *  android.os.RemoteException
 *  android.os.ServiceManager
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.NoSuchMethodError
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.c;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.nfc.INfcAdapter;
import android.nfc.NfcAdapter;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class c {
    private static c Bm = null;
    private static NfcAdapter Bn = null;
    private static boolean sIsInitialized = false;
    private static INfcAdapter sService;
    private Context mContext;

    c(Context context) {
        this.mContext = context;
        Bn = NfcAdapter.getDefaultAdapter((Context)this.mContext);
        c.fm();
    }

    public static c R(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (Bm == null) {
                Bm = new c(context);
            }
            Bn = NfcAdapter.getDefaultAdapter((Context)context);
            c.fm();
            c c2 = Bm;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return c2;
        }
    }

    private static void T(int n2) {
        if (sService != null) {
            try {
                sService.setListenMode2(sService.asBinder(), n2);
                return;
            }
            catch (RemoteException remoteException) {
                Log.d((String)"SdlNfcController", (String)"cannot connect to NFC service");
                Log.e((String)"SdlNfcController", (String)remoteException.getMessage(), (Throwable)remoteException);
                return;
            }
            catch (NoSuchMethodError noSuchMethodError) {
                Log.e((String)"SdlNfcController", (String)noSuchMethodError.getMessage(), (Throwable)noSuchMethodError);
                return;
            }
        }
        Log.d((String)"SdlNfcController", (String)"cannot set nfc listen mode");
    }

    public static void fi() {
        if (c.isEnabled()) {
            c.fm();
            c.T(255);
            Log.d((String)"SdlNfcController", (String)"enable nfc");
        }
    }

    public static void fj() {
        if (c.isEnabled()) {
            c.fm();
            c.T(0);
            Log.d((String)"SdlNfcController", (String)"disable nfc");
        }
    }

    private static boolean fk() {
        IPackageManager iPackageManager = ActivityThread.getPackageManager();
        if (iPackageManager == null) {
            Log.e((String)"SdlNfcController", (String)"Cannot get package manager, assuming no NFC feature");
            return false;
        }
        try {
            boolean bl = iPackageManager.hasSystemFeature("android.hardware.nfc");
            return bl;
        }
        catch (RemoteException remoteException) {
            Log.e((String)"SdlNfcController", (String)"Package manager query failed, assuming no NFC feature", (Throwable)remoteException);
            return false;
        }
    }

    private static INfcAdapter fl() {
        IBinder iBinder = ServiceManager.getService((String)"nfc");
        if (iBinder == null) {
            return null;
        }
        return INfcAdapter.Stub.asInterface((IBinder)iBinder);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void fm() {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (!sIsInitialized) {
                if (!c.fk()) {
                    Log.e((String)"SdlNfcController", (String)"this device does not have NFC support");
                } else {
                    sService = c.fl();
                    if (sService == null) {
                        Log.e((String)"SdlNfcController", (String)"could not retrieve NFC service");
                    } else {
                        sIsInitialized = true;
                        Log.d((String)"SdlNfcController", (String)"get nfc service successfully");
                    }
                }
            }
            // ** MonitorExit[var4] (shouldn't be in output)
            return;
        }
    }

    private static boolean isEnabled() {
        if (Bn.isEnabled()) {
            Log.d((String)"SdlNfcController", (String)"NFC Status : ON");
            return true;
        }
        Log.d((String)"SdlNfcController", (String)"NFC Status : OFF");
        return false;
    }
}

