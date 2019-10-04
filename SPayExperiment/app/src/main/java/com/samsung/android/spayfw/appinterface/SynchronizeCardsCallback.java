/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.CardState;
import com.samsung.android.spayfw.appinterface.ISynchronizeCardsCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;
import java.util.List;

public abstract class SynchronizeCardsCallback
implements PaymentFrameworkConnection {
    private PFSynchronizeCardsCallback pfSynchronizeCardsCb = new PFSynchronizeCardsCallback();
    private SynchronizeCardsCallback scb = this;

    ISynchronizeCardsCallback getPFSynchronizeCardsCb() {
        return this.pfSynchronizeCardsCb;
    }

    public abstract void onFail(String var1, int var2);

    public abstract void onSuccess(String var1, List<CardState> var2);

    private class PFSynchronizeCardsCallback
    extends ISynchronizeCardsCallback.Stub {
        private PFSynchronizeCardsCallback() {
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(SynchronizeCardsCallback.this.scb);
            SynchronizeCardsCallback.this.scb.onFail(string, n2);
        }

        @Override
        public void onSuccess(String string, List<CardState> list) {
            PaymentFramework.removeFromTrackMap(SynchronizeCardsCallback.this.scb);
            SynchronizeCardsCallback.this.scb.onSuccess(string, list);
        }
    }

}

