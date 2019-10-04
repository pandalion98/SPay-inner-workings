/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Message
 *  android.os.RemoteException
 *  com.google.gson.JsonObject
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.List
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.core.a.aa;
import com.samsung.android.spayfw.core.a.e;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.a.v;
import com.samsung.android.spayfw.core.a.z;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.g;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import java.util.List;

public class p
extends o {
    protected PushMessage kU;
    protected IPushMessageCallback lS;
    protected Integer lT;
    private String lU = null;

    public p(Context context, PushMessage pushMessage, Integer n2, IPushMessageCallback iPushMessageCallback) {
        super(context);
        this.kU = pushMessage;
        this.lS = iPushMessageCallback;
        this.lT = n2;
    }

    private void Q(String string) {
        com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "deleting token id = " + string);
        if (this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Co, string) < 1) {
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "Not able to delete Token from DB");
        }
        try {
            this.iJ.t(string);
            return;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", exception.getMessage());
            return;
        }
    }

    static /* synthetic */ String a(p p2) {
        return p2.lU;
    }

    static /* synthetic */ void a(p p2, String string) {
        p2.Q(string);
    }

    private void a(final String string, c c2) {
        final String string2 = c2.getCardBrand();
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
                block29 : {
                    block21 : {
                        block19 : {
                            block20 : {
                                block18 : {
                                    block28 : {
                                        block22 : {
                                            block23 : {
                                                block27 : {
                                                    block24 : {
                                                        block26 : {
                                                            block25 : {
                                                                var3_3 = true;
                                                                com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "onRequestComplete: Asset change: code " + var1_1);
                                                                var4_4 = p.this.iJ.r(string);
                                                                switch (var1_1) {
                                                                    default: {
                                                                        var5_5 = -1;
                                                                        var3_3 = false;
                                                                        var6_6 = false;
                                                                        var9_7 = null;
                                                                        var7_8 = null;
                                                                        var8_9 = null;
                                                                        var10_10 = null;
                                                                        break block22;
                                                                    }
                                                                    case 200: {
                                                                        if (var4_4 == null) break block23;
                                                                        var7_8 = var4_4.ac().getTokenStatus();
                                                                        var15_11 = var2_2.getResult();
                                                                        if (var15_11 != null) {
                                                                            var18_12 = m.a(p.this.mContext, var4_4, var15_11);
                                                                            var19_13 = var18_12 != null ? var18_12.getMetadata() : null;
                                                                        }
                                                                        break block24;
                                                                    }
                                                                    case 404: 
                                                                    case 410: {
                                                                        com.samsung.android.spayfw.b.c.w("PushMessageProcessor", "unable to find the token. something wrong. deleting the token");
                                                                        var13_16 = new TokenStatus("DISPOSED", null);
                                                                        var7_8 = "DISPOSED";
                                                                        if (var4_4 == null) break block25;
                                                                        var14_17 = var4_4.ad().updateTokenStatusTA(null, var13_16);
                                                                        p.a(p.this, string);
                                                                        if (var14_17 == null || var14_17.getErrorCode() != 0) break;
                                                                        break block26;
                                                                    }
                                                                    case 500: {
                                                                        var5_5 = -205;
                                                                        var3_3 = false;
                                                                        var6_6 = false;
                                                                        var7_8 = null;
                                                                        var8_9 = null;
                                                                        var9_7 = null;
                                                                        var10_10 = null;
                                                                        break block22;
                                                                    }
                                                                    case 0: 
                                                                    case 503: {
                                                                        var5_5 = -201;
                                                                        var3_3 = false;
                                                                        var6_6 = false;
                                                                        var7_8 = null;
                                                                        var8_9 = null;
                                                                        var9_7 = null;
                                                                        var10_10 = null;
                                                                        break block22;
                                                                    }
                                                                    case -2: {
                                                                        var5_5 = -206;
                                                                        var3_3 = false;
                                                                        var6_6 = false;
                                                                        var7_8 = null;
                                                                        var8_9 = null;
                                                                        var9_7 = null;
                                                                        var10_10 = null;
                                                                        break block22;
                                                                    }
                                                                }
                                                                var3_3 = false;
                                                                break block26;
                                                            }
                                                            var14_17 = null;
                                                            var3_3 = false;
                                                        }
                                                        var9_7 = var14_17;
                                                        var10_10 = var13_16;
                                                        var6_6 = var3_3;
                                                        var5_5 = -6;
                                                        var3_3 = false;
                                                        var8_9 = null;
                                                        break block22;
                                                        if (var18_12 != null && var18_12.getMetadata() != null) {
                                                            m.a(p.this.mContext, var18_12.getMetadata(), var4_4);
                                                        }
                                                        var17_14 = var19_13;
                                                        var16_15 = var3_3;
                                                        break block27;
                                                    }
                                                    var16_15 = false;
                                                    var17_14 = null;
                                                }
                                                var8_9 = var17_14;
                                                var6_6 = var16_15;
                                                var5_5 = 0;
                                                var9_7 = null;
                                                var10_10 = null;
                                                break block22;
                                            }
                                            var5_5 = 0;
                                            var6_6 = false;
                                            var7_8 = null;
                                            var8_9 = null;
                                            var9_7 = null;
                                            var10_10 = null;
                                        }
                                        if (var5_5 != -6) break block28;
                                        p.this.lS.onTokenStatusUpdate(p.this.kU.getNotificationId(), string, var10_10);
                                        ** GOTO lbl111
                                    }
                                    if (var5_5 == 0) ** GOTO lbl110
                                    try {
                                        block17 : {
                                            try {
                                                p.this.lS.onFail(p.this.kU.getNotificationId(), var5_5);
                                                break block17;
                                            }
                                            catch (Exception var12_18) {
                                                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", var12_18.getMessage(), var12_18);
                                                if (var2_2 == null) break block20;
                                                m.a(var2_2.getResult(), var8_9);
                                                break block19;
                                            }
lbl110: // 2 sources:
                                            p.this.lS.onTokenMetaDataUpdate(p.a(p.this), string, var8_9);
                                        }
                                        if (var2_2 == null) break block18;
                                        m.a(var2_2.getResult(), var8_9);
                                        break block19;
                                    }
                                    catch (Throwable var11_19) {
                                        if (var2_2 != null) {
                                            m.a(var2_2.getResult(), var8_9);
                                            throw var11_19;
                                        }
                                        break block21;
                                    }
                                }
                                m.a(null, var8_9);
                                break block19;
                            }
                            m.a(null, var8_9);
                        }
                        if (var3_3 == false) return;
                        if (var6_6) {
                            p.this.a(p.this.kU.getNotificationId(), string, var7_8, "ASSET_CHANGE", c.y(string2), var9_7, false);
                            return;
                        }
                        break block29;
                    }
                    m.a(null, var8_9);
                    throw var11_19;
                }
                com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processAssetChange:Send error report to TR server");
                p.this.b(p.this.kU.getNotificationId(), string, var7_8, "ASSET_CHANGE", c.y(string2), var9_7, false);
            }
        });
    }

    private void a(final String string, final String string2, final c c2) {
        String string3 = c2.getCardBrand();
        if (string3 == null || string3.isEmpty()) {
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "unable to find card brand");
            try {
                this.lS.onFail(this.lU, -5);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        String string4 = c2.ac().getTokenStatus();
        if ("ENROLLED".equals((Object)string4) && this.lT != null) {
            if (this.lT > 13) {
                com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "processProvision: Max attemps exceeded; something went wrong, aborting!");
                try {
                    this.lS.onFail(this.lU, -1);
                    return;
                }
                catch (RemoteException remoteException) {
                    com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
                    return;
                }
            }
            long l2 = 1000L * (long)this.lT.intValue();
            this.lT = 1 + this.lT;
            Message message = j.a(9, this.kU, (Object)this.lT, this.lS);
            PaymentFrameworkApp.az().sendMessageDelayed(message, l2);
            com.samsung.android.spayfw.b.c.i("PushMessageProcessor", "received push before complete provisionToken : delay process count   " + (Object)this.lT);
            return;
        }
        if ("ACTIVE".equals((Object)string4)) {
            com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "processProvision: token creation already happened on SDK. just send report ");
            this.a(this.kU.getNotificationId(), string2, string4, "PROVISION", c.y(c2.getCardBrand()), null, false);
            try {
                this.lS.onTokenStatusUpdate(this.lU, string2, new TokenStatus(string4, c2.ac().aP()));
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        this.lQ.x(c.y(string3), string2).a(new Request.a<com.samsung.android.spayfw.remoteservice.c<TokenResponseData>, g>(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<TokenResponseData> var2_2) {
                block47 : {
                    block48 : {
                        com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "processProvision::onRequestComplete: code = " + var1_1);
                        var3_3 = p.this.iJ.r(string2);
                        if (var3_3 == null) {
                            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processProvision : unable to get Card object :" + string2);
                            try {
                                var32_4 = new TokenStatus("DISPOSED", null);
                                p.this.lS.onTokenStatusUpdate(p.this.kU.getNotificationId(), string2, var32_4);
                                return;
                            }
                            catch (RemoteException var35_6) {
                                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", var35_6.getMessage(), var35_6);
                                return;
                            }
                            finally {
                                var36_7 = null;
                                if (var2_2 != null) {
                                    var36_7 = var2_2.getResult();
                                }
                                m.a(var36_7, null);
                            }
                        }
                        var4_10 = var3_3.getCardBrand();
                        if ("ACTIVE".equals((Object)var3_3.ac().getTokenStatus())) {
                            com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "processProvision: token creation already happend on SDK. just send report ");
                            p.this.a(p.this.kU.getNotificationId(), string2, var3_3.ac().getTokenStatus(), "PROVISION", c.y(c2.getCardBrand()), null, false);
                            try {
                                p.this.lS.onTokenStatusUpdate(p.a(p.this), string2, new TokenStatus(var3_3.ac().getTokenStatus(), var3_3.ac().aP()));
                                return;
                            }
                            catch (RemoteException var31_11) {
                                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", var31_11.getMessage(), var31_11);
                                return;
                            }
                            finally {
                                m.a(var2_2.getResult(), null);
                            }
                        }
                        switch (var1_1) {
                            default: {
                                var5_13 = -1;
                                var8_14 = false;
                                var9_15 = false;
                                var7_16 = false;
                                var6_17 = "PENDING";
                                var10_18 = null;
                                break block48;
                            }
                            case 200: {
                                var20_21 = var2_2.getResult();
                                com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "Provision Data : " + (Object)var20_21.getData());
                                var21_22 = p.this.jJ.bq(string2);
                                if (var21_22 != null) ** GOTO lbl54
                                com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processProvision : unable to get Card object from db :" + string2);
                                p.this.iJ.s(var3_3.getEnrollmentId());
                                var5_13 = -6;
                                var8_14 = true;
                                var6_17 = "DISPOSED";
                                var7_16 = false;
                                var9_15 = false;
                                var10_18 = null;
                                break block48;
lbl54: // 1 sources:
                                if (c2.ac() != null && c2.ac().aQ() != null) {
                                    com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "processProvision: token creation already happend on SDK. just update status");
                                    var24_23 = c2.ac().aQ();
                                    var7_16 = true;
                                    var23_24 = c2.ad().updateTokenStatusTA(var20_21.getData(), var20_21.getStatus());
                                } else {
                                    var22_26 = new com.samsung.android.spayfw.payprovider.c();
                                    var22_26.a(var20_21.getData());
                                    var22_26.e(m.b(var2_2.getResult()));
                                    var23_24 = c2.ad().createTokenTA(string2, var22_26, 2);
                                    var7_16 = false;
                                    var24_23 = null;
                                }
                                if (var23_24 == null || var23_24.getErrorCode() != 0) ** GOTO lbl67
lbl67: // 1 sources:
                                com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "Provider unable to store provision info");
                                var5_13 = -1;
                                var6_17 = "PENDING";
                                var8_14 = true;
                                var10_18 = var23_24;
                                var9_15 = false;
                                break block48;
                            }
                            case 404: 
                            case 410: {
                                com.samsung.android.spayfw.b.c.w("PushMessageProcessor", "unable to find the token. something wrong. deleting the token");
                                var5_13 = -6;
                                var19_29 = new TokenStatus("DISPOSED", null);
                                var10_18 = c2.ad().updateTokenStatusTA(null, var19_29);
                                p.a(p.this, string2);
                                var6_17 = "DISPOSED";
                                var7_16 = false;
                                var8_14 = false;
                                var9_15 = false;
                                break block48;
                            }
                            case 500: {
                                var5_13 = -205;
                                var6_17 = "PENDING";
                                var7_16 = false;
                                var8_14 = false;
                                var9_15 = false;
                                var10_18 = null;
                                break block48;
                            }
                            case 0: 
                            case 503: {
                                var5_13 = -201;
                                var6_17 = "PENDING";
                                var7_16 = false;
                                var8_14 = false;
                                var9_15 = false;
                                var10_18 = null;
                                break block48;
                            }
                            case -2: {
                                var5_13 = -206;
                                var6_17 = "PENDING";
                                var7_16 = false;
                                var8_14 = false;
                                var9_15 = false;
                                var10_18 = null;
                                break block48;
                            }
                        }
                        var25_25 = var7_16 == false ? var23_24.getProviderTokenKey() : var24_23;
                        if (var25_25 == null) {
                            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "Provision Token- onRequestComplete: provider not returning tokenref ");
                            var5_13 = -1;
                            var8_14 = true;
                            var6_17 = "PENDING";
                            var10_18 = var23_24;
                            var9_15 = false;
                        } else {
                            var26_27 = m.a(var21_22, var20_21, var25_25);
                            c2.ac().setTokenStatus(var26_27.getTokenStatus());
                            c2.ac().c(var25_25);
                            c2.ac().H(var26_27.fy());
                            c2.j(var26_27.ab());
                            p.this.jJ.d(var26_27);
                            var6_17 = m.F(var26_27.getTokenStatus());
                            var28_28 = com.samsung.android.spayfw.fraud.a.x(p.this.mContext);
                            if (var28_28 != null && string2 != null) {
                                var28_28.l(string2, var6_17);
                            } else {
                                com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "FraudCollector: storeTokenEnrollmentSuccess cannot get data");
                            }
                            var10_18 = var23_24;
                            var8_14 = true;
                            var9_15 = true;
                            var5_13 = 0;
                        }
                    }
                    var11_19 = null;
                    if (var5_13 == 0) ** GOTO lbl147
                    try {
                        p.this.lS.onFail(p.a(p.this), var5_13);
                        var18_20 = com.samsung.android.spayfw.fraud.a.x(p.this.mContext);
                        var11_19 = null;
                        if (var18_20 != null) {
                            var18_20.bs();
                        } else {
                            com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "FraudCollector: TokenEnrollmentFailed cannot get data");
                            var11_19 = null;
                        }
                        ** GOTO lbl162
lbl147: // 1 sources:
                        try {
                            var11_19 = var14_34 = m.a(p.this.mContext, c2, var2_2.getResult());
                            if (var11_19 == null) ** GOTO lbl157
                            ** GOTO lbl154
                        }
                        catch (Exception var15_30) {
                            block45 : {
                                block46 : {
                                    block49 : {
                                        block44 : {
                                            var16_33 = null;
                                            break block49;
lbl154: // 1 sources:
                                            try {
                                                if (var11_19.getMetadata() != null) {
                                                    m.a(p.this.mContext, var11_19.getMetadata(), c2);
                                                }
lbl157: // 4 sources:
                                                if (!var7_16) {
                                                    com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "On Create Token");
                                                    p.this.lS.onCreateToken(p.a(p.this), string, var11_19);
                                                    break block44;
                                                }
                                                p.this.lS.onTokenStatusUpdate(p.a(p.this), string2, new TokenStatus(c2.ac().getTokenStatus(), c2.ac().aP()));
                                            }
                                            catch (Exception var15_32) {
                                                var16_33 = var11_19;
                                            }
                                        }
                                        if (var2_2 != null) {
                                            if (var11_19 != null) {
                                                m.a(var2_2.getResult(), var11_19.getMetadata());
                                            } else {
                                                m.a(var2_2.getResult(), null);
                                            }
                                        }
                                        break block45;
                                    }
                                    try {
                                        com.samsung.android.spayfw.b.c.c("PushMessageProcessor", var15_31.getMessage(), (Throwable)var15_31);
                                        if (var2_2 == null) break block45;
                                        if (var16_33 == null) break block46;
                                    }
                                    catch (Throwable var17_37) {
                                        var11_19 = var16_33;
                                        var13_36 = var17_37;
                                        ** GOTO lbl194
                                    }
                                    m.a(var2_2.getResult(), var16_33.getMetadata());
                                    break block45;
                                }
                                m.a(var2_2.getResult(), null);
                            }
                            if (var8_14 == false) return;
                            if (var9_15) {
                                p.this.a(p.this.kU.getNotificationId(), string2, var6_17, "PROVISION", c.y(var4_10), var10_18, false);
                                return;
                            }
                            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processProvision:Send error report to TR server");
                            p.this.b(p.this.kU.getNotificationId(), string2, var6_17, "PROVISION", c.y(var4_10), var10_18, false);
                            return;
                        }
                    }
                    catch (Throwable var12_35) {
                        var13_36 = var12_35;
lbl194: // 2 sources:
                        if (var2_2 == null) throw var13_36;
                        if (var11_19 == null) break block47;
                        m.a(var2_2.getResult(), var11_19.getMetadata());
                        throw var13_36;
                    }
                }
                m.a(var2_2.getResult(), null);
                throw var13_36;
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void b(String string, c c2) {
        com.samsung.android.spayfw.payprovider.c c3 = c2.ad().getReplenishmentRequestDataTA();
        if (c3 == null || c3.getErrorCode() != 0) {
            try {
                this.lS.onFail(this.lU, -36);
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
            }
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", " unable to get replenish data from pay provider");
            return;
        }
        try {
            z z2 = z.b(this.mContext, string, this.lS, this.lU);
            if (z2 == null) return;
            z2.process();
            return;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("PushMessageProcessor", exception.getMessage(), exception);
            return;
        }
    }

    private void bd() {
        try {
            new e(this.mContext, this.kU, this.lS).process();
            return;
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return;
        }
    }

    private void be() {
        List<com.samsung.android.spayfw.storage.models.a> list = this.jJ.c(TokenRecordStorage.TokenGroup.TokenColumn.CD, this.kU.getCardNumber());
        com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "Card Id : " + this.kU.getCardNumber());
        if (list != null && list.size() > 0) {
            com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "TokenRecord Size : " + list.size());
            com.samsung.android.spayfw.storage.models.a a2 = (com.samsung.android.spayfw.storage.models.a)list.get(0);
            com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "Token Id : " + a2.getTrTokenId());
            if (a2.getTrTokenId() != null) {
                new aa(this.mContext, a2.getTrTokenId(), this.kU, this.lS).process();
                return;
            }
        }
        com.samsung.android.spayfw.b.c.i("PushMessageProcessor", "No Card with Id : " + this.kU.getCardNumber() + ". Proceed with " + "enrollment");
        this.bd();
    }

    private void c(final String string, final c c2) {
        final String string2 = c2.getCardBrand();
        g g2 = this.lQ.x(c.y(c2.getCardBrand()), string);
        g2.h(false);
        g2.a(new Request.a<com.samsung.android.spayfw.remoteservice.c<TokenResponseData>, g>(){

            /*
             * Unable to fully structure code
             * Enabled aggressive exception aggregation
             */
            @Override
            public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<TokenResponseData> var2_2) {
                block30 : {
                    block29 : {
                        block28 : {
                            var3_3 = null;
                            var4_4 = true;
                            com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "onRequestComplete: Status change: code " + var1_1);
                            var5_5 = p.this.iJ.r(string);
                            var6_6 = p.this.jJ.bq(string);
                            if (var6_6 == null || var5_5 == null) {
                                if (var5_5 == null) {
                                    com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processStatusChange : unable to get Card object :" + string);
lbl9: // 2 sources:
                                    do {
                                        var7_7 = new TokenStatus("DISPOSED", null);
                                        var8_8 = "DISPOSED";
                                        var9_9 = false;
                                        var10_10 = 0;
lbl14: // 3 sources:
                                        do {
                                            if (var10_10 == 0) break block28;
                                            p.this.lS.onFail(p.a(p.this), var10_10);
lbl18: // 2 sources:
                                            do {
                                                if (var4_4) {
                                                    if (!var9_9) break block29;
                                                    p.this.a(p.this.kU.getNotificationId(), string, var8_8, "STATUS_CHANGE", c.y(string2), var3_3, false);
                                                }
lbl22: // 4 sources:
                                                do {
                                                    if ("DISPOSED".equals((Object)var8_8)) {
                                                        p.a(p.this, string);
                                                    }
                                                    return;
                                                    break;
                                                } while (true);
                                                break;
                                            } while (true);
                                            break;
                                        } while (true);
                                        break;
                                    } while (true);
                                }
                                com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processStatusChange: munable to get Card object from db :" + string);
                                ** continue;
                            }
                            var8_8 = var6_6.getTokenStatus();
                            block2 : switch (var1_1) {
                                default: {
                                    var10_10 = -1;
                                    var7_7 = null;
                                    var9_9 = false;
                                    var4_4 = false;
                                    var3_3 = null;
                                    break;
                                }
                                case 200: {
                                    var12_11 = var2_2.getResult();
                                    if (var12_11 == null || var12_11.getStatus() == null) {
                                        com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "TokenResponseData or status is null");
                                        var7_7 = null;
                                        var9_9 = false;
                                        var10_10 = 0;
                                        var3_3 = null;
                                        break;
                                    }
                                    if (var5_5.ac() == null || var5_5.ac().aQ() == null) {
                                        com.samsung.android.spayfw.b.c.i("PushMessageProcessor", "Token Ref id is null. no need to inform to provider");
                                        var7_7 = null;
                                        var9_9 = false;
                                        var10_10 = 0;
                                        var3_3 = null;
                                        break;
                                    }
                                    var3_3 = var5_5.ad().updateTokenStatusTA(var12_11.getData(), var12_11.getStatus());
                                    if (var3_3 == null || var3_3.getErrorCode() != 0) {
                                        com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "updateTokenStatus failed on provider side");
                                        var9_9 = false;
lbl57: // 2 sources:
                                        do {
                                            var13_12 = new TokenStatus(m.a(var12_11), var12_11.getStatus().getReason());
                                            if (!var9_9) break block30;
                                            var6_6.setTokenStatus(var13_12.getCode());
                                            var6_6.H(var13_12.getReason());
                                            p.this.jJ.d(var6_6);
                                            var5_5.ac().setTokenStatus(var6_6.getTokenStatus());
                                            var5_5.ac().H(var6_6.fy());
                                            var8_8 = m.F(var6_6.getTokenStatus());
                                            var15_13 = com.samsung.android.spayfw.fraud.a.x(p.this.mContext);
                                            if (var15_13 != null) {
                                                var15_13.k(var6_6.getTrTokenId(), var6_6.getTokenStatus());
lbl69: // 2 sources:
                                                do {
                                                    var7_7 = var13_12;
                                                    var10_10 = 0;
                                                    break block2;
                                                    break;
                                                } while (true);
                                            }
                                            break;
                                        } while (true);
                                    } else {
                                        var9_9 = var4_4;
                                        ** continue;
                                    }
                                    com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "FraudCollector: updateFTokenRecordStatus cannot get data");
                                    ** continue;
                                }
                                case 404: 
                                case 410: {
                                    com.samsung.android.spayfw.b.c.w("PushMessageProcessor", "unable to find the token on server. something wrong. deleting the token");
                                    var7_7 = new TokenStatus("DISPOSED", null);
                                    var8_8 = "DISPOSED";
                                    var3_3 = c2.ad().updateTokenStatusTA(null, var7_7);
                                    if (var3_3 == null || var3_3.getErrorCode() != 0) {
                                        com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "updateTokenStatus failed on provider side");
                                        var9_9 = false;
                                        var10_10 = 0;
                                        break;
                                    }
                                    var9_9 = var4_4;
                                    var10_10 = 0;
                                    break;
                                }
                                case 500: {
                                    var10_10 = -205;
                                    var7_7 = null;
                                    var9_9 = false;
                                    var4_4 = false;
                                    var3_3 = null;
                                    break;
                                }
                                case 0: 
                                case 503: {
                                    var10_10 = -201;
                                    var7_7 = null;
                                    var9_9 = false;
                                    var4_4 = false;
                                    var3_3 = null;
                                    break;
                                }
                                case -2: {
                                    var10_10 = -206;
                                    var7_7 = null;
                                    var9_9 = false;
                                    var4_4 = false;
                                    var3_3 = null;
                                    break;
                                }
                            }
                            ** GOTO lbl14
                        }
                        try {
                            p.this.lS.onTokenStatusUpdate(p.a(p.this), string, var7_7);
                        }
                        catch (RemoteException var11_14) {
                            com.samsung.android.spayfw.b.c.c("PushMessageProcessor", var11_14.getMessage(), var11_14);
                        }
                        ** while (true)
                    }
                    com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "error happend during token status change. report to server");
                    p.this.b(p.this.kU.getNotificationId(), string, var8_8, "STATUS_CHANGE", c.y(string2), var3_3, false);
                    ** while (true)
                }
                var7_7 = var13_12;
                var10_10 = 0;
                ** while (true)
            }
        });
    }

    private void d(String string, c c2) {
        if (v.S(string) != null) {
            com.samsung.android.spayfw.b.c.w("PushMessageProcessor", "Token Change Checker Pending. Update.");
            v.a(this.mContext, string, this.lS, this.lU);
            return;
        }
        com.samsung.android.spayfw.b.c.w("PushMessageProcessor", "Ignore Token Change Event");
    }

    private void e(String string, c c2) {
        new aa(this.mContext, string, this.kU, this.lS).process();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void process() {
        c c2;
        int n2 = -5;
        if (this.lS == null) {
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "Callback is NULL");
            return;
        }
        if (this.kU == null || this.kU.getMessage() == null || this.kU.getCategory() == null) {
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "pushMessage is null/empty/undefined category");
            PushMessage pushMessage = this.kU;
            String string = null;
            if (pushMessage != null) {
                String string2 = this.kU.getMessage();
                string = null;
                if (string2 != null) {
                    String string3 = this.kU.getNotificationId();
                    string = null;
                    if (string3 != null) {
                        string = this.kU.getNotificationId();
                    }
                }
            }
            try {
                this.lS.onFail(string, -5);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        this.lU = this.kU.getNotificationId();
        String string = this.kU.getCategory();
        if ("CATEGORY_CARD".equals((Object)string)) {
            String string4 = this.kU.getCardEvent();
            com.samsung.android.spayfw.b.c.i("PushMessageProcessor", "Push Event : " + string4);
            if ("ENROLL_CC".equals((Object)string4)) {
                this.bd();
                return;
            }
            if ("UPDATE_CC".equals((Object)string4)) {
                this.be();
                return;
            }
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processPushMessage::process - Invalid event type = " + string4);
            try {
                this.lS.onFail(this.lU, -5);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        if (!"CATEGORY_TOKEN".equals((Object)string)) return;
        String string5 = this.kU.getTokenId();
        String string6 = this.kU.getEnrollmentId();
        com.samsung.android.spayfw.b.c.d("PushMessageProcessor", "processPushMessage- process: " + this.kU.toString());
        if (this.iJ == null || this.jJ == null || string5 == null) {
            if (string5 == null) {
            } else {
                n2 = -1;
                com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "Internal error - Account/DB null");
            }
            try {
                this.lS.onFail(this.lU, n2);
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
        if (this.iJ.r(string5) != null) {
            c2 = this.iJ.r(string5);
        } else {
            if (string6 == null || string6.isEmpty()) {
                com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processPushMessage- unable to find card for given enrollment id and TokenId");
                try {
                    this.lS.onFail(this.lU, -6);
                    return;
                }
                catch (RemoteException remoteException) {
                    com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
                    return;
                }
            }
            c2 = this.iJ.q(string6);
        }
        if (c2 == null) {
            try {
                this.lS.onFail(this.lU, -5);
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
            }
            com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processPushMessage- unable to get card object");
            return;
        }
        String string7 = this.kU.getTokenEvent();
        com.samsung.android.spayfw.b.c.i("PushMessageProcessor", "Push Event : " + string7);
        if ("PROVISION".equals((Object)string7)) {
            this.a(string6, string5, c2);
            return;
        }
        if ("STATUS_CHANGE".equals((Object)string7)) {
            this.c(string5, c2);
            return;
        }
        if ("REPLENISH".equals((Object)string7)) {
            this.b(string5, c2);
            return;
        }
        if ("TOKEN_CHANGE".equals((Object)string7)) {
            this.d(string5, c2);
            return;
        }
        if ("TRANSACTION".equals((Object)string7)) {
            this.e(string5, c2);
            return;
        }
        if ("ASSET_CHANGE".equals((Object)string7)) {
            this.a(string5, c2);
            return;
        }
        com.samsung.android.spayfw.b.c.e("PushMessageProcessor", "processPushMessage::process - Invalid event type = " + string7);
        try {
            this.lS.onFail(this.lU, -5);
            return;
        }
        catch (RemoteException remoteException) {
            com.samsung.android.spayfw.b.c.c("PushMessageProcessor", remoteException.getMessage(), remoteException);
            return;
        }
    }

}

