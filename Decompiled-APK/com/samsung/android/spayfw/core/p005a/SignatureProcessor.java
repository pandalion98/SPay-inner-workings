package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IUserSignatureCallback;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.p002b.Log;

/* renamed from: com.samsung.android.spayfw.core.a.s */
public class SignatureProcessor extends Processor {
    private static SignatureProcessor mh;

    protected SignatureProcessor(Context context) {
        super(context);
    }

    public static final synchronized SignatureProcessor m510v(Context context) {
        SignatureProcessor signatureProcessor;
        synchronized (SignatureProcessor.class) {
            if (mh == null) {
                mh = new SignatureProcessor(context);
            }
            signatureProcessor = mh;
        }
        return signatureProcessor;
    }

    public final void m511a(String str, IUserSignatureCallback iUserSignatureCallback) {
        Log.m285d("SignatureProcessor", "getUserSignature start ");
        if (str == null || iUserSignatureCallback == null) {
            if (iUserSignatureCallback != null) {
                iUserSignatureCallback.onFail(-5);
            }
            Log.m286e("SignatureProcessor", "getUserSignature input data is null! ");
            return;
        }
        Card q = this.iJ.m558q(str);
        if (q == null || q.ad() == null || q.ac() == null) {
            iUserSignatureCallback.onFail(-1);
            Log.m286e("SignatureProcessor", "storeUserSignature card object or provider or token is null! ");
            return;
        }
        byte[] br = this.jJ.br(q.ac().getTokenId());
        if (br == null || br.length == 0) {
            iUserSignatureCallback.onFail(-1);
            Log.m286e("SignatureProcessor", "getUserSignature database has returned null signature! ");
            return;
        }
        byte[] decryptUserSignatureTA = q.ad().decryptUserSignatureTA(br);
        if (decryptUserSignatureTA == null || decryptUserSignatureTA.length == 0) {
            iUserSignatureCallback.onFail(-1);
            Log.m286e("SignatureProcessor", "getUserSignature TA returned null signature! ");
            return;
        }
        Log.m285d("SignatureProcessor", "getUserSignature end, returning user signature. ");
        iUserSignatureCallback.onSuccess(decryptUserSignatureTA);
    }

    public final void m512a(String str, byte[] bArr, ICommonCallback iCommonCallback) {
        Log.m285d("SignatureProcessor", "storeUserSignature start ");
        if (str == null || iCommonCallback == null || bArr == null || bArr.length == 0) {
            if (iCommonCallback != null) {
                iCommonCallback.onFail(str, -5);
            }
            Log.m286e("SignatureProcessor", "storeUserSignature input data is null! ");
            return;
        }
        Card q = this.iJ.m558q(str);
        if (q == null || q.ad() == null || q.ac() == null) {
            iCommonCallback.onFail(str, -1);
            Log.m286e("SignatureProcessor", "storeUserSignature card object or provider is null! ");
            return;
        }
        Object encryptUserSignatureTA = q.ad().encryptUserSignatureTA(bArr);
        if (encryptUserSignatureTA == null || TextUtils.isEmpty(encryptUserSignatureTA)) {
            iCommonCallback.onFail(str, -1);
            Log.m286e("SignatureProcessor", "storeUserSignature TA returned null signature! ");
        } else if (this.jJ.m1231d(q.ac().getTokenId(), encryptUserSignatureTA.getBytes()) == -1) {
            iCommonCallback.onFail(str, -1);
            Log.m286e("SignatureProcessor", "storeUserSignature failed to store signature! ");
        } else {
            iCommonCallback.onSuccess(str);
            Log.m285d("SignatureProcessor", "storeUserSignature end ");
        }
    }
}
