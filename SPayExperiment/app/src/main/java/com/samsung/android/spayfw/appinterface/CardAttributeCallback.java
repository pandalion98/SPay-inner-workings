/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.CardAttributes;
import com.samsung.android.spayfw.appinterface.ICardAttributeCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class CardAttributeCallback
implements PaymentFrameworkConnection {
    private CardAttributeCallback cab = this;
    private Callback iCallback = new Callback();

    ICardAttributeCallback getICallback() {
        return this.iCallback;
    }

    public abstract void onFail(int var1);

    public abstract void onSuccess(String var1, CardAttributes var2);

    private class Callback
    extends ICardAttributeCallback.Stub {
        private Callback() {
        }

        @Override
        public void onFail(int n2) {
            PaymentFramework.removeFromTrackMap(CardAttributeCallback.this.cab);
            CardAttributeCallback.this.cab.onFail(n2);
        }

        @Override
        public void onSuccess(String string, CardAttributes cardAttributes) {
            PaymentFramework.removeFromTrackMap(CardAttributeCallback.this.cab);
            CardAttributeCallback.this.cab.onSuccess(string, cardAttributes);
        }
    }

}

