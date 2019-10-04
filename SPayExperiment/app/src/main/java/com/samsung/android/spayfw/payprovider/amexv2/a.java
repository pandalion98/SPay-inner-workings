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
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Number
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.security.SecureRandom
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Locale
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  java.util.TimeZone
 *  java.util.TreeMap
 */
package com.samsung.android.spayfw.payprovider.amexv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenChannelUpdateResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenCloseResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenInAppResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.model.TokenPersoResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenRefreshStatusResponse;
import com.americanexpress.mobilepayments.hceclient.service.AmexPaySaturn;
import com.americanexpress.mobilepayments.hceclient.service.AmexPaySaturnImpl;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
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
import com.samsung.android.spayfw.payprovider.amexv2.b;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.g;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.i;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.h;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

public class a
extends PaymentNetworkProvider {
    private static k ps;
    private static boolean qF;
    private EnrollCardInfo kQ;
    private BillingInfo mBillingInfo;
    private Context mContext;
    private f pj = null;
    private CertificateInfo pl;
    private CertificateInfo pm;
    private boolean pn = false;
    private boolean po = false;
    private SharedPreferences pp;
    private boolean pr = false;
    private c qA;
    private AmexPaySaturn qB;
    private String qC;
    private String qD;
    private Map<String, String> qE;
    private boolean qG = false;
    private boolean qH = false;
    private boolean qI = false;
    final String qu = "amount";
    final String qv = "currency_code";
    final String qw = "utc";
    final String qx = "cardholder_name";
    final String qy = "eci_indicator";
    final String qz = "5";

    static {
        qF = false;
    }

    public a(Context context, String string, f f2) {
        super(context, string);
        if (context == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "AmexPayProvider : ERROR: context is null");
            return;
        }
        this.mContext = context;
        this.mTAController = c.D(this.mContext);
        this.qA = (c)this.mTAController;
        this.qB = new AmexPaySaturnImpl();
        this.pp = this.mContext.getSharedPreferences("AmexStorage", 0);
        this.qE = new HashMap();
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

    private void aB(String string) {
        if (string == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "createConfigMap : ERROR: Invalid key-value pair string, cannot create congfig map");
            return;
        }
        String[] arrstring = string.split(",");
        int n2 = arrstring.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            String[] arrstring2 = arrstring[i2].split("=");
            if (arrstring2 == null || arrstring2.length != 2) continue;
            this.qE.put((Object)arrstring2[0], (Object)arrstring2[1]);
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "createConfigMap: Config Map created");
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
    private void av(String string) {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment:  Enter for token : " + string);
        if (qF) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : token is open, cannot replenish now, retry later");
        } else if (TextUtils.isEmpty((CharSequence)string)) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : ERROR: invalid Token ref ID ");
        } else {
            TokenRefreshStatusResponse tokenRefreshStatusResponse = this.qB.tokenRefreshStatus(this.cx(), string);
            if (!tokenRefreshStatusResponse.getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : ERROR: tokenRefreshStatus failed on SDK" + tokenRefreshStatusResponse.getReasonCode());
            } else {
                String string2 = this.getPFTokenStatus();
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment : pfTokenState : " + string2);
                String string3 = tokenRefreshStatusResponse.getTokenState();
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment : tokenState : " + string3);
                if (string3 == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : ERROR: Token State is null");
                } else {
                    int n2 = tokenRefreshStatusResponse.getLupcCount();
                    boolean bl = tokenRefreshStatusResponse.isRefreshRequired();
                    long l2 = tokenRefreshStatusResponse.getLupcRefreshCheckBack();
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment : nfcLupcCount : " + n2);
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment : refreshRequired : " + bl);
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment : checkBack : " + l2);
                    if (bl && n2 == 0 && tokenRefreshStatusResponse.getMaxATC() == 0 && tokenRefreshStatusResponse.getTokenDataVersion() == null) {
                        if (string2.equalsIgnoreCase("ACTIVE")) {
                            this.pp.edit().putBoolean(string + "_reperso_required", true).apply();
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : ERROR: Token needs Re-perso");
                            this.cy();
                        } else {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : Not triggerring Re-perso as token is in " + string2 + " state");
                        }
                    } else {
                        this.pp.edit().putBoolean(string + "_reperso_required", false).apply();
                        if (string3.equals((Object)"02") && string2.equalsIgnoreCase("SUSPENDED")) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : ERROR: Token Suspended cannot trigger replenishment");
                        } else if (string3.equals((Object)"03")) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleReplenishment : ERROR: Token Suspended or in pending state, cannot trigger replenishment");
                        } else if (this.ax(string)) {
                            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "handleReplenishment : Replenish Retry Alarm Set");
                        } else {
                            f f2 = new f(string);
                            f2.setTrTokenId(string);
                            if (bl) {
                                com.samsung.android.spayfw.b.c.i("AmexPayProvider", "handleReplenishment : replenish token now");
                                this.cy();
                            } else {
                                long l3 = 1000L * l2 + com.samsung.android.spayfw.utils.h.am(this.mContext);
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment : keyReplenishTs : " + l3);
                                h.a(this.mContext, l3, f2);
                            }
                        }
                    }
                }
            }
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleReplenishment:  Exit");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean ax(String var1_1) {
        block4 : {
            var2_2 = true;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "setupReplenishRetryAlarm : Enter");
            var3_3 = this.pp.getString(var1_1 + "_replenish_retry", null);
            if (var3_3 == null) ** GOTO lbl9
            try {
                block5 : {
                    var5_4 = var3_3.split("\\|");
                    var6_5 = var5_4.length;
                    break block5;
lbl9: // 1 sources:
                    com.samsung.android.spayfw.b.c.w("AmexPayProvider", "setupReplenishRetryAlarm : WARNING: Retry data is empty, unable to set alarm");
                    var2_2 = false;
                    break block4;
                }
                for (var7_6 = 0; var7_6 < var6_5; ++var7_6) {
                    var8_7 = var5_4[var7_6];
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "setupReplenishRetryAlarm : retryParts : " + var8_7);
                }
                var9_8 = Long.parseLong((String)var5_4[1]);
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "setupReplenishRetryAlarm : retryTime : " + var9_8);
                var11_9 = new f(var1_1);
                var11_9.setTrTokenId(var1_1);
                h.a(this.mContext, var9_8, var11_9);
            }
            catch (Exception var4_10) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", "setupReplenishRetryAlarm : ERROR: " + var4_10.getMessage(), var4_10);
                var2_2 = false;
            }
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "setupReplenishRetryAlarm : Exit");
        return var2_2;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Bundle b(EnrollCardInfo enrollCardInfo) {
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final JsonObject c(JsonObject jsonObject) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            TreeMap treeMap = new TreeMap();
            for (Map.Entry entry : jsonObject.entrySet()) {
                JsonElement jsonElement = (JsonElement)entry.getValue();
                if (((JsonElement)entry.getValue()).isJsonObject()) {
                    jsonElement = a.c(jsonElement.getAsJsonObject());
                }
                treeMap.put(entry.getKey(), (Object)jsonElement);
            }
            JsonObject jsonObject2 = new JsonObject();
            Iterator iterator = treeMap.entrySet().iterator();
            do {
                if (!iterator.hasNext()) {
                    // ** MonitorExit[var10_1] (shouldn't be in output)
                    return jsonObject2;
                }
                Map.Entry entry = (Map.Entry)iterator.next();
                jsonObject2.add((String)entry.getKey(), (JsonElement)entry.getValue());
            } while (true);
        }
    }

    private long cx() {
        long l2 = com.samsung.android.spayfw.utils.h.am(this.mContext) / 1000L;
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getRealTimeInSeconds : Time in seconds = " + l2);
        return l2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void cy() {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "fireReplenishmentRequest : Enter");
        if (this.mProviderTokenKey == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "fireReplenishmentRequest : ERROR: providerTokenKey is null");
        } else if (ps == null) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "fireReplenishmentRequest : ERROR: Uninitialized Common Framework requester");
        } else if (qF) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "fireReplenishmentRequest : Card selected, retry later");
        } else {
            ps.a(this.mProviderTokenKey);
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "fireReplenishmentRequest : Exit");
    }

    private byte[] generateRndBytes(int n2) {
        if (n2 < 1) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "generateRndBytes : ERROR: Invalid input length");
            return null;
        }
        SecureRandom secureRandom = new SecureRandom();
        byte[] arrby = new byte[n2];
        secureRandom.nextBytes(arrby);
        return arrby;
    }

    @Override
    protected boolean allowPaymentRetry() {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "allowPaymentRetry : returns : true");
            return true;
        }
    }

    @Override
    protected boolean authenticateTransaction(SecuredObject securedObject) {
        a a2 = this;
        synchronized (a2) {
            boolean bl;
            block5 : {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "authenticateTransaction : Enter");
                bl = true;
                if (this.qB.tokenSetCDCVM(securedObject.getSecureObjectData()).getReasonCode().equals((Object)"00")) break block5;
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "authenticateTransaction : ERROR: tokenSetCDCVM failed on SDK");
                bl = false;
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "authenticateTransaction : Exit");
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void beginPay(boolean bl, boolean bl2) {
        a a2 = this;
        // MONITORENTER : a2
        // MONITOREXIT : a2
    }

    @Override
    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "checkIfReplenishmentNeeded : Enter");
            this.av(this.mProviderTokenKey.cn());
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "checkIfReplenishmentNeeded : Exit");
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void clearCard() {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "clearCard : Enter");
            if (qF) {
                qF = false;
                TokenCloseResponse tokenCloseResponse = this.qB.tokenClose();
                if (tokenCloseResponse.getReasonCode().equals((Object)"00")) {
                    if (this.pj != null) {
                        com.samsung.android.spayfw.b.c.i("AmexPayProvider", "clearCard : check if replenishment required");
                        this.av(this.pj.cn());
                    }
                } else {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "clearCard : ERROR : tokenClose failed on SDK " + tokenCloseResponse.getReasonCode());
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "clearCard : Card selection cleared");
            this.pj = null;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "clearCard : Exit");
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean cq() {
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startPayment : Enter");
        if (this.pn) {
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "startPayment : Payment already in progress");
        } else {
            this.po = false;
            this.qG = false;
            this.qI = false;
            this.qH = false;
            this.pn = true;
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startPayment : Exit");
        return true;
    }

    /*
     * Exception decompiling
     */
    @Override
    protected e createToken(String var1_1, com.samsung.android.spayfw.payprovider.c var2_2, int var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [5[TRYBLOCK]], but top level block is 7[TRYBLOCK]
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
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public byte[] decryptUserSignature(String string) {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "decryptUserSignature : Enter");
            boolean bl = TextUtils.isEmpty((CharSequence)string);
            byte[] arrby = null;
            if (!bl) {
                try {
                    String string2 = this.qA.a(string, false);
                    boolean bl2 = TextUtils.isEmpty((CharSequence)string2);
                    arrby = null;
                    if (!bl2) {
                        byte[] arrby2;
                        arrby = arrby2 = i.fromBase64(string2);
                    }
                }
                catch (AmexTAException amexTAException) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", "decryptUserSignature : ERROR: Cannot decrypt User signature data" + amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                    arrby = null;
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "decryptUserSignature : Exit");
            return arrby;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void delete() {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "delete : Enter");
            if (this.kQ != null) {
                this.kQ.decrementRefCount();
            } else {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "delete : Card info is null, do nothing");
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "delete : Exit");
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public String encryptUserSignature(byte[] arrby) {
        a a2 = this;
        synchronized (a2) {
            String string;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "encryptUserSignature : Enter");
            if (arrby == null) {
                string = null;
            } else {
                try {
                    String string2;
                    string = string2 = this.qA.a(i.toBase64(arrby), true);
                }
                catch (AmexTAException amexTAException) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", "encryptUserSignature : ERROR: Cannot encrypt User signature data" + amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                    string = null;
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "encryptUserSignature : Exit");
            return string;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void endPay() {
        a a2 = this;
        // MONITORENTER : a2
        // MONITOREXIT : a2
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        a a2 = this;
        synchronized (a2) {
            byte[] arrby = null;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "generateInAppPaymentPayload: Enter: " + System.currentTimeMillis());
            if (!this.cq()) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "generateInAppPaymentPayload : ERROR: Start Payment Failed");
            } else {
                this.qI = true;
                c.b b2 = new c.b();
                b2.merchantCertificate = inAppDetailedTransactionInfo.cd();
                b2.txnAttributes = new HashMap();
                b2.txnAttributes.put((Object)"amount", (Object)inAppDetailedTransactionInfo.getAmount());
                b2.txnAttributes.put((Object)"currency_code", (Object)inAppDetailedTransactionInfo.getCurrencyCode());
                b2.txnAttributes.put((Object)"utc", (Object)String.valueOf((long)(com.samsung.android.spayfw.utils.h.am(this.mContext) / 1000L)));
                if (inAppDetailedTransactionInfo.getCardholderName() != null && !inAppDetailedTransactionInfo.getCardholderName().isEmpty()) {
                    b2.txnAttributes.put((Object)"cardholder_name", (Object)inAppDetailedTransactionInfo.getCardholderName());
                }
                b2.txnAttributes.put((Object)"eci_indicator", (Object)"5");
                byte[] arrby2 = this.generateRndBytes(4);
                TokenInAppResponse tokenInAppResponse = this.qB.tokenInApp(com.samsung.android.spayfw.utils.h.encodeHex(arrby2), b2.toString());
                if (!tokenInAppResponse.getReasonCode().equals((Object)"00")) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "generateInAppPaymentPayload : ERROR: tokenInApp failed on SDK");
                    arrby = null;
                } else {
                    byte[] arrby3;
                    com.samsung.android.spayfw.b.c.m("AmexPayProvider", "generateInAppPaymentPayload : InApp Payload : " + tokenInAppResponse.getPaymentPayload());
                    arrby = arrby3 = tokenInAppResponse.getPaymentPayload().getBytes();
                }
            }
            this.qI = false;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "generateInAppPaymentPayload: end: " + System.currentTimeMillis());
            return arrby;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getDeleteRequestData(Bundle bundle) {
        com.samsung.android.spayfw.payprovider.c c2;
        block24 : {
            JsonObject jsonObject;
            block23 : {
                String string;
                JsonObject jsonObject2;
                c.c c3;
                block22 : {
                    a a2 = this;
                    // MONITORENTER : a2
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeleteRequestData : Enter");
                    c2 = new com.samsung.android.spayfw.payprovider.c();
                    jsonObject = new JsonObject();
                    if (this.mProviderTokenKey == null) {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeleteRequestData : ERROR: Token not found");
                    } else {
                        string = this.mProviderTokenKey.cn();
                        if (string == null) {
                            string = this.mProviderTokenKey.getTrTokenId();
                        }
                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeleteRequestData : tokenRefID : " + string);
                        TokenRefreshStatusResponse tokenRefreshStatusResponse = this.qB.tokenRefreshStatus(this.cx(), string);
                        if (!tokenRefreshStatusResponse.getReasonCode().equals((Object)"00")) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeleteRequestData : ERROR: tokenRefreshStatus failed on SDK");
                        } else {
                            block21 : {
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeleteRequestData : ClientVersion = " + tokenRefreshStatusResponse.getClientVersion());
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeleteRequestData : TokenDataVersion = " + tokenRefreshStatusResponse.getTokenDataVersion());
                                jsonObject2 = new JsonObject();
                                c.a a3 = this.qA.cA();
                                if (a3.deviceCertificate != null) {
                                    jsonObject2.addProperty("devicePublicKeyCert", a3.deviceCertificate);
                                }
                                if (a3.deviceSigningCertificate == null) break block21;
                                jsonObject2.addProperty("deviceSigningPublicKeyCert", a3.deviceSigningCertificate);
                            }
                            jsonObject2.addProperty("clientAPIVersion", tokenRefreshStatusResponse.getClientVersion());
                            jsonObject2.addProperty("tokenDataVersion", tokenRefreshStatusResponse.getTokenDataVersion());
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeleteRequestData : token status : " + tokenRefreshStatusResponse.getReasonCode() + " : " + tokenRefreshStatusResponse.getDetailCode());
                            jsonObject.addProperty("tokenRefId", string);
                            jsonObject.addProperty("responseCode", tokenRefreshStatusResponse.getReasonCode());
                            jsonObject.addProperty("detailCode", tokenRefreshStatusResponse.getDetailCode());
                            String string2 = a.c(jsonObject).toString();
                            com.samsung.android.spayfw.b.c.m("AmexPayProvider", "getDeleteRequestData : Sorted JSON Data" + string2);
                            try {
                                c3 = this.qA.c(this.pl.getContent(), null, string2);
                                if (c3 == null) {
                                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeleteRequestData : ERROR: Signed data is null");
                                }
                                break block22;
                            }
                            catch (AmexTAException amexTAException) {
                                com.samsung.android.spayfw.b.c.c("AmexPayProvider", "getDeleteRequestData : ERROR: Unable to sign data" + amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                            }
                        }
                    }
                    break block24;
                    catch (AmexTAException amexTAException) {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeleteRequestData : ERROR: Unable to get device certificates");
                        amexTAException.printStackTrace();
                    }
                    break block24;
                }
                jsonObject.addProperty("secureTokenDataSignature", c3.requestDataSignature);
                jsonObject.add("secureDeviceData", (JsonElement)jsonObject2);
                String string3 = this.pp.getString(string + "_transaction_json_data", null);
                String string4 = this.pp.getString(string + "_transaction_param", null);
                if (string3 != null && string4 != null) {
                    try {
                        String string5 = this.qA.o(string3, string4);
                        if (string5 == null) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeleteRequestData : ERROR: Cannot decrypt Access Key");
                        }
                        String string6 = new String(Base64.decode((String)string5, (int)2));
                        c.c c4 = this.qA.c(this.pl.getContent(), string6, null);
                        if (c4 != null) {
                            jsonObject.addProperty("encryptedData", c4.encryptedRequestData);
                            jsonObject.addProperty("encryptionParameters", c4.encryptionParams);
                            break block23;
                        }
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeleteRequestData : ERROR: Cannot prepare Access Key payload");
                    }
                    catch (AmexTAException amexTAException) {
                        com.samsung.android.spayfw.b.c.c("AmexPayProvider", "getDeleteRequestData : ERROR: Invalid encryptedData" + amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                    }
                } else {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeleteRequestData : WARNING: Access key not found");
                }
            }
            c2.a(jsonObject);
            c2.setErrorCode(0);
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeleteRequestData : Exit");
        // MONITOREXIT : a2
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected CertificateInfo[] getDeviceCertificates() {
        a a2 = this;
        synchronized (a2) {
            CertificateInfo[] arrcertificateInfo;
            c.a a3;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeviceCertificates : Enter");
            try {
                c.a a4;
                a3 = a4 = this.qA.cA();
            }
            catch (AmexTAException amexTAException) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                a3 = null;
            }
            if (a3 == null || a3.deviceCertificate == null || a3.deviceSigningCertificate == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getDeviceCertificates : ERROR: Null device certificates");
                arrcertificateInfo = null;
            } else {
                arrcertificateInfo = new CertificateInfo[2];
                arrcertificateInfo[0] = new CertificateInfo();
                arrcertificateInfo[0].setUsage("CA");
                arrcertificateInfo[0].setAlias("DeviceCert");
                arrcertificateInfo[0].setContent(a3.deviceCertificate);
                arrcertificateInfo[1] = new CertificateInfo();
                arrcertificateInfo[1].setUsage("VER");
                arrcertificateInfo[1].setAlias("Amex-DeviceSigningCert");
                arrcertificateInfo[1].setContent(a3.deviceSigningCertificate);
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getDeviceCertificates : Exit");
            return arrcertificateInfo;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        com.samsung.android.spayfw.payprovider.c c2;
        block20 : {
            c.a a3;
            JsonObject jsonObject;
            JsonObject jsonObject2;
            JsonObject jsonObject3;
            c.c c3;
            block18 : {
                block24 : {
                    EnrollCardReferenceInfo enrollCardReferenceInfo;
                    block22 : {
                        EnrollCardPanInfo enrollCardPanInfo;
                        block23 : {
                            block21 : {
                                block19 : {
                                    a a2 = this;
                                    // MONITORENTER : a2
                                    c2 = new com.samsung.android.spayfw.payprovider.c();
                                    c2.setErrorCode(-2);
                                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getEnrollmentRequestData : Enter");
                                    if (this.pl != null) break block19;
                                    c2.setErrorCode(0);
                                    c2.a(new JsonObject());
                                    c2.e(this.b(enrollCardInfo));
                                    break block20;
                                }
                                if (enrollCardInfo != null) break block21;
                                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getEnrollmentRequestData : ERROR: Invalid or Incomplete card information, cannot continue with enrollment");
                                c2.setErrorCode(-4);
                                break block20;
                            }
                            jsonObject3 = new JsonObject();
                            if (!(enrollCardInfo instanceof EnrollCardPanInfo)) break block22;
                            enrollCardPanInfo = (EnrollCardPanInfo)enrollCardInfo;
                            if (enrollCardPanInfo.getPAN() != null) break block23;
                            c2.setErrorCode(-4);
                            break block20;
                        }
                        if (enrollCardPanInfo.getPAN() != null && !enrollCardPanInfo.getPAN().isEmpty()) {
                            jsonObject3.addProperty("accountNumber", enrollCardPanInfo.getPAN());
                        }
                        break block24;
                    }
                    if (enrollCardInfo instanceof EnrollCardReferenceInfo && (enrollCardReferenceInfo = (EnrollCardReferenceInfo)enrollCardInfo).getReferenceType() != null && enrollCardReferenceInfo.getReferenceType().equals((Object)"referenceId") && enrollCardReferenceInfo.getExtraEnrollData() != null) {
                        jsonObject3.addProperty("accountRefId", enrollCardReferenceInfo.getExtraEnrollData().getString("cardReferenceId"));
                    }
                }
                if (enrollCardInfo.getCardEntryMode() != null) {
                    if (enrollCardInfo.getCardEntryMode().equals((Object)"MAN")) {
                        jsonObject3.addProperty("accountInputMethod", "01");
                    } else if (enrollCardInfo.getCardEntryMode().equals((Object)"OCR")) {
                        jsonObject3.addProperty("accountInputMethod", "02");
                    } else {
                        jsonObject3.addProperty("accountInputMethod", "03");
                    }
                    if (enrollCardInfo.getCardEntryMode().equals((Object)"FILE")) {
                        jsonObject3.addProperty("onFileIndicator", Boolean.valueOf((boolean)true));
                    } else {
                        jsonObject3.addProperty("onFileIndicator", Boolean.valueOf((boolean)false));
                    }
                }
                jsonObject = new JsonObject();
                jsonObject.add("accountData", (JsonElement)jsonObject3);
                jsonObject2 = new JsonObject();
                a3 = this.qA.cA();
                if (a3.deviceEncryptionCertificate == null) break block18;
                jsonObject2.addProperty("deviceEncryptionPublicKeyCert", a3.deviceEncryptionCertificate);
            }
            if (a3.deviceCertificate != null) {
                jsonObject2.addProperty("devicePublicKeyCert", a3.deviceCertificate);
            }
            if (a3.deviceSigningCertificate != null) {
                jsonObject2.addProperty("deviceSigningPublicKeyCert", a3.deviceSigningCertificate);
            }
            String string = a.c(jsonObject3).toString() + a.c(jsonObject2).toString();
            try {
                c3 = this.qA.c(this.pl.getContent(), jsonObject.toString(), string);
            }
            catch (AmexTAException amexTAException) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getEnrollmentRequestData : ERROR: Unable to Sign enrollment data");
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            }
            JsonObject jsonObject4 = new JsonObject();
            jsonObject4.addProperty("encryptedData", c3.encryptedRequestData);
            jsonObject4.add("secureDeviceData", (JsonElement)jsonObject2);
            jsonObject4.addProperty("encryptionParameters", c3.encryptionParams);
            jsonObject4.addProperty("accountDataSignature", c3.requestDataSignature);
            c2.a(jsonObject4);
            this.mBillingInfo = billingInfo;
            enrollCardInfo.incrementRefCount();
            this.kQ = enrollCardInfo;
            c2.e(this.b(enrollCardInfo));
            c2.setErrorCode(0);
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "accountData : " + a.c(jsonObject3).toString());
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "encryptedData : " + a.c(jsonObject).toString());
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "secureDeviceData : " + a.c(jsonObject2).toString());
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "enrollRequest : " + a.c(jsonObject4).toString());
            break block20;
            catch (AmexTAException amexTAException) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getEnrollmentRequestData : ERROR: Unable to get device certificates");
                amexTAException.printStackTrace();
            }
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getEnrollmentRequestData : Exit");
        // MONITOREXIT : a2
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean getPayReadyState() {
        boolean bl = false;
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getPayReadyState : Enter");
            if (this.mProviderTokenKey == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getPayReadyState : ERROR : No token to check for readiness");
            } else if (this.pj != null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getPayReadyState : WARNING : Token Busy");
                bl = true;
            } else {
                String string = this.mProviderTokenKey.cn();
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getPayReadyState : Token ID :  " + string);
                TokenRefreshStatusResponse tokenRefreshStatusResponse = this.qB.tokenRefreshStatus(this.cx(), string);
                if (!tokenRefreshStatusResponse.getReasonCode().equals((Object)"00")) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getPayReadyState : ERROR : tokenRefreshStatus failed on SDK " + tokenRefreshStatusResponse.getReasonCode());
                    bl = false;
                } else {
                    int n2 = tokenRefreshStatusResponse.getLupcCount();
                    boolean bl2 = tokenRefreshStatusResponse.isRefreshRequired();
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getPayReadyState : LUPC Count : " + n2);
                    if (bl2 && n2 == 0 && tokenRefreshStatusResponse.getMaxATC() == 0 && tokenRefreshStatusResponse.getTokenDataVersion() == null) {
                        this.pp.edit().putBoolean(string + "_reperso_required", true);
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getPayReadyState : ERROR : Token needs Re-perso");
                        this.cy();
                        bl = false;
                    } else {
                        this.pp.edit().putBoolean(string + "_reperso_required", false);
                        if (n2 == 0) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getPayReadyState : ERROR : Token unavailable for payment");
                            bl = false;
                        } else if (!TextUtils.isEmpty((CharSequence)tokenRefreshStatusResponse.getTokenState()) && !tokenRefreshStatusResponse.getTokenState().equalsIgnoreCase("01")) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getPayReadyState : ERROR : Token unavailable for payment :" + tokenRefreshStatusResponse.getTokenState());
                            bl = false;
                        } else {
                            if (bl2) {
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getPayReadyState : WARNING : Token needs to be replenished");
                            }
                            bl = true;
                        }
                    }
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getPayReadyState : Exit");
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getProvisionRequestData : Enter");
            com.samsung.android.spayfw.payprovider.c c2 = new com.samsung.android.spayfw.payprovider.c();
            c2.setErrorCode(-2);
            if (this.pl == null) {
                c2.setErrorCode(0);
                c2.a(new JsonObject());
            } else if (this.kQ == null || this.mBillingInfo == null) {
                c2.setErrorCode(-4);
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getProvisionRequestData : ERROR: Card data is null");
            } else {
                JsonObject jsonObject = new JsonObject();
                JsonObject jsonObject2 = new JsonObject();
                if (this.mBillingInfo.getCity() != null) {
                    jsonObject2.addProperty("city", this.mBillingInfo.getCity());
                }
                if (this.mBillingInfo.getCountry() != null) {
                    jsonObject2.addProperty("country", this.mBillingInfo.getCountry());
                }
                if (this.mBillingInfo.getStreet1() != null) {
                    jsonObject2.addProperty("addressLine1", this.mBillingInfo.getStreet1());
                }
                if (this.mBillingInfo.getStreet2() != null) {
                    jsonObject2.addProperty("addressLine2", this.mBillingInfo.getStreet2());
                }
                if (this.mBillingInfo.getState() != null) {
                    jsonObject2.addProperty("state", this.mBillingInfo.getState());
                }
                if (this.mBillingInfo.getZip() != null) {
                    jsonObject2.addProperty("zipCode", this.mBillingInfo.getZip());
                }
                jsonObject.add("billingAddress", (JsonElement)jsonObject2);
                JsonObject jsonObject3 = new JsonObject();
                if (this.kQ instanceof EnrollCardPanInfo) {
                    EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo)this.kQ;
                    jsonObject3.addProperty("cardExpiry", enrollCardPanInfo.getExpMonth() + "/" + enrollCardPanInfo.getExpYear());
                    jsonObject3.addProperty("cid", enrollCardPanInfo.getCVV());
                    jsonObject.add("cardVerificationValues", (JsonElement)jsonObject3);
                }
                jsonObject.addProperty("cardholderName", this.kQ.getName());
                JsonObject jsonObject4 = new JsonObject();
                jsonObject4.add("accountData", (JsonElement)jsonObject);
                JsonObject jsonObject5 = new JsonObject();
                this.qD = "ProvReqID_" + System.currentTimeMillis();
                String[] arrstring = new String[]{'1' + this.qD};
                byte[] arrby = g.d(arrstring);
                int n2 = this.qA.open(arrby);
                if (n2 < 0) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getProvisionRequestData : ERROR: AmexTAController open failed = " + n2);
                } else {
                    byte[] arrby2 = new byte[384];
                    int n3 = this.qA.initializeSecureChannel(null, arrby2);
                    if (n3 < 0) {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getProvisionRequestData : ERROR: AmexTAController initializeSecureChannel failed = " + n3);
                    } else {
                        TokenRefreshStatusResponse tokenRefreshStatusResponse;
                        c.c c3;
                        String string;
                        byte[] arrby3 = Arrays.copyOfRange((byte[])arrby2, (int)0, (int)n3);
                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getProvisionRequestData : Ephemeral Key in LLVAR  = " + new String(arrby3));
                        byte[][] arrby4 = g.llVarToBytes(arrby3);
                        if (arrby4 != null && arrby4[0] != null) {
                            string = new String(arrby4[0]);
                        } else {
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getProvisionRequestData : ephemeralPublicKeyBytes or ephemeralPublicKeyBytes[0] is null");
                            string = null;
                        }
                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getProvisionRequestData : Success calling TA initializeSecureChannel, eccPublicKey = " + string);
                        jsonObject5.addProperty("ephemeralPublicKey", string);
                        try {
                            c.a a3 = this.qA.cA();
                            if (a3.deviceCertificate != null) {
                                jsonObject5.addProperty("devicePublicKeyCert", a3.deviceCertificate);
                            }
                            if (a3.deviceSigningCertificate != null) {
                                jsonObject5.addProperty("deviceSigningPublicKeyCert", a3.deviceSigningCertificate);
                            }
                            if (a3.deviceEncryptionCertificate != null) {
                                jsonObject5.addProperty("deviceEncryptionPublicKeyCert", a3.deviceEncryptionCertificate);
                            }
                        }
                        catch (AmexTAException amexTAException) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getProvisionRequestData : ERROR: Unable to get device certificates");
                            amexTAException.printStackTrace();
                        }
                        if ((tokenRefreshStatusResponse = this.qB.tokenRefreshStatus(this.cx(), null)) != null && (!TextUtils.isEmpty((CharSequence)tokenRefreshStatusResponse.getReasonCode()) && tokenRefreshStatusResponse.getReasonCode().equalsIgnoreCase("12") || tokenRefreshStatusResponse.getReasonCode().equalsIgnoreCase("11"))) {
                            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "getProvisionRequestData : Trying a one time recovery for fatal cases");
                            TokenCloseResponse tokenCloseResponse = this.qB.tokenClose();
                            if (tokenCloseResponse != null) {
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "tokenClose returns :" + tokenCloseResponse.getDetailCode());
                            }
                            tokenRefreshStatusResponse = this.qB.tokenRefreshStatus(this.cx(), null);
                        }
                        if (tokenRefreshStatusResponse != null) {
                            jsonObject5.addProperty("clientAPIVersion", tokenRefreshStatusResponse.getClientVersion());
                        }
                        String string2 = a.c(jsonObject).toString() + a.c(jsonObject5).toString();
                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getProvisionRequestData : dataToBeSigned " + string2);
                        try {
                            c.c c4;
                            c3 = c4 = this.qA.c(this.pl.getContent(), jsonObject4.toString(), string2);
                        }
                        catch (AmexTAException amexTAException) {
                            com.samsung.android.spayfw.b.c.c("AmexPayProvider", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
                            c3 = null;
                        }
                        if (c3 == null) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getProvisionRequestData : ERROR: Unable to sign the request data");
                        } else {
                            if (this.qA.close(arrby) != 0) {
                                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getProvisionRequestData : ERROR: TA close failed");
                            }
                            JsonObject jsonObject6 = new JsonObject();
                            jsonObject6.addProperty("encryptedData", c3.encryptedRequestData);
                            jsonObject6.add("secureDeviceData", (JsonElement)jsonObject5);
                            jsonObject6.addProperty("encryptionParameters", c3.encryptionParams);
                            jsonObject6.addProperty("accountDataSignature", c3.requestDataSignature);
                            JsonObject jsonObject7 = new JsonObject();
                            jsonObject7.addProperty("imei", DeviceInfo.getDeviceImei(this.mContext));
                            jsonObject7.addProperty("serial", DeviceInfo.getDeviceSerialNumber());
                            jsonObject7.addProperty("msisdn", DeviceInfo.getMsisdn(this.mContext));
                            jsonObject6.add("deviceData", (JsonElement)jsonObject7);
                            c2.a(jsonObject6);
                            c2.e(this.a(provisionTokenInfo));
                            c2.setErrorCode(0);
                            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "accountData : " + a.c(jsonObject).toString());
                            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "encryptedData : " + a.c(jsonObject4).toString());
                            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "secureDeviceData : " + a.c(jsonObject5).toString());
                            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "enrollRequest : " + a.c(jsonObject6).toString());
                        }
                    }
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getProvisionRequestData : Exit");
            return c2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getReplenishmentRequestData() {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.payprovider.c c2;
            block29 : {
                byte[] arrby;
                JsonObject jsonObject;
                block33 : {
                    TokenRefreshStatusResponse tokenRefreshStatusResponse;
                    JsonObject jsonObject2;
                    c.c c3;
                    block30 : {
                        String string;
                        c.a a3;
                        block32 : {
                            byte[] arrby2;
                            byte[] arrby3;
                            block28 : {
                                String string2;
                                block31 : {
                                    block27 : {
                                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : Enter");
                                        c2 = new com.samsung.android.spayfw.payprovider.c();
                                        c2.setErrorCode(-2);
                                        if (this.mProviderTokenKey == null) {
                                            c2.setErrorCode(-4);
                                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: Invalid token");
                                        } else if (qF) {
                                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : Card selected and in use, retry later");
                                        } else {
                                            jsonObject = new JsonObject();
                                            string2 = this.mProviderTokenKey.cn();
                                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : Token Ref Id = " + string2);
                                            String[] arrstring = new String[]{'1' + string2};
                                            arrby = g.d(arrstring);
                                            int n2 = this.qA.open(arrby);
                                            if (n2 < 0) {
                                                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: TA open failed = " + n2);
                                                c2.setErrorCode(-6);
                                            } else {
                                                byte[] arrby4 = new byte[384];
                                                int n3 = this.qA.initializeSecureChannel(null, arrby4);
                                                if (n3 < 0) {
                                                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA initializeSecureChannel failed = " + n3);
                                                    c2.setErrorCode(-6);
                                                } else {
                                                    byte[] arrby5 = Arrays.copyOfRange((byte[])arrby4, (int)0, (int)n3);
                                                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : Ephemeral Key in LLVAR  = " + new String(arrby5));
                                                    byte[][] arrby6 = g.llVarToBytes(arrby5);
                                                    if (arrby6 != null && arrby6[0] != null) {
                                                        string = new String(arrby6[0]);
                                                    } else {
                                                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "ephemeralPublicKeyBytes or ephemeralPublicKeyBytes[0] is null");
                                                        string = null;
                                                    }
                                                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : eccPublicKey = " + string);
                                                    try {
                                                        a3 = this.qA.cA();
                                                        if (a3 == null) {
                                                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA getDeviceCertificates failed");
                                                            c2.setErrorCode(-6);
                                                        }
                                                        break block27;
                                                    }
                                                    catch (AmexTAException amexTAException) {
                                                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: Cannot fetch device certificates");
                                                        c2.setErrorCode(-6);
                                                        amexTAException.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                        break block29;
                                    }
                                    tokenRefreshStatusResponse = this.qB.tokenRefreshStatus(this.cx(), string2);
                                    if (tokenRefreshStatusResponse.getReasonCode().equals((Object)"00")) break block31;
                                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: tokenRefreshStatus failed on SDK");
                                    c2.setErrorCode(-2);
                                    break block29;
                                }
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : getClientVersion = " + tokenRefreshStatusResponse.getClientVersion());
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : getTokenDataVersion = " + tokenRefreshStatusResponse.getTokenDataVersion());
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : getMaxATC = " + tokenRefreshStatusResponse.getMaxATC());
                                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : getLupcCount = " + tokenRefreshStatusResponse.getLupcCount());
                                jsonObject2 = new JsonObject();
                                if (!this.pp.getBoolean(string2 + "_reperso_required", false)) break block32;
                                JsonObject jsonObject3 = new JsonObject();
                                jsonObject2.addProperty("ephemeral_public_key", string);
                                jsonObject2.addProperty("amex_device_public_key", a3.deviceCertificate);
                                jsonObject2.addProperty("amex_device_signing_public_key", a3.deviceSigningCertificate);
                                jsonObject2.addProperty("client_api_version", tokenRefreshStatusResponse.getClientVersion());
                                jsonObject2.addProperty("token_data_version", "");
                                jsonObject2.addProperty("maximum_atc", Integer.toString((int)tokenRefreshStatusResponse.getMaxATC()));
                                jsonObject2.addProperty("remaining_lupc_count", Integer.toString((int)tokenRefreshStatusResponse.getLupcCount()));
                                jsonObject3.add("security_data", (JsonElement)jsonObject2);
                                com.samsung.android.spayfw.b.c.i("AmexPayProvider", "getReplenishmentRequestData : TSP Enc cert : " + this.pl.getContent());
                                try {
                                    String string3 = a.c(jsonObject3).toString();
                                    com.samsung.android.spayfw.b.c.i("AmexPayProvider", "getReplenishmentRequestData : inputJwsString : " + string3);
                                    arrby2 = this.qA.a(AmexCommands.ProcessDataJwsJwe.JsonOperation.rm, null, string3.getBytes(), this.pl.getContent());
                                    if (arrby2 == null) {
                                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processDataJwsJwe failed on JWS");
                                        c2.setErrorCode(-6);
                                    }
                                    arrby3 = this.qA.a(AmexCommands.ProcessDataJwsJwe.JsonOperation.rn, this.qC.getBytes(), null, this.pl.getContent());
                                    if (arrby3 != null) break block28;
                                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processDataJwsJwe failed on JWE");
                                    c2.setErrorCode(-6);
                                    break block29;
                                }
                                catch (AmexTAException amexTAException) {
                                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: Cannot perform JWS/JWE on Input data");
                                    amexTAException.printStackTrace();
                                }
                                break block29;
                            }
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : JWS String: " + new String(arrby2));
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : JWE String: " + new String(arrby3));
                            JsonObject jsonObject4 = new JsonObject();
                            jsonObject4.addProperty("securityDataSignature", new String(arrby2));
                            jsonObject.addProperty("encryptedPayload", new String(arrby3));
                            jsonObject.addProperty("persoVersion", "2");
                            jsonObject.add("signatureData", (JsonElement)jsonObject4);
                            break block33;
                        }
                        jsonObject2.addProperty("ephemeralPublicKey", string);
                        if (a3.deviceCertificate != null) {
                            jsonObject2.addProperty("devicePublicKeyCert", a3.deviceCertificate);
                        }
                        if (a3.deviceSigningCertificate != null) {
                            jsonObject2.addProperty("deviceSigningPublicKeyCert", a3.deviceSigningCertificate);
                        }
                        jsonObject2.addProperty("clientAPIVersion", tokenRefreshStatusResponse.getClientVersion());
                        jsonObject2.addProperty("tokenDataVersion", tokenRefreshStatusResponse.getTokenDataVersion());
                        jsonObject2.addProperty("maxATC", (Number)tokenRefreshStatusResponse.getMaxATC());
                        jsonObject2.addProperty("remainingLUPCCount", (Number)tokenRefreshStatusResponse.getLupcCount());
                        try {
                            c3 = this.qA.c(this.pl.getContent(), null, null);
                            if (c3 == null) {
                                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processRequestData failed");
                                c2.setErrorCode(-2);
                            }
                            break block30;
                        }
                        catch (AmexTAException amexTAException) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processRequestData failed");
                            c2.setErrorCode(-2);
                            amexTAException.printStackTrace();
                        }
                        break block29;
                    }
                    jsonObject.addProperty("encryptedData", c3.encryptedRequestData);
                    jsonObject.addProperty("encryptionParameters", c3.encryptionParams);
                    jsonObject.add("secureDeviceData", (JsonElement)jsonObject2);
                    jsonObject.addProperty("responseCode", tokenRefreshStatusResponse.getReasonCode());
                    jsonObject.addProperty("detailCode", tokenRefreshStatusResponse.getDetailCode());
                    jsonObject.addProperty("detailMessage", tokenRefreshStatusResponse.getDetailMessage());
                    jsonObject.addProperty("secureTokenDataSignature", "null");
                }
                if (this.qA.close(arrby) != 0) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "getReplenishmentRequestData : ERROR: TA close failed");
                    c2.setErrorCode(-6);
                } else {
                    c2.a(a.c(jsonObject));
                    c2.setErrorCode(0);
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "getReplenishmentRequestData : Exit");
            return c2;
        }
    }

    @Override
    public int getTransactionData(Bundle bundle, com.samsung.android.spayfw.payprovider.i i2) {
        a a2 = this;
        synchronized (a2) {
            int n2 = b.a(this.mContext, this.qA, this.pl, this.pp).a(this.mProviderTokenKey, bundle, i2);
            return n2;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getVerifyIdvRequestData(VerifyIdvInfo var1_1) {
        block13 : {
            var12_2 = this;
            // MONITORENTER : var12_2
            var2_3 = new com.samsung.android.spayfw.payprovider.c();
            var2_3.setErrorCode(0);
            if (var1_1 != null) ** GOTO lbl10
            var2_3.setErrorCode(-4);
            // MONITOREXIT : var12_2
            return var2_3;
lbl10: // 1 sources:
            var4_4 = new JsonObject();
            try {
                var11_5 = this.qA.cA();
                if (var11_5.deviceCertificate != null) {
                    var4_4.addProperty("devicePublicKeyCert", var11_5.deviceCertificate);
                }
                if (var11_5.deviceSigningCertificate != null) {
                    var4_4.addProperty("deviceSigningPublicKeyCert", var11_5.deviceSigningCertificate);
                }
                ** GOTO lbl28
            }
            catch (Exception var5_9) {
                try {
                    var2_3.setErrorCode(-2);
                    var5_9.printStackTrace();
                }
                catch (Exception var9_8) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", var9_8.getMessage(), var9_8);
                    var2_3.setErrorCode(-2);
                    return var2_3;
                }
lbl28: // 4 sources:
                var7_7 = var10_6 = this.qA.c(this.pl.getContent(), null, var1_1.getValue());
                break block13;
            }
            catch (AmexTAException var6_10) {
                var2_3.setErrorCode(-2);
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", var6_10.getMessage(), (Throwable)var6_10);
                var7_7 = null;
            }
        }
        if (var7_7 == null) {
            var2_3.setErrorCode(-2);
            return var2_3;
        }
        var8_11 = new JsonObject();
        var8_11.addProperty("authenticationCodeSignature", var7_7.requestDataSignature);
        var8_11.add("secureDeviceData", (JsonElement)var4_4);
        var2_3.a(var8_11);
        return var2_3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        byte[] arrby2 = null;
        a a2 = this;
        synchronized (a2) {
            long l2 = System.currentTimeMillis();
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "handleApdu: Enter: " + l2);
            if (!this.pn) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "handleApdu: ERROR: handleApdu must never be called when there is already a pending NFC");
            } else {
                if (arrby == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error: apduBuffer received is NULL");
                    return null;
                }
                com.samsung.android.spayfw.b.c.m("AmexPayProvider", "HandlAPDU - Request = " + com.samsung.android.spayfw.utils.h.encodeHex(arrby));
                TokenAPDUResponse tokenAPDUResponse = this.qB.tokenAPDU(arrby);
                if (!tokenAPDUResponse.getReasonCode().equals((Object)"00")) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "tokenAPDU failed on SDK");
                }
                arrby2 = tokenAPDUResponse.getOutBuffer();
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
            }
            return arrby2;
        }
    }

    @Override
    protected void init() {
        if (this.mProviderTokenKey != null) {
            String string = this.pp.getString(this.mProviderTokenKey.cn() + "_config_string", null);
            if (string != null) {
                this.aB(string);
                return;
            }
            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "init : ERROR: Cannot find config entry for this token");
            return;
        }
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "init : Token key is null");
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    protected void interruptMstPay() {
        a a2 = this;
        // MONITORENTER : a2
        // MONITOREXIT : a2
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public boolean isPayAllowedForPresentationMode(int n2) {
        a a2 = this;
        // MONITORENTER : a2
        boolean bl = true;
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isPayAllowedForPresentationMode : Enter with presentation mode as : " + n2);
        if (n2 == 2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isPayAllowedForPresentationMode : Enter (CARD_PRESENT_MODE_MST)");
            if (this.qE.size() == 0) {
                if (this.mProviderTokenKey == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "isPayAllowedForPresentationMode : ERROR: Invalid token reference");
                    return bl;
                }
                String string = this.mProviderTokenKey.cn();
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isPayAllowedForPresentationMode : token Ref ID : " + string);
                String string2 = this.pp.getString(string + "_config_string", null);
                if (string2 == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "isPayAllowedForPresentationMode : ERROR: cannot find config for this token");
                    return bl;
                }
                this.aB(string2);
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isPayAllowedForPresentationMode : Token Config Map -> " + this.qE.toString());
            if (this.qE.containsKey((Object)"MST_SUPPORTED")) {
                bl = Boolean.parseBoolean((String)((String)this.qE.get((Object)"MST_SUPPORTED")));
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isPayAllowedForPresentationMode : returning " + bl);
        }
        // MONITOREXIT : a2
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        a a2 = this;
        synchronized (a2) {
            JsonElement jsonElement = jsonObject.get("certificateIdentifier");
            if (jsonElement == null) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isReplenishDataAvailable: Certificate Id is not present");
                this.qC = "";
            } else {
                this.qC = jsonElement.getAsString();
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isReplenishDataAvailable: Certificate Id is  present : " + this.qC);
            }
            if (jsonObject.getAsJsonObject("secureTokenData") == null) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isReplenishDataAvailable: returns : false");
                return false;
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "isReplenishDataAvailable: returns : true");
            return true;
        }
    }

    @Override
    protected void loadTA() {
        this.qA.loadTA();
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "loadTA : Load AMEX TA");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void onPaySwitch(int n2, int n3) {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "onPaySwitch : Enter");
            super.onPaySwitch(n2, n3);
            if (n2 == 1 && n3 == 2) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "onPaySwitch : ERROR: Payment mode switching from NFC to MST. Must never happen");
            } else {
                this.qH = true;
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "onPaySwitch : Exit");
            return;
        }
    }

    @Override
    protected boolean prepareMstPay() {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "prepareMstPay : Enter");
            this.cq();
            this.po = true;
            if (!this.qB.tokenMST().getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "prepareMstPay : ERROR: tokenMST failed on SDK");
                this.stopMstPay(false);
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "prepareMstPay : Exit");
            boolean bl = this.po;
            return bl;
        }
    }

    @Override
    public boolean prepareNfcPay() {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "prepareNfcPay : Enter");
            this.cq();
            this.qG = true;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "prepareNfcPay : Exit");
            boolean bl = this.qG;
            return bl;
        }
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        a a2 = this;
        synchronized (a2) {
            TransactionDetails transactionDetails = b.a(this.mContext, this.qA, this.pl, this.pp).a(this.mProviderTokenKey, object);
            return transactionDetails;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void replenishAlarmExpired() {
        a a2 = this;
        synchronized (a2) {
            if (this.mProviderTokenKey == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishAlarmExpired : ERROR: cannot check replenishment, providerTokenKey is null");
            } else {
                this.av(this.mProviderTokenKey.cn());
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    protected e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        e e2;
        TokenPersoResponse tokenPersoResponse;
        String string12;
        JsonObject jsonObject3;
        block20 : {
            String string2;
            String string5;
            String string3;
            String string;
            String string4;
            a a2 = this;
            // MONITORENTER : a2
            com.samsung.android.spayfw.b.c.i("AmexPayProvider", "replenishToken : Enter");
            e2 = new e();
            try {
                e2.setErrorCode(0);
                if (jsonObject == null) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : ERROR: Input Data is NULL");
                    e2.setErrorCode(-4);
                    // MONITOREXIT : a2
                    return e2;
                }
                JsonObject jsonObject2 = jsonObject.getAsJsonObject("secureTokenData");
                if (jsonObject2 != null) {
                    String string6 = jsonObject2.get("initializationVector").getAsString();
                    String string7 = jsonObject2.get("encryptedTokenData").getAsString();
                    String string8 = jsonObject2.get("encryptedTokenDataHMAC").getAsString();
                    String string9 = jsonObject2.get("cloudPublicKeyCert").getAsString();
                    string4 = string7;
                    string2 = string6;
                    string5 = string9;
                    string3 = string8;
                } else {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : secureTokenData is NULL");
                    string5 = null;
                    string3 = null;
                    string4 = null;
                    string2 = null;
                }
                string = this.kQ != null ? this.kQ.getWalletId() : com.samsung.android.spayfw.core.e.h(this.mContext).getConfig("CONFIG_WALLET_ID");
            }
            catch (Exception exception) {
                com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
                this.cy();
                e2.setErrorCode(-2);
                return e2;
            }
            if (string == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : ERROR: Wallet Id is NULL");
                e2.setErrorCode(-2);
                return e2;
            }
            if (a.au(jsonObject.get("tokenStatus").getAsString()) == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : ERROR: Token Status is NULL");
                e2.setErrorCode(-2);
                return e2;
            }
            StringBuilder stringBuilder = new StringBuilder().append(string5);
            String string10 = this.pm == null ? "" : this.pm.getContent();
            String string11 = stringBuilder.append(string10).toString();
            string12 = this.mProviderTokenKey.cn();
            if (!this.qB.tokenOpen(OperationMode.PROVISION, string12).getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : ERROR: tokenOpen failed on SDK");
                e2.setErrorCode(-2);
                return e2;
            }
            String string13 = string11.replace((CharSequence)"\n", (CharSequence)"");
            Object[] arrobject = new Object[]{'1' + string13, '2' + com.samsung.android.spayfw.utils.h.encodeHex(string.getBytes())};
            byte[] arrby = LLVARUtil.objectsToLLVar(arrobject);
            if (!this.qB.tokenChannelUpdate(arrby).getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : ERROR: tokenChannelUpdate failed on SDK");
                e2.setErrorCode(-2);
                return e2;
            }
            Object[] arrobject2 = new Object[]{'1' + string4, '1' + string2, '1' + string3};
            byte[] arrby2 = LLVARUtil.objectsToLLVar(arrobject2);
            tokenPersoResponse = this.qB.tokenPerso(HexUtils.getSafePrintChars(arrby2));
            TokenCloseResponse tokenCloseResponse = this.qB.tokenClose();
            if (!tokenPersoResponse.getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : ERROR: tokenPerso failed on SDK");
                e2.setErrorCode(-2);
                return e2;
            }
            this.pp.edit().putString(string12 + "_config_string", tokenPersoResponse.getTokenConfiguration()).apply();
            this.aB(tokenPersoResponse.getTokenConfiguration());
            if (!tokenCloseResponse.getReasonCode().equals((Object)"00")) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "replenishToken : ERROR: tokenClose failed on SDK");
                e2.setErrorCode(-2);
                return e2;
            }
            if (this.getPFTokenStatus() != "ACTIVE") {
                this.setPFTokenStatus("ACTIVE");
            }
            jsonObject3 = new JsonObject();
            try {
                c.a a3 = this.qA.cA();
                if (a3.deviceCertificate != null) {
                    jsonObject3.addProperty("devicePublicKeyCert", a3.deviceCertificate);
                }
                if (a3.deviceSigningCertificate == null) break block20;
                jsonObject3.addProperty("deviceSigningPublicKeyCert", a3.deviceSigningCertificate);
            }
            catch (AmexTAException amexTAException) {
                e2.setErrorCode(-2);
                return e2;
            }
        }
        e2.setProviderTokenKey(new f(string12));
        JsonObject jsonObject4 = new JsonObject();
        if (this.pp.getBoolean(string12 + "_reperso_required", false)) {
            jsonObject4.addProperty("persoVersion", "2");
            JsonObject jsonObject5 = new JsonObject();
            jsonObject5.addProperty("secureTokenDataSignature", tokenPersoResponse.getTokenDataSignature());
            jsonObject4.add("signatureData", (JsonElement)jsonObject5);
        } else {
            jsonObject4.addProperty("secureTokenDataSignature", tokenPersoResponse.getTokenDataSignature());
        }
        jsonObject4.add("secureDeviceData", (JsonElement)jsonObject3);
        e2.b(jsonObject4);
        this.pp.edit().remove(string12 + "_replenish_retry").apply();
        ps.b(this.mProviderTokenKey);
        this.av(string12);
        return e2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public SelectCardResult selectCard() {
        a a2 = this;
        synchronized (a2) {
            SelectCardResult selectCardResult;
            block10 : {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "selectCard : Enter");
                f f2 = this.mProviderTokenKey;
                SelectCardResult selectCardResult2 = null;
                if (f2 == null) return selectCardResult2;
                if (!this.getPayReadyState()) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "selectCard : ERROR: Can not pay since LUPC reached zero or token status not active");
                    return null;
                }
                if (this.pn) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "selectCard : ERROR: Select Card called before previous Payment did not complete. This must never happen");
                    this.qG = false;
                    this.pr = false;
                    this.stopMstPay(false);
                }
                try {
                    this.qA.initializeSecuritySetup();
                    byte[] arrby = this.qA.getNonce(32);
                    if (arrby == null) {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "selectCard : ERROR: getNonce returned null");
                        return null;
                    }
                    selectCardResult = new SelectCardResult(c.cz().getTAInfo().getTAId(), arrby);
                    this.pj = this.mProviderTokenKey;
                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "selectCard : Calling Token Open");
                    TokenOperationStatus tokenOperationStatus = this.qB.tokenOpen(OperationMode.PAYMENT, this.pj.cn());
                    if (tokenOperationStatus.getReasonCode().equals((Object)"00")) break block10;
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "selectCard : ERROR: tokenOpen failed on SDK " + tokenOperationStatus.getReasonCode());
                    return selectCardResult;
                }
                catch (Exception exception) {
                    com.samsung.android.spayfw.b.c.c("AmexPayProvider", exception.getMessage(), exception);
                    return null;
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "selectCard : set card selected status on tokenOpen success");
            qF = true;
            return selectCardResult;
        }
    }

    @Override
    public void setPaymentFrameworkRequester(k k2) {
        a a2 = this;
        synchronized (a2) {
            ps = k2;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        int n2 = 0;
        while (n2 < arrcertificateInfo.length) {
            if (arrcertificateInfo[n2].getAlias().contains((CharSequence)"tsp_rsa")) {
                this.pl = arrcertificateInfo[n2];
            } else if (arrcertificateInfo[n2].getAlias().equals((Object)"tsp_ecc")) {
                this.pm = arrcertificateInfo[n2];
            }
            ++n2;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void setupReplenishAlarm() {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Entered setup Replenish Alarm");
            String string = this.mProviderTokenKey.cn();
            if (string == null) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "TrTokenId is null");
            } else {
                this.av(string);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startMstPay : Enter");
            boolean bl = false;
            if (!this.pn) {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "startMstPay: ERROR: startMstPay must never happen when there is already a pending MST");
            } else {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startMstPay : input config " + Arrays.toString((byte[])arrby));
                try {
                    bl = this.qA.a(n2, arrby);
                    if (!bl) {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "startMstPay: ERROR: MST transmission failed");
                    }
                }
                catch (Exception exception) {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "startMstPay: ERROR: MST transmission exception : " + exception.getMessage());
                    exception.printStackTrace();
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "startMstPay : Exit");
            return bl;
        }
    }

    @Override
    protected void stopMstPay(boolean bl) {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopMstPay: Enter");
            this.pn = false;
            this.po = false;
            this.qH = false;
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopMstPay: Exit");
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected Bundle stopNfcPay(int n2) {
        a a2 = this;
        synchronized (a2) {
            Bundle bundle;
            block5 : {
                int n3;
                block3 : {
                    block4 : {
                        block2 : {
                            n3 = 1;
                            bundle = new Bundle();
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopNfcPay : Enter");
                            if (this.pn && this.qG) break block2;
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopNfcPay : ERROR: Stop NFC Pay called when payment is not in progress");
                            break block3;
                        }
                        if (this.pr) break block4;
                        n3 = n2 == 4 ? 4 : 3;
                        if (n2 != 4) break block3;
                        this.pr = false;
                        bundle.putShort("nfcApduErrorCode", (short)4);
                        break block5;
                    }
                    n3 = 2;
                }
                bundle.putShort("nfcApduErrorCode", (short)n3);
                this.pn = false;
                this.qH = false;
                this.pr = false;
                this.qG = false;
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "stopNfcPay: Exit reason : " + n2 + " ISO ret = " + n3);
            }
            return bundle;
        }
    }

    @Override
    protected void unloadTA() {
        this.qA.unloadTA();
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "unloadTA : Unload AMEX TA");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void updateRequestStatus(d d2) {
        a a2 = this;
        synchronized (a2) {
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateRequestStatus : Enter: Req type = " + d2.getRequestType() + ", Req status = " + d2.ci());
            if (d2.ck() != null) {
                com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateRequestStatus : Token key string = " + d2.ck().cn());
            }
            if (d2.getRequestType() != 23) {
                switch (d2.ci()) {
                    default: {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "Error in updating status");
                        break;
                    }
                    case -1: {
                        if (d2.ck() != null) {
                            this.qA.aC(this.qD);
                        }
                        if (d2.getRequestType() != 11 || !d2.cj().equals((Object)"403.5") || d2.ck() == null) break;
                        this.a(d2.ck(), true);
                        this.ax(d2.ck().cn());
                        break;
                    }
                    case 0: {
                        if (d2.getRequestType() != 3 || d2.ck() == null) break;
                        this.qA.q(this.qD, d2.ck().cn());
                    }
                }
            }
            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateRequestStatus : Exit");
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    protected e updateTokenStatus(JsonObject var1_1, TokenStatus var2_2) {
        block21 : {
            block23 : {
                block24 : {
                    block28 : {
                        block27 : {
                            block26 : {
                                block25 : {
                                    block22 : {
                                        block20 : {
                                            var10_3 = this;
                                            // MONITORENTER : var10_3
                                            var3_4 = new JsonObject();
                                            var4_5 = new e();
                                            var4_5.setErrorCode(0);
                                            var3_4.addProperty("responseCode", "00");
                                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Enter");
                                            if (var2_2 != null) break block20;
                                            var4_5.setErrorCode(-4);
                                            break block21;
                                        }
                                        if (this.mProviderTokenKey != null) break block22;
                                        if (!var2_2.getCode().equals((Object)"DISPOSED")) {
                                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : ERROR: Invalid token, Token may not exist");
                                            var4_5.setErrorCode(-4);
                                        }
                                        break block21;
                                    }
                                    var6_6 = this.mProviderTokenKey.cn();
                                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Token Ref ID = " + var6_6);
                                    var7_7 = this.qB.tokenRefreshStatus(this.cx(), var6_6);
                                    if (var7_7 == null || !var7_7.getReasonCode().equals((Object)"00")) break block23;
                                    var8_8 = var7_7.getTokenState();
                                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : tokenState from sdk: " + var8_8);
                                    if (!var2_2.getCode().equals((Object)"ACTIVE")) break block24;
                                    if (!var7_7.isRefreshRequired() || var7_7.getLupcCount() != 0 || var7_7.getMaxATC() != 0 || var7_7.getTokenDataVersion() != null) break block25;
                                    com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Triggerring a Re-perso for token : " + var6_6 + " which was in " + this.getPFTokenStatus() + " state");
                                    this.pp.edit().putBoolean(var6_6 + "_reperso_required", true).apply();
                                    ** GOTO lbl-1000
                                }
                                if (var8_8 != null) break block26;
                                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "updateTokenStatus : ERROR: Token State is null");
                                var4_5.setErrorCode(-2);
                                break block21;
                            }
                            if (!var8_8.equals((Object)"03")) break block27;
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Activating Token");
                            ** GOTO lbl-1000
                        }
                        if (!var8_8.equals((Object)"02")) break block28;
                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Resuming Token");
                        ** GOTO lbl-1000
                    }
                    if (var8_8.equals((Object)"01")) {
                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Token already in active state, nothing to do");
                    } else lbl-1000: // 4 sources:
                    {
                        this.cy();
                    }
                    break block21;
                }
                if (var2_2.getCode().equals((Object)"SUSPENDED") || var2_2.getCode().equals((Object)"DISPOSED")) {
                    if (TextUtils.isEmpty((CharSequence)var6_6)) {
                        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "Nothing to delete : ");
                    } else if (!this.qB.tokenOpen(OperationMode.LCM, var6_6).getReasonCode().equals((Object)"00")) {
                        com.samsung.android.spayfw.b.c.e("AmexPayProvider", "updateTokenStatus : ERROR: tokenOpen failed on SDK");
                        var4_5.setErrorCode(-2);
                    } else {
                        if (var2_2.getCode().equals((Object)"SUSPENDED")) {
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Suspending Token");
                            this.setPFTokenStatus("SUSPENDED");
                            var9_9 = this.qB.tokenLCM(StateMode.SUSPEND);
                        } else {
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Deleting Token");
                            this.setPFTokenStatus("DISPOSED");
                            var9_9 = this.qB.tokenLCM(StateMode.DELETE);
                            this.pp.edit().remove(var6_6 + "_transaction_json_data").remove(var6_6 + "_replenish_retry").apply();
                            this.qA.aC(var6_6);
                        }
                        h.a(this.mContext, this.mProviderTokenKey);
                        if (var9_9 != null) {
                            com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : LCM status = " + var9_9.getReasonCode() + " : " + var9_9.getDetailCode());
                            if (!var9_9.getReasonCode().equals((Object)"00")) {
                                var4_5.setErrorCode(-2);
                            }
                            var3_4.addProperty("responseCode", var9_9.getReasonCode());
                        } else {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "updateTokenStatus : ERROR: Error processing tokenLCM");
                            var4_5.setErrorCode(-2);
                        }
                        if (!this.qB.tokenClose().getReasonCode().equals((Object)"00")) {
                            com.samsung.android.spayfw.b.c.e("AmexPayProvider", "updateTokenStatus : ERROR: tokenClose failed on SDK");
                            var4_5.setErrorCode(-2);
                        }
                    }
                } else {
                    com.samsung.android.spayfw.b.c.e("AmexPayProvider", "updateTokenStatus : ERROR: Unknown Token Status : " + var2_2.getCode());
                    var4_5.setErrorCode(-5);
                }
                break block21;
            }
            if (var2_2.getCode().equals((Object)"ACTIVE") || var2_2.getCode().equals((Object)"SUSPENDED")) {
                var4_5.setErrorCode(-5);
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "updateTokenStatus : ERROR: Unknown Token : " + var6_6);
            } else {
                com.samsung.android.spayfw.b.c.e("AmexPayProvider", "updateTokenStatus : ERROR: Token already deleted");
            }
        }
        var4_5.b(var3_4);
        com.samsung.android.spayfw.b.c.d("AmexPayProvider", "updateTokenStatus : Exit");
        // MONITOREXIT : var10_3
        return var4_5;
    }
}

