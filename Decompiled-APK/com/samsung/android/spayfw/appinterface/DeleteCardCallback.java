package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IDeleteCardCallback.Stub;

public abstract class DeleteCardCallback implements PaymentFrameworkConnection {
    private PFDeleteCardCallback pfDeleteCardCb;
    private DeleteCardCallback scb;

    private class PFDeleteCardCallback extends Stub {
        private PFDeleteCardCallback() {
        }

        public void onSuccess(String str, TokenStatus tokenStatus) {
            PaymentFramework.removeFromTrackMap(DeleteCardCallback.this.scb);
            DeleteCardCallback.this.scb.onSuccess(str, tokenStatus);
        }

        public void onFail(String str, int i, TokenStatus tokenStatus) {
            PaymentFramework.removeFromTrackMap(DeleteCardCallback.this.scb);
            DeleteCardCallback.this.scb.onFail(str, i, tokenStatus);
        }
    }

    public abstract void onFail(String str, int i, TokenStatus tokenStatus);

    public abstract void onSuccess(String str, TokenStatus tokenStatus);

    public DeleteCardCallback() {
        this.scb = this;
        this.pfDeleteCardCb = new PFDeleteCardCallback();
    }

    IDeleteCardCallback getPFDeleteCardCb() {
        return this.pfDeleteCardCb;
    }
}
