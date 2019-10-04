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
 *  java.lang.System
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.l;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;

public class k
extends o {
    protected IVerifyIdvCallback lr;
    protected VerifyIdvInfo ls;
    protected String mEnrollmentId;

    public k(Context context, String string, VerifyIdvInfo verifyIdvInfo, IVerifyIdvCallback iVerifyIdvCallback) {
        super(context);
        this.mEnrollmentId = string;
        this.ls = verifyIdvInfo;
        this.lr = iVerifyIdvCallback;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void process() {
        com.samsung.android.spayfw.b.c.d("IdvVerifier", "verifyIdv()");
        if (this.mEnrollmentId == null || this.lr == null || this.iJ == null || this.ls == null || this.ls.getValue() == null) {
            var1_1 = -5;
            if (this.iJ == null) {
                com.samsung.android.spayfw.b.c.e("IdvVerifier", "verifyIdv  - Failed to initialize account");
                var1_1 = -1;
            } else {
                com.samsung.android.spayfw.b.c.e("IdvVerifier", "verifyIdv Failed - Invalid inputs");
            }
            if (this.lr == null) return;
            try {
                this.lr.onFail(this.mEnrollmentId, var1_1, null);
                return;
            }
            catch (RemoteException var2_2) {
                com.samsung.android.spayfw.b.c.c("IdvVerifier", var2_2.getMessage(), var2_2);
                return;
            }
        }
        var3_3 = this.iJ.q(this.mEnrollmentId);
        if (var3_3 == null) {
            com.samsung.android.spayfw.b.c.e("IdvVerifier", "verifyIdv Failed - unable to find the card in memory. delete card in db");
            try {
                this.lr.onFail(this.mEnrollmentId, -6, null);
                return;
            }
            catch (RemoteException var12_4) {
                com.samsung.android.spayfw.b.c.c("IdvVerifier", var12_4.getMessage(), var12_4);
                return;
            }
        }
        if (var3_3.ac() == null || var3_3.ac().getTokenStatus() == null || !"PENDING_PROVISION".equals((Object)var3_3.ac().getTokenStatus())) {
            com.samsung.android.spayfw.b.c.e("IdvVerifier", "verifyIdv Failed - token is null or token staus is not correct. ");
            if (var3_3.ac() != null && var3_3.ac().getTokenStatus() != null) {
                com.samsung.android.spayfw.b.c.e("IdvVerifier", "verifyIdv Failed - token status:  " + var3_3.ac().getTokenStatus());
            }
            try {
                this.lr.onFail(this.mEnrollmentId, -4, null);
                return;
            }
            catch (RemoteException var4_5) {
                com.samsung.android.spayfw.b.c.c("IdvVerifier", var4_5.getMessage(), var4_5);
                return;
            }
        }
        var5_6 = System.currentTimeMillis();
        com.samsung.android.spayfw.b.c.d("IdvVerifier", "Verify Idv Info : " + this.ls.toString());
        var7_7 = var3_3.ad().getVerifyIdvRequestDataTA(this.ls);
        if (var7_7 == null || var7_7.cg() == null) ** GOTO lbl-1000
        var11_8 = var7_7.cg().getString("tac");
        var7_7.cg().remove("tac");
        if (var11_8 != null) {
            var8_9 = "TAC " + var11_8;
        } else lbl-1000: // 2 sources:
        {
            var8_9 = null;
        }
        if (this.ls.getType().equals((Object)"APP") && (var8_9 == null || var8_9.isEmpty())) {
            com.samsung.android.spayfw.b.c.d("IdvVerifier", "No TAC, status via push");
            try {
                this.lr.onSuccess(this.mEnrollmentId, null);
                return;
            }
            catch (RemoteException var10_10) {
                com.samsung.android.spayfw.b.c.c("IdvVerifier", var10_10.getMessage(), var10_10);
                return;
            }
        }
        if (var8_9 == null) {
            var8_9 = "OTP " + this.ls.getValue();
        }
        var9_11 = l.a(var5_6, this.mEnrollmentId, null, var7_7);
        this.lQ.a(c.y(var3_3.getCardBrand()), var9_11, var8_9).a(new a(this.mEnrollmentId, this.lr));
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<TokenResponseData>, com.samsung.android.spayfw.remoteservice.tokenrequester.o> {
        IVerifyIdvCallback lr;
        String mEnrollmentId;

        public a(String string, IVerifyIdvCallback iVerifyIdvCallback) {
            this.mEnrollmentId = string;
            this.lr = iVerifyIdvCallback;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive exception aggregation
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<TokenResponseData> var2_2) {
            block29 : {
                block30 : {
                    block31 : {
                        block28 : {
                            com.samsung.android.spayfw.b.c.d("IdvVerifier", "VerifyIdv : onRequestComplete:  " + var1_1);
                            var3_3 = false;
                            var4_4 = null;
                            block2 : switch (var1_1) {
                                default: {
                                    var9_5 = false;
                                    var13_6 = null;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var7_10 = -1;
                                    var8_11 = null;
lbl13: // 17 sources:
                                    do {
                                        if (var7_10 != 0) break block2;
                                        this.lr.onSuccess(this.mEnrollmentId, var8_11);
lbl17: // 2 sources:
                                        do {
                                            if (var12_9) {
                                                if (!var9_5) break block28;
                                                k.this.a(null, var10_7, var11_8, "STATUS_CHANGE", c.y(var13_6), var4_4, false);
                                            }
                                            return;
                                            break;
                                        } while (true);
                                        break;
                                    } while (true);
                                }
                                case 201: {
                                    var30_12 = k.this.iJ.q(this.mEnrollmentId);
                                    var31_13 = k.this.jJ.bp(this.mEnrollmentId);
                                    if (var30_12 != null && var31_13 != null) ** GOTO lbl42
                                    com.samsung.android.spayfw.b.c.e("IdvVerifier", "unable to get card object ");
                                    if (var31_13 != null) {
                                        com.samsung.android.spayfw.b.c.i("IdvVerifier", "delete record from db ");
                                        k.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                                    }
                                    if (var30_12 != null) {
                                        com.samsung.android.spayfw.b.c.e("IdvVerifier", "delete card object");
                                        k.this.iJ.s(this.mEnrollmentId);
                                    }
                                    var7_10 = -8;
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    ** GOTO lbl13
lbl42: // 1 sources:
                                    if (var2_2 != null && var2_2.getResult() != null && var2_2.getResult().getId() != null) ** GOTO lbl53
                                    com.samsung.android.spayfw.b.c.e("IdvVerifier", "VerifyIdv - onRequestComplete: resultData/TrTokenId is null");
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    var7_10 = 0;
                                    ** GOTO lbl13
lbl53: // 1 sources:
                                    var34_14 = var2_2.getResult().getId();
                                    var35_15 = new q();
                                    var35_15.setTokenId(var2_2.getResult().getId());
                                    var30_12.a(var35_15);
                                    var36_16 = var2_2.getResult().getData();
                                    if (var36_16 == null) ** GOTO lbl79
                                    var37_17 = new com.samsung.android.spayfw.payprovider.c();
                                    var37_17.a(var36_16);
                                    var37_17.e(m.b(var2_2.getResult()));
                                    var38_18 = var30_12.ad().createTokenTA(var34_14, var37_17, 3);
                                    if (var38_18 == null) break block29;
                                    var39_19 = var38_18.getProviderTokenKey();
lbl65: // 2 sources:
                                    do {
                                        if (var39_19 != null) ** GOTO lbl77
                                        com.samsung.android.spayfw.b.c.e("IdvVerifier", "Provision Token- onRequestComplete: provider not returning tokenref");
                                        var7_10 = -1;
                                        var8_11 = null;
                                        var9_5 = false;
                                        var10_7 = null;
                                        var11_8 = null;
                                        var12_9 = false;
                                        var13_6 = null;
                                        var4_4 = null;
                                        ** GOTO lbl13
lbl77: // 1 sources:
                                        var31_13.setTokenRefId(var39_19.cn());
                                        var30_12.ac().c(var39_19);
lbl79: // 2 sources:
                                        if ((var8_11 = m.a(k.this.mContext, var30_12, var2_2.getResult())) != null && var8_11.getMetadata() != null) {
                                            m.a(k.this.mContext, var8_11.getMetadata(), var30_12);
                                        }
                                        var31_13.setTrTokenId(var34_14);
                                        if (!k.this.a(var31_13)) ** GOTO lbl99
                                        com.samsung.android.spayfw.b.c.e("IdvVerifier", "Duplicate Token Ref Id / Tr Token Id");
                                        var41_20 = new TokenStatus("DISPOSED", null);
                                        var30_12.ad().updateTokenStatusTA(null, var41_20);
                                        k.this.iJ.s(this.mEnrollmentId);
                                        k.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                                        var45_21 = new Intent("com.samsung.android.spayfw.action.notification");
                                        var45_21.putExtra("notiType", "syncAllCards");
                                        PaymentFrameworkApp.a(var45_21);
                                        var11_8 = "DISPOSED";
                                        var7_10 = -6;
                                        var9_5 = false;
                                        var10_7 = null;
                                        var12_9 = false;
                                        var13_6 = null;
                                        var4_4 = null;
                                        ** GOTO lbl13
lbl99: // 1 sources:
                                        k.this.jJ.d(var31_13);
                                        var9_5 = false;
                                        var10_7 = null;
                                        var11_8 = null;
                                        var12_9 = false;
                                        var13_6 = null;
                                        var4_4 = null;
                                        var7_10 = 0;
                                        ** GOTO lbl13
                                        break;
                                    } while (true);
                                }
                                case 202: {
                                    if (var2_2 != null && var2_2.getResult() != null && var2_2.getResult().getStatus() != null) ** GOTO lbl120
                                    com.samsung.android.spayfw.b.c.e("IdvVerifier", "VerifyIdv::onRequestComplete: resultData/status is null");
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    var7_10 = 0;
                                    ** GOTO lbl13
lbl120: // 1 sources:
                                    var19_22 = k.this.iJ.q(this.mEnrollmentId);
                                    var20_23 = k.this.jJ.bp(this.mEnrollmentId);
                                    if (var19_22 != null && var20_23 != null) ** GOTO lbl139
                                    com.samsung.android.spayfw.b.c.e("IdvVerifier", "unable to get card object ");
                                    if (var20_23 != null) {
                                        com.samsung.android.spayfw.b.c.i("IdvVerifier", "delete record from db ");
                                        k.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                                    }
                                    if (var19_22 != null) {
                                        com.samsung.android.spayfw.b.c.e("IdvVerifier", "delete card object");
                                        k.this.iJ.s(this.mEnrollmentId);
                                    }
                                    var7_10 = -8;
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    ** GOTO lbl13
lbl139: // 1 sources:
                                    var23_24 = var2_2.getResult();
                                    var24_25 = var23_24.getId();
                                    var25_26 = var19_22.getCardBrand();
                                    if (var19_22.ac() != null && var19_22.ac().aQ() != null) ** GOTO lbl166
                                    com.samsung.android.spayfw.b.c.i("IdvVerifier", "VerifyIdv::onRequestComplete: Token Ref id is null. no need to inform to provider");
lbl144: // 3 sources:
                                    while (var3_3) {
                                        var26_27 = new TokenStatus(m.a(var23_24), var23_24.getStatus().getReason());
                                        var20_23.setTokenStatus(var26_27.getCode());
                                        var20_23.H(var26_27.getReason());
                                        k.this.jJ.d(var20_23);
                                        var19_22.ac().setTokenStatus(var20_23.getTokenStatus());
                                        var19_22.ac().H(var20_23.fy());
                                        var28_28 = m.F(var20_23.getTokenStatus());
                                        var8_11 = m.a(k.this.mContext, var19_22, var2_2.getResult());
                                        if (var8_11 != null && var8_11.getMetadata() != null) {
                                            m.a(k.this.mContext, var8_11.getMetadata(), var19_22);
                                        }
                                        if ((var29_29 = com.samsung.android.spayfw.fraud.a.x(k.this.mContext)) == null) ** GOTO lbl173
                                        var29_29.k(var20_23.getTrTokenId(), var20_23.getTokenStatus());
lbl157: // 2 sources:
                                        do {
                                            var12_9 = true;
                                            var9_5 = var3_3;
                                            var13_6 = var25_26;
                                            var10_7 = var24_25;
                                            var11_8 = var28_28;
                                            var7_10 = 0;
                                            ** GOTO lbl13
                                            break;
                                        } while (true);
                                    }
                                    break block30;
lbl166: // 1 sources:
                                    var4_4 = var19_22.ad().updateTokenStatusTA(null, var23_24.getStatus());
                                    if (var4_4 != null && var4_4.getErrorCode() == 0) ** GOTO lbl171
                                    com.samsung.android.spayfw.b.c.e("IdvVerifier", "VerifyIdv::onRequestComplete: updateTokenStatus failed on provider side");
                                    var3_3 = false;
                                    ** GOTO lbl144
lbl171: // 1 sources:
                                    var3_3 = true;
                                    ** GOTO lbl144
lbl173: // 1 sources:
                                    com.samsung.android.spayfw.b.c.d("IdvVerifier", "VerifyIdv::onRequestComplete: updateFTokenRecordStatus cannot get data");
                                    ** continue;
                                }
                                case 403: 
                                case 407: {
                                    var5_30 = -17;
                                    var17_31 = var2_2.fa();
                                    if (var17_31 == null || var17_31.getCode() == null) break block31;
                                    var18_32 = var17_31.getCode();
                                    if (!"407.3".equals((Object)var18_32) && !"407.2".equals((Object)var18_32)) ** GOTO lbl192
                                    var5_30 = -17;
lbl182: // 7 sources:
                                    do {
                                        var7_10 = var5_30;
                                        var8_11 = null;
                                        var9_5 = false;
                                        var10_7 = null;
                                        var11_8 = null;
                                        var12_9 = false;
                                        var13_6 = null;
                                        var4_4 = null;
                                        ** GOTO lbl13
                                        break;
                                    } while (true);
lbl192: // 1 sources:
                                    if (!"407.1".equals((Object)var18_32)) ** GOTO lbl195
                                    var5_30 = -16;
                                    ** GOTO lbl182
lbl195: // 1 sources:
                                    if (!"403.2".equals((Object)var18_32)) ** GOTO lbl198
                                    var5_30 = -18;
                                    ** GOTO lbl182
lbl198: // 1 sources:
                                    if (!"403.3".equals((Object)var18_32)) ** GOTO lbl201
                                    var5_30 = -19;
                                    ** GOTO lbl182
lbl201: // 1 sources:
                                    if (!"403.6".equals((Object)var18_32)) ** GOTO lbl204
                                    var5_30 = -20;
                                    ** GOTO lbl182
lbl204: // 1 sources:
                                    if (!"403.7".equals((Object)var18_32)) ** GOTO lbl182
                                    var5_30 = -21;
                                    ** continue;
                                }
                                case 205: {
                                    k.this.iJ.s(this.mEnrollmentId);
                                    k.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                                    var7_10 = -8;
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    ** GOTO lbl13
                                }
                                case 0: 
                                case 503: {
                                    var7_10 = -201;
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    ** GOTO lbl13
                                }
                                case 404: 
                                case 500: {
                                    var7_10 = -205;
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    ** GOTO lbl13
                                }
                                case -2: {
                                    var7_10 = -206;
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    ** GOTO lbl13
                                }
                                case 400: {
                                    var5_30 = -1;
                                    var6_33 = var2_2.fa();
                                    if (var6_33 == null || var6_33.getCode() == null) break block31;
                                    if ("403.1".equals((Object)var6_33.getCode())) {
                                        var5_30 = -202;
                                    }
                                    var7_10 = var5_30;
                                    var8_11 = null;
                                    var9_5 = false;
                                    var10_7 = null;
                                    var11_8 = null;
                                    var12_9 = false;
                                    var13_6 = null;
                                    var4_4 = null;
                                    ** GOTO lbl13
                                }
                            }
                            try {
                                this.lr.onFail(this.mEnrollmentId, var7_10, var8_11);
                            }
                            catch (RemoteException var14_34) {
                                com.samsung.android.spayfw.b.c.c("IdvVerifier", var14_34.getMessage(), var14_34);
                            }
                            ** while (true)
                        }
                        com.samsung.android.spayfw.b.c.e("IdvVerifier", "error happend during token status change. report to server");
                        k.this.b(null, var10_7, var11_8, "STATUS_CHANGE", c.y(var13_6), var4_4, false);
                        return;
                    }
                    var7_10 = var5_30;
                    var8_11 = null;
                    var9_5 = false;
                    var10_7 = null;
                    var11_8 = null;
                    var12_9 = false;
                    var13_6 = null;
                    var4_4 = null;
                    ** GOTO lbl13
                }
                var9_5 = var3_3;
                var13_6 = var25_26;
                var10_7 = var24_25;
                var12_9 = true;
                var8_11 = null;
                var11_8 = null;
                var7_10 = 0;
                ** while (true)
            }
            var39_19 = null;
            ** while (true)
        }
    }

}

