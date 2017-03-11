package com.samsung.android.spayfw.core.p005a;

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

/* renamed from: com.samsung.android.spayfw.core.a.h */
public interface IPaymentProcessor {
    Card m417X();

    void m418a(ICommonCallback iCommonCallback);

    void m419a(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback, boolean z);

    void m420a(String str, ISelectCardCallback iSelectCardCallback, boolean z);

    void m421a(String str, String str2, ICommonCallback iCommonCallback);

    void clearCard();

    void clearPay();

    void getInAppToken(String str, MerchantInfo merchantInfo, String str2, IInAppPayCallback iInAppPayCallback);

    byte[] processApdu(byte[] bArr, Bundle bundle);

    int retryPay(PayConfig payConfig);

    void startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback);

    ApduReasonCode m422u(int i);
}
