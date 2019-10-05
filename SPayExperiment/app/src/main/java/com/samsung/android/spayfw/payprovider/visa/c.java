/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.util.Base64
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonSyntaxException
 *  java.io.UnsupportedEncodingException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.security.MessageDigest
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.k;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.h;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetailsDao;
import com.samsung.android.spayfw.payprovider.visa.db.a;
import com.samsung.android.visasdk.facade.VisaPaymentSDK;
import com.samsung.android.visasdk.facade.VisaPaymentSDKImpl;
import com.samsung.android.visasdk.facade.data.ApduResponse;
import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.EnrollPanRequest;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import com.samsung.android.visasdk.facade.data.ProvisionAckRequest;
import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.ReplenishAckRequest;
import com.samsung.android.visasdk.facade.data.ReplenishRequest;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TransactionError;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import com.samsung.android.visasdk.facade.data.UpdateReason;
import com.samsung.android.visasdk.facade.data.VerifyingEntity;
import com.samsung.android.visasdk.facade.data.VerifyingType;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.facade.exception.TokenKeyInvalidException;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class c {
    private static Map<String, a> zO = null;
    private Context mContext;
    private VisaTokenDetailsDao zG;
    private VisaPaymentSDK zN;

    public c(Context context, VisaTokenDetailsDao visaTokenDetailsDao) {
        this.mContext = context;
        this.zG = visaTokenDetailsDao;
        VisaPaymentSDKImpl.initialize(context, com.samsung.android.spayfw.utils.c.getDbPassword());
        this.eE();
        this.init();
    }

    private int Q(int n2) {
        if (n2 <= 0) {
            return (int)1.0;
        }
        if (n2 == 1) {
            return 0;
        }
        double d2 = 0.25 * (double)n2;
        if (n2 <= 15) {
            d2 += 1.0;
        }
        Log.d("VisaPayProviderSdk", "calculatereplenishpmts - maxPmts= " + n2 + " replenishPmts = " + (int)d2);
        return (int)d2;
    }

    private void deleteEntry(String string) {
        c c2 = this;
        synchronized (c2) {
            if (zO != null && zO.get((Object)string) != null) {
                zO.remove((Object)string);
            }
            return;
        }
    }

    private void eE() {
        try {
            if (this.zN == null) {
                this.zN = VisaPaymentSDKImpl.getInstance(c.eG());
            }
            return;
        }
        catch (Exception exception) {
            Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int eF() {
        c c2 = this;
        synchronized (c2) {
            block4 : {
                if (zO != null) break block4;
                zO = new HashMap();
                return -1;
            }
            int n2 = zO.size();
            return n2;
        }
    }

    private static Bundle eG() {
        if (!"GB".equals((Object)com.samsung.android.spayfw.utils.h.fP())) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean("pdolValues", true);
        return bundle;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private String getEmailAddressHash(String string) {
        String string2;
        try {
            String string3;
            String string4 = string.toLowerCase();
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
            byte[] arrby = string4.getBytes();
            byte[] arrby2 = null;
            for (int i2 = 0; i2 < 1000; ++i2) {
                messageDigest.reset();
                messageDigest.update(arrby);
                arrby2 = messageDigest.digest();
                arrby = arrby2;
            }
            messageDigest.reset();
            messageDigest.update(string4.getBytes());
            byte[] arrby3 = messageDigest.digest();
            byte[] arrby4 = new byte[arrby3.length + arrby2.length];
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby4, (int)0, (int)arrby3.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)arrby3.length, (int)arrby2.length);
            messageDigest.reset();
            messageDigest.update(arrby4);
            string2 = string3 = Base64.encodeToString((byte[])messageDigest.digest(), (int)11);
        }
        catch (Exception exception) {
            string2 = null;
            Exception exception2 = exception;
            Log.c("VisaPayProviderSdk", exception2.getMessage(), exception2);
            return string2;
        }
        Log.d("VisaPayProviderSdk", " emailHash: " + string2);
        return string2;
        {
            catch (Exception exception) {}
        }
    }

    private void init() {
        if (this.eF() == -1) {
            List<String> list = this.zG.eM();
            Log.d("VisaPayProviderSdk", " init: " + list);
            if (list != null && !list.isEmpty()) {
                for (int i2 = 0; i2 < list.size(); ++i2) {
                    this.a(this.zG.ba((String)list.get(i2)));
                }
            }
        }
    }

    private long w(long l2) {
        Log.d("VisaPayProviderSdk", "getReplenishTsinMs - keyExpTs = " + l2);
        if (l2 <= 0L) {
            Log.e("VisaPayProviderSdk", "getReplenishTsinMs - keyExpTs  value is negative. setting default interval ");
            return 600000L + com.samsung.android.spayfw.utils.h.am(this.mContext);
        }
        if (Long.toString((long)l2).length() <= 10) {
            l2 *= 1000L;
        }
        Log.d("VisaPayProviderSdk", "getReplenishTsinMs - keyExpTs in milliseconds= " + l2);
        return l2;
    }

    private long x(long l2) {
        long l3;
        long l4 = com.samsung.android.spayfw.utils.h.am(this.mContext);
        Log.d("VisaPayProviderSdk", "calculatereplenishts - keyExpTs = " + l2 + " CurrnetTime: " + l4);
        if (l2 <= 0L) {
            Log.e("VisaPayProviderSdk", "calculatereplenishts - keyExpTs  value is negative. setting default interval ");
            return l4 + 600000L;
        }
        if (Long.toString((long)l2).length() <= 10) {
            Log.d("VisaPayProviderSdk", "calculatereplenishts - keyExpTs in milliseconds= " + (l2 *= 1000L));
        }
        if ((l3 = l4 + 3L * (l2 - l4) / 4L) <= l4) {
            Log.i("VisaPayProviderSdk", "Replenish time is <= currnetTime. Setting default Replenish Time600000");
            l3 = l4 + 600000L;
        }
        Log.d("VisaPayProviderSdk", "calculatereplenishts - replenishTs = " + l3);
        return l3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(f f2, JsonObject jsonObject, TokenStatus tokenStatus, k k2) {
        int n2 = -5;
        this.eE();
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "updateTokenStatus:Sdk instance is null");
            return -4;
        }
        if (f2 == null || f2.cn() == null) {
            Log.e("VisaPayProviderSdk", "updateTokenStatus:provider token key is null");
            return -4;
        }
        String string = null;
        if (tokenStatus != null) {
            string = tokenStatus.getCode();
        }
        if (string == null) {
            Log.e("VisaPayProviderSdk", "updateTokenStatus:status code is null");
            return -4;
        }
        String string2 = f2.cn();
        TokenKey tokenKey = new TokenKey(f2.cm());
        a a2 = this.zG.ba(string2);
        if (a2 == null) {
            Log.i("VisaPayProviderSdk", "updateTokenStatus:record is null for providerTokenKey: " + string2);
            return -4;
        }
        f2.setTrTokenId(a2.getTrTokenId());
        try {
            String string3 = this.zN.getTokenStatus(tokenKey);
            Log.d("VisaPayProviderSdk", "VisaTokenStatus: " + string3);
            UpdateReason updateReason = new UpdateReason();
            if (string.equals((Object)"SUSPENDED")) {
                this.zN.suspendToken(tokenKey, updateReason);
                h.a(this.mContext, f2);
                a2.y(-1L);
                a2.z(-1L);
                a2.setMaxPmts(-1);
                a2.R(-1);
                this.zG.d(a2);
                this.a(a2);
                Log.d("VisaPayProviderSdk", "updateTokenStatus:token suspended so replenish values set to zero ");
                return 0;
            } else if (string.equals((Object)"ACTIVE")) {
                this.zN.resumeToken(tokenKey);
                if (k2 != null && "SUSPENDED".equals((Object)string3)) {
                    Log.d("VisaPayProviderSdk", "token resumed so triggering replenish: trTokenId: " + a2.getTrTokenId() + " providerKey: " + string2);
                    k2.a(f2);
                }
                if (k2 == null) return 0;
                if (!"INACTIVE".equals((Object)string3)) return 0;
                Log.d("VisaPayProviderSdk", "token resumed so setup replenish alarm: trTokenId: " + a2.getTrTokenId() + " providerKey: " + string2);
                this.b(a2.getTrTokenId(), f2, k2);
                return 0;
            } else if (string.equals((Object)"DISPOSED")) {
                Log.d("VisaPayProviderSdk", "updateTokenStatus:Sdk: deleting token from sdk:tokenId " + a2.getTrTokenId());
                h.a(this.mContext, f2);
                this.zN.deleteToken(tokenKey);
                this.zG.bb(string2);
                this.deleteEntry(string2);
                return 0;
            } else {
                Log.d("VisaPayProviderSdk", "updateTokenStatus:Sdk: statusCode " + string);
            }
            return 0;
        }
        catch (TokenInvalidException tokenInvalidException) {
            Log.c("VisaPayProviderSdk", tokenInvalidException.getMessage(), (Throwable)((Object)tokenInvalidException));
            return n2;
        }
        catch (TokenKeyInvalidException tokenKeyInvalidException) {
            tokenKeyInvalidException.printStackTrace();
            return n2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return -4;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public Bundle a(int n2, f f2) {
        Map<String, String> map;
        this.eE();
        Bundle bundle = new Bundle();
        if (f2 == null) {
            Log.e("VisaPayProviderSdk", "stopNfcPay ProviderTokenKey is null");
            bundle.putShort("nfcApduErrorCode", (short)1);
            return bundle;
        }
        TokenKey tokenKey = new TokenKey(f2.cm());
        TransactionStatus transactionStatus = this.zN.processTransactionComplete(tokenKey);
        Log.d("VisaPayProviderSdk", "stopNfcPay: reason : " + n2 + ", sdk ret = " + (Object)((Object)transactionStatus.getError()));
        short s2 = transactionStatus.getError() != TransactionError.NO_ERROR ? (short)3 : 2;
        bundle.putShort("nfcApduErrorCode", s2);
        if (transactionStatus.getError() == TransactionError.NO_ERROR && transactionStatus.isTapNGoAllowed()) {
            bundle.putInt("tapNGotransactionErrorCode", 0);
        } else if (transactionStatus.getError() == TransactionError.NO_AUTH_AMOUNT_REQ_NOT_SATISFIED) {
            bundle.putInt("tapNGotransactionErrorCode", -101);
        } else if (transactionStatus.getError() == TransactionError.NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED) {
            bundle.putInt("tapNGotransactionErrorCode", -103);
        } else if (transactionStatus.getError() == TransactionError.NO_AUTH_CURRENCY_REQ_NOT_SATISFIED) {
            bundle.putInt("tapNGotransactionErrorCode", -102);
        }
        if ((map = transactionStatus.getPdolValues()) != null && !map.isEmpty()) {
            Bundle bundle2 = new Bundle();
            for (Map.Entry entry : map.entrySet()) {
                bundle2.putString((String)entry.getKey(), (String)entry.getValue());
            }
            bundle.putBundle("pdolValues", bundle2);
        }
        return bundle;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public com.samsung.android.spayfw.payprovider.c a(TokenKey tokenKey) {
        this.eE();
        com.samsung.android.spayfw.payprovider.c c2 = new com.samsung.android.spayfw.payprovider.c();
        c2.setErrorCode(0);
        if (this.zN == null || tokenKey == null) {
            if (this.zN == null) {
                Log.e("VisaPayProviderSdk", "getReplenishmentRequestData:Sdk instance is null");
            } else {
                Log.e("VisaPayProviderSdk", "getReplenishmentRequestData:tokenId is null");
            }
            c2.setErrorCode(-4);
            return c2;
        }
        Log.d("VisaPayProviderSdk", "getReplenishmentRequestData:tokenId: " + tokenKey);
        try {
            ReplenishRequest replenishRequest = this.zN.constructReplenishRequest(tokenKey);
            if (replenishRequest == null) return c2;
            new JsonObject();
            Gson gson = new Gson();
            String string = gson.toJson((Object)replenishRequest);
            JsonObject jsonObject = (JsonObject)gson.fromJson(string, JsonObject.class);
            Log.d("VisaPayProviderSdk", "getReplenishmentRequestData: jsonData: " + string);
            c2.a(jsonObject);
            Log.d("VisaPayProviderSdk", "getReplenishmentRequestData:data: " + (Object)c2.ch());
            c2.setErrorCode(0);
            return c2;
        }
        catch (TokenInvalidException tokenInvalidException) {
            Log.c("VisaPayProviderSdk", tokenInvalidException.getMessage(), (Throwable)((Object)tokenInvalidException));
            Log.e("VisaPayProviderSdk", "constructReplenishRequest returns exception");
            c2.setErrorCode(-2);
            return c2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c2.setErrorCode(-2);
            return c2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public e a(String string, JsonObject jsonObject) {
        this.eE();
        e e2 = new e();
        e2.setErrorCode(0);
        if (this.zN == null || string == null || this.zG == null) {
            Log.e("VisaPayProviderSdk", "setProvisionResponse invalid input");
            e2.setErrorCode(-4);
            return e2;
        }
        new ProvisionResponse();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        try {
            ProvisionResponse provisionResponse = (ProvisionResponse)gson.fromJson((JsonElement)jsonObject, ProvisionResponse.class);
            Log.d("VisaPayProviderSdk", "ProvisionToken: " + gson.toJson((Object)provisionResponse));
            TokenKey tokenKey = this.zN.storeProvisionedToken(provisionResponse, null);
            if (tokenKey != null) {
                Log.d("VisaPayProviderSdk", "setProvisionResponse : trTokenId: " + string + " tokenKey: " + tokenKey.getTokenId());
                f f2 = new f(tokenKey.getTokenId());
                e2.setProviderTokenKey(f2);
                ProvisionAckRequest provisionAckRequest = this.zN.constructProvisionAck(tokenKey);
                if (provisionAckRequest != null) {
                    Gson gson2 = new Gson();
                    String string2 = gson2.toJson((Object)provisionAckRequest);
                    Log.d("VisaPayProviderSdk", "constructProvisionAck : " + string2);
                    e2.b((JsonObject)gson2.fromJson(string2, JsonObject.class));
                } else {
                    e2.setErrorCode(-2);
                    Log.w("VisaPayProviderSdk", "constructProvisionAck returns null");
                }
                if (provisionResponse == null) return e2;
                if (provisionResponse.getTokenInfo().getHceData() == null) return e2;
                if (provisionResponse.getTokenInfo().getHceData().getDynParams() == null) return e2;
                int n2 = provisionResponse.getTokenInfo().getHceData().getDynParams().getMaxPmts();
                long l2 = provisionResponse.getTokenInfo().getHceData().getDynParams().getKeyExpTS();
                Log.d("VisaPayProviderSdk", "maxPmts: " + n2);
                a a2 = new a(string, f2.cn(), n2, this.Q(n2), this.w(l2), this.x(l2), com.samsung.android.spayfw.utils.h.am(this.mContext));
                this.zG.c(a2);
                this.a(a2);
                return e2;
            }
            Log.e("VisaPayProviderSdk", "storeProvisionedToken returns null");
            e2.setErrorCode(-2);
            return e2;
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            e2.setErrorCode(-2);
            Log.c("VisaPayProviderSdk", jsonSyntaxException.getMessage(), jsonSyntaxException);
            return e2;
        }
        catch (Exception exception) {
            e2.setErrorCode(-2);
            exception.printStackTrace();
        }
        return e2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public e a(String string, f f2, JsonObject jsonObject, TokenStatus tokenStatus, k k2) {
        this.eE();
        e e2 = new e();
        e2.setErrorCode(0);
        if (this.zN == null || string == null || f2 == null) {
            Log.e("VisaPayProviderSdk", "replenishToken:Sdk instance or tokenId is null");
            e2.setErrorCode(-4);
            return e2;
        }
        if (jsonObject == null) {
            Log.e("VisaPayProviderSdk", "replenishToken:data is null");
            e2.setErrorCode(-4);
            return e2;
        }
        new ProvisionResponse();
        Gson gson = new Gson();
        try {
            ProvisionResponse provisionResponse = (ProvisionResponse)gson.fromJson((JsonElement)jsonObject, ProvisionResponse.class);
            Log.d("VisaPayProviderSdk", "replenishToken:ProvisionResponse " + gson.toJson((Object)provisionResponse));
            if (provisionResponse == null || provisionResponse.getTokenInfo() == null) {
                Log.e("VisaPayProviderSdk", "replenishToken:incoming data is invalid");
                e2.setErrorCode(-2);
                return e2;
            }
            TokenInfo tokenInfo = provisionResponse.getTokenInfo();
            if (tokenInfo == null || tokenInfo.getHceData() == null || tokenInfo.getHceData().getDynParams() == null || tokenInfo.getHceData().getDynParams().getEncKeyInfo() == null) {
                Log.e("VisaPayProviderSdk", "replenishToken:incoming data(tokenInfo) is invalid");
                e2.setErrorCode(-2);
                return e2;
            }
            Log.d("VisaPayProviderSdk", "replenishToken:TokenInfo " + gson.toJson((Object)tokenInfo));
            int n2 = tokenInfo.getHceData().getDynParams().getMaxPmts();
            long l2 = tokenInfo.getHceData().getDynParams().getKeyExpTS();
            Log.d("VisaPayProviderSdk", "maxPmts " + n2);
            Log.d("VisaPayProviderSdk", "keyExpTs " + l2);
            TokenKey tokenKey = new TokenKey(f2.cm());
            if (!this.zN.processReplenishmentResponse(tokenKey, tokenInfo)) {
                Log.e("VisaPayProviderSdk", "processReplenishmentResponse: ret false");
                e2.setErrorCode(-2);
                return e2;
            }
            Log.i("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest: ");
            ReplenishAckRequest replenishAckRequest = this.zN.constructReplenishAcknowledgementRequest(tokenKey);
            if (replenishAckRequest == null) {
                Log.e("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest returns null");
                e2.setErrorCode(-5);
                return e2;
            }
            Gson gson2 = new Gson();
            String string2 = gson2.toJson((Object)replenishAckRequest);
            Log.d("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest : " + string2);
            e2.b((JsonObject)gson2.fromJson(string2, JsonObject.class));
            long l3 = this.w(l2);
            int n3 = this.Q(n2);
            long l4 = this.x(l3);
            a a2 = this.zG.aZ(string);
            if (a2 == null) {
                Log.e("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest returns null");
                e2.setErrorCode(-5);
                return e2;
            }
            a2.setMaxPmts(n2);
            a2.R(n3);
            a2.y(l3);
            a2.z(l4);
            this.zG.d(a2);
            this.a(a2);
            Log.d("VisaPayProviderSdk", "updateReplenishStatus : maxPmts:  " + n2 + " replenishPmts: " + n3 + "keyExpTs: " + l3 + " replenishExpTs: " + l4);
            a a3 = this.zG.aZ(string);
            if (a3 != null) {
                Log.d("VisaPayProviderSdk", "updateReplenishStatus from db : " + a3.dump());
            }
            this.b(string, f2, k2);
            f2.setTrTokenId(string);
            k2.b(f2);
            return e2;
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            Log.c("VisaPayProviderSdk", jsonSyntaxException.getMessage(), jsonSyntaxException);
            Log.e("VisaPayProviderSdk", "replenishToken = JsonSyntaxException");
            e2.setErrorCode(-4);
            return e2;
        }
        catch (TokenInvalidException tokenInvalidException) {
            Log.c("VisaPayProviderSdk", tokenInvalidException.getMessage(), (Throwable)((Object)tokenInvalidException));
            Log.e("VisaPayProviderSdk", "replenishToken = TokenInvalidException");
            e2.setErrorCode(-5);
            return e2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            e2.setErrorCode(-2);
            return e2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public PaymentDataRequest a(TokenKey tokenKey, String string, boolean bl) {
        Log.d("VisaPayProviderSdk", "getInAppRequestData");
        this.eE();
        if (string == null || tokenKey == null) {
            Log.e("VisaPayProviderSdk", "getInAppRequestData, input is null");
            return null;
        }
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "getInAppRequestData, sdk is null");
            return null;
        }
        try {
            String string2;
            VisaPaymentSDK visaPaymentSDK = this.zN;
            if (bl) {
                string2 = "RECURRING";
                return visaPaymentSDK.constructPaymentDataRequest(null, tokenKey, string, string2);
            }
            string2 = "ECOM";
            return visaPaymentSDK.constructPaymentDataRequest(null, tokenKey, string, string2);
        }
        catch (Exception exception) {
            Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
            return null;
        }
    }

    public void a(a a2) {
        c c2 = this;
        synchronized (c2) {
            if (zO != null && a2 != null && a2.eH() != null) {
                zO.put((Object)a2.eH(), (Object)a2);
                Log.d("VisaPayProviderSdk", " addEntry: " + a2.dump());
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void a(TokenKey tokenKey, boolean bl, boolean bl2) {
        String string;
        VisaPaymentSDK visaPaymentSDK;
        Log.d("VisaPayProviderSdk", "processInAppTransactionComplete " + bl2);
        this.eE();
        if (tokenKey == null) {
            Log.e("VisaPayProviderSdk", "tokenKey is null ");
            return;
        }
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "sdk is null");
            return;
        }
        try {
            visaPaymentSDK = this.zN;
            string = bl ? "RECURRING" : "ECOM";
        }
        catch (Exception exception) {
            Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
            return;
        }
        visaPaymentSDK.processInAppTransactionComplete(tokenKey, string, bl2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void a(String string, f f2, k k2) {
        String string2 = this.f(f2);
        if (string2 == null) {
            Log.e("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus is null");
            return;
        } else {
            Log.d("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus " + string2);
            if (!string2.equals((Object)"ACTIVE")) {
                Log.e("VisaPayProviderSdk", "Not Replenishing as Token is Suspended or Pending.");
                return;
            }
            if (string == null) return;
            {
                a a2 = this.zG.ba(string);
                if (a2 == null) {
                    Log.e("VisaPayProviderSdk", "no token record. ignore updateAndCheckReplenishStatus ");
                    return;
                }
                int n2 = a2.getMaxPmts();
                int n3 = a2.eI();
                Log.d("VisaPayProviderSdk", "current: maxPmts " + n2 + "replenishPmts: " + n3);
                int n4 = n2 - 1;
                if (n4 < 0) {
                    n4 = 0;
                }
                this.zG.a(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.MAX_PMTS, n4, string);
                Log.d("VisaPayProviderSdk", " New current: maxPmts " + n4 + " replenishPmts: " + n3);
                a a3 = this.aX(string);
                if (a3 != null) {
                    a3.setMaxPmts(n4);
                    this.a(a3);
                }
                if (n4 > n3) return;
                {
                    Log.d("VisaPayProviderSdk", "updateAndCheckReplenishStatus: triggering Replenish request ");
                    f2.setTrTokenId(a2.getTrTokenId());
                    k2.a(f2);
                    return;
                }
            }
        }
    }

    boolean a(TokenKey tokenKey, int n2) {
        Log.d("VisaPayProviderSdk", "isPresentationModeSupported " + n2);
        if (n2 == 2) {
            this.eE();
            if (tokenKey == null) {
                Log.e("VisaPayProviderSdk", "tokenKey is null ");
                return false;
            }
            if (this.zN == null) {
                Log.e("VisaPayProviderSdk", "sdk is null");
                return false;
            }
            try {
                boolean bl = this.zN.isMstSupported(tokenKey);
                return bl;
            }
            catch (Exception exception) {
                Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
                return false;
            }
        }
        return true;
    }

    public boolean aW(String string) {
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public a aX(String string) {
        c c2 = this;
        synchronized (c2) {
            if (zO == null) return null;
            if (zO.isEmpty()) return null;
            return (a)zO.get((Object)string);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public com.samsung.android.spayfw.payprovider.c b(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        com.samsung.android.spayfw.payprovider.c c2;
        block37 : {
            this.eE();
            c2 = new com.samsung.android.spayfw.payprovider.c();
            c2.setErrorCode(0);
            Log.d("VisaPayProviderSdk", "getEnrollRequestData: enter ");
            if (enrollCardInfo == null || this.zN == null) {
                Log.e("VisaPayProviderSdk", "getEnrollRequestData: input is invalid ");
                c2.setErrorCode(-4);
                return c2;
            }
            Log.m("VisaPayProviderSdk", "EnrollCardInfo:" + enrollCardInfo.toString());
            this.zN.getEnrollPANTemplate();
            JsonObject jsonObject = new JsonObject();
            if (enrollCardInfo instanceof EnrollCardPanInfo) {
                try {
                    EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo)enrollCardInfo;
                    JsonObject jsonObject2 = new JsonObject();
                    JsonObject jsonObject3 = new JsonObject();
                    JsonObject jsonObject4 = new JsonObject();
                    if (billingInfo != null) {
                        Log.m("VisaPayProviderSdk", "BillingInfo:" + billingInfo.toString());
                        if (billingInfo.getStreet1() != null && !billingInfo.getStreet1().equals((Object)"")) {
                            Log.d("VisaPayProviderSdk", "street1:" + billingInfo.getStreet1());
                            jsonObject3.addProperty("line1", billingInfo.getStreet1());
                        }
                        if (billingInfo.getStreet2() != null && !billingInfo.getStreet2().equals((Object)"")) {
                            Log.d("VisaPayProviderSdk", "street2:" + billingInfo.getStreet2());
                            jsonObject3.addProperty("line2", billingInfo.getStreet2());
                        }
                        if (billingInfo.getCity() != null && !billingInfo.getCity().equals((Object)"")) {
                            Log.d("VisaPayProviderSdk", "city:" + billingInfo.getCity());
                            jsonObject3.addProperty("city", billingInfo.getCity());
                        }
                        if (billingInfo.getState() != null && !billingInfo.getState().equals((Object)"")) {
                            Log.d("VisaPayProviderSdk", "state:" + billingInfo.getState());
                            jsonObject3.addProperty("state", billingInfo.getState());
                        }
                        if (billingInfo.getCountry() != null && !billingInfo.getCountry().equals((Object)"")) {
                            Log.d("VisaPayProviderSdk", "country:" + billingInfo.getCountry());
                            jsonObject3.addProperty("country", billingInfo.getCountry());
                        }
                        if (billingInfo.getZip() != null && !billingInfo.getZip().equals((Object)"")) {
                            Log.d("VisaPayProviderSdk", "zip:" + billingInfo.getZip());
                            jsonObject3.addProperty("postalCode", billingInfo.getZip());
                        }
                        jsonObject2.add("billingAddress", (JsonElement)jsonObject3);
                    }
                    if (enrollCardPanInfo.getExpMonth() != null && !enrollCardPanInfo.getExpMonth().isEmpty()) {
                        jsonObject4.addProperty("month", enrollCardPanInfo.getExpMonth());
                    }
                    if (enrollCardPanInfo.getExpYear() != null && !enrollCardPanInfo.getExpYear().isEmpty()) {
                        jsonObject4.addProperty("year", "20" + enrollCardPanInfo.getExpYear());
                    }
                    jsonObject2.addProperty("accountNumber", enrollCardPanInfo.getPAN());
                    jsonObject2.addProperty("cvv2", enrollCardPanInfo.getCVV());
                    jsonObject2.addProperty("name", enrollCardPanInfo.getName());
                    if (jsonObject4.has("month") && jsonObject4.has("year")) {
                        jsonObject2.add("expirationDate", (JsonElement)jsonObject4);
                    }
                    Log.m("VisaPayProviderSdk", "paymentInstrument: " + jsonObject2.toString());
                    byte[] arrby = jsonObject2.toString().getBytes("utf-8");
                    byte[] arrby2 = new byte[1 + arrby.length];
                    arrby2[0] = 21;
                    System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)1, (int)arrby.length);
                    EnrollPanRequest enrollPanRequest = this.zN.constructEnrollRequest(arrby2);
                    if (enrollPanRequest == null) {
                        Log.e("VisaPayProviderSdk", "constructEnrollRequest null");
                        c2.setErrorCode(-2);
                        break block37;
                    }
                    jsonObject.addProperty("encPaymentInstrument", enrollPanRequest.getEncPaymentInstrument());
                    Log.d("VisaPayProviderSdk", "getEnrollRequestData: " + enrollPanRequest.getEncPaymentInstrument());
                    c2.a(jsonObject);
                }
                catch (UnsupportedEncodingException unsupportedEncodingException) {
                    c2.setErrorCode(-2);
                    Log.c("VisaPayProviderSdk", unsupportedEncodingException.getMessage(), unsupportedEncodingException);
                }
                catch (Exception exception) {
                    c2.setErrorCode(-2);
                    exception.printStackTrace();
                }
            } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
                EnrollCardReferenceInfo enrollCardReferenceInfo = (EnrollCardReferenceInfo)enrollCardInfo;
                if (enrollCardReferenceInfo.getReferenceType() != null && enrollCardReferenceInfo.getExtraEnrollData() != null) {
                    Log.d("VisaPayProviderSdk", "Card Type : " + enrollCardReferenceInfo.getReferenceType());
                    if (enrollCardReferenceInfo.getReferenceType().equals((Object)"app2app")) {
                        jsonObject.addProperty("encPaymentInstrument", new String(enrollCardReferenceInfo.getExtraEnrollData().getByteArray("enrollPayload")));
                        c2.a(jsonObject);
                    } else if (enrollCardReferenceInfo.getReferenceType().equals((Object)"referenceId")) {
                        Log.d("VisaPayProviderSdk", "Card Import enrollment");
                        try {
                            Bundle bundle = ((EnrollCardReferenceInfo)enrollCardInfo).getExtraEnrollData();
                            if (bundle != null) {
                                String string = bundle.getString("cardCvv");
                                String string2 = bundle.getString("cardExpMM");
                                String string3 = bundle.getString("cardExpYY");
                                String string4 = bundle.getString("cardZip");
                                JsonObject jsonObject5 = new JsonObject();
                                JsonObject jsonObject6 = new JsonObject();
                                JsonObject jsonObject7 = new JsonObject();
                                if (string4 != null && !string4.isEmpty()) {
                                    Log.m("VisaPayProviderSdk", "BillingInfo:" + string4);
                                    jsonObject6.addProperty("postalCode", string4);
                                    jsonObject5.add("billingAddress", (JsonElement)jsonObject6);
                                }
                                if (string2 != null && !string2.isEmpty()) {
                                    jsonObject7.addProperty("month", string2);
                                }
                                if (string3 != null && !string3.isEmpty()) {
                                    jsonObject7.addProperty("year", "20" + string3);
                                }
                                jsonObject5.addProperty("cvv2", string);
                                if (jsonObject7.has("month") && jsonObject7.has("year")) {
                                    jsonObject5.add("expirationDate", (JsonElement)jsonObject7);
                                }
                                Log.m("VisaPayProviderSdk", "paymentInstrument: " + jsonObject5.toString());
                                byte[] arrby = jsonObject5.toString().getBytes("utf-8");
                                byte[] arrby3 = new byte[1 + arrby.length];
                                arrby3[0] = 21;
                                System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)1, (int)arrby.length);
                                EnrollPanRequest enrollPanRequest = this.zN.constructEnrollRequest(arrby3);
                                if (enrollPanRequest == null) {
                                    Log.e("VisaPayProviderSdk", "constructEnrollRequest null");
                                    c2.setErrorCode(-2);
                                }
                                jsonObject.addProperty("encPaymentInstrument", enrollPanRequest.getEncPaymentInstrument());
                                Log.d("VisaPayProviderSdk", "getEnrollRequestData: " + enrollPanRequest.getEncPaymentInstrument());
                                c2.a(jsonObject);
                            } else {
                                c2.setErrorCode(-2);
                            }
                        }
                        catch (UnsupportedEncodingException unsupportedEncodingException) {
                            c2.setErrorCode(-2);
                            Log.c("VisaPayProviderSdk", unsupportedEncodingException.getMessage(), unsupportedEncodingException);
                        }
                        catch (Exception exception) {
                            c2.setErrorCode(-2);
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Log.e("VisaPayProviderSdk", "enrollCardReferenceInfo reference type or extra data is null");
                }
            }
        }
        Bundle bundle = new Bundle();
        if (enrollCardInfo.getUserEmail() != null) {
            bundle.putString("emailHash", this.getEmailAddressHash(enrollCardInfo.getUserEmail()));
        }
        c2.e(bundle);
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void b(String string, f f2, k k2) {
        String string2;
        Log.i("VisaPayProviderSdk", "Entered setup Replenish Alarm");
        a a2 = null;
        if (string != null) {
            Log.e("VisaPayProviderSdk", "trtokenid not null");
            a2 = this.zG.aZ(string);
        }
        if ((string2 = this.f(f2)) == null) {
            Log.e("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus is null");
            return;
        }
        Log.d("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus " + string2);
        if (!string2.equals((Object)"ACTIVE")) {
            Log.e("VisaPayProviderSdk", "Not Replenishing as Token is Suspended or Pending.");
            return;
        }
        if (a2 == null) return;
        Log.d("VisaPayProviderSdk", "visa token details not null");
        Log.d("VisaPayProviderSdk", "visa token details replenishts = " + a2.eK());
        Log.d("VisaPayProviderSdk", "current time  = " + com.samsung.android.spayfw.utils.h.am(this.mContext));
        if (string == null) return;
        {
            if (a2.eK() - com.samsung.android.spayfw.utils.h.am(this.mContext) > 0L && a2.getMaxPmts() > a2.eI()) {
                Log.d("VisaPayProviderSdk", "Setting up Replenish Alarm");
                h.a(this.mContext, a2.eK(), f2);
                return;
            }
        }
        Log.i("VisaPayProviderSdk", "Run Replenish Rightaway: trTokenId " + string);
        Log.d("VisaPayProviderSdk", "Visa token record: " + a2.dump());
        k2.a(f2);
    }

    public void deleteToken(TokenKey tokenKey) {
        this.eE();
        try {
            this.zN.deleteToken(tokenKey);
            return;
        }
        catch (TokenInvalidException tokenInvalidException) {
            Log.c("VisaPayProviderSdk", tokenInvalidException.getMessage(), (Throwable)((Object)tokenInvalidException));
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    String f(f f2) {
        this.eE();
        if (f2 == null || f2.cn() == null) {
            Log.e("VisaPayProviderSdk", "getTokenStatus :  token key is null");
            return null;
        }
        TokenKey tokenKey = new TokenKey(f2.cm());
        return this.zN.getTokenStatus(tokenKey);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    Bundle getTokenMetaData(TokenKey tokenKey) {
        String string;
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "sdk is null");
            return null;
        }
        if (tokenKey == null) {
            Log.e("VisaPayProviderSdk", "tokenKey is null");
            return null;
        }
        try {
            Bundle bundle = this.zN.getTokenMetaData(tokenKey);
            if (bundle == null) {
                Log.e("VisaPayProviderSdk", "metaData is null");
                return null;
            }
            string = bundle.getString("qvsdc_issuercountrycode");
            if (string == null) {
                Log.e("VisaPayProviderSdk", "issuerCountryCode is null");
                return null;
            }
        }
        catch (Exception exception) {
            Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("tokenMetadataIssuerCountryCode", string);
        return bundle;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        boolean bl;
        if (jsonObject != null) {
            new ProvisionResponse();
            ProvisionResponse provisionResponse = (ProvisionResponse)new Gson().fromJson((JsonElement)jsonObject, ProvisionResponse.class);
            if (provisionResponse == null || provisionResponse.getTokenInfo() == null) {
                Log.i("VisaPayProviderSdk", "isReplenishDataAvailable:incoming data is invalid");
                return false;
            }
            TokenInfo tokenInfo = provisionResponse.getTokenInfo();
            if (tokenInfo == null || tokenInfo.getHceData() == null || tokenInfo.getHceData().getDynParams() == null || tokenInfo.getHceData().getDynParams().getEncKeyInfo() == null) {
                Log.i("VisaPayProviderSdk", "isReplenishDataAvailable:incoming data(tokenInfo) is invalid");
                return false;
            }
            bl = true;
        } else {
            bl = false;
        }
        Log.i("VisaPayProviderSdk", "isReplenishDataAvailable:ret :" + bl);
        return bl;
    }

    public boolean prepareMstPay() {
        this.eE();
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "prepareMstPay:Sdk instance is null");
            return false;
        }
        try {
            boolean bl = this.zN.prepareMstData();
            return bl;
        }
        catch (Exception exception) {
            Log.e("VisaPayProviderSdk", "prepareMstData returns exception");
            exception.printStackTrace();
            return false;
        }
    }

    public byte[] processApdu(byte[] arrby, Bundle bundle) {
        ApduResponse apduResponse;
        this.eE();
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "processApdu:Sdk instance is null");
            return null;
        }
        Log.d("VisaPayProviderSdk", "processApdu = " + new String(arrby));
        boolean bl = this.zN.isCvmVerified();
        long l2 = System.currentTimeMillis();
        Log.d("VisaPayProviderSdk", "visa start processApdu measuretime=" + l2);
        try {
            apduResponse = this.zN.processCommandApdu(arrby, bundle, bl);
        }
        catch (Exception exception) {
            Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
            return new byte[]{105, -123};
        }
        long l3 = System.currentTimeMillis();
        Log.d("VisaPayProviderSdk", "visa end processApdu measuretime=" + l3);
        Log.d("VisaPayProviderSdk", "visa total processApdu measuretime=" + (l3 - l2));
        Log.d("VisaPayProviderSdk", "apduResponse = " + new String(apduResponse.getApduData()));
        return apduResponse.getApduData();
    }

    public void selectCard(TokenKey tokenKey) {
        this.eE();
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "selectCard:Sdk instance is null ");
            return;
        }
        try {
            this.zN.selectCard(tokenKey);
            return;
        }
        catch (TokenInvalidException tokenInvalidException) {
            Log.c("VisaPayProviderSdk", tokenInvalidException.getMessage(), (Throwable)((Object)tokenInvalidException));
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setPayAuthenticationMode(String string) {
        this.eE();
        Log.d("VisaPayProviderSdk", "setPayAuthenticationMode: " + string);
        VerifyingType verifyingType = VerifyingType.PASSCODE;
        boolean bl = true;
        if ("FP".equalsIgnoreCase(string)) {
            verifyingType = VerifyingType.OTHER_CD_CVM;
        } else if ("NONE".equalsIgnoreCase(string)) {
            verifyingType = VerifyingType.NO_CD_CVM;
            bl = false;
        }
        try {
            CvmMode cvmMode = new CvmMode(VerifyingEntity.MOBILE_APP, verifyingType);
            this.zN.setCvmVerificationMode(cvmMode);
            this.zN.setCvmVerified(bl);
            Log.d("VisaPayProviderSdk", "setPayAuthenticationMode: type " + (Object)((Object)verifyingType));
            return;
        }
        catch (Exception exception) {
            Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
            return;
        }
    }

    public void stopMstPay(boolean bl) {
        this.eE();
        Log.i("VisaPayProviderSdk", "stopMstPay: status: " + bl);
        if (this.zN == null) {
            Log.e("VisaPayProviderSdk", "stopMstPay:Sdk instance is null");
            return;
        }
        try {
            this.zN.transactionComplete(bl);
            return;
        }
        catch (Exception exception) {
            Log.c("VisaPayProviderSdk", exception.getMessage(), exception);
            return;
        }
    }
}

