/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.util.Log;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardResult;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class EnrollCardCallback
implements PaymentFrameworkConnection {
    private static String TAG = "EnrollCardCallback";
    private EnrollCardInfo eCardInfo;
    private EnrollCardCallback ecb = this;
    private PFEnrollCardCallback pfEnrollCb = new PFEnrollCardCallback();

    IEnrollCardCallback getPFEnrollCb() {
        return this.pfEnrollCb;
    }

    public abstract void onFail(int var1, EnrollCardResult var2);

    public abstract void onSuccess(EnrollCardResult var1);

    public void setEnrollCardInfo(EnrollCardInfo enrollCardInfo) {
        this.eCardInfo = enrollCardInfo;
    }

    private class PFEnrollCardCallback
    extends IEnrollCardCallback.Stub {
        private PFEnrollCardCallback() {
        }

        @Override
        public void onFail(int n2, EnrollCardResult enrollCardResult) {
            PaymentFramework.removeFromTrackMap(EnrollCardCallback.this.ecb);
            Log.d((String)TAG, (String)"EnrollCardCallback.onFail()");
            if (EnrollCardCallback.this.eCardInfo != null && n2 != 1 && n2 != -15 && n2 != -206) {
                EnrollCardCallback.this.eCardInfo.decrementRefCount();
            }
            EnrollCardCallback.this.ecb.onFail(n2, enrollCardResult);
        }

        @Override
        public void onSuccess(EnrollCardResult enrollCardResult) {
            PaymentFramework.removeFromTrackMap(EnrollCardCallback.this.ecb);
            Log.d((String)TAG, (String)"EnrollCardCallback.success()");
            if (EnrollCardCallback.this.eCardInfo != null) {
                EnrollCardCallback.this.eCardInfo.decrementRefCount();
            }
            EnrollCardCallback.this.ecb.onSuccess(enrollCardResult);
        }
    }

}

