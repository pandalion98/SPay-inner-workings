package com.samsung.android.sdk.pass.process;

import android.content.Context;
import android.os.IBinder;
import com.samsung.android.fingerprint.FingerprintEvent;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.fingerprint.FingerprintManager.FingerprintClientSpecBuilder;
import com.samsung.android.fingerprint.IFingerprintClient;
import com.samsung.android.fingerprint.IFingerprintClient.Stub;

public class SpassFingerprintProcess {
    public static final String APP_AUTHNR = "authnr";
    public static final String APP_FP_ASM = "fp_asm";
    static final int SDK_VERSION = 16777216;
    private Context mContext;
    private IFingerprintClient mFingerprintClient;
    private FingerprintManager mFm;
    private IBinder mToken;

    /* renamed from: com.samsung.android.sdk.pass.process.SpassFingerprintProcess.1 */
    class C03251 extends Stub {
        C03251() {
        }

        public void onFingerprintEvent(FingerprintEvent fingerprintEvent) {
        }
    }

    public SpassFingerprintProcess(Context context) {
        this.mContext = null;
        this.mToken = null;
        this.mFm = null;
        this.mFingerprintClient = new C03251();
        this.mContext = context;
        this.mFm = FingerprintManager.getInstance(this.mContext);
    }

    private synchronized void ensureServiceSupported() {
        if (this.mFm == null) {
            throw new UnsupportedOperationException("Fingerprint Service is not running on the device.");
        }
    }

    private void registerClient() {
        ensureServiceSupported();
        FingerprintClientSpecBuilder fingerprintClientSpecBuilder = new FingerprintClientSpecBuilder("com.samsung.android.sdk.pass.process");
        if (this.mToken == null) {
            this.mToken = this.mFm.registerClient(this.mFingerprintClient, fingerprintClientSpecBuilder.build());
        }
        if (this.mToken == null) {
            throw new IllegalStateException("registerClient: failed");
        }
    }

    private void unregiserClient() {
        ensureServiceSupported();
        if (this.mToken != null) {
            this.mFm.unregisterClient(this.mToken);
            this.mToken = null;
        }
    }

    public byte[] process(byte[] bArr, String str) {
        ensureServiceSupported();
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        registerClient();
        byte[] process = this.mFm.process(this.mToken, str, bArr);
        unregiserClient();
        return process;
    }

    public byte[] processFIDO(byte[] bArr) {
        ensureServiceSupported();
        if (bArr == null) {
            throw new IllegalArgumentException();
        }
        registerClient();
        byte[] processFIDO = this.mFm.processFIDO(this.mContext, this.mToken, null, bArr);
        unregiserClient();
        return processFIDO;
    }
}
