/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  com.google.gson.JsonObject
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.l;
import com.samsung.android.spayfw.core.m;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage;

public class y
extends o {
    protected final IProvisionTokenCallback mA;
    protected ProvisionTokenInfo mB;
    protected final String mEnrollmentId;

    public y(Context context, String string, ProvisionTokenInfo provisionTokenInfo, IProvisionTokenCallback iProvisionTokenCallback) {
        super(context);
        this.mEnrollmentId = string;
        this.mB = provisionTokenInfo;
        this.mA = iProvisionTokenCallback;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void process() {
        int n2 = -1;
        if (this.mEnrollmentId == null || this.mA == null || this.iJ == null) {
            if (this.mEnrollmentId == null) {
                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Token Failed - Enrollment Id is null");
            }
            if (this.iJ == null) {
                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Token Failed - Failed to initialize account");
            } else {
                n2 = -5;
            }
            if (this.mA == null) {
                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Token Failed - Provision Callback is null");
                return;
            }
            this.mA.onFail(this.mEnrollmentId, n2, null);
            return;
        } else {
            c c2 = this.iJ.q(this.mEnrollmentId);
            if (c2 == null || c2.ac() == null) {
                if (c2 == null) {
                    n2 = -6;
                    com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Token Failed - unable to get card object");
                } else {
                    com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Token Failed - token is null");
                }
                this.mA.onFail(this.mEnrollmentId, n2, null);
                return;
            }
            if (c2.ac().getTokenId() != null || !"ENROLLED".equals((Object)c2.ac().getTokenStatus())) {
                com.samsung.android.spayfw.b.c.w("TokenProvisioner", "Provision Token Failed - alrerady provisioned");
                this.mA.onFail(this.mEnrollmentId, -3, null);
                return;
            }
            com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bp(this.mEnrollmentId);
            if (a2 == null) {
                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Token Failed - unable to find record from db");
                this.iJ.s(this.mEnrollmentId);
                this.mA.onFail(this.mEnrollmentId, -8, null);
                return;
            }
            long l2 = a2.fA();
            com.samsung.android.spayfw.payprovider.c c3 = c2.ad().getProvisionRequestDataTA(this.mB);
            com.samsung.android.spayfw.fraud.a a3 = com.samsung.android.spayfw.fraud.a.x(this.mContext);
            if (!c2.getCardBrand().equals((Object)"LO")) {
                if (a3 != null) {
                    a3.bs();
                } else {
                    com.samsung.android.spayfw.b.c.d("TokenProvisioner", "process: cannot get fraud data");
                }
            }
            TokenRequestData tokenRequestData = l.a(l2, this.mEnrollmentId, this.mB, c3);
            com.samsung.android.spayfw.remoteservice.tokenrequester.e e2 = this.lQ.a(c.y(c2.getCardBrand()), tokenRequestData);
            e2.bk(this.iJ.u(c2.getCardBrand()));
            e2.a(new a(this.mEnrollmentId, this.mA));
            if (c2.getCardBrand().equals((Object)"LO") || a3 == null) return;
            {
                a3.X(this.mEnrollmentId);
                return;
            }
        }
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.tokenrequester.f, com.samsung.android.spayfw.remoteservice.tokenrequester.e> {
        IProvisionTokenCallback mA;
        String mEnrollmentId;

        public a(String string, IProvisionTokenCallback iProvisionTokenCallback) {
            this.mEnrollmentId = string;
            this.mA = iProvisionTokenCallback;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.tokenrequester.f var2_2) {
            block54 : {
                block53 : {
                    block44 : {
                        block45 : {
                            block50 : {
                                block52 : {
                                    block51 : {
                                        block49 : {
                                            block48 : {
                                                block47 : {
                                                    block46 : {
                                                        var3_3 = null;
                                                        com.samsung.android.spayfw.b.c.i("TokenProvisioner", "Provision Token  - onRequestComplete: " + var1_1);
                                                        var4_4 = y.this.iJ.q(this.mEnrollmentId);
                                                        switch (var1_1) {
                                                            default: {
                                                                var5_5 = -1;
                                                                var7_6 = false;
                                                                var6_7 = "PENDING";
                                                                var10_8 = null;
                                                                var8_9 = false;
                                                                var9_10 = null;
                                                                break block45;
                                                            }
                                                            case 202: {
                                                                if (var2_2 != null) {
                                                                    var23_14 = (TokenResponseData)var2_2.getResult();
                                                                    var24_15 = var23_14.getCard() != null ? var23_14.getCard().getBrand() : null;
                                                                }
                                                                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Response is null");
                                                                var5_5 = -204;
                                                                var6_7 = "PENDING";
                                                                var7_6 = false;
                                                                var8_9 = false;
                                                                var9_10 = null;
                                                                var10_8 = null;
                                                                var3_3 = null;
                                                                break block45;
                                                            }
                                                            case 205: {
                                                                var5_5 = -202;
                                                                y.this.iJ.s(this.mEnrollmentId);
                                                                y.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                                                                var6_7 = "PENDING";
                                                                var7_6 = false;
                                                                var8_9 = false;
                                                                var9_10 = null;
                                                                var10_8 = null;
                                                                var3_3 = null;
                                                                break block45;
                                                            }
                                                            case 0: 
                                                            case 503: {
                                                                var5_5 = -201;
                                                                var6_7 = "PENDING";
                                                                var7_6 = false;
                                                                var8_9 = false;
                                                                var9_10 = null;
                                                                var10_8 = null;
                                                                var3_3 = null;
                                                                break block45;
                                                            }
                                                            case 404: 
                                                            case 500: {
                                                                var5_5 = -205;
                                                                var6_7 = "PENDING";
                                                                var7_6 = false;
                                                                var8_9 = false;
                                                                var9_10 = null;
                                                                var10_8 = null;
                                                                var3_3 = null;
                                                                break block45;
                                                            }
                                                            case -2: {
                                                                var5_5 = -206;
                                                                var6_7 = "PENDING";
                                                                var7_6 = false;
                                                                var8_9 = false;
                                                                var9_10 = null;
                                                                var10_8 = null;
                                                                var3_3 = null;
                                                                break block45;
                                                            }
                                                            case 400: {
                                                                var5_5 = -1;
                                                                if (var2_2 != null) {
                                                                    var19_32 = var2_2.fa();
                                                                    if (var19_32 != null && var19_32.getCode() != null) {
                                                                        var20_33 = var19_32.getCode();
                                                                        if ("400.5".equals((Object)var20_33) || "400.3".equals((Object)var20_33)) {
                                                                            var5_5 = -11;
                                                                        } else if ("400.4".equals((Object)var20_33)) {
                                                                            var5_5 = -3;
                                                                        } else if ("403.4".equals((Object)var20_33)) {
                                                                            var5_5 = -13;
                                                                        } else if ("403.8".equals((Object)var20_33)) {
                                                                            var5_5 = -209;
                                                                        }
                                                                    }
                                                                    var6_7 = "PENDING";
                                                                    var7_6 = false;
                                                                    var8_9 = false;
                                                                    var9_10 = null;
                                                                    var10_8 = null;
                                                                    var3_3 = null;
                                                                } else {
                                                                    var6_7 = "PENDING";
                                                                    var7_6 = false;
                                                                    var8_9 = false;
                                                                    var9_10 = null;
                                                                    var10_8 = null;
                                                                    var3_3 = null;
                                                                }
                                                                break block45;
                                                            }
                                                        }
                                                        com.samsung.android.spayfw.b.c.d("TokenProvisioner", "ProvisionCallback:onRequestComplete: Provision Data : " + (Object)var23_14.getData());
                                                        var25_16 = var23_14.getData();
                                                        var26_17 = false;
                                                        if (var25_16 != null) {
                                                            var26_17 = true;
                                                        }
                                                        var27_18 = y.this.jJ.bp(this.mEnrollmentId);
                                                        if (var4_4 != null && var27_18 != null) break block46;
                                                        com.samsung.android.spayfw.b.c.e("TokenProvisioner", "unable to get card object from mAccount or db record is null ");
                                                        if (var27_18 != null) {
                                                            com.samsung.android.spayfw.b.c.i("TokenProvisioner", "delete record from db ");
                                                            y.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                                                        }
                                                        if (var4_4 != null) {
                                                            com.samsung.android.spayfw.b.c.i("TokenProvisioner", "delete card object");
                                                            y.this.iJ.s(this.mEnrollmentId);
                                                        }
                                                        var9_10 = var24_15;
                                                        var7_6 = var26_17;
                                                        var6_7 = "DISPOSED";
                                                        var5_5 = -8;
                                                        var8_9 = false;
                                                        var10_8 = null;
                                                        var3_3 = null;
                                                        break block45;
                                                    }
                                                    var30_19 = var23_14.getData();
                                                    var31_20 = null;
                                                    var32_21 = null;
                                                    var33_22 = false;
                                                    if (var30_19 == null) break block47;
                                                    var34_23 = new com.samsung.android.spayfw.payprovider.c();
                                                    var34_23.a(var23_14.getData());
                                                    var34_23.e(m.b((TokenResponseData)var2_2.getResult()));
                                                    var32_21 = var4_4.ad().createTokenTA(var23_14.getId(), var34_23, 1);
                                                    if (var32_21 == null || var32_21.getErrorCode() != 0 || var32_21.getProviderTokenKey() == null) break block48;
                                                    var31_20 = var32_21.getProviderTokenKey();
                                                    var33_22 = true;
                                                }
                                                var36_24 = m.a(var27_18, var23_14, var31_20);
                                                if (var23_14.getData() == null || var23_14.getStatus() == null) {
                                                    var36_24.setTokenStatus("PENDING_PROVISION");
                                                }
                                                if (!y.this.a(var36_24)) break block49;
                                                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Duplicate Token Ref Id / Tr Token Id");
                                                var42_25 = new TokenStatus("DISPOSED", null);
                                                var4_4.ad().updateTokenStatusTA(null, var42_25);
                                                y.this.iJ.s(this.mEnrollmentId);
                                                y.this.jJ.d(TokenRecordStorage.TokenGroup.TokenColumn.Cn, this.mEnrollmentId);
                                                var46_26 = new Intent("com.samsung.android.spayfw.action.notification");
                                                var46_26.putExtra("notiType", "syncAllCards");
                                                PaymentFrameworkApp.a(var46_26);
                                                var9_10 = var24_15;
                                                var7_6 = var26_17;
                                                var6_7 = "DISPOSED";
                                                var5_5 = -6;
                                                var8_9 = false;
                                                var10_8 = null;
                                                var3_3 = null;
                                                break block45;
                                            }
                                            if (var32_21 == null) {
                                                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "PayProvider error: ProviderResponseData is null");
                                            } else if (var32_21.getErrorCode() != 0) {
                                                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "PayProvider error: pay provider return error: " + var32_21.getErrorCode());
                                            } else {
                                                com.samsung.android.spayfw.b.c.e("TokenProvisioner", "PayProvider error: providerTokenKey is null");
                                            }
                                            var5_5 = -1;
                                            var35_27 = var24_15;
                                            var7_6 = var26_17;
                                            var6_7 = "PENDING";
                                            var10_8 = var32_21;
                                            var9_10 = var35_27;
                                            var8_9 = false;
                                            var3_3 = null;
                                            break block45;
                                        }
                                        y.this.jJ.d(var36_24);
                                        var4_4.ac().setTokenId(var36_24.getTrTokenId());
                                        var4_4.ac().setTokenStatus(var36_24.getTokenStatus());
                                        var4_4.ac().H(var36_24.fy());
                                        if (var31_20 != null) {
                                            var4_4.ac().c(var31_20);
                                        } else {
                                            var38_31 = new f(null);
                                            var38_31.setTrTokenId(var36_24.getTrTokenId());
                                            var4_4.ad().setProviderTokenKey(var38_31);
                                        }
                                        var4_4.setCardBrand(var36_24.getCardBrand());
                                        var4_4.setCardType(var36_24.getCardType());
                                        var4_4.j(var36_24.ab());
                                        var39_28 = m.a(y.this.mContext, var2_2, var4_4, var32_21);
                                        if (!var33_22) break block50;
                                        var40_29 = m.F(var36_24.getTokenStatus());
                                        if (var4_4.getCardBrand() == null) break block51;
                                        if (var4_4.getCardBrand().equals((Object)"LO")) break block52;
                                        var41_30 = com.samsung.android.spayfw.fraud.a.x(y.this.mContext);
                                        if (var41_30 != null) {
                                            var41_30.l(var4_4.ac().getTokenId(), var40_29);
                                        } else {
                                            com.samsung.android.spayfw.b.c.d("TokenProvisioner", "ProvisionCallback:onRequestComplete: cannot get fraud data");
                                        }
                                        var10_8 = var32_21;
                                        var3_3 = var39_28;
                                        var8_9 = var33_22;
                                        var9_10 = var24_15;
                                        var7_6 = var26_17;
                                        var6_7 = var40_29;
                                        var5_5 = 0;
                                        break block45;
                                    }
                                    com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Card Brand is null");
                                    var10_8 = var32_21;
                                    var3_3 = var39_28;
                                    var8_9 = var33_22;
                                    var9_10 = var24_15;
                                    var7_6 = var26_17;
                                    var6_7 = var40_29;
                                    var5_5 = 0;
                                    break block45;
                                }
                                var10_8 = var32_21;
                                var3_3 = var39_28;
                                var8_9 = var33_22;
                                var9_10 = var24_15;
                                var7_6 = var26_17;
                                var6_7 = var40_29;
                                var5_5 = 0;
                                break block45;
                            }
                            var3_3 = var39_28;
                            var10_8 = var32_21;
                            var8_9 = var33_22;
                            var9_10 = var24_15;
                            var7_6 = var26_17;
                            var6_7 = "PENDING";
                            var5_5 = 0;
                        }
                        if (var5_5 != 0) {
                            com.samsung.android.spayfw.b.c.e("TokenProvisioner", "Provision Token Failed - Error Code = " + var5_5);
                            if (var4_4 != null) {
                                var17_11 = new d(3, -1, null);
                                var4_4.ad().updateRequestStatus(var17_11);
                            }
                            this.mA.onFail(this.mEnrollmentId, var5_5, var3_3);
                            if (!var7_6 || var4_4 == null || var4_4.getCardBrand().equals((Object)"LO")) ** GOTO lbl266
                            var18_12 = com.samsung.android.spayfw.fraud.a.x(y.this.mContext);
                            if (var18_12 != null) {
                                var18_12.bs();
                            }
                            com.samsung.android.spayfw.b.c.d("TokenProvisioner", "ProvisionCallback:onRequestComplete: cannot get fraud data");
                        }
                        if (var4_4 == null) break block53;
                        var11_35 = null;
                        if (var2_2 != null) {
                            var16_36 = var2_2.getResult();
                            var11_35 = null;
                            if (var16_36 == null) break block44;
                            var11_35 = new f(((TokenResponseData)var2_2.getResult()).getId());
                            break block44;
                        }
                        break block44;
                        catch (Exception var15_34) {
                            com.samsung.android.spayfw.b.c.c("TokenProvisioner", var15_34.getMessage(), var15_34);
                        }
                        break block54;
                    }
                    var12_37 = new d(3, 0, var11_35);
                    var4_4.ad().updateRequestStatus(var12_37);
                }
                this.mA.onSuccess(this.mEnrollmentId, var3_3);
                break block54;
                catch (Throwable var13_38) {
                    throw var13_38;
                }
                finally {
                    m.a(var2_2, var3_3);
                }
            }
            if (var7_6 == false) return;
            var14_13 = null;
            if (var4_4 != null) {
                var14_13 = var4_4.ac().getTokenId();
            }
            if (var8_9) {
                com.samsung.android.spayfw.b.c.i("TokenProvisioner", "processProvision:Send success report to TR server");
                y.this.a(null, var14_13, var6_7, "PROVISION", c.y(var9_10), var10_8, false);
                return;
            }
            com.samsung.android.spayfw.b.c.i("TokenProvisioner", "processProvision:Send error report to TR server");
            y.this.b(null, var14_13, var6_7, "PROVISION", c.y(var9_10), var10_8, false);
        }
    }

}

