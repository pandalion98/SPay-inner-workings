package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback.Stub;
import java.util.List;

public abstract class RefreshIdvResponseCallback implements PaymentFrameworkConnection {
    private RefreshIdvResponseCallback callback;
    private PFRefreshIdvCallback pfRefreshIdvCb;

    private class PFRefreshIdvCallback extends Stub {
        private PFRefreshIdvCallback() {
        }

        public void onSuccess(String str, List<IdvMethod> list) {
            PaymentFramework.removeFromTrackMap(RefreshIdvResponseCallback.this.callback);
            RefreshIdvResponseCallback.this.callback.onSuccess(str, list);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(RefreshIdvResponseCallback.this.callback);
            RefreshIdvResponseCallback.this.callback.onFail(str, i);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onSuccess(String str, List<IdvMethod> list);

    public RefreshIdvResponseCallback() {
        this.callback = this;
        this.pfRefreshIdvCb = new PFRefreshIdvCallback();
    }

    IRefreshIdvCallback getPFRefreshIdvCallback() {
        return this.pfRefreshIdvCb;
    }
}
