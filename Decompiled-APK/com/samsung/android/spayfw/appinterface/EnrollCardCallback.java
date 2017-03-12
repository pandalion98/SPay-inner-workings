package com.samsung.android.spayfw.appinterface;

import android.util.Log;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback.Stub;

public abstract class EnrollCardCallback implements PaymentFrameworkConnection {
    private static String TAG;
    private EnrollCardInfo eCardInfo;
    private EnrollCardCallback ecb;
    private PFEnrollCardCallback pfEnrollCb;

    private class PFEnrollCardCallback extends Stub {
        private PFEnrollCardCallback() {
        }

        public void onSuccess(EnrollCardResult enrollCardResult) {
            PaymentFramework.removeFromTrackMap(EnrollCardCallback.this.ecb);
            Log.d(EnrollCardCallback.TAG, "EnrollCardCallback.success()");
            if (EnrollCardCallback.this.eCardInfo != null) {
                EnrollCardCallback.this.eCardInfo.decrementRefCount();
            }
            EnrollCardCallback.this.ecb.onSuccess(enrollCardResult);
        }

        public void onFail(int i, EnrollCardResult enrollCardResult) {
            PaymentFramework.removeFromTrackMap(EnrollCardCallback.this.ecb);
            Log.d(EnrollCardCallback.TAG, "EnrollCardCallback.onFail()");
            if (!(EnrollCardCallback.this.eCardInfo == null || i == 1 || i == -15 || i == PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID)) {
                EnrollCardCallback.this.eCardInfo.decrementRefCount();
            }
            EnrollCardCallback.this.ecb.onFail(i, enrollCardResult);
        }
    }

    public abstract void onFail(int i, EnrollCardResult enrollCardResult);

    public abstract void onSuccess(EnrollCardResult enrollCardResult);

    public EnrollCardCallback() {
        this.ecb = this;
        this.pfEnrollCb = new PFEnrollCardCallback();
    }

    static {
        TAG = "EnrollCardCallback";
    }

    IEnrollCardCallback getPFEnrollCb() {
        return this.pfEnrollCb;
    }

    public void setEnrollCardInfo(EnrollCardInfo enrollCardInfo) {
        this.eCardInfo = enrollCardInfo;
    }
}
