package com.samsung.android.spayfw.payprovider.visa.inapp;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.visa.VisaRequesterClient;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.GenCryptogramResponseData;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.GenCryptogramResponseData.CryptogramInfo;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.GenCryptogramResponseData.TokenInfo;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.InAppData;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

public class InAppPayment {
    public static final String ECOM_TRANSACTION_TYPE = "ECOM";
    public static final String RECURRING_TRANSACTION_TYPE = "RECURRING";

    public static GenCryptogramResponseData getCryptogramInfo(Context context, String str, PaymentDataRequest paymentDataRequest) {
        if (context == null || paymentDataRequest == null || str == null) {
            Log.m286e("InAppPayment", " getCryptogramInfo: input is null");
            throw new PaymentProviderException(-36);
        }
        try {
            Response a = m1146a(context, str, paymentDataRequest);
            if (a == null) {
                Log.m286e("InAppPayment", " sendGenCryptogramRequest: return is null");
                throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL);
            }
            int statusCode = a.getStatusCode();
            Log.m285d("InAppPayment", " sendGenCryptogramRequest: status " + statusCode);
            switch (statusCode) {
                case ECCurve.COORD_AFFINE /*0*/:
                    throw new PaymentProviderException(-9);
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                case 201:
                case 202:
                    Object obj = (Data) a.getResult();
                    if (obj == null || obj.getData() == null) {
                        Log.m286e("InAppPayment", "responseData is null");
                        throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL);
                    }
                    Gson create = new GsonBuilder().disableHtmlEscaping().create();
                    Log.m285d("InAppPayment", "responseJson string: " + create.toJson(obj));
                    return (GenCryptogramResponseData) create.fromJson(obj.getData(), GenCryptogramResponseData.class);
                case 400:
                    throw new PaymentProviderException(-5);
                case 401:
                case 403:
                    throw new PaymentProviderException(-4);
                case 406:
                case 410:
                    throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_REJECT);
                case 408:
                case 503:
                    throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE);
                case 421:
                    throw new PaymentProviderException(-1);
                default:
                    Log.m286e("InAppPayment", " sendGenCryptogramRequest: error " + statusCode);
                    throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL);
            }
        } catch (Throwable e) {
            Log.m286e("InAppPayment", "generateInAppPaymentPayload: interrupted");
            Log.m284c("InAppPayment", e.getMessage(), e);
            throw new PaymentProviderException(-36);
        }
    }

    private static Response<Data> m1146a(Context context, String str, PaymentDataRequest paymentDataRequest) {
        if (context == null || str == null || paymentDataRequest == null) {
            Log.m286e("InAppPayment", "tokenId or requestData null");
            return null;
        }
        Log.m285d("InAppPayment", "tokenId :" + str);
        Gson create = new GsonBuilder().disableHtmlEscaping().create();
        Data data = new Data();
        data.setData(create.toJsonTree(paymentDataRequest).getAsJsonObject());
        return VisaRequesterClient.m1143G(context).m1144a(str, data).eS();
    }

    public static InAppData buildInAppPaymentData(Context context, InAppDetailedTransactionInfo inAppDetailedTransactionInfo, GenCryptogramResponseData genCryptogramResponseData) {
        if (context == null || genCryptogramResponseData == null || inAppDetailedTransactionInfo == null) {
            Log.m286e("InAppPayment", " buildInAppPaymentData: input are null");
            return null;
        }
        InAppData inAppData = new InAppData();
        inAppData.setAmount(inAppDetailedTransactionInfo.getAmount());
        inAppData.setCurrency_code(inAppDetailedTransactionInfo.getCurrencyCode());
        inAppData.setUtc(String.valueOf(Utils.am(context)));
        CryptogramInfo cryptogramInfo = genCryptogramResponseData.getCryptogramInfo();
        if (cryptogramInfo == null || cryptogramInfo.getCryptogram() == null) {
            Log.m286e("InAppPayment", " cryptogram: empty");
            return null;
        }
        inAppData.setCryptogram(cryptogramInfo.getCryptogram());
        inAppData.setEci_indicator(cryptogramInfo.getEci());
        TokenInfo tokenInfo = genCryptogramResponseData.getTokenInfo();
        if (tokenInfo == null || tokenInfo.getEncTokenInfo() == null || tokenInfo.getTokenExpirationDate() == null) {
            Log.m286e("InAppPayment", " tokenInfo: empty");
            return null;
        }
        inAppData.setTokenPANExpiration(tokenInfo.getTokenExpirationDate());
        return inAppData;
    }
}
