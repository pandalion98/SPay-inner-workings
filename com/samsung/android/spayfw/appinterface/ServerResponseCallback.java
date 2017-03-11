package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IServerResponseCallback.Stub;

public abstract class ServerResponseCallback implements PaymentFrameworkConnection {
    private PFServerResponseCallback pfsrCb;
    private ServerResponseCallback srCb;

    private class PFServerResponseCallback extends Stub {
        private PFServerResponseCallback() {
        }

        public void onFail(int i) {
            ServerResponseCallback.this.srCb.onFail(i);
        }

        public void onSuccess(int i, ServerResponseData serverResponseData) {
            ServerResponseCallback.this.srCb.onSuccess(i, serverResponseData);
        }
    }

    public abstract void onFail(int i);

    public abstract void onSuccess(int i, ServerResponseData serverResponseData);

    public ServerResponseCallback() {
        this.srCb = this;
        this.pfsrCb = new PFServerResponseCallback();
    }

    IServerResponseCallback getPFServerCallback() {
        return this.pfsrCb;
    }
}
