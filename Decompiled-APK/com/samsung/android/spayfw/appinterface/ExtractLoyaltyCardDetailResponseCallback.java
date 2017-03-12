package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback.Stub;

public abstract class ExtractLoyaltyCardDetailResponseCallback implements PaymentFrameworkConnection {
    private ExtractLoyaltyCardDetailResponseCallback lcCb;
    private PFExtractLoyaltyCardDetailResponseCallback pfLcCb;

    private class PFExtractLoyaltyCardDetailResponseCallback extends Stub {
        private PFExtractLoyaltyCardDetailResponseCallback() {
        }

        public void onSuccess(LoyaltyCardDetail loyaltyCardDetail) {
            PaymentFramework.removeFromTrackMap(ExtractLoyaltyCardDetailResponseCallback.this.lcCb);
            ExtractLoyaltyCardDetailResponseCallback.this.lcCb.onSuccess(loyaltyCardDetail);
        }

        public void onFail(int i) {
            PaymentFramework.removeFromTrackMap(ExtractLoyaltyCardDetailResponseCallback.this.lcCb);
            ExtractLoyaltyCardDetailResponseCallback.this.lcCb.onFail(i);
        }
    }

    public abstract void onFail(int i);

    public abstract void onSuccess(LoyaltyCardDetail loyaltyCardDetail);

    public ExtractLoyaltyCardDetailResponseCallback() {
        this.lcCb = this;
        this.pfLcCb = new PFExtractLoyaltyCardDetailResponseCallback();
    }

    IExtractLoyaltyCardDetailResponseCallback getPFExtractLoyaltyCardDetailCallback() {
        return this.pfLcCb;
    }
}
