package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ISelectCardCallback.Stub;

public abstract class SelectCardCallback implements PaymentFrameworkConnection {
    private PFSelectCardCallback pfSelectCardCb;
    private SelectCardCallback scb;

    private class PFSelectCardCallback extends Stub {
        private PFSelectCardCallback() {
        }

        public void onSuccess(String str, SelectCardResult selectCardResult) {
            PaymentFramework.removeFromTrackMap(SelectCardCallback.this.scb);
            SelectCardCallback.this.scb.onSuccess(str, selectCardResult);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(SelectCardCallback.this.scb);
            SelectCardCallback.this.scb.onFail(str, i);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onSuccess(String str, SelectCardResult selectCardResult);

    public SelectCardCallback() {
        this.scb = this;
        this.pfSelectCardCb = new PFSelectCardCallback();
    }

    ISelectCardCallback getPFSelectCardCb() {
        return this.pfSelectCardCb;
    }
}
