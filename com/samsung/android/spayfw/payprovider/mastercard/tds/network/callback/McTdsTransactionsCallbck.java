package com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsTransactionResponse;
import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient.AsyncNetworkHttpClient;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;

public class McTdsTransactionsCallbck implements AsyncNetworkHttpClient {
    private static final int AUTHORIZATION_FAIL_CODE = 401;
    private static final String TAG = "McTdsTransactionsCallbck";
    private static final String TDS_TAG_ERROR = "e_McTdsTransactionsCallbck";
    private static final String TDS_TAG_INFO = "i_McTdsTransactionsCallbck";
    private final long mCardMasterId;
    private TransactionResponse pfCallBack;

    public McTdsTransactionsCallbck(long j, TransactionResponse transactionResponse) {
        this.mCardMasterId = j;
        this.pfCallBack = transactionResponse;
    }

    public void onComplete(int i, Map<String, List<String>> map, byte[] bArr) {
        Context context = McProvider.getContext();
        String str = "INVALID_AUTHENTICATION_CODE";
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(-9);
        StringBuilder stringBuilder = new StringBuilder();
        if (context == null) {
            stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: Err. Context missing. Cannot store authCode in db");
            Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
            if (this.pfCallBack != null) {
                Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + "McTdsTransactionsCallbck: Err. Context missing. Cannot store authCode in db");
                providerResponseData.as(stringBuilder.toString());
                this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -4, null, providerResponseData);
                return;
            }
            return;
        }
        int i2;
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(context);
        if (this.pfCallBack == null) {
            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: Err. Missing framework callback : pfCallBackError");
        }
        Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: statusCode : " + i);
        switch (i) {
            case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                i2 = 0;
                break;
            default:
                i2 = -36;
                break;
        }
        if (bArr == null) {
            stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: responseData empty received: pfCallBackError");
            Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
            providerResponseData.setErrorCode(-7);
            providerResponseData.as(stringBuilder.toString());
            if (this.pfCallBack != null) {
                this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -7, null, providerResponseData);
                return;
            }
            return;
        }
        String str2 = new String(bArr);
        if (i == 0) {
            stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: network err responseData:" + str2);
            Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
            providerResponseData.setErrorCode(-7);
            providerResponseData.as(stringBuilder.toString());
            if (this.pfCallBack != null) {
                this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -7, null, providerResponseData);
                return;
            }
            return;
        }
        Log.m285d(TAG, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: responseJsonString: " + str2);
        Gson gson = new Gson();
        try {
            McTdsManager instance = McTdsManager.getInstance(this.mCardMasterId);
            if (i == AUTHORIZATION_FAIL_CODE) {
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: AUTHORIZATION_FAIL_CODE : pfCallBackError");
                Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
                providerResponseData.setErrorCode(-8);
                providerResponseData.as(stringBuilder.toString());
                if (this.pfCallBack != null) {
                    this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -8, null, providerResponseData);
                }
                instance.reRegisterIfNeeded(str);
                return;
            }
            McTdsTransactionResponse mcTdsTransactionResponse = (McTdsTransactionResponse) gson.fromJson(str2, McTdsTransactionResponse.class);
            if (mcTdsTransactionResponse == null || i2 != 0) {
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: Error:  Empty payload : pfCallBackError");
                Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
                providerResponseData.setErrorCode(-7);
                providerResponseData.as(stringBuilder.toString());
                if (this.pfCallBack != null) {
                    this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -7, null, providerResponseData);
                }
            } else if (TextUtils.isEmpty(mcTdsTransactionResponse.getErrorCode())) {
                Object obj;
                if (mcTdsTransactionResponse.getTransactions() == null || mcTdsTransactionResponse.getTransactions().length == 0) {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck:  Empty transaction list obtained ");
                    stringBuilder.append("Step1");
                    obj = null;
                } else {
                    Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " List Size : " + mcTdsTransactionResponse.getTransactions().length);
                    i2 = 1;
                }
                str = mcTdsTransactionResponse.getAuthenticationCode();
                String lastUpdatedTag = mcTdsTransactionResponse.getLastUpdatedTag();
                String responseHost = mcTdsTransactionResponse.getResponseHost();
                try {
                    McTdsMetaData mcTdsMetaData = (McTdsMetaData) mcTdsMetaDataDaoImpl.getData(this.mCardMasterId);
                    if (!TextUtils.isEmpty(responseHost)) {
                        Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: responseHost Received: ");
                        Log.m285d(TAG, "responseHost: " + responseHost);
                        mcTdsMetaData.setTdsUrl(responseHost);
                    }
                    if (!(obj == null || TextUtils.isEmpty(lastUpdatedTag))) {
                        Log.m287i(TAG, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: lastUpdatedTag Received: " + lastUpdatedTag);
                        mcTdsMetaData.setLastUpdateTag(lastUpdatedTag);
                    }
                    if (!TextUtils.isEmpty(str)) {
                        Log.m287i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: authCode Received: ");
                        Log.m285d(TAG, "authCode: " + str);
                        mcTdsMetaData.setAuthCode(str);
                        if (!mcTdsMetaDataDaoImpl.storeAuthCode(str, this.mCardMasterId)) {
                            Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck:Err saving authCode into DB");
                        }
                    }
                    if (!mcTdsMetaDataDaoImpl.updateData(mcTdsMetaData, this.mCardMasterId)) {
                        Log.m286e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck:Err saving updated data into DB");
                    }
                    TransactionDetails transactionDetailsForApp = mcTdsTransactionResponse.getTransactionDetailsForApp();
                    if (this.pfCallBack != null) {
                        this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), 0, transactionDetailsForApp, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: pfCallBackError Exception with TDS DB: " + e.getMessage());
                    Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
                    providerResponseData.setErrorCode(-2);
                    providerResponseData.as(stringBuilder.toString());
                    if (this.pfCallBack != null) {
                        this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -2, null, providerResponseData);
                    }
                }
            } else {
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: : pfCallBackError ErrorCode: " + mcTdsTransactionResponse.getErrorCode() + " resultCode:" + i2);
                Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
                providerResponseData.setErrorCode(-9);
                providerResponseData.as(stringBuilder.toString());
                if (this.pfCallBack != null) {
                    this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -9, null, providerResponseData);
                }
                instance.reRegisterIfNeeded(mcTdsTransactionResponse.getErrorCode());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: : pfCallBackError Exception: " + e2.getMessage() + "StatusCode: " + i);
            Log.m286e(TDS_TAG_ERROR, stringBuilder.toString());
            providerResponseData.setErrorCode(-2);
            providerResponseData.as(stringBuilder.toString());
            if (this.pfCallBack != null) {
                this.pfCallBack.m344a(new ProviderTokenKey(this.mCardMasterId), -2, null, providerResponseData);
            }
        }
    }
}
