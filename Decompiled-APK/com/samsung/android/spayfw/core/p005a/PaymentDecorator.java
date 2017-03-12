package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.payprovider.MerchantServerRequester.MerchantInfo;

/* renamed from: com.samsung.android.spayfw.core.a.m */
public abstract class PaymentDecorator implements IPaymentProcessor {
    protected IPaymentProcessor ly;
    protected Context mContext;

    public PaymentDecorator(Context context, PaymentProcessor paymentProcessor) {
        this.mContext = context;
        this.ly = paymentProcessor;
    }

    public void clearCard() {
        this.ly.clearCard();
    }

    public void clearPay() {
        this.ly.clearPay();
    }

    public Card m440X() {
        return this.ly.m417X();
    }

    public ApduReasonCode m445u(int i) {
        return this.ly.m422u(i);
    }

    public byte[] processApdu(byte[] bArr, Bundle bundle) {
        return this.ly.processApdu(bArr, bundle);
    }

    public int retryPay(PayConfig payConfig) {
        return this.ly.retryPay(payConfig);
    }

    public void m443a(String str, ISelectCardCallback iSelectCardCallback, boolean z) {
        this.ly.m420a(str, iSelectCardCallback, z);
    }

    public void startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback) {
        this.ly.startInAppPay(securedObject, inAppTransactionInfo, iInAppPayCallback);
    }

    public void m442a(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback, boolean z) {
        this.ly.m419a(securedObject, payConfig, iPayCallback, z);
    }

    public void m441a(ICommonCallback iCommonCallback) {
        this.ly.m418a(iCommonCallback);
    }

    public void m444a(String str, String str2, ICommonCallback iCommonCallback) {
        this.ly.m421a(str, str2, iCommonCallback);
    }

    public void getInAppToken(String str, MerchantInfo merchantInfo, String str2, IInAppPayCallback iInAppPayCallback) {
        this.ly.getInAppToken(str, merchantInfo, str2, iInAppPayCallback);
    }
}
