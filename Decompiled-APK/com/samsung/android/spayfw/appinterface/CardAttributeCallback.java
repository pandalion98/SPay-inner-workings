package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ICardAttributeCallback.Stub;

public abstract class CardAttributeCallback implements PaymentFrameworkConnection {
    private CardAttributeCallback cab;
    private Callback iCallback;

    private class Callback extends Stub {
        private Callback() {
        }

        public void onSuccess(String str, CardAttributes cardAttributes) {
            PaymentFramework.removeFromTrackMap(CardAttributeCallback.this.cab);
            CardAttributeCallback.this.cab.onSuccess(str, cardAttributes);
        }

        public void onFail(int i) {
            PaymentFramework.removeFromTrackMap(CardAttributeCallback.this.cab);
            CardAttributeCallback.this.cab.onFail(i);
        }
    }

    public abstract void onFail(int i);

    public abstract void onSuccess(String str, CardAttributes cardAttributes);

    public CardAttributeCallback() {
        this.cab = this;
        this.iCallback = new Callback();
    }

    ICardAttributeCallback getICallback() {
        return this.iCallback;
    }
}
