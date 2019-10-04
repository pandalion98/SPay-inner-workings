/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import java.util.List;

public abstract class RefreshIdvResponseCallback
implements PaymentFrameworkConnection {
    private RefreshIdvResponseCallback callback = this;
    private PFRefreshIdvCallback pfRefreshIdvCb = new PFRefreshIdvCallback();

    IRefreshIdvCallback getPFRefreshIdvCallback() {
        return this.pfRefreshIdvCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onSuccess(String var1, List<IdvMethod> var2);

    private class PFRefreshIdvCallback
    extends IRefreshIdvCallback.Stub {
        private PFRefreshIdvCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(RefreshIdvResponseCallback.this.callback);
            RefreshIdvResponseCallback.this.callback.onFail(string, n2);
        }

        @Override
        public void onSuccess(String string, List<IdvMethod> list) {
            PaymentFramework.removeFromTrackMap(RefreshIdvResponseCallback.this.callback);
            RefreshIdvResponseCallback.this.callback.onSuccess(string, list);
        }
    }

}

