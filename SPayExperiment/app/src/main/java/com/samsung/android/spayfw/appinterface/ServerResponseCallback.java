/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IServerResponseCallback;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import com.samsung.android.spayfw.appinterface.ServerResponseData;

public abstract class ServerResponseCallback
implements PaymentFrameworkConnection {
    private PFServerResponseCallback pfsrCb = new PFServerResponseCallback();
    private ServerResponseCallback srCb = this;

    IServerResponseCallback getPFServerCallback() {
        return this.pfsrCb;
    }

    public abstract void onFail(int var1);

    public abstract void onSuccess(int var1, ServerResponseData var2);

    private class PFServerResponseCallback
    extends IServerResponseCallback.Stub {
        private PFServerResponseCallback() {
        }

        @Override
        public void onFail(int n2) {
            ServerResponseCallback.this.srCb.onFail(n2);
        }

        @Override
        public void onSuccess(int n2, ServerResponseData serverResponseData) {
            ServerResponseCallback.this.srCb.onSuccess(n2, serverResponseData);
        }
    }

}

