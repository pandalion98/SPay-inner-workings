package com.samsung.android.spayfw.p003c;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.nfc.INfcAdapter;
import android.nfc.INfcAdapter.Stub;
import android.nfc.NfcAdapter;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Log;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.c.c */
public class SdlNfcController {
    private static SdlNfcController Bm;
    private static NfcAdapter Bn;
    private static boolean sIsInitialized;
    private static INfcAdapter sService;
    private Context mContext;

    static {
        Bm = null;
        Bn = null;
        sIsInitialized = false;
    }

    public static synchronized SdlNfcController m301R(Context context) {
        SdlNfcController sdlNfcController;
        synchronized (SdlNfcController.class) {
            if (Bm == null) {
                Bm = new SdlNfcController(context);
            }
            Bn = NfcAdapter.getDefaultAdapter(context);
            SdlNfcController.fm();
            sdlNfcController = Bm;
        }
        return sdlNfcController;
    }

    SdlNfcController(Context context) {
        this.mContext = context;
        Bn = NfcAdapter.getDefaultAdapter(this.mContext);
        SdlNfcController.fm();
    }

    public static void fi() {
        if (SdlNfcController.isEnabled()) {
            SdlNfcController.fm();
            SdlNfcController.m302T(GF2Field.MASK);
            Log.d("SdlNfcController", "enable nfc");
        }
    }

    public static void fj() {
        if (SdlNfcController.isEnabled()) {
            SdlNfcController.fm();
            SdlNfcController.m302T(0);
            Log.d("SdlNfcController", "disable nfc");
        }
    }

    private static boolean isEnabled() {
        if (Bn.isEnabled()) {
            Log.d("SdlNfcController", "NFC Status : ON");
            return true;
        }
        Log.d("SdlNfcController", "NFC Status : OFF");
        return false;
    }

    private static void m302T(int i) {
        if (sService != null) {
            try {
                sService.setListenMode2(sService.asBinder(), i);
                return;
            } catch (Throwable e) {
                Log.d("SdlNfcController", "cannot connect to NFC service");
                Log.e("SdlNfcController", e.getMessage(), e);
                return;
            } catch (Throwable e2) {
                Log.e("SdlNfcController", e2.getMessage(), e2);
                return;
            }
        }
        Log.d("SdlNfcController", "cannot set nfc listen mode");
    }

    private static boolean fk() {
        boolean z = false;
        IPackageManager packageManager = ActivityThread.getPackageManager();
        if (packageManager == null) {
            Log.e("SdlNfcController", "Cannot get package manager, assuming no NFC feature");
        } else {
            try {
                z = packageManager.hasSystemFeature("android.hardware.nfc");
            } catch (Throwable e) {
                Log.e("SdlNfcController", "Package manager query failed, assuming no NFC feature", e);
            }
        }
        return z;
    }

    private static INfcAdapter fl() {
        IBinder service = ServiceManager.getService("nfc");
        if (service == null) {
            return null;
        }
        return Stub.asInterface(service);
    }

    private static synchronized void fm() {
        synchronized (SdlNfcController.class) {
            if (!sIsInitialized) {
                if (SdlNfcController.fk()) {
                    sService = SdlNfcController.fl();
                    if (sService == null) {
                        Log.e("SdlNfcController", "could not retrieve NFC service");
                    } else {
                        sIsInitialized = true;
                        Log.d("SdlNfcController", "get nfc service successfully");
                    }
                } else {
                    Log.e("SdlNfcController", "this device does not have NFC support");
                }
            }
        }
    }
}
