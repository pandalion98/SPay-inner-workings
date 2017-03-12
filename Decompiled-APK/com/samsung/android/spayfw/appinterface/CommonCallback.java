package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ICommonCallback.Stub;

public abstract class CommonCallback implements PaymentFrameworkConnection {
    private CommonCallback ccb;
    private PFCommonCallback pfCommonCb;

    private class PFCommonCallback extends Stub {
        private PFCommonCallback() {
        }

        public void onSuccess(String str) {
            PaymentFramework.removeFromTrackMap(CommonCallback.this.ccb);
            CommonCallback.this.ccb.onSuccess(str);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(CommonCallback.this.ccb);
            CommonCallback.this.ccb.onFail(str, i);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onSuccess(String str);

    public CommonCallback() {
        this.ccb = this;
        this.pfCommonCb = new PFCommonCallback();
    }

    ICommonCallback getICallback() {
        return this.pfCommonCb;
    }
}
