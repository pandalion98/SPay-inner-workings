/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.location.Location
 *  android.os.Bundle
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  java.io.Serializable
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.security.SecureRandom
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Locale
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  java.util.TimeZone
 *  java.util.TreeMap
 */
package com.samsung.android.spayfw.payprovider.amex;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.EndTransactionResponse;
import com.americanexpress.sdkmodulelib.model.ProcessInAppPaymentResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.TokenDataVersionResponse;
import com.americanexpress.sdkmodulelib.model.TokenStatusResponse;
import com.americanexpress.sdkmodulelib.payment.NFCPaymentProviderProxy;
import com.americanexpress.sdkmodulelib.payment.NFCPaymentProviderProxyImpl;
import com.americanexpress.sdkmodulelib.payment.TokenDataManager;
import com.americanexpress.sdkmodulelib.payment.TokenDataManagerImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.k;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.amex.AmexUtils;
import com.samsung.android.spayfw.payprovider.amex.b;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.c;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.h;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

public class a
extends PaymentNetworkProvider {
    private EnrollCardInfo kQ;
    private BillingInfo mBillingInfo;
    private Context mContext;
    private AmexTAController pg;
    private NFCPaymentProviderProxy ph;
    private TokenDataManager pi;
    private f pj = null;
    private CertificateInfo pl;
    private CertificateInfo pm;
    private boolean pn = false;
    private boolean po = false;
    private SharedPreferences pp;
    private String pq;
    private boolean pr = false;
    private k ps;

    public a(Context context, String string, f f2) {
        super(context, string);
        if (context == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "context is null");
            return;
        }
        this.mContext = context;
        this.mTAController = AmexTAController.C(this.mContext);
        this.pg = (AmexTAController)this.mTAController;
        this.pi = new TokenDataManagerImpl();
        this.ph = new NFCPaymentProviderProxyImpl();
        this.pp = this.mContext.getSharedPreferences("AmexStorage", 0);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Bundle a(ProvisionTokenInfo var1_1) {
        block27 : {
            var2_2 = new ArrayList();
            var3_3 = new Bundle();
            var4_4 = new ArrayList();
            var3_3.putSerializable("riskData", (Serializable)var2_2);
            var5_5 = var1_1.getActivationParams();
            var2_2.add((Object)new RiskDataParam("networkOperator", DeviceInfo.getNetworkOperatorName(this.mContext)));
            var7_6 = com.samsung.android.spayfw.utils.h.aj(this.mContext) != false ? "wifi" : "cellular";
            var2_2.add((Object)new RiskDataParam("networkType", var7_6));
            var2_2.add((Object)new RiskDataParam("ipAddress", DeviceInfo.getLocalIpAddress()));
            var10_7 = TimeZone.getDefault().getID();
            if (var10_7 != null && !var10_7.isEmpty()) {
                var2_2.add((Object)new RiskDataParam("deviceTimezone", var10_7));
            }
            var2_2.add((Object)new RiskDataParam("timezoneSetByCarrier", DeviceInfo.getAutoTimeZone(this.mContext)));
            var12_8 = DeviceInfo.getLastKnownLocation(this.mContext);
            if (var12_8 != null) {
                var2_2.add((Object)new RiskDataParam("deviceLatitude", var12_8.getLatitude() + ""));
                var2_2.add((Object)new RiskDataParam("deviceLongitude", var12_8.getLongitude() + ""));
            }
            var2_2.add((Object)new RiskDataParam("locale", Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry()));
            var14_9 = new com.samsung.android.spayfw.fraud.b(this.mContext);
            var2_2.add((Object)new RiskDataParam("deviceScore", var14_9.E((int)Integer.MAX_VALUE).nk));
            var2_2.add((Object)new RiskDataParam("accountScore", var5_5.get((Object)"accountScore")));
            try {
                var54_10 = (String)var5_5.get((Object)"userAccountFirstCreated");
                if (var54_10 != null && !var54_10.isEmpty()) {
                    var55_11 = Integer.parseInt((String)var54_10);
                    var56_12 = var55_11 / 7 + "";
                    var2_2.add((Object)new RiskDataParam("accountTenureOnFile", var56_12));
                    var2_2.add((Object)new RiskDataParam("accountIdTenure", var56_12));
                    if (var55_11 < 30) {
                        var4_4.add((Object)"LT");
                    }
                }
            }
            catch (Exception var17_21) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", var17_21.getMessage(), var17_21);
            }
            try {
                var52_13 = (String)var5_5.get((Object)"walletAccountFirstCreated");
                if (var52_13 != null && !var52_13.isEmpty()) {
                    var2_2.add((Object)new RiskDataParam("walletAccountTenure", Integer.parseInt((String)var52_13) / 7 + ""));
                }
            }
            catch (Exception var18_22) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", var18_22.getMessage(), var18_22);
            }
            var2_2.add((Object)new RiskDataParam("countryOnDevice", DeviceInfo.getDeviceCountry()));
            var2_2.add((Object)new RiskDataParam("countryonAccountId", var5_5.get((Object)"walletAccountCountry")));
            var2_2.add((Object)new RiskDataParam("noOfRegdDevicesForUser", var5_5.get((Object)"noOfRegdDevicesForUser")));
            var2_2.add((Object)new RiskDataParam("noOfDevicesWithTokensForUser", var5_5.get((Object)"noOfDevicesWithTokensForUser")));
            var2_2.add((Object)new RiskDataParam("activeTokensCountForUser", var5_5.get((Object)"numberOfActiveTokens")));
            var2_2.add((Object)new RiskDataParam("daysSinceLastWalletActivity", var5_5.get((Object)"daysSinceLastAccountActivity")));
            var2_2.add((Object)new RiskDataParam("walletTransactionsCount", var5_5.get((Object)"numberOfTransactionsInLast12months")));
            var2_2.add((Object)new RiskDataParam("weeksSinceWalletActivation", var5_5.get((Object)"weeksSinceWalletActivation")));
            var2_2.add((Object)new RiskDataParam("weeksSinceWalletActivation", var5_5.get((Object)"weeksSinceWalletActivation")));
            try {
                var48_14 = (String)var5_5.get((Object)"accountToDeviceBindingAge");
                if (var48_14 != null && !var48_14.isEmpty()) {
                    var49_15 = Integer.parseInt((String)var48_14);
                    var2_2.add((Object)new RiskDataParam("ageOfAcctOnDevice", var49_15 / 7 + ""));
                    if (var49_15 < 30) {
                        var4_4.add((Object)"GD");
                    }
                }
            }
            catch (Exception var28_23) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", var28_23.getMessage(), var28_23);
            }
            var2_2.add((Object)new RiskDataParam("noOfProvisioningAttempts", var14_9.x(1)));
            var2_2.add((Object)new RiskDataParam("tokensOnDeviceScore", var14_9.y(Integer.MAX_VALUE)));
            var32_16 = 1 + var14_9.x(30);
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "provAttempts : " + var32_16);
            if (var32_16 >= 10) {
                var4_4.add((Object)"XC");
            }
            if (var32_16 >= 3) {
                var4_4.add((Object)"MC");
            }
            var35_17 = var14_9.B(30);
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "billingAddress : " + var35_17);
            if (var35_17 >= 10) {
                var4_4.add((Object)"XZ");
            }
            if (var35_17 >= 3) {
                var4_4.add((Object)"MZ");
            }
            var38_18 = var14_9.z(30);
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "lastNames : " + var38_18);
            if (var38_18 >= 10) {
                var4_4.add((Object)"XN");
            }
            if (var38_18 >= 3) {
                var4_4.add((Object)"MN");
            }
            var41_19 = var14_9.C(30);
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "resetCount : " + var41_19);
            if (var41_19 < 10) break block27;
            var4_4.add((Object)"XR");
            {
                catch (Exception var29_25) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", var29_25.getMessage(), var29_25);
                    return var3_3;
                }
            }
        }
        try {
            var46_20 = (String)var5_5.get((Object)"accountToCardBindingAge");
            if (var46_20 != null && !var46_20.isEmpty() && Integer.parseInt((String)var46_20) < 30) {
                var4_4.add((Object)"LP");
            }
            ** GOTO lbl98
        }
        catch (Exception var43_24) {
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", var43_24.getMessage(), var43_24);
lbl98: // 3 sources:
            if (DeviceInfo.isVpnConnected(this.mContext) || DeviceInfo.isProxyEnabled(this.mContext)) {
                var4_4.add((Object)"GV");
            }
            var2_2.add((Object)new RiskDataParam("reasonCodes", (Object)var4_4));
            return var3_3;
        }
    }

    private static final short a(byte[] arrby, short s2) {
        return (short)(((short)arrby[s2] << 8) + (255 & (short)arrby[(short)(s2 + 1)]));
    }

    /*
     * Enabled aggressive block sorting
     */
    private void a(f f2, boolean bl) {
        String string;
        String string2 = null;
        String string3 = this.pp.getString(f2.cn() + "_replenish_retry", null);
        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "incrementReplenishRetryCount : " + bl);
        if (!com.samsung.android.spayfw.utils.h.al(this.mContext)) {
            bl = false;
        } else {
            string2 = string3;
        }
        if (!bl) {
            long l2;
            int n2;
            long l3 = com.samsung.android.spayfw.utils.h.am(this.mContext);
            if (string2 != null) {
                int n3 = Integer.parseInt((String)string2.split("|")[0]);
                switch (n3) {
                    default: {
                        n2 = 4;
                        l2 = l3 + 86400000L;
                        break;
                    }
                    case 0: {
                        n2 = n3 + 1;
                        l2 = l3 + 600000L;
                        break;
                    }
                    case 1: {
                        n2 = n3 + 1;
                        l2 = 5400000L + l3;
                        break;
                    }
                    case 2: {
                        n2 = n3 + 1;
                        l2 = 10800000L + l3;
                        break;
                    }
                }
            } else {
                com.samsung.android.spayfw.b.c.w("AmexPayProvider", "Retry Data is Empty");
                n2 = 1;
                l2 = l3 + 600000L;
            }
            string = n2 + "|" + l2;
            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "Retry Count :" + n2);
        } else {
            string = "4|" + (600000L + (86400000L + com.samsung.android.spayfw.utils.h.am(this.mContext)));
        }
        this.pp.edit().putString(f2.cn() + "_replenish_retry", string).apply();
    }

    private static String au(String string) {
        if (string == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Token Status received is null");
            return null;
        }
        if ("01".equals((Object)string)) {
            return "01";
        }
        if ("03".equals((Object)string)) {
            return "02";
        }
        if ("02".equals((Object)string)) {
            return "03";
        }
        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Token Status translation failure");
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean aw(String string) {
        block5 : {
            block4 : {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "canPay id " + string);
                TokenDataResponse tokenDataResponse = this.pi.getTokenData(string);
                TokenDataStatus tokenDataStatus = tokenDataResponse.getTokenDataStatus();
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
                if (!tokenDataStatus.getReasonCode().equals((Object)"00")) break block4;
                AmexUtils.LupcMetaData lupcMetaData = AmexUtils.aA(tokenDataResponse.getLupcMetadataBlob());
                if (lupcMetaData == null) {
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "lupcMetaData is null");
                    return false;
                }
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "nfcLupcCount : " + lupcMetaData.nfcLupcCount);
                long l2 = com.samsung.android.spayfw.utils.h.am(this.mContext) / 1000L;
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "currentTime : " + l2);
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "nfcLupcExpiry : " + lupcMetaData.nfcLupcExpiry);
                if (lupcMetaData.nfcLupcCount > 0 && lupcMetaData.nfcLupcExpiry > l2) break block5;
            }
            return false;
        }
        return true;
    }

    private boolean ax(String string) {
        String string2 = this.pp.getString(string + "_replenish_retry", null);
        if (string2 != null) {
            String[] arrstring = string2.split("\\|");
            int n2 = arrstring.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                String string3 = arrstring[i2];
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "retryParts : " + string3);
            }
            try {
                long l2 = Long.parseLong((String)arrstring[1]);
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "retryTime : " + l2);
                f f2 = new f(string);
                f2.setTrTokenId(string);
                h.a(this.mContext, l2, f2);
                return true;
            }
            catch (Exception exception) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Exception when trying to set replenish retry alarm");
                return false;
            }
        }
        com.samsung.android.spayfw.b.c.w("AmexPayProvider", "Trying to set alarm but retry data is empty.");
        return false;
    }

    public static final JsonObject c(JsonObject jsonObject) {
        TreeMap treeMap = new TreeMap();
        for (Map.Entry entry : jsonObject.entrySet()) {
            JsonElement jsonElement = (JsonElement)entry.getValue();
            if (((JsonElement)entry.getValue()).isJsonObject()) {
                jsonElement = a.c(jsonElement.getAsJsonObject());
            }
            treeMap.put(entry.getKey(), (Object)jsonElement);
        }
        JsonObject jsonObject2 = new JsonObject();
        for (Map.Entry entry : treeMap.entrySet()) {
            jsonObject2.add((String)entry.getKey(), (JsonElement)entry.getValue());
        }
        return jsonObject2;
    }

    private byte[] generateRndBytes(int n2) {
        if (n2 < 1) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Invalid input length");
            return null;
        }
        SecureRandom secureRandom = new SecureRandom();
        byte[] arrby = new byte[n2];
        secureRandom.nextBytes(arrby);
        return arrby;
    }

    @Override
    protected boolean authenticateTransaction(SecuredObject securedObject) {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "authenticateTransaction");
        try {
            boolean bl = this.pg.authenticateTransaction(securedObject.getSecureObjectData());
            return bl;
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void av(String string) {
        long l2;
        AmexUtils.LupcMetaData lupcMetaData;
        if (TextUtils.isEmpty((CharSequence)string)) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error handleReplenishment invalid trTokenId ");
            return;
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "replenishIfRequired id " + string);
        TokenStatusResponse tokenStatusResponse = this.pi.getTokenStatus(string);
        if (tokenStatusResponse == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error handleReplenishment TokenStatusResponse object is null! ");
            return;
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager token status : " + tokenStatusResponse.getTokenStatus());
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenStatusResponse.getTokenDataStatus().getReasonCode() + " : " + tokenStatusResponse.getTokenDataStatus().getDetailCode());
        if (tokenStatusResponse.getTokenStatus() != null && (tokenStatusResponse.getTokenStatus().equals((Object)"02") || tokenStatusResponse.getTokenStatus().equals((Object)"03"))) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Not Replenishing as Token is Suspended or Pending.");
            return;
        }
        if (this.ax(string)) {
            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "Replenish Retry Alarm Set");
            return;
        }
        TokenDataResponse tokenDataResponse = this.pi.getTokenData(string);
        if (tokenDataResponse == null || tokenDataResponse.getTokenDataStatus() == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error handleReplenishment TokenDataResponse object is null! ");
            return;
        }
        TokenDataStatus tokenDataStatus = tokenDataResponse.getTokenDataStatus();
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
        if (!tokenDataStatus.getReasonCode().equals((Object)"00")) return;
        {
            String string2 = tokenDataResponse.getLupcMetadataBlob();
            l2 = com.samsung.android.spayfw.utils.h.am(this.mContext) / 1000L;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "currentTime : " + l2);
            lupcMetaData = AmexUtils.aA(string2);
            if (lupcMetaData == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error in handleReplenishment lupcMetaData is null!");
                return;
            }
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "nfcLupcCount : " + lupcMetaData.nfcLupcCount);
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "nfcLupcExpiry : " + lupcMetaData.nfcLupcExpiry);
        f f2 = new f(string);
        f2.setTrTokenId(string);
        if (lupcMetaData.nfcLupcCount < 3) {
            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "replenish token");
            this.replenishAlarmExpired();
            return;
        }
        if (lupcMetaData.nfcLupcExpiry - l2 < 86400L) {
            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "replenish token");
            this.replenishAlarmExpired();
            return;
        }
        h.a(this.mContext, 1000L * (lupcMetaData.nfcLupcExpiry - 86400L), f2);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    Bundle b(EnrollCardInfo enrollCardInfo) {
        Bundle bundle;
        try {
            bundle = new Bundle();
        }
        catch (Exception exception) {
            bundle = null;
            Exception exception2 = exception;
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception2.getMessage(), exception2);
            return bundle;
        }
        if (enrollCardInfo.getUserEmail() != null) {
            bundle.putString("emailHash", AmexUtils.az(enrollCardInfo.getUserEmail()));
        }
        bundle.putString("appId", this.mContext.getPackageName());
        return bundle;
        {
            catch (Exception exception) {}
        }
    }

    @Override
    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
        this.av(this.mProviderTokenKey.cn());
    }

    @Override
    protected void clearCard() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    boolean cq() {
        int n2;
        block6 : {
            block8 : {
                block7 : {
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startPayment");
                    if (this.pn) {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: Previous Payment is not yet stopped");
                        return false;
                    }
                    try {
                        if (this.getAuthType().equalsIgnoreCase("PIN")) {
                            n2 = 2;
                            break block6;
                        }
                        if (!this.getAuthType().equalsIgnoreCase("FP") && !this.getAuthType().equalsIgnoreCase("IRIS")) break block7;
                        break block8;
                    }
                    catch (Exception exception) {
                        com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
                        return false;
                    }
                }
                n2 = this.getAuthType().equalsIgnoreCase("BACKUP PASSWORD") ? 4 : 0;
                break block6;
            }
            n2 = 5;
        }
        String string = String.valueOf((long)(com.samsung.android.spayfw.utils.h.am(this.mContext) / 1000L));
        TokenDataStatus tokenDataStatus = this.ph.startTransaction(this.pj.cn(), 1, n2, string);
        if (!tokenDataStatus.getReasonCode().equals((Object)"00")) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.startTransaction failed Reason Code: " + tokenDataStatus.getReasonCode() + "Detail Code = " + tokenDataStatus.getDetailCode() + "Detail Message = " + tokenDataStatus.getDetailMessage());
            return false;
        }
        this.po = false;
        this.pn = true;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    boolean cr() {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopPayment");
        if (!this.pn) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: Stop Payment is called when there is no Payment in Progress");
            return false;
        }
        try {
            EndTransactionResponse endTransactionResponse = this.ph.endTransaction();
            if (endTransactionResponse == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "FATAL Error: mAmexNfcPaymentProviderProxy.endTransaction failed");
                return false;
            }
            TokenDataStatus tokenDataStatus = endTransactionResponse.getTokenDataStatus();
            if (!tokenDataStatus.getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.endTransaction failed Reason Code: " + tokenDataStatus.getReasonCode() + "Detail Code = " + tokenDataStatus.getDetailCode() + "Detail Message = " + tokenDataStatus.getDetailMessage());
                return false;
            }
            if (endTransactionResponse.getLupcMetaDataBlob() == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "LUPC MetaDataBlob (returned by endTransaction is null");
                return false;
            }
            AmexUtils.LupcMetaData lupcMetaData = AmexUtils.aA(endTransactionResponse.getLupcMetaDataBlob());
            if (lupcMetaData != null) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Remaining LUPC Count (after endTransaction): " + lupcMetaData.nfcLupcCount);
                do {
                    return true;
                    break;
                } while (true);
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "lupcMetaData is null");
            return true;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
            return false;
        }
        finally {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopPayment : cleanup state and check replenishment");
            if (this.pj != null) {
                this.av(this.pj.cn());
            }
            this.pn = false;
            this.pj = null;
            this.po = false;
        }
    }

    /*
     * Exception decompiling
     */
    @Override
    protected e createToken(String var1_1, com.samsung.android.spayfw.payprovider.c var2_2, int var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[TRYBLOCK]], but top level block is 4[TRYBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte[] decryptUserSignature(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        try {
            String string2 = this.pg.a(string, false);
            if (TextUtils.isEmpty((CharSequence)string2)) return null;
            return c.fromBase64(string2);
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "decryptUserSignature Error occured while gettting decrypted data from TA");
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            return null;
        }
    }

    @Override
    public void delete() {
        if (this.kQ != null) {
            this.kQ.decrementRefCount();
        }
    }

    @Override
    public String encryptUserSignature(byte[] arrby) {
        if (arrby == null) {
            return null;
        }
        try {
            String string = this.pg.a(c.toBase64(arrby), true);
            return string;
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "encryptUserSignature Error occured while gettting encypted data from TA");
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        byte[] arrby;
        long l2;
        long l3 = System.currentTimeMillis();
        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "generateInAppPaymentPayload: start: " + l3);
        if (this.pn) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: generateInAppPaymentPayload is called when payment is already in progress");
            return null;
        }
        if (!this.cq()) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Start Payment Failed");
            return null;
        }
        String string = String.valueOf((long)com.samsung.android.spayfw.utils.h.am(this.mContext));
        AmexTAController.InAppTZTxnInfo inAppTZTxnInfo = new AmexTAController.InAppTZTxnInfo();
        inAppTZTxnInfo.txnAttributes = new HashMap();
        inAppTZTxnInfo.txnAttributes.put((Object)"amount", (Object)inAppDetailedTransactionInfo.getAmount());
        inAppTZTxnInfo.txnAttributes.put((Object)"currency_code", (Object)inAppDetailedTransactionInfo.getCurrencyCode());
        inAppTZTxnInfo.txnAttributes.put((Object)"utc", (Object)string);
        if (inAppDetailedTransactionInfo.getCardholderName() != null && !inAppDetailedTransactionInfo.getCardholderName().isEmpty()) {
            inAppTZTxnInfo.txnAttributes.put((Object)"cardholder_name", (Object)inAppDetailedTransactionInfo.getCardholderName());
        }
        inAppTZTxnInfo.txnAttributes.put((Object)"eci_indicator", (Object)"5");
        inAppTZTxnInfo.nonce = inAppDetailedTransactionInfo.getNonce();
        try {
            ProcessInAppPaymentResponse processInAppPaymentResponse;
            byte[] arrby2 = this.generateRndBytes(4);
            if (inAppDetailedTransactionInfo.cd() == null) {
                processInAppPaymentResponse = this.ph.processInAppPayment(inAppTZTxnInfo, com.samsung.android.spayfw.utils.h.encodeHex(arrby2));
            } else {
                inAppTZTxnInfo.merchantCertificate = inAppDetailedTransactionInfo.cd();
                processInAppPaymentResponse = this.ph.processInAppPayment(inAppTZTxnInfo, com.samsung.android.spayfw.utils.h.encodeHex(arrby2));
            }
            if (processInAppPaymentResponse == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: mAmexNfcPaymentProviderProxy.processInAppPayment returned null");
                return null;
            }
            TokenDataStatus tokenDataStatus = processInAppPaymentResponse.getTokenDataStatus();
            if (!tokenDataStatus.getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.processInAppPayment failed Reason Code: " + tokenDataStatus.getReasonCode() + "Detail Code = " + tokenDataStatus.getDetailCode() + "Detail Message = " + tokenDataStatus.getDetailMessage());
                return null;
            }
            arrby = com.samsung.android.spayfw.utils.h.fromBase64(processInAppPaymentResponse.getPaymentPayload());
            l2 = System.currentTimeMillis();
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Exception occurred during generateInAppPaymentPayload ");
            return null;
        }
        finally {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopping In App Payment");
            this.cr();
        }
        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "generateInAppPaymentPayload: end: " + l2);
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "InApp Payload " + Arrays.toString((byte[])arrby));
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "InApp Payload " + arrby.length);
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getDeleteRequestData(Bundle bundle) {
        AmexTAController.ProcessRequestDataResponse processRequestDataResponse;
        com.samsung.android.spayfw.payprovider.c c2 = new com.samsung.android.spayfw.payprovider.c();
        c2.setErrorCode(0);
        if (this.mProviderTokenKey == null) {
            c2.setErrorCode(-4);
            return c2;
        }
        JsonObject jsonObject = new JsonObject();
        String string = this.mProviderTokenKey.cn();
        if (string == null) {
            string = this.mProviderTokenKey.getTrTokenId();
        }
        JsonObject jsonObject2 = new JsonObject();
        try {
            AmexTAController.DevicePublicCerts devicePublicCerts = this.pg.cu();
            AmexTAController.EphemeralKeyInfo ephemeralKeyInfo = this.pg.cv();
            if (devicePublicCerts.deviceCertificate != null) {
                jsonObject2.addProperty("devicePublicKeyCert", devicePublicCerts.deviceCertificate);
            }
            if (devicePublicCerts.deviceSigningCertificate != null) {
                jsonObject2.addProperty("deviceSigningPublicKeyCert", devicePublicCerts.deviceSigningCertificate);
            }
            if (ephemeralKeyInfo != null) {
                jsonObject2.addProperty("ephemeralPublicKey", ephemeralKeyInfo.ephemeralPublicKey);
            }
            jsonObject2.addProperty("clientAPIVersion", this.pi.getClientVersion());
            jsonObject2.addProperty("tokenDataVersion", this.pi.getTokenDataVersion(string).getTokenDataVersion());
        }
        catch (Exception exception) {
            c2.setErrorCode(-2);
            exception.printStackTrace();
        }
        TokenDataStatus tokenDataStatus = this.pi.getTokenData(string).getTokenDataStatus();
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
        jsonObject.addProperty("tokenRefId", string);
        jsonObject.addProperty("responseCode", tokenDataStatus.getReasonCode());
        jsonObject.addProperty("detailCode", tokenDataStatus.getDetailCode());
        String string2 = a.c(jsonObject).toString();
        com.samsung.android.spayfw.b.c.m("AmexPayProvider", "Sorted Data" + string2);
        try {
            AmexTAController.ProcessRequestDataResponse processRequestDataResponse2;
            processRequestDataResponse = processRequestDataResponse2 = this.pg.b(this.pl.getContent(), null, string2);
        }
        catch (AmexTAException amexTAException) {
            c2.setErrorCode(-2);
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            processRequestDataResponse = null;
        }
        if (processRequestDataResponse == null) {
            c2.setErrorCode(-2);
            return c2;
        }
        jsonObject.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
        jsonObject.add("secureDeviceData", (JsonElement)jsonObject2);
        String string3 = this.pp.getString(string + "_transaction_json_data", null);
        String string4 = this.pp.getString(string + "_transaction_param", null);
        if (string3 != null && string4 != null) {
            String string5;
            try {
                String string6;
                string5 = string6 = this.pg.o(string3, string4);
            }
            catch (AmexTAException amexTAException) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "encryptedData is invalid");
                string5 = null;
            }
            if (string5 != null) {
                AmexTAController.ProcessRequestDataResponse processRequestDataResponse3;
                String string7 = new String(Base64.decode((String)string5, (int)2));
                try {
                    AmexTAController.ProcessRequestDataResponse processRequestDataResponse4;
                    processRequestDataResponse3 = processRequestDataResponse4 = this.pg.b(this.pl.getContent(), string7, null);
                }
                catch (AmexTAException amexTAException) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                    processRequestDataResponse3 = null;
                }
                if (processRequestDataResponse3 != null) {
                    jsonObject.addProperty("encryptedData", processRequestDataResponse3.encryptedRequestData);
                    jsonObject.addProperty("encryptionParameters", processRequestDataResponse3.encryptionParams);
                }
            } else {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Cannot Decrypt Access Key");
            }
        } else {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "No Access Key.");
        }
        c2.a(jsonObject);
        return c2;
    }

    @Override
    protected CertificateInfo[] getDeviceCertificates() {
        CertificateInfo[] arrcertificateInfo;
        block4 : {
            AmexTAController.DevicePublicCerts devicePublicCerts;
            block5 : {
                arrcertificateInfo = null;
                devicePublicCerts = this.pg.cu();
                String string = devicePublicCerts.deviceCertificate;
                arrcertificateInfo = null;
                if (string == null) break block4;
                if (devicePublicCerts.deviceSigningCertificate != null) break block5;
                return null;
            }
            try {
                arrcertificateInfo = new CertificateInfo[2];
                arrcertificateInfo[0] = new CertificateInfo();
                arrcertificateInfo[0].setUsage("CA");
                arrcertificateInfo[0].setAlias("DeviceCert");
                arrcertificateInfo[0].setContent(devicePublicCerts.deviceCertificate);
                arrcertificateInfo[1] = new CertificateInfo();
                arrcertificateInfo[1].setUsage("VER");
                arrcertificateInfo[1].setAlias("Amex-DeviceSigningCert");
                arrcertificateInfo[1].setContent(devicePublicCerts.deviceSigningCertificate);
                return arrcertificateInfo;
            }
            catch (AmexTAException amexTAException) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            }
        }
        return arrcertificateInfo;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        com.samsung.android.spayfw.payprovider.c c2;
        AmexTAController.ProcessRequestDataResponse processRequestDataResponse;
        JsonObject jsonObject;
        block26 : {
            c2 = new com.samsung.android.spayfw.payprovider.c();
            try {
                EnrollCardReferenceInfo enrollCardReferenceInfo;
                AmexTAController.ProcessRequestDataResponse processRequestDataResponse2;
                if (this.pl == null) {
                    c2.setErrorCode(0);
                    c2.a(new JsonObject());
                    c2.e(this.b(enrollCardInfo));
                    return c2;
                }
                c2.setErrorCode(0);
                if (enrollCardInfo == null) {
                    c2.setErrorCode(-4);
                    return c2;
                }
                JsonObject jsonObject2 = new JsonObject();
                if (enrollCardInfo instanceof EnrollCardPanInfo) {
                    EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo)enrollCardInfo;
                    if (enrollCardPanInfo.getPAN() == null) {
                        c2.setErrorCode(-4);
                        return c2;
                    }
                    if (enrollCardPanInfo.getPAN() != null && !enrollCardPanInfo.getPAN().isEmpty()) {
                        jsonObject2.addProperty("accountNumber", enrollCardPanInfo.getPAN());
                    }
                } else if (enrollCardInfo instanceof EnrollCardReferenceInfo && (enrollCardReferenceInfo = (EnrollCardReferenceInfo)enrollCardInfo).getReferenceType() != null && enrollCardReferenceInfo.getReferenceType().equals((Object)"referenceId") && enrollCardReferenceInfo.getExtraEnrollData() != null) {
                    jsonObject2.addProperty("accountRefId", enrollCardReferenceInfo.getExtraEnrollData().getString("cardReferenceId"));
                }
                if (enrollCardInfo.getCardEntryMode() != null) {
                    if (enrollCardInfo.getCardEntryMode().equals((Object)"MAN")) {
                        jsonObject2.addProperty("accountInputMethod", "01");
                    } else if (enrollCardInfo.getCardEntryMode().equals((Object)"OCR")) {
                        jsonObject2.addProperty("accountInputMethod", "02");
                    } else {
                        jsonObject2.addProperty("accountInputMethod", "03");
                    }
                    if (enrollCardInfo.getCardEntryMode().equals((Object)"FILE")) {
                        jsonObject2.addProperty("onFileIndicator", Boolean.valueOf((boolean)true));
                    } else {
                        jsonObject2.addProperty("onFileIndicator", Boolean.valueOf((boolean)false));
                    }
                }
                JsonObject jsonObject3 = new JsonObject();
                jsonObject3.add("accountData", (JsonElement)jsonObject2);
                jsonObject = new JsonObject();
                try {
                    AmexTAController.DevicePublicCerts devicePublicCerts = this.pg.cu();
                    if (devicePublicCerts.deviceEncryptionCertificate != null) {
                        jsonObject.addProperty("deviceEncryptionPublicKeyCert", devicePublicCerts.deviceEncryptionCertificate);
                    }
                    if (devicePublicCerts.deviceCertificate != null) {
                        jsonObject.addProperty("devicePublicKeyCert", devicePublicCerts.deviceCertificate);
                    }
                    if (devicePublicCerts.deviceSigningCertificate != null) {
                        jsonObject.addProperty("deviceSigningPublicKeyCert", devicePublicCerts.deviceSigningCertificate);
                    }
                }
                catch (Exception exception) {
                    c2.setErrorCode(-2);
                    exception.printStackTrace();
                }
                String string = a.c(jsonObject2).toString() + a.c(jsonObject).toString();
                processRequestDataResponse = processRequestDataResponse2 = this.pg.b(this.pl.getContent(), jsonObject3.toString(), string);
                break block26;
            }
            catch (Exception exception) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
                c2.setErrorCode(-2);
                return c2;
            }
            catch (AmexTAException amexTAException) {
                c2.setErrorCode(-2);
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                processRequestDataResponse = null;
            }
        }
        if (processRequestDataResponse == null) {
            c2.setErrorCode(-2);
            return c2;
        }
        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("encryptedData", processRequestDataResponse.encryptedRequestData);
        jsonObject4.add("secureDeviceData", (JsonElement)jsonObject);
        jsonObject4.addProperty("encryptionParameters", processRequestDataResponse.encryptionParams);
        jsonObject4.addProperty("accountDataSignature", processRequestDataResponse.requestDataSignature);
        c2.a(jsonObject4);
        this.mBillingInfo = billingInfo;
        enrollCardInfo.incrementRefCount();
        this.kQ = enrollCardInfo;
        c2.e(this.b(enrollCardInfo));
        return c2;
    }

    @Override
    public boolean getPayReadyState() {
        return this.aw(this.mProviderTokenKey.cn());
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getProvisionRequestData(ProvisionTokenInfo var1_1) {
        block23 : {
            var2_2 = new com.samsung.android.spayfw.payprovider.c();
            if (this.pl == null) {
                var2_2.setErrorCode(0);
                var2_2.a(new JsonObject());
                return var2_2;
            }
            var2_2.setErrorCode(0);
            if (this.kQ == null || this.mBillingInfo == null) {
                var2_2.setErrorCode(-4);
                return var2_2;
            }
            var4_3 = new JsonObject();
            var5_4 = new JsonObject();
            if (this.mBillingInfo.getCity() != null) {
                var5_4.addProperty("city", this.mBillingInfo.getCity());
            }
            if (this.mBillingInfo.getCountry() != null) {
                var5_4.addProperty("country", this.mBillingInfo.getCountry());
            }
            if (this.mBillingInfo.getStreet1() != null) {
                var5_4.addProperty("addressLine1", this.mBillingInfo.getStreet1());
            }
            if (this.mBillingInfo.getStreet2() != null) {
                var5_4.addProperty("addressLine2", this.mBillingInfo.getStreet2());
            }
            if (this.mBillingInfo.getState() != null) {
                var5_4.addProperty("state", this.mBillingInfo.getState());
            }
            if (this.mBillingInfo.getZip() != null) {
                var5_4.addProperty("zipCode", this.mBillingInfo.getZip());
            }
            var4_3.add("billingAddress", (JsonElement)var5_4);
            var6_5 = new JsonObject();
            if (this.kQ instanceof EnrollCardPanInfo) {
                var20_6 = (EnrollCardPanInfo)this.kQ;
                var6_5.addProperty("cardExpiry", var20_6.getExpMonth() + "/" + var20_6.getExpYear());
                var6_5.addProperty("cid", var20_6.getCVV());
                var4_3.add("cardVerificationValues", (JsonElement)var6_5);
            }
            var4_3.addProperty("cardholderName", this.kQ.getName());
            var7_7 = new JsonObject();
            var7_7.add("accountData", (JsonElement)var4_3);
            var8_8 = new JsonObject();
lbl-1000: // 3 sources:
            {
                catch (Exception var3_17) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", var3_17.getMessage(), var3_17);
                    var2_2.setErrorCode(-2);
                    return var2_2;
                }
            }
            var18_9 = this.pg.cu();
            var11_11 = var19_10 = this.pg.cv();
            var8_8.addProperty("clientAPIVersion", this.pi.getClientVersion());
            if (var18_9.deviceCertificate != null) {
                var8_8.addProperty("devicePublicKeyCert", var18_9.deviceCertificate);
            }
            if (var18_9.deviceSigningCertificate != null) {
                var8_8.addProperty("deviceSigningPublicKeyCert", var18_9.deviceSigningCertificate);
            }
            if (var18_9.deviceEncryptionCertificate != null) {
                var8_8.addProperty("deviceEncryptionPublicKeyCert", var18_9.deviceEncryptionCertificate);
            }
            if (var11_11 == null) break block23;
            var8_8.addProperty("ephemeralPublicKey", var11_11.ephemeralPublicKey);
        }
        ** try [egrp 3[TRYBLOCK] [17 : 492->635)] { 
lbl55: // 1 sources:
        ** GOTO lbl63
        catch (Exception var10_16) {
            ** GOTO lbl-1000
        }
        catch (Exception var9_21) {
            var10_15 = var9_21;
            var11_11 = null;
        }
lbl-1000: // 2 sources:
        {
            var2_2.setErrorCode(-2);
            var10_15.printStackTrace();
lbl63: // 2 sources:
            try {
                com.samsung.android.spayfw.b.c.m("AmexPayProvider", "encryptedData.toString() " + var7_7.toString());
                var16_12 = a.c(var4_3).toString() + a.c(var8_8).toString();
                com.samsung.android.spayfw.b.c.m("AmexPayProvider", "dataToBeSigned " + var16_12);
                var13_14 = var17_13 = this.pg.b(this.pl.getContent(), var7_7.toString(), var16_12);
            }
            catch (AmexTAException var12_18) {
                var2_2.setErrorCode(-2);
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", var12_18.getMessage(), (Throwable)var12_18);
                var13_14 = null;
            }
            if (var13_14 == null) {
                var2_2.setErrorCode(-2);
                return var2_2;
            }
        }
        var14_19 = new JsonObject();
        var14_19.addProperty("encryptedData", var13_14.encryptedRequestData);
        var14_19.add("secureDeviceData", (JsonElement)var8_8);
        var14_19.addProperty("encryptionParameters", var13_14.encryptionParams);
        var14_19.addProperty("accountDataSignature", var13_14.requestDataSignature);
        var15_20 = new JsonObject();
        var15_20.addProperty("imei", DeviceInfo.getDeviceImei(this.mContext));
        var15_20.addProperty("serial", DeviceInfo.getDeviceSerialNumber());
        var15_20.addProperty("msisdn", DeviceInfo.getMsisdn(this.mContext));
        var14_19.add("deviceData", (JsonElement)var15_20);
        var2_2.a(var14_19);
        var2_2.e(this.a(var1_1));
        if (var11_11 == null) return var2_2;
        this.pq = var11_11.encryptedEphemeralPrivateKey;
        return var2_2;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getReplenishmentRequestData() {
        AmexTAController.ProcessRequestDataResponse processRequestDataResponse;
        AmexTAController.EphemeralKeyInfo ephemeralKeyInfo;
        com.samsung.android.spayfw.payprovider.c c2;
        JsonObject jsonObject;
        String string2;
        String string3;
        String string;
        JsonObject jsonObject2;
        block17 : {
            String string5;
            String string4;
            block16 : {
                AmexTAController.EphemeralKeyInfo ephemeralKeyInfo2;
                c2 = new com.samsung.android.spayfw.payprovider.c();
                c2.setErrorCode(0);
                if (this.mProviderTokenKey == null) {
                    c2.setErrorCode(-4);
                    return c2;
                }
                jsonObject2 = new JsonObject();
                string = this.mProviderTokenKey.cn();
                jsonObject = new JsonObject();
                AmexTAController.DevicePublicCerts devicePublicCerts = this.pg.cu();
                ephemeralKeyInfo = ephemeralKeyInfo2 = this.pg.cv();
                if (devicePublicCerts.deviceCertificate != null) {
                    jsonObject.addProperty("devicePublicKeyCert", devicePublicCerts.deviceCertificate);
                }
                if (devicePublicCerts.deviceSigningCertificate != null) {
                    jsonObject.addProperty("deviceSigningPublicKeyCert", devicePublicCerts.deviceSigningCertificate);
                }
                if (ephemeralKeyInfo != null) {
                    jsonObject.addProperty("ephemeralPublicKey", ephemeralKeyInfo.ephemeralPublicKey);
                }
                jsonObject.addProperty("clientAPIVersion", this.pi.getClientVersion());
                jsonObject.addProperty("tokenDataVersion", this.pi.getTokenDataVersion(string).getTokenDataVersion());
                break block16;
                {
                    catch (Exception exception) {}
                }
                catch (Exception exception) {
                    Exception exception2 = exception;
                    ephemeralKeyInfo = null;
                    c2.setErrorCode(-2);
                    exception2.printStackTrace();
                }
            }
            TokenDataResponse tokenDataResponse = this.pi.getTokenData(string);
            TokenDataStatus tokenDataStatus = tokenDataResponse.getTokenDataStatus();
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
            if (!tokenDataStatus.getReasonCode().equals((Object)"00")) {
                c2.setErrorCode(-2);
                return c2;
            }
            jsonObject2.addProperty("responseCode", tokenDataStatus.getReasonCode());
            jsonObject2.addProperty("detailCode", tokenDataStatus.getDetailCode());
            jsonObject2.addProperty("detailMessage", tokenDataStatus.getDetailMessage());
            string3 = "";
            string2 = string5 = this.pg.decryptTokenData(tokenDataResponse.getApduBlob());
            string3 = string4 = this.pg.decryptTokenData(tokenDataResponse.getMetaDataBlob());
            break block17;
            {
                catch (AmexTAException amexTAException) {}
            }
            catch (AmexTAException amexTAException) {
                string2 = "";
                AmexTAException amexTAException2 = amexTAException;
                c2.setErrorCode(-2);
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException2.getMessage(), (Throwable)((Object)amexTAException2));
            }
        }
        String string6 = string2 + string3;
        try {
            AmexTAController.ProcessRequestDataResponse processRequestDataResponse2;
            processRequestDataResponse = processRequestDataResponse2 = this.pg.b(this.pl.getContent(), null, string6);
        }
        catch (AmexTAException amexTAException) {
            c2.setErrorCode(-2);
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            processRequestDataResponse = null;
        }
        if (processRequestDataResponse == null) {
            c2.setErrorCode(-2);
            return c2;
        }
        jsonObject2.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
        jsonObject2.add("secureDeviceData", (JsonElement)jsonObject);
        c2.a(jsonObject2);
        if (ephemeralKeyInfo == null) return c2;
        this.pp.edit().putString(string + "_keys", ephemeralKeyInfo.encryptedEphemeralPrivateKey).apply();
        return c2;
    }

    @Override
    public int getTransactionData(Bundle bundle, i i2) {
        return b.a(this.mContext, this.pg, this.pl, this.pp).a(this.mProviderTokenKey, bundle, i2);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getVerifyIdvRequestData(VerifyIdvInfo var1_1) {
        block11 : {
            var2_2 = new com.samsung.android.spayfw.payprovider.c();
            var2_2.setErrorCode(0);
            if (var1_1 != null) ** GOTO lbl7
            var2_2.setErrorCode(-4);
            return var2_2;
lbl7: // 1 sources:
            var3_3 = new JsonObject();
            try {
                var10_4 = this.pg.cu();
                if (var10_4.deviceCertificate != null) {
                    var3_3.addProperty("devicePublicKeyCert", var10_4.deviceCertificate);
                }
                if (var10_4.deviceSigningCertificate != null) {
                    var3_3.addProperty("deviceSigningPublicKeyCert", var10_4.deviceSigningCertificate);
                }
                ** GOTO lbl25
            }
            catch (Exception var4_8) {
                try {
                    var2_2.setErrorCode(-2);
                    var4_8.printStackTrace();
                }
                catch (Exception var8_7) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", var8_7.getMessage(), var8_7);
                    var2_2.setErrorCode(-2);
                    return var2_2;
                }
lbl25: // 4 sources:
                var6_6 = var9_5 = this.pg.b(this.pl.getContent(), null, var1_1.getValue());
                break block11;
            }
            catch (AmexTAException var5_9) {
                var2_2.setErrorCode(-2);
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", var5_9.getMessage(), (Throwable)var5_9);
                var6_6 = null;
            }
        }
        if (var6_6 == null) {
            var2_2.setErrorCode(-2);
            return var2_2;
        }
        var7_10 = new JsonObject();
        var7_10.addProperty("authenticationCodeSignature", var6_6.requestDataSignature);
        var7_10.add("secureDeviceData", (JsonElement)var3_3);
        var2_2.a(var7_10);
        return var2_2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        long l2 = System.currentTimeMillis();
        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "handleApdu: start: " + l2);
        if (!this.pn) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: prepareMstPay must never happen when there is already a pending MST");
            return null;
        }
        if (arrby == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: apduBuffer received is NULL");
            return null;
        }
        com.samsung.android.spayfw.b.c.m("AmexPayProvider", "HandlAPDU - Request = " + com.samsung.android.spayfw.utils.h.encodeHex(arrby));
        byte[] arrby2 = this.ph.generateAPDU(arrby).getApduBytes();
        if (a.a(arrby, (short)0) == -32594 && arrby2 != null) {
            byte[] arrby3 = new byte[]{arrby2[-2 + arrby2.length], arrby2[-1 + arrby2.length]};
            short s2 = (short)(256 * (short)arrby3[0] + (short)arrby3[1]);
            if (s2 == -28672) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "set amex nfc payment to true");
                this.pr = true;
            } else {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "amex nfc payment response code: " + s2);
            }
            com.samsung.android.spayfw.b.c.m("AmexPayProvider", "HandlAPDU - Response = " + com.samsung.android.spayfw.utils.h.encodeHex(arrby2));
        }
        long l3 = System.currentTimeMillis();
        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "handleApdu: end: " + l3 + "Time Taken = " + (1L + (l3 - l2)));
        return arrby2;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void interruptMstPay() {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean isPayAllowedForPresentationMode(int n2) {
        if (this.mProviderTokenKey == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "ProviderTokenKey is null");
            return false;
        }
        if (n2 != 2) return true;
        {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isPayAllowedForPresentationMode " + this.mProviderTokenKey.getTrTokenId());
            TokenDataResponse tokenDataResponse = this.pi.getTokenData(this.mProviderTokenKey.getTrTokenId());
            TokenDataStatus tokenDataStatus = tokenDataResponse.getTokenDataStatus();
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
            if (!tokenDataStatus.getReasonCode().equals((Object)"00")) return false;
            {
                AmexUtils.LupcMetaData lupcMetaData = AmexUtils.aA(tokenDataResponse.getLupcMetadataBlob());
                if (lupcMetaData != null) {
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "otherLupcCount : " + lupcMetaData.otherLupcCount);
                    if (lupcMetaData.otherLupcCount <= 0) return false;
                    return true;
                }
            }
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "lupcMetaData null");
        return true;
    }

    @Override
    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        return jsonObject.getAsJsonObject("secureTokenData") != null;
    }

    @Override
    protected void loadTA() {
        this.pg.loadTA();
        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "load real TA");
    }

    @Override
    protected void onPaySwitch(int n2, int n3) {
        super.onPaySwitch(n2, n3);
        if (n2 == 1 && n3 == 2) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: Payment mode switching from NFC to MST. Must never happen");
            return;
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "onPaySwitch");
    }

    @Override
    protected boolean prepareMstPay() {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "prepareMstPay");
        if (!this.pn && !this.cq()) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: startPayment failed");
            return false;
        }
        if (!this.po) {
            this.po = true;
            TokenDataStatus tokenDataStatus = this.ph.processOther();
            if (!tokenDataStatus.getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.processOther failed Reason Code: " + tokenDataStatus.getReasonCode() + "Detail Code = " + tokenDataStatus.getDetailCode() + "Detail Message = " + tokenDataStatus.getDetailMessage());
                this.cr();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean prepareNfcPay() {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "prepareNfcPay");
        if (!this.pn && !this.cq()) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Start Payment Failed");
            return false;
        }
        return true;
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        return b.a(this.mContext, this.pg, this.pl, this.pp).a(this.mProviderTokenKey, object);
    }

    @Override
    protected void replenishAlarmExpired() {
        if (this.mProviderTokenKey == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "cannot fire replenishment, providerTokenKey is null");
            return;
        }
        if (this.ps != null) {
            this.ps.a(this.mProviderTokenKey);
            return;
        }
        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "CMN FW REQUESTER IS NOT INITIALIZED");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "Replenish Token");
        e e2 = new e();
        try {
            String string;
            AmexTAController.ProcessRequestDataResponse processRequestDataResponse;
            JsonObject jsonObject2;
            block15 : {
                AmexTAController.ProcessTokenDataResponse processTokenDataResponse;
                e2.setErrorCode(0);
                if (jsonObject == null || jsonObject.getAsJsonObject("secureTokenData") == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Input Data is NULL");
                    e2.setErrorCode(-4);
                    return e2;
                }
                JsonObject jsonObject3 = jsonObject.getAsJsonObject("secureTokenData");
                String string2 = jsonObject3.get("initializationVector").getAsString();
                String string3 = jsonObject3.get("encryptedTokenData").getAsString();
                String string4 = jsonObject3.get("encryptedTokenDataHMAC").getAsString();
                String string5 = jsonObject3.get("cloudPublicKeyCert").getAsString();
                String string6 = this.kQ != null ? this.kQ.getWalletId() : com.samsung.android.spayfw.core.e.h(this.mContext).getConfig("CONFIG_WALLET_ID");
                if (string6 == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Wallet Id is NULL");
                    e2.setErrorCode(-2);
                    return e2;
                }
                String string7 = a.au(jsonObject.get("tokenStatus").getAsString());
                if (string7 == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Token Status is NULL");
                    e2.setErrorCode(-2);
                    return e2;
                }
                StringBuilder stringBuilder = new StringBuilder().append(string5);
                String string8 = this.pm == null ? "" : this.pm.getContent();
                String string9 = stringBuilder.append(string8).toString();
                string = this.mProviderTokenKey.cn();
                String string10 = this.pp.getString(string + "_keys", null);
                this.pp.edit().remove(string + "_keys").apply();
                try {
                    AmexTAController.ProcessTokenDataResponse processTokenDataResponse2;
                    processTokenDataResponse = processTokenDataResponse2 = this.pg.a(string9, string2, string3, string4, string7, com.samsung.android.spayfw.utils.h.encodeHex(string6.getBytes()), string10);
                }
                catch (AmexTAException amexTAException) {
                    e2.setErrorCode(-2);
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                    processTokenDataResponse = null;
                }
                if (processTokenDataResponse == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Token Data Response is NULL");
                    e2.setErrorCode(-2);
                    return e2;
                }
                TokenDataStatus tokenDataStatus = this.pi.updateTokenData(string, processTokenDataResponse.eAPDUBlob, processTokenDataResponse.eNFCLUPCBlob, processTokenDataResponse.eOtherLUPCBlob, processTokenDataResponse.eMetadataBlob, processTokenDataResponse.lupcMetadataBlob);
                if (!tokenDataStatus.getReasonCode().equals((Object)"00")) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "mAmexNfcPaymentProviderProxy.updateTokenData failed Reason Code: " + tokenDataStatus.getReasonCode() + "Detail Code = " + tokenDataStatus.getDetailCode() + "Detail Message = " + tokenDataStatus.getDetailMessage());
                    e2.setErrorCode(-2);
                    this.a(this.mProviderTokenKey, false);
                    this.ax(string);
                    return e2;
                }
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
                jsonObject2 = new JsonObject();
                try {
                    AmexTAController.DevicePublicCerts devicePublicCerts = this.pg.cu();
                    if (devicePublicCerts.deviceCertificate != null) {
                        jsonObject2.addProperty("devicePublicKeyCert", devicePublicCerts.deviceCertificate);
                    }
                    if (devicePublicCerts.deviceSigningCertificate == null) break block15;
                    jsonObject2.addProperty("deviceSigningPublicKeyCert", devicePublicCerts.deviceSigningCertificate);
                }
                catch (AmexTAException amexTAException) {
                    e2.setErrorCode(-2);
                    return e2;
                }
            }
            try {
                AmexTAController.ProcessRequestDataResponse processRequestDataResponse2;
                processRequestDataResponse = processRequestDataResponse2 = this.pg.b(this.pl.getContent(), null, string);
            }
            catch (AmexTAException amexTAException) {
                e2.setErrorCode(-2);
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                processRequestDataResponse = null;
            }
            if (processRequestDataResponse == null) {
                e2.setErrorCode(-2);
                return e2;
            }
            e2.setProviderTokenKey(new f(string));
            JsonObject jsonObject4 = new JsonObject();
            jsonObject4.addProperty("secureTokenDataSignature", processRequestDataResponse.requestDataSignature);
            jsonObject4.add("secureDeviceData", (JsonElement)jsonObject2);
            e2.b(jsonObject4);
            this.pp.edit().remove(string + "_replenish_retry").apply();
            this.ps.b(this.mProviderTokenKey);
            this.av(string);
            return e2;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
            this.replenishAlarmExpired();
            e2.setErrorCode(-2);
            return e2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public SelectCardResult selectCard() {
        byte[] arrby;
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "selectCard");
        if (this.mProviderTokenKey == null) {
            return null;
        }
        if (!this.aw(this.mProviderTokenKey.cn())) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: Can not pay since LUPC reached zero or token status not active");
            return null;
        }
        if (this.pn) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: Select Card called before previous Payment did not complete. This must never happen");
            this.cr();
        }
        try {
            byte[] arrby2;
            this.pg.initializeSecuritySetup();
            arrby = arrby2 = this.pg.getNonce(32);
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            arrby = null;
        }
        if (arrby == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: getNonce returned null");
            return null;
        }
        SelectCardResult selectCardResult = new SelectCardResult(AmexTAController.ct().getTAInfo().getTAId(), arrby);
        this.pj = this.mProviderTokenKey;
        return selectCardResult;
    }

    @Override
    public void setPaymentFrameworkRequester(k k2) {
        this.ps = k2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        int n2 = 0;
        while (n2 < arrcertificateInfo.length) {
            if (arrcertificateInfo[n2].getAlias().equals((Object)"tsp_rsa")) {
                this.pl = arrcertificateInfo[n2];
            } else if (arrcertificateInfo[n2].getAlias().equals((Object)"tsp_ecc")) {
                this.pm = arrcertificateInfo[n2];
            }
            ++n2;
        }
        return true;
    }

    @Override
    public void setupReplenishAlarm() {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Entered setup Replenish Alarm");
        String string = this.mProviderTokenKey.cn();
        if (string == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "TrTokenId is null");
            return;
        }
        this.av(string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        boolean bl;
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startMstPay(for transmit)");
        if (!this.pn) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: prepareMstPay must never happen when there is already a pending MST");
            return false;
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startMstPay: input config " + Arrays.toString((byte[])arrby));
        try {
            boolean bl2;
            bl = bl2 = this.pg.a(n2, arrby);
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
            bl = false;
        }
        if (!bl) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "failure to do startMstPay");
        }
        return true;
    }

    @Override
    protected void stopMstPay(boolean bl) {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopMstPay: stop amex mst pay");
        this.cr();
        this.po = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected Bundle stopNfcPay(int n2) {
        short s2;
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopNfcPay");
        if (!this.pn) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Stop NFC Pay called when payment is not in progress");
            this.pr = false;
            Bundle bundle = new Bundle();
            bundle.putShort("nfcApduErrorCode", (short)1);
            return bundle;
        }
        if (!this.pr) {
            if (n2 == 4) {
                this.pr = false;
                Bundle bundle = new Bundle();
                bundle.putShort("nfcApduErrorCode", (short)4);
                return bundle;
            }
            s2 = 3;
        } else {
            s2 = 2;
        }
        boolean bl = this.cr();
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopNfcPay: spayfw reason : " + n2 + " sdk iso ret = " + bl);
        this.pr = false;
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", s2);
        return bundle;
    }

    @Override
    protected void unloadTA() {
        this.pg.unloadTA();
        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "unload real TA");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void updateRequestStatus(d d2) {
        block7 : {
            block6 : {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateRequestStatus : " + d2.getRequestType() + " " + d2.ci());
                if (d2.ck() != null) {
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateRequestStatus : " + d2.ck().cn());
                }
                if (d2.getRequestType() == 23) break block6;
                switch (d2.ci()) {
                    default: {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error in updating status");
                        return;
                    }
                    case -1: {
                        if (d2.ck() != null) {
                            this.pp.edit().remove(d2.ck().cn() + "_keys").apply();
                        }
                        if (d2.getRequestType() != 11 || !d2.cj().equals((Object)"403.5")) break;
                        this.a(d2.ck(), true);
                        this.ax(d2.ck().cn());
                        return;
                    }
                    case 0: {
                        if (d2.getRequestType() == 3) break block7;
                    }
                }
            }
            return;
        }
        this.pp.edit().putString(d2.ck().cn() + "_keys", this.pq).apply();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        e e2 = new e();
        e2.setErrorCode(0);
        if (tokenStatus == null) {
            e2.setErrorCode(-4);
            return e2;
        }
        if (this.mProviderTokenKey == null) {
            if (tokenStatus.getCode().equals((Object)"DISPOSED")) {
                return e2;
            }
            e2.setErrorCode(-4);
            return e2;
        }
        String string = this.mProviderTokenKey.cn();
        TokenStatusResponse tokenStatusResponse = this.pi.getTokenStatus(string);
        JsonObject jsonObject2 = new JsonObject();
        if (tokenStatusResponse != null && tokenStatusResponse.getTokenStatus() != null) {
            TokenDataStatus tokenDataStatus;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager token status : " + tokenStatusResponse.getTokenStatus());
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenStatusResponse.getTokenDataStatus().getReasonCode() + " : " + tokenStatusResponse.getTokenDataStatus().getDetailCode());
            if (tokenStatus.getCode().equals((Object)"ACTIVE") && tokenStatusResponse.getTokenStatus().equals((Object)"03")) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Activating Token");
                tokenDataStatus = this.pi.activateToken(string);
                this.av(string);
            } else if (tokenStatus.getCode().equals((Object)"ACTIVE")) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Resuming Token");
                try {
                    TokenDataStatus tokenDataStatus2;
                    tokenDataStatus = tokenDataStatus2 = this.pi.resumeToken(string);
                }
                catch (Exception exception) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", exception.getMessage());
                    tokenDataStatus = null;
                }
                this.av(string);
            } else if (tokenStatus.getCode().equals((Object)"SUSPENDED")) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Suspending Token");
                tokenDataStatus = this.pi.suspendToken(string);
                h.a(this.mContext, this.mProviderTokenKey);
            } else if (tokenStatus.getCode().equals((Object)"DISPOSED")) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Deleting Token");
                tokenDataStatus = this.pi.deleteToken(string);
                this.pp.edit().remove(string + "_keys").remove(string + "_transaction_json_data").remove(string + "_replenish_retry").apply();
                h.a(this.mContext, this.mProviderTokenKey);
            } else {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Unknown Token Status : " + tokenStatus.getCode());
                tokenDataStatus = null;
            }
            if (tokenDataStatus != null) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "AmexTokenDataManager status " + tokenDataStatus.getReasonCode() + " : " + tokenDataStatus.getDetailCode());
                jsonObject2.addProperty("responseCode", tokenDataStatus.getReasonCode());
                if (!tokenDataStatus.getReasonCode().equals((Object)"00")) {
                    e2.setErrorCode(-2);
                }
            }
        } else if (tokenStatus.getCode().equals((Object)"ACTIVE") || tokenStatus.getCode().equals((Object)"SUSPENDED")) {
            e2.setErrorCode(-5);
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Unknown Token : " + string);
        } else {
            jsonObject2.addProperty("responseCode", "00");
            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "Token Already Deleted");
        }
        e2.b(jsonObject2);
        return e2;
    }
}

