/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.a;

import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.payprovider.MerchantServerRequester;

public interface h {
    public c X();

    public void a(ICommonCallback var1);

    public void a(SecuredObject var1, PayConfig var2, IPayCallback var3, boolean var4);

    public void a(String var1, ISelectCardCallback var2, boolean var3);

    public void a(String var1, String var2, ICommonCallback var3);

    public void clearCard();

    public void clearPay();

    public void getInAppToken(String var1, MerchantServerRequester.MerchantInfo var2, String var3, IInAppPayCallback var4);

    public byte[] processApdu(byte[] var1, Bundle var2);

    public int retryPay(PayConfig var1);

    public void startInAppPay(SecuredObject var1, InAppTransactionInfo var2, IInAppPayCallback var3);

    public ApduReasonCode u(int var1);
}

