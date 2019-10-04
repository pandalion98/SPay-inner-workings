/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.PaymentInstrument;
import com.samsung.android.visasdk.paywave.model.TokenInfo;

public class ProvisionedData {
    private PaymentInstrument paymentInstrument;
    private TokenInfo tokenInfo;

    public PaymentInstrument getPaymentInstrument() {
        return this.paymentInstrument;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }
}

