package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ISynchronizeCardsCallback.Stub;
import java.util.List;

public abstract class SynchronizeCardsCallback implements PaymentFrameworkConnection {
    private PFSynchronizeCardsCallback pfSynchronizeCardsCb;
    private SynchronizeCardsCallback scb;

    private class PFSynchronizeCardsCallback extends Stub {
        private PFSynchronizeCardsCallback() {
        }

        public void onSuccess(String str, List<CardState> list) {
            PaymentFramework.removeFromTrackMap(SynchronizeCardsCallback.this.scb);
            SynchronizeCardsCallback.this.scb.onSuccess(str, list);
        }

        public void onFail(String str, int i) {
            PaymentFramework.removeFromTrackMap(SynchronizeCardsCallback.this.scb);
            SynchronizeCardsCallback.this.scb.onFail(str, i);
        }
    }

    public abstract void onFail(String str, int i);

    public abstract void onSuccess(String str, List<CardState> list);

    public SynchronizeCardsCallback() {
        this.scb = this;
        this.pfSynchronizeCardsCb = new PFSynchronizeCardsCallback();
    }

    ISynchronizeCardsCallback getPFSynchronizeCardsCb() {
        return this.pfSynchronizeCardsCb;
    }
}
