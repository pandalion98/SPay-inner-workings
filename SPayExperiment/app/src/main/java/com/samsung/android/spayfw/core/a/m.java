/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.a;

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
import com.samsung.android.spayfw.core.a.h;
import com.samsung.android.spayfw.core.a.n;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.payprovider.MerchantServerRequester;

public abstract class m
implements h {
    protected h ly;
    protected Context mContext;

    public m(Context context, n n2) {
        this.mContext = context;
        this.ly = n2;
    }

    @Override
    public c X() {
        return this.ly.X();
    }

    @Override
    public void a(ICommonCallback iCommonCallback) {
        this.ly.a(iCommonCallback);
    }

    @Override
    public void a(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback, boolean bl) {
        this.ly.a(securedObject, payConfig, iPayCallback, bl);
    }

    @Override
    public void a(String string, ISelectCardCallback iSelectCardCallback, boolean bl) {
        this.ly.a(string, iSelectCardCallback, bl);
    }

    @Override
    public void a(String string, String string2, ICommonCallback iCommonCallback) {
        this.ly.a(string, string2, iCommonCallback);
    }

    @Override
    public void clearCard() {
        this.ly.clearCard();
    }

    @Override
    public void clearPay() {
        this.ly.clearPay();
    }

    @Override
    public void getInAppToken(String string, MerchantServerRequester.MerchantInfo merchantInfo, String string2, IInAppPayCallback iInAppPayCallback) {
        this.ly.getInAppToken(string, merchantInfo, string2, iInAppPayCallback);
    }

    @Override
    public byte[] processApdu(byte[] arrby, Bundle bundle) {
        return this.ly.processApdu(arrby, bundle);
    }

    @Override
    public int retryPay(PayConfig payConfig) {
        return this.ly.retryPay(payConfig);
    }

    @Override
    public void startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback) {
        this.ly.startInAppPay(securedObject, inAppTransactionInfo, iInAppPayCallback);
    }

    @Override
    public ApduReasonCode u(int n2) {
        return this.ly.u(n2);
    }
}

