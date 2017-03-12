package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ICardDataCallback.Stub;

public abstract class CardDataCallback implements PaymentFrameworkConnection {
    private PFCardDataCallback pfCardDataCb;
    private CardDataCallback scb;

    private class PFCardDataCallback extends Stub {
        private PFCardDataCallback() {
        }

        public void onSuccess(String str, Token token) {
            PaymentFramework.removeFromTrackMap(CardDataCallback.this.scb);
            CardDataCallback.this.scb.onSuccess(str, token);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(CardDataCallback.this.scb);
            CardDataCallback.this.scb.onFail(str, i);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onSuccess(String str, Token token);

    public CardDataCallback() {
        this.scb = this;
        this.pfCardDataCb = new PFCardDataCallback();
    }

    ICardDataCallback getPFCardDataCb() {
        return this.pfCardDataCb;
    }
}
