/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.text.TextUtils;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IUserSignatureCallback;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.c;

public class s
extends o {
    private static s mh;

    protected s(Context context) {
        super(context);
    }

    public static final s v(Context context) {
        Class<s> class_ = s.class;
        synchronized (s.class) {
            if (mh == null) {
                mh = new s(context);
            }
            s s2 = mh;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return s2;
        }
    }

    public final void a(String string, IUserSignatureCallback iUserSignatureCallback) {
        Log.d("SignatureProcessor", "getUserSignature start ");
        if (string == null || iUserSignatureCallback == null) {
            if (iUserSignatureCallback != null) {
                iUserSignatureCallback.onFail(-5);
            }
            Log.e("SignatureProcessor", "getUserSignature input data is null! ");
            return;
        }
        c c2 = this.iJ.q(string);
        if (c2 == null || c2.ad() == null || c2.ac() == null) {
            iUserSignatureCallback.onFail(-1);
            Log.e("SignatureProcessor", "storeUserSignature card object or provider or token is null! ");
            return;
        }
        byte[] arrby = this.jJ.br(c2.ac().getTokenId());
        if (arrby == null || arrby.length == 0) {
            iUserSignatureCallback.onFail(-1);
            Log.e("SignatureProcessor", "getUserSignature database has returned null signature! ");
            return;
        }
        byte[] arrby2 = c2.ad().decryptUserSignatureTA(arrby);
        if (arrby2 == null || arrby2.length == 0) {
            iUserSignatureCallback.onFail(-1);
            Log.e("SignatureProcessor", "getUserSignature TA returned null signature! ");
            return;
        }
        Log.d("SignatureProcessor", "getUserSignature end, returning user signature. ");
        iUserSignatureCallback.onSuccess(arrby2);
    }

    public final void a(String string, byte[] arrby, ICommonCallback iCommonCallback) {
        Log.d("SignatureProcessor", "storeUserSignature start ");
        if (string == null || iCommonCallback == null || arrby == null || arrby.length == 0) {
            if (iCommonCallback != null) {
                iCommonCallback.onFail(string, -5);
            }
            Log.e("SignatureProcessor", "storeUserSignature input data is null! ");
            return;
        }
        c c2 = this.iJ.q(string);
        if (c2 == null || c2.ad() == null || c2.ac() == null) {
            iCommonCallback.onFail(string, -1);
            Log.e("SignatureProcessor", "storeUserSignature card object or provider is null! ");
            return;
        }
        String string2 = c2.ad().encryptUserSignatureTA(arrby);
        if (string2 == null || TextUtils.isEmpty((CharSequence)string2)) {
            iCommonCallback.onFail(string, -1);
            Log.e("SignatureProcessor", "storeUserSignature TA returned null signature! ");
            return;
        }
        if (this.jJ.d(c2.ac().getTokenId(), string2.getBytes()) == -1) {
            iCommonCallback.onFail(string, -1);
            Log.e("SignatureProcessor", "storeUserSignature failed to store signature! ");
            return;
        }
        iCommonCallback.onSuccess(string);
        Log.d("SignatureProcessor", "storeUserSignature end ");
    }
}

