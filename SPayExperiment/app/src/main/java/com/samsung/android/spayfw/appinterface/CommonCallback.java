/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class CommonCallback
implements PaymentFrameworkConnection {
    private CommonCallback ccb = this;
    private PFCommonCallback pfCommonCb = new PFCommonCallback();

    ICommonCallback getICallback() {
        return this.pfCommonCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onSuccess(String var1);

    private class PFCommonCallback
    extends ICommonCallback.Stub {
        private PFCommonCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(CommonCallback.this.ccb);
            CommonCallback.this.ccb.onFail(string, n2);
        }

        @Override
        public void onSuccess(String string) {
            PaymentFramework.removeFromTrackMap(CommonCallback.this.ccb);
            CommonCallback.this.ccb.onSuccess(string);
        }
    }

}

