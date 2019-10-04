/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PaymentFrameworkConnection;

public abstract class PayCallback
implements PaymentFrameworkConnection {
    private PayCallback pcb = this;
    private PFPayCallback pfPayCb = new PFPayCallback();

    IPayCallback getPFPayCb() {
        return this.pfPayCb;
    }

    public abstract void onExtractGiftCardDetail(GiftCardDetail var1);

    public abstract void onFail(String var1, int var2);

    public abstract void onFinish(String var1, int var2, ApduReasonCode var3);

    public abstract void onPay(String var1, int var2, int var3);

    public abstract void onPaySwitch(String var1, int var2, int var3);

    public abstract void onRetry(String var1, int var2, int var3);

    private class PFPayCallback
    extends IPayCallback.Stub {
        private PFPayCallback() {
        }

        @Override
        public void onExtractGiftCardDetail(GiftCardDetail giftCardDetail) {
            PayCallback.this.pcb.onExtractGiftCardDetail(giftCardDetail);
        }

        @Override
        public void onFail(String string, int n2) {
            PaymentFramework.removeFromTrackMap(PayCallback.this.pcb);
            PayCallback.this.pcb.onFail(string, n2);
        }

        @Override
        public void onFinish(String string, int n2, ApduReasonCode apduReasonCode) {
            PaymentFramework.removeFromTrackMap(PayCallback.this.pcb);
            PayCallback.this.pcb.onFinish(string, n2, apduReasonCode);
        }

        @Override
        public void onPay(String string, int n2, int n3) {
            PayCallback.this.pcb.onPay(string, n2, n3);
        }

        @Override
        public void onPaySwitch(String string, int n2, int n3) {
            PayCallback.this.pcb.onPaySwitch(string, n2, n3);
        }

        @Override
        public void onRetry(String string, int n2, int n3) {
            PayCallback.this.pcb.onRetry(string, n2, n3);
        }
    }

}

