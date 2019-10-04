/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.IBinder
 *  com.samsung.android.fingerprint.FingerprintEvent
 *  com.samsung.android.fingerprint.FingerprintManager
 *  com.samsung.android.fingerprint.FingerprintManager$FingerprintClientSpecBuilder
 *  com.samsung.android.fingerprint.IFingerprintClient
 *  com.samsung.android.fingerprint.IFingerprintClient$Stub
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 */
package com.samsung.android.sdk.pass.process;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.fingerprint.IFingerprintClient;

public class SpassFingerprintProcess {
    public static final String APP_AUTHNR = "authnr";
    public static final String APP_FP_ASM = "fp_asm";
    static final int SDK_VERSION = 16777216;
    private Context mContext = null;
    private IFingerprintClient mFingerprintClient = new IFingerprintClient.Stub(){

        public void onFingerprintEvent(FingerprintEvent fingerprintEvent) {
        }
    };
    private FingerprintManager mFm = null;
    private IBinder mToken = null;

    public SpassFingerprintProcess(Context context) {
        this.mContext = context;
        this.mFm = FingerprintManager.getInstance((Context)this.mContext);
    }

    private void ensureServiceSupported() {
        SpassFingerprintProcess spassFingerprintProcess = this;
        synchronized (spassFingerprintProcess) {
            if (this.mFm == null) {
                throw new UnsupportedOperationException("Fingerprint Service is not running on the device.");
            }
            return;
        }
    }

    private void registerClient() {
        this.ensureServiceSupported();
        FingerprintManager.FingerprintClientSpecBuilder fingerprintClientSpecBuilder = new FingerprintManager.FingerprintClientSpecBuilder("com.samsung.android.sdk.pass.process");
        if (this.mToken == null) {
            this.mToken = this.mFm.registerClient(this.mFingerprintClient, fingerprintClientSpecBuilder.build());
        }
        if (this.mToken == null) {
            throw new IllegalStateException("registerClient: failed");
        }
    }

    private void unregiserClient() {
        this.ensureServiceSupported();
        if (this.mToken != null) {
            this.mFm.unregisterClient(this.mToken);
            this.mToken = null;
        }
    }

    public byte[] process(byte[] arrby, String string) {
        this.ensureServiceSupported();
        if (arrby == null) {
            throw new IllegalArgumentException();
        }
        this.registerClient();
        byte[] arrby2 = this.mFm.process(this.mToken, string, arrby);
        this.unregiserClient();
        return arrby2;
    }

    public byte[] processFIDO(byte[] arrby) {
        this.ensureServiceSupported();
        if (arrby == null) {
            throw new IllegalArgumentException();
        }
        this.registerClient();
        byte[] arrby2 = this.mFm.processFIDO(this.mContext, this.mToken, null, arrby);
        this.unregiserClient();
        return arrby2;
    }

}

