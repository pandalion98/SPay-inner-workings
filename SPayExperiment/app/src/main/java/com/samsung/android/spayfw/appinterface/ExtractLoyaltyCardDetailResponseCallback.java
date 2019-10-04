/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.LoyaltyCardDetail;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class ExtractLoyaltyCardDetailResponseCallback
implements PaymentFrameworkConnection {
    private ExtractLoyaltyCardDetailResponseCallback lcCb = this;
    private PFExtractLoyaltyCardDetailResponseCallback pfLcCb = new PFExtractLoyaltyCardDetailResponseCallback();

    IExtractLoyaltyCardDetailResponseCallback getPFExtractLoyaltyCardDetailCallback() {
        return this.pfLcCb;
    }

    public abstract void onFail(int var1);

    public abstract void onSuccess(LoyaltyCardDetail var1);

    private class PFExtractLoyaltyCardDetailResponseCallback
    extends IExtractLoyaltyCardDetailResponseCallback.Stub {
        private PFExtractLoyaltyCardDetailResponseCallback() {
        }

        @Override
        public void onFail(int n2) {
            PaymentFramework.removeFromTrackMap(ExtractLoyaltyCardDetailResponseCallback.this.lcCb);
            ExtractLoyaltyCardDetailResponseCallback.this.lcCb.onFail(n2);
        }

        @Override
        public void onSuccess(LoyaltyCardDetail loyaltyCardDetail) {
            PaymentFramework.removeFromTrackMap(ExtractLoyaltyCardDetailResponseCallback.this.lcCb);
            ExtractLoyaltyCardDetailResponseCallback.this.lcCb.onSuccess(loyaltyCardDetail);
        }
    }

}

