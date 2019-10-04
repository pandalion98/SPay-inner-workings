/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McTdsMetaDataDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsTransactionResponse;
import com.samsung.android.spayfw.utils.a;
import java.util.List;
import java.util.Map;

public class McTdsTransactionsCallbck
implements a.a {
    private static final int AUTHORIZATION_FAIL_CODE = 401;
    private static final String TAG = "McTdsTransactionsCallbck";
    private static final String TDS_TAG_ERROR = "e_McTdsTransactionsCallbck";
    private static final String TDS_TAG_INFO = "i_McTdsTransactionsCallbck";
    private final long mCardMasterId;
    private i pfCallBack;

    public McTdsTransactionsCallbck(long l2, i i2) {
        this.mCardMasterId = l2;
        this.pfCallBack = i2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onComplete(int n2, Map<String, List<String>> map, byte[] arrby) {
        McTdsTransactionResponse mcTdsTransactionResponse;
        block30 : {
            int n3;
            boolean bl;
            Context context = McProvider.getContext();
            e e2 = new e();
            e2.setErrorCode(-9);
            StringBuilder stringBuilder = new StringBuilder();
            if (context == null) {
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: Err. Context missing. Cannot store authCode in db");
                c.e(TDS_TAG_ERROR, stringBuilder.toString());
                if (this.pfCallBack == null) return;
                {
                    c.e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + "McTdsTransactionsCallbck: Err. Context missing. Cannot store authCode in db");
                    e2.as(stringBuilder.toString());
                    this.pfCallBack.a(new f(this.mCardMasterId), -4, null, e2);
                    return;
                }
            }
            McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = new McTdsMetaDataDaoImpl(context);
            if (this.pfCallBack == null) {
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: Err. Missing framework callback : pfCallBackError");
            }
            c.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: statusCode : " + n2);
            switch (n2) {
                default: {
                    n3 = -36;
                    break;
                }
                case 200: {
                    n3 = 0;
                }
            }
            if (arrby == null) {
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: responseData empty received: pfCallBackError");
                c.e(TDS_TAG_ERROR, stringBuilder.toString());
                e2.setErrorCode(-7);
                e2.as(stringBuilder.toString());
                if (this.pfCallBack == null) return;
                {
                    this.pfCallBack.a(new f(this.mCardMasterId), -7, null, e2);
                    return;
                }
            }
            String string = new String(arrby);
            if (n2 == 0) {
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: network err responseData:" + string);
                c.e(TDS_TAG_ERROR, stringBuilder.toString());
                e2.setErrorCode(-7);
                e2.as(stringBuilder.toString());
                if (this.pfCallBack == null) return;
                {
                    this.pfCallBack.a(new f(this.mCardMasterId), -7, null, e2);
                    return;
                }
            }
            c.d(TAG, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: responseJsonString: " + string);
            Gson gson = new Gson();
            try {
                McTdsManager mcTdsManager = McTdsManager.getInstance(this.mCardMasterId);
                if (n2 == 401) {
                    stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: AUTHORIZATION_FAIL_CODE : pfCallBackError");
                    c.e(TDS_TAG_ERROR, stringBuilder.toString());
                    e2.setErrorCode(-8);
                    e2.as(stringBuilder.toString());
                    if (this.pfCallBack != null) {
                        this.pfCallBack.a(new f(this.mCardMasterId), -8, null, e2);
                    }
                    mcTdsManager.reRegisterIfNeeded("INVALID_AUTHENTICATION_CODE");
                    return;
                }
                mcTdsTransactionResponse = (McTdsTransactionResponse)gson.fromJson(string, McTdsTransactionResponse.class);
                if (mcTdsTransactionResponse == null || n3 != 0) {
                    stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: Error:  Empty payload : pfCallBackError");
                    c.e(TDS_TAG_ERROR, stringBuilder.toString());
                    e2.setErrorCode(-7);
                    e2.as(stringBuilder.toString());
                    if (this.pfCallBack == null) return;
                    {
                        this.pfCallBack.a(new f(this.mCardMasterId), -7, null, e2);
                        return;
                    }
                }
                if (!TextUtils.isEmpty((CharSequence)mcTdsTransactionResponse.getErrorCode())) {
                    stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: : pfCallBackError ErrorCode: " + mcTdsTransactionResponse.getErrorCode() + " resultCode:" + n3);
                    c.e(TDS_TAG_ERROR, stringBuilder.toString());
                    e2.setErrorCode(-9);
                    e2.as(stringBuilder.toString());
                    if (this.pfCallBack != null) {
                        this.pfCallBack.a(new f(this.mCardMasterId), -9, null, e2);
                    }
                    mcTdsManager.reRegisterIfNeeded(mcTdsTransactionResponse.getErrorCode());
                    return;
                }
                if (mcTdsTransactionResponse.getTransactions() == null || mcTdsTransactionResponse.getTransactions().length == 0) {
                    c.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck:  Empty transaction list obtained ");
                    stringBuilder.append("Step1");
                    bl = false;
                } else {
                    c.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " List Size : " + mcTdsTransactionResponse.getTransactions().length);
                    bl = true;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: : pfCallBackError Exception: " + exception.getMessage() + "StatusCode: " + n2);
                c.e(TDS_TAG_ERROR, stringBuilder.toString());
                e2.setErrorCode(-2);
                e2.as(stringBuilder.toString());
                if (this.pfCallBack == null) return;
                {
                    this.pfCallBack.a(new f(this.mCardMasterId), -2, null, e2);
                    return;
                }
            }
            String string2 = mcTdsTransactionResponse.getAuthenticationCode();
            String string3 = mcTdsTransactionResponse.getLastUpdatedTag();
            String string4 = mcTdsTransactionResponse.getResponseHost();
            try {
                McTdsMetaData mcTdsMetaData = (McTdsMetaData)mcTdsMetaDataDaoImpl.getData(this.mCardMasterId);
                if (!TextUtils.isEmpty((CharSequence)string4)) {
                    c.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: responseHost Received: ");
                    c.d(TAG, "responseHost: " + string4);
                    mcTdsMetaData.setTdsUrl(string4);
                }
                if (bl && !TextUtils.isEmpty((CharSequence)string3)) {
                    c.i(TAG, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: lastUpdatedTag Received: " + string3);
                    mcTdsMetaData.setLastUpdateTag(string3);
                }
                if (!TextUtils.isEmpty((CharSequence)string2)) {
                    c.i(TDS_TAG_INFO, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: authCode Received: ");
                    c.d(TAG, "authCode: " + string2);
                    mcTdsMetaData.setAuthCode(string2);
                    if (!mcTdsMetaDataDaoImpl.storeAuthCode(string2, this.mCardMasterId)) {
                        c.e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck:Err saving authCode into DB");
                    }
                }
                if (mcTdsMetaDataDaoImpl.updateData(mcTdsMetaData, this.mCardMasterId)) break block30;
                c.e(TDS_TAG_ERROR, "tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck:Err saving updated data into DB");
            }
            catch (Exception exception) {
                exception.printStackTrace();
                stringBuilder.append("tokenId: " + this.mCardMasterId + " McTdsTransactionsCallbck: pfCallBackError Exception with TDS DB: " + exception.getMessage());
                c.e(TDS_TAG_ERROR, stringBuilder.toString());
                e2.setErrorCode(-2);
                e2.as(stringBuilder.toString());
                if (this.pfCallBack == null) return;
                this.pfCallBack.a(new f(this.mCardMasterId), -2, null, e2);
                return;
            }
        }
        TransactionDetails transactionDetails = mcTdsTransactionResponse.getTransactionDetailsForApp();
        if (this.pfCallBack == null) return;
        {
            this.pfCallBack.a(new f(this.mCardMasterId), 0, transactionDetails, null);
            return;
        }
    }
}

