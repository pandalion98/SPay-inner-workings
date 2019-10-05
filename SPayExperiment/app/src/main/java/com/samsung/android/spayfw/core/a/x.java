/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.RemoteException
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.List
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.CardState;
import com.samsung.android.spayfw.appinterface.ICardDataCallback;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.g;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class x
extends o {
    private ICardDataCallback my = null;

    public x(Context context) {
        super(context);
    }

    static /* synthetic */ ICardDataCallback a(x x2) {
        return x2.my;
    }

    static /* synthetic */ boolean a(x x2, String string, String string2) {
        return x2.j(string, string2);
    }

    private void f(final String string, final c c2) {
        this.lQ.x(c.y(c2.getCardBrand()), string).a(new Request.a<com.samsung.android.spayfw.remoteservice.c<TokenResponseData>, g>(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<TokenResponseData> var2_2) {
                block34 : {
                    block35 : {
                        var3_3 = x.this.iJ.r(string);
                        Log.d("TokenManager", "onRequestComplete: getCardData: code " + var1_1);
                        switch (var1_1) {
                            default: {
                                var5_4 = null;
                                var4_5 = -1;
                                break block34;
                            }
                            case 200: {
                                var17_8 = x.this.jJ.bq(string);
                                if (var3_3 != null && var17_8 != null) ** GOTO lbl21
                                Log.e("TokenManager", "unable to get card object ");
                                if (var17_8 != null) {
                                    Log.i("TokenManager", "delete record from db ");
                                    x.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Co, string);
                                }
                                if (c2 != null) {
                                    Log.e("TokenManager", "delete card object");
                                    x.this.iJ.t(string);
                                }
                                var4_5 = -6;
                                var5_4 = null;
                                break block34;
lbl21: // 1 sources:
                                if (var2_2 != null && var2_2.getResult() != null) ** GOTO lbl26
                                Log.e("TokenManager", "TokenResponseData is null");
                                var4_5 = -205;
                                var5_4 = null;
                                break block34;
lbl26: // 1 sources:
                                var20_9 = var2_2.getResult();
                                var21_10 = m.a(x.this.mContext, var3_3, var20_9);
                                if (var21_10 != null && var21_10.getMetadata() != null) {
                                    m.a(x.this.mContext, var21_10.getMetadata(), var3_3);
                                }
                                var22_11 = var20_9.getStatus();
                                var23_12 = c.y(var3_3.getCardBrand());
                                if (var22_11 != null && var22_11.getCode() != null) ** GOTO lbl37
                                Log.e("TokenManager", "TokenResponseData:  newTokenStatus is null");
                                var4_5 = -205;
                                var5_4 = var21_10;
                                break block34;
lbl37: // 1 sources:
                                var24_13 = m.a(var20_9);
                                Log.i("TokenManager", "TokenResponseData:tokenStaus in db  " + var3_3.ac().getTokenStatus());
                                Log.i("TokenManager", "TokenResponseData:tokenStaus from server  " + var22_11.getCode());
                                if (!"PENDING_PROVISION".equals((Object)var3_3.ac().getTokenStatus())) ** GOTO lbl105
                                if (var20_9.getData() == null || var3_3.ac().aQ() != null) ** GOTO lbl97
                                var32_14 = new com.samsung.android.spayfw.payprovider.c();
                                var32_14.a(var20_9.getData());
                                var32_14.e(m.b(var2_2.getResult()));
                                var33_15 = var3_3.ad().createTokenTA(var3_3.ac().getTokenId(), var32_14, 2);
                                if (var33_15 == null || var33_15.getErrorCode() != 0) ** GOTO lbl84
                                var37_16 = var33_15.getProviderTokenKey();
                                if (var37_16 != null) ** GOTO lbl53
                                Log.e("TokenManager", "Provision Token- onRequestComplete: provider not returning tokenref ");
                                var4_5 = -1;
                                var5_4 = var21_10;
                                break block34;
lbl53: // 1 sources:
                                var3_3.ac().setTokenStatus(var24_13);
                                var3_3.ac().H(var22_11.getReason());
                                var3_3.ac().c(var37_16);
                                var17_8.setTokenStatus(var3_3.ac().getTokenStatus());
                                var17_8.H(var3_3.ac().aP());
                                var35_17 = m.a(var17_8, var20_9, var37_16);
                                if (!x.this.a(var35_17)) ** GOTO lbl73
                                Log.e("TokenManager", "Duplicate Token Ref Id / Tr Token Id");
                                var40_18 = new TokenStatus("DISPOSED", null);
                                c2.ad().updateTokenStatusTA(null, var40_18);
                                x.this.iJ.t(string);
                                x.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Co, string);
                                Log.e("TokenManager", "processProvision:Send error report to TR server");
                                x.this.b(null, string, m.F(var35_17.getTokenStatus()), "PROVISION", var23_12, null, false);
                                var44_19 = new Intent("com.samsung.android.spayfw.action.notification");
                                var44_19.putExtra("notiType", "syncAllCards");
                                PaymentFrameworkApp.a(var44_19);
                                var5_4 = var21_10;
                                var4_5 = -6;
                                break block34;
lbl73: // 1 sources:
                                x.this.jJ.d(var35_17);
                                var3_3.j(var35_17.ab());
                                var34_20 = 0;
                                var36_21 = true;
                                var39_22 = com.samsung.android.spayfw.fraud.a.x(x.this.mContext);
                                if (var39_22 != null && string != null) {
                                    var39_22.l(string, var22_11.getCode());
                                } else {
                                    Log.d("TokenManager", "FraudCollector: storeTokenEnrollmentSuccess cannot get data");
                                    var34_20 = 0;
                                }
                                ** GOTO lbl88
lbl84: // 1 sources:
                                Log.e("TokenManager", "Provider unable to store provision info");
                                var34_20 = -1;
                                var35_17 = var17_8;
                                var36_21 = false;
lbl88: // 3 sources:
                                if (var36_21) {
                                    x.this.a(null, string, var35_17.getTokenStatus(), "PROVISION", var23_12, var33_15, false);
                                } else {
                                    Log.e("TokenManager", "processProvision:Send error report to TR server");
                                    x.this.b(null, string, m.F(var35_17.getTokenStatus()), "PROVISION", var23_12, var33_15, false);
                                }
                                var25_23 = false;
                                var17_8 = var35_17;
                                var26_24 = var34_20;
                                break block35;
lbl97: // 1 sources:
                                if (!x.a(x.this, var3_3.ac().getTokenStatus(), var22_11.getCode())) break;
                                if ("PENDING".equals((Object)var22_11.getCode())) {
                                    var25_23 = false;
                                    var26_24 = 0;
                                } else {
                                    var25_23 = true;
                                    var26_24 = 0;
                                }
                                break block35;
lbl105: // 1 sources:
                                if ("SUSPENDED".equals((Object)var3_3.ac().getTokenStatus())) {
                                    if (!x.a(x.this, var3_3.ac().getTokenStatus(), var22_11.getCode())) break;
                                    var25_23 = true;
                                    var26_24 = 0;
                                } else {
                                    if (!"ACTIVE".equals((Object)var3_3.ac().getTokenStatus()) || !x.a(x.this, var3_3.ac().getTokenStatus(), var22_11.getCode())) break;
                                    var25_23 = true;
                                    var26_24 = 0;
                                }
                                break block35;
                            }
                            case 404: 
                            case 410: {
                                Log.w("TokenManager", "unable to find the token on server. something wrong. deleting the token");
                                if (x.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Co, string) < 1) {
                                    Log.e("TokenManager", "Not able to delete Token from DB");
                                }
                                var14_27 = new TokenStatus("DISPOSED", null);
                                if (var3_3 != null) {
                                    var3_3.ad().updateTokenStatusTA(null, var14_27);
                                    x.this.iJ.s(var3_3.ac().getTokenId());
                                }
                                var4_5 = -6;
                                var5_4 = null;
                                break block34;
                            }
                            case 500: {
                                var4_5 = -205;
                                var5_4 = null;
                                break block34;
                            }
                            case 0: 
                            case 503: {
                                var4_5 = -201;
                                var5_4 = null;
                                break block34;
                            }
                            case -2: {
                                var4_5 = -206;
                                var5_4 = null;
                                break block34;
                            }
                        }
                        var25_23 = false;
                        var26_24 = 0;
                    }
                    if (var25_23) {
                        Log.d("TokenManager", "getCardData: update Card status ");
                        var3_3.ad().updateTokenStatusTA(var20_9.getData(), var22_11);
                        var17_8.setTokenStatus(var24_13);
                        var17_8.H(var22_11.getReason());
                        x.this.jJ.d(var17_8);
                        var3_3.ac().setTokenStatus(var24_13);
                        var3_3.ac().H(var22_11.getReason());
                        var29_25 = m.F(var17_8.getTokenStatus());
                        var30_26 = com.samsung.android.spayfw.fraud.a.x(x.this.mContext);
                        if (var30_26 != null) {
                            var30_26.k(var17_8.getTokenRefId(), var17_8.getTokenStatus());
                        } else {
                            Log.d("TokenManager", "FraudCollector: updateFTokenRecordStatus cannot get data");
                        }
                        if ("DISPOSED".equals((Object)var22_11.getCode())) {
                            if (x.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Co, string) < 1) {
                                Log.e("TokenManager", "Not able to delete Token from DB");
                            }
                            x.this.iJ.s(var3_3.ac().getTokenId());
                        }
                        x.this.a(null, string, var29_25, "STATUS_CHANGE", var23_12, null, false);
                        var5_4 = var21_10;
                        var4_5 = var26_24;
                    } else {
                        Log.d("TokenManager", "getCardData: payprovider update token meta data called ");
                        var3_3.ad().updateTokenMetaDataTA(var20_9.getData(), var21_10);
                        var5_4 = var21_10;
                        var4_5 = var26_24;
                    }
                }
                if (var5_4 != null && var5_4.getMetadata() != null && var3_3 != null) {
                    m.a(x.this.mContext, var5_4.getMetadata(), var3_3);
                }
                if (var4_5 != 0) {
                    x.a(x.this).onFail(string, var4_5);
                    return;
                }
                if (var5_4 == null) ** GOTO lbl177
                try {
                    try {
                        Log.d("TokenManager", "getCardData: " + var5_4.toString());
lbl177: // 2 sources:
                        x.a(x.this).onSuccess(string, var5_4);
                        return;
                    }
                    catch (RemoteException var9_28) {
                        Log.c("TokenManager", var9_28.getMessage(), var9_28);
                        return;
                    }
                }
                catch (Throwable var6_31) {
                    throw var6_31;
                }
                finally {
                    var12_6 = var2_2 == null ? null : var2_2.getResult();
                    {
                        var13_7 = var5_4 == null ? null : var5_4.getMetadata();
                        m.a(var12_6, var13_7);
                    }
                }
            }
        });
    }

    private boolean j(String string, String string2) {
        boolean bl = true;
        if (string.equals((Object)string2) || ("ENROLLED".equals((Object)string) || "PENDING_PROVISION".equals((Object)string) || "ACTIVE".equals((Object)string) || "SUSPENDED".equals((Object)string)) && "PENDING".equals((Object)string2)) {
            bl = false;
        }
        return bl;
    }

    public void a(String string, ICardDataCallback iCardDataCallback) {
        if (string == null || iCardDataCallback == null || this.iJ == null) {
            int n2 = -5;
            if (string == null) {
                Log.e("TokenManager", "getCardData Failed - token Id is null");
            }
            if (this.iJ == null) {
                Log.e("TokenManager", "getCardData Failed - Failed to initialize account");
                n2 = -1;
            }
            if (iCardDataCallback != null) {
                iCardDataCallback.onFail(string, n2);
                return;
            }
            Log.e("TokenManager", "getCardData Failed - Provision Callback is null");
            return;
        }
        this.my = iCardDataCallback;
        c c2 = this.iJ.r(string);
        if (c2 == null) {
            Log.e("TokenManager", "getCardData Failed - Invalid token Id");
            this.my.onFail(string, -6);
            return;
        }
        this.f(string, c2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public int clearEnrolledCard(String string) {
        if (this.iJ == null || string == null) {
            if (this.iJ != null) {
                Log.e("TokenManager", "clearEnrolledCard  - enrollmentId is null");
                return -5;
            }
            Log.e("TokenManager", "clearEnrolledCard  - Failed to initialize account");
            return -1;
        } else {
            c c2 = this.iJ.q(string);
            if (c2 == null || c2.ac() == null) return -1;
            {
                if (!"ENROLLED".equals((Object)c2.ac().getTokenStatus())) {
                    Log.w("TokenManager", "Not able to delete : Tokenstatus: " + c2.ac().getTokenStatus());
                    return -4;
                }
                Log.i("TokenManager", "clearEnrolledCard: " + string);
                this.iJ.s(string);
                if (this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, string) >= 1) return 0;
                {
                    Log.e("TokenManager", "Not able to delete enrollementId from DB");
                }
                return 0;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public List<CardState> getAllCardState(Bundle var1_1) {
        block10 : {
            if (this.iJ == null) {
                Log.e("TokenManager", "getAllCardState  - mAccount is null");
                return new ArrayList();
            }
            if (var1_1 == null || var1_1.getStringArray("cardType") == null) {
                Log.e("TokenManager", "getAllCardState - card filter or card type is null. return null");
                return null;
            }
            var2_2 = var1_1.getStringArray("cardType");
            Log.d("TokenManager", "getAllCardState: " + Arrays.toString((Object[])var2_2));
            var3_3 = this.iJ.W();
            var4_4 = new ArrayList();
            var5_5 = new ArrayList((Collection)Arrays.asList((Object[])var2_2));
            if (var3_3 == null || var3_3.isEmpty()) break block10;
            Log.d("TokenManager", "getAllCards: cardList:" + var3_3.size());
            for (var6_6 = 0; var6_6 < var3_3.size(); ++var6_6) {
                block15 : {
                    block14 : {
                        block12 : {
                            block13 : {
                                block11 : {
                                    var7_7 = new CardState();
                                    var8_8 = (c)var3_3.get(var6_6);
                                    if (var8_8 == null || var8_8.getCardBrand() == null) {
                                        Log.w("TokenManager", "card or card brand is null");
                                        continue;
                                    }
                                    Log.d("TokenManager", "getAllCards: card:" + var8_8.toString());
                                    if (var8_8.ac() == null || var8_8.ac().getTokenId() == null || !var8_8.ac().getTokenId().equals((Object)"GIFT")) ** GOTO lbl28
                                    if (var5_5.contains((Object)"GIFT")) {
                                        Log.d("TokenManager", "card type is gift and request filter have gift");
                                        var9_9 = true;
                                    } else {
                                        Log.d("TokenManager", "card type is gift and request filter doesn't have gift");
                                        continue;
lbl28: // 1 sources:
                                        var9_9 = false;
                                    }
                                    if (!var8_8.getCardBrand().equals((Object)"LO")) break block11;
                                    if (!var5_5.contains((Object)"LOYALTY")) break block12;
                                    Log.d("TokenManager", "card brand is loyalty and request filter have loyalty");
                                    var9_9 = true;
                                }
                                if (!var8_8.getCardBrand().equals((Object)"GM")) break block13;
                                if (!var5_5.contains((Object)"GLOBAL_MEMBERSHIP")) break block14;
                                Log.d("TokenManager", "card brand is Global Membership and request filter have Global Membership");
                                var9_9 = true;
                            }
                            if (!(var5_5.contains((Object)"DEBIT") || var5_5.contains((Object)"CREDIT") || var5_5.contains((Object)"SAMSUNG_REWARD") || var9_9)) {
                                Log.d("TokenManager", "card type is payment.Request filter dose not have payment type");
                                continue;
                            }
                            break block15;
                        }
                        Log.d("TokenManager", "card type is loyalty and request filter doesn't have loyalty");
                        continue;
                    }
                    Log.d("TokenManager", "card type is Global Membership and request filter doesn't have Global Membership");
                    continue;
                }
                var7_7.setEnrollmentId(var8_8.getEnrollmentId());
                if (var8_8.ac() != null) {
                    if (var8_8.ac().getTokenId() != null) {
                        var7_7.setTokenId(var8_8.ac().getTokenId());
                    }
                    var7_7.setTokenStatus(new TokenStatus(var8_8.ac().getTokenStatus(), var8_8.ac().aP()));
                    var11_10 = var8_8.ac().aQ() != null ? var8_8.ad().getPayReadyState() : false;
                    var7_7.setAvailableForPayState(var11_10);
                }
                var4_4.add((Object)var7_7);
            }
        }
        if (!var4_4.isEmpty()) {
            Log.d("TokenManager", "getAllCardState: " + var4_4.toString());
            return var4_4;
        }
        Log.e("TokenManager", "getAllCardState is empty");
        return var4_4;
    }

    /*
     * Enabled aggressive block sorting
     */
    public List<String> getPaymentReadyState(String string) {
        ArrayList arrayList = new ArrayList();
        if (this.iJ == null) {
            Log.e("TokenManager", "getPaymentReadyState  - mAccount is null");
            return arrayList;
        }
        if (string != null && this.iJ.r(string) != null) {
            c c2 = this.iJ.r(string);
            if (c2 == null) return arrayList;
            if (c2.ac() == null) return arrayList;
            if (c2.ac().aQ() == null) return arrayList;
            if (c2.ad().getPayReadyState()) {
                arrayList.add((Object)string);
                return arrayList;
            }
            Log.i("TokenManager", "not ready for payment: " + string);
            return arrayList;
        }
        List<c> list = this.iJ.W();
        int n2 = 0;
        while (n2 < list.size()) {
            c c3 = (c)list.get(n2);
            if (c3 != null && c3.ac() != null && c3.ac().getTokenId() != null && c3.ac().aQ() != null) {
                if (c3.ad().getPayReadyState()) {
                    arrayList.add((Object)c3.ac().getTokenId());
                } else {
                    Log.i("TokenManager", "not ready for payment: " + c3.ac().getTokenId());
                }
            }
            ++n2;
        }
        return arrayList;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public TokenStatus getTokenStatus(String string) {
        Log.d("TokenManager", "getTokenStatus()");
        if (string == null || string.isEmpty()) {
            Log.e("TokenManager", "getTokenStatus() token id is empty or null");
            return null;
        }
        if (this.jJ == null) return null;
        com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bq(string);
        if (a2 == null) return null;
        return new TokenStatus(a2.getTokenStatus(), a2.fy());
    }

    /*
     * Enabled aggressive block sorting
     */
    public List<String> isDsrpBlobMissing() {
        Log.d("TokenManager", "Entered isDsrpBlobMissing");
        ArrayList arrayList = new ArrayList();
        if (this.iJ == null) {
            Log.e("TokenManager", "isDsrpBlobMissing  - mAccount is null");
            return arrayList;
        }
        List<c> list = this.iJ.W();
        if (list != null && !list.isEmpty()) {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                c c2 = (c)list.get(i2);
                if (c2 == null || c2.ac() == null || c2.ac().aQ() == null || c2.ad() == null || !c2.ad().isDsrpBlobMissing()) continue;
                Log.d("TokenManager", "dsrpBlobMissing: tokenId = " + c2.ac().getTokenId());
                arrayList.add((Object)c2.ac().getTokenId());
            }
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int isDsrpBlobMissingForTokenId(String string) {
        Log.d("TokenManager", "Entered isDsrpBlobMissingForTokenId");
        if (this.iJ == null) {
            Log.e("TokenManager", "isDsrpBlobMissingForTokenId  - mAccount is null");
            return 0;
        } else {
            boolean bl;
            c c2 = this.iJ.r(string);
            if (c2 == null || c2.ac() == null || c2.ac().aQ() == null || c2.ad() == null || !(bl = c2.ad().isDsrpBlobMissing())) return 0;
            {
                Log.d("TokenManager", "dsrpBlobMissing = " + bl);
                return -45;
            }
        }
    }

}

