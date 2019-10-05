/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Message
 *  android.os.RemoteException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.RemoteException;
import com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.e;
import com.samsung.android.spayfw.core.i;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spaytzsvc.api.TAException;

public class f
extends o {
    private static f le;
    PaymentDetailsRecord lf;

    private f(Context context) {
        super(context);
    }

    private void d(PaymentDetailsRecord paymentDetailsRecord) {
        i i2 = PaymentFrameworkApp.az();
        if (i2 != null) {
            Log.d("GiftCardProcessor", "Post PAYFW_OPT_ANALYTICS_REPORT request");
            i2.sendMessage(j.a(21, paymentDetailsRecord, null));
            return;
        }
        Log.e("GiftCardProcessor", "HANDLER IS NOT INITIAILIZED");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final f n(Context context) {
        Class<f> class_ = f.class;
        synchronized (f.class) {
            try {
                if (le != null) return le;
                le = new f(context);
                return le;
            }
            catch (TAException tAException) {
                Log.c("GiftCardProcessor", tAException.getMessage(), (Throwable)((Object)tAException));
                return null;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected c M(String var1_1) {
        var2_2 = null;
        if (this.iJ == null) {
            Log.d("GiftCardProcessor", "Initializing Samsung Account - userId = " + var1_1);
            this.iJ = a.a(this.mContext, var1_1);
        }
        if (this.iJ == null) {
            Log.e("GiftCardProcessor", "unable to create account");
            return null;
        }
        var5_3 = this.iJ.r("GIFT");
        if (var5_3 != null) return var5_3;
        var2_2 = new c(this.mContext, "GI", "GIFT", "GIFT", 0);
        try {
            var6_4 = new q();
            var6_4.setTokenStatus("ACTIVE");
            var6_4.setTokenId("GIFT");
            var7_5 = new com.samsung.android.spayfw.payprovider.f("GIFT");
            var6_4.c(var7_5);
            var2_2.a(var6_4);
            this.iJ.a(var2_2);
            if (var2_2.ad() != null) {
                Log.d("GiftCardProcessor", "Set Provider Token Key for Gift Card");
                var2_2.ad().setProviderTokenKey(var7_5);
            }
            var9_6 = new com.samsung.android.spayfw.storage.models.a("GIFT");
            var9_6.setUserId(a.a(this.mContext, null).getAccountId());
            var9_6.setTrTokenId("GIFT");
            var9_6.j(0);
            var9_6.setCardBrand(var2_2.getCardBrand());
            var9_6.setTokenStatus("ACTIVE");
            var9_6.setTokenRefId("GIFT");
            this.jJ.c(var9_6);
            return var2_2;
        }
        catch (TAException var4_7) {}
        ** GOTO lbl-1000
        catch (Exception var3_9) {}
        ** GOTO lbl-1000
        catch (Exception var12_11) {
            var3_10 = var12_11;
            var2_2 = var5_3;
        }
lbl-1000: // 2 sources:
        {
            Log.c("GiftCardProcessor", var3_10.getMessage(), var3_10);
            return var2_2;
        }
        catch (TAException var11_12) {
            var4_8 = var11_12;
            var2_2 = var5_3;
        }
lbl-1000: // 2 sources:
        {
            Log.c("GiftCardProcessor", var4_8.getMessage(), (Throwable)var4_8);
            return var2_2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, IGiftCardExtractDetailCallback iGiftCardExtractDetailCallback) {
        byte[] arrby;
        String string;
        byte[] arrby2;
        String string2;
        block12 : {
            block13 : {
                block14 : {
                    string = null;
                    try {
                        Log.d("GiftCardProcessor", "extractGiftCardDetail()");
                        if (extractGiftCardDetailRequest != null) {
                            arrby = extractGiftCardDetailRequest.getTzEncData();
                            arrby2 = extractGiftCardDetailRequest.getCardRefID();
                            string2 = extractGiftCardDetailRequest.getTokenId();
                            string = extractGiftCardDetailRequest.getCardName();
                        } else {
                            string2 = null;
                            arrby2 = null;
                            arrby = null;
                        }
                        if (arrby != null && iGiftCardExtractDetailCallback != null && securedObject != null) break block12;
                        if (iGiftCardExtractDetailCallback != null) {
                            iGiftCardExtractDetailCallback.onFail(-5);
                        } else {
                            Log.e("GiftCardProcessor", "pay callback is null");
                        }
                        if (arrby == null) {
                            Log.e("GiftCardProcessor", "extractGiftCardDetail- invalid inputs: giftCardTzEncData is null");
                            break block13;
                        }
                        if (securedObject != null) break block14;
                        Log.e("GiftCardProcessor", "extractGiftCardDetail- invalid inputs: secObj is null");
                        break block13;
                    }
                    catch (RemoteException remoteException) {
                        Log.c("GiftCardProcessor", remoteException.getMessage(), remoteException);
                        com.samsung.android.spayfw.core.f.j(this.mContext).ap();
                        return;
                    }
                }
                Log.e("GiftCardProcessor", "extractGiftCardDetail- invalid inputs: callback is null");
            }
            com.samsung.android.spayfw.core.f.j(this.mContext).ap();
            return;
        }
        c c2 = this.M(null);
        if (c2 == null || c2.ad() == null) {
            iGiftCardExtractDetailCallback.onFail(-1);
            Log.e("GiftCardProcessor", "extractGiftCardDetail - unable to get card Object");
            com.samsung.android.spayfw.core.f.j(this.mContext).ap();
            return;
        }
        GiftCardDetail giftCardDetail = c2.ad().extractGiftCardDetailTA(arrby2, arrby, securedObject, false);
        this.lf = new PaymentDetailsRecord();
        if (this.lf != null) {
            this.lf.setPaymentType(c.y(c2.getCardBrand()));
            this.lf.setBarcodeAttempted("ATTEMPTED");
            this.lf.setTrTokenId("GIFT");
            this.lf.setCardId(string2);
            this.lf.setCardName(string);
        }
        this.d(this.lf);
        if (giftCardDetail.getErrorCode() == 0) {
            iGiftCardExtractDetailCallback.onSuccess(giftCardDetail);
        } else {
            iGiftCardExtractDetailCallback.onFail(giftCardDetail.getErrorCode());
        }
        com.samsung.android.spayfw.core.f.j(this.mContext).ap();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
        c c2;
        GiftCardRegisterResponseData giftCardRegisterResponseData = new GiftCardRegisterResponseData();
        try {
            Log.d("GiftCardProcessor", "getGiftCardRegisterData()");
            if (giftCardRegisterRequestData == null || iGiftCardRegisterCallback == null) {
                if (iGiftCardRegisterCallback != null) {
                    Log.e("GiftCardProcessor", "getGiftCardRegisterData- invalid inputs: data is null");
                    iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
                    return;
                }
                Log.e("GiftCardProcessor", "getGiftCardRegisterData- invalid inputs: callback is null");
                return;
            }
            if (giftCardRegisterRequestData.getGiftCardData() == null || giftCardRegisterRequestData.getServerCaCert() == null || giftCardRegisterRequestData.getServerEncCert() == null || giftCardRegisterRequestData.getServerVerCert() == null || giftCardRegisterRequestData.getUserId() == null || giftCardRegisterRequestData.getWalletId() == null) {
                Log.e("GiftCardProcessor", "getGiftCardRegisterData- invalid data");
                iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
                return;
            }
        }
        catch (RemoteException remoteException) {
            Log.c("GiftCardProcessor", remoteException.getMessage(), remoteException);
            return;
        }
        Log.d("GiftCardProcessor", "Wallet ID =  " + giftCardRegisterRequestData.getWalletId());
        String string = e.h(this.mContext).getConfig("CONFIG_WALLET_ID");
        if (string == null) {
            int n2 = e.h(this.mContext).setConfig("CONFIG_WALLET_ID", giftCardRegisterRequestData.getWalletId());
            Log.i("GiftCardProcessor", "Wallet ID Set: " + n2);
            if (n2 != 0) {
                iGiftCardRegisterCallback.onFail(-5, null);
                return;
            }
        } else if (!string.equals((Object)giftCardRegisterRequestData.getWalletId())) {
            Log.e("GiftCardProcessor", "Wallet ID Mismatched");
            iGiftCardRegisterCallback.onFail(-208, null);
            return;
        }
        if ((c2 = this.M(giftCardRegisterRequestData.getUserId())) == null || c2.ad() == null) {
            iGiftCardRegisterCallback.onFail(-1, giftCardRegisterResponseData);
            Log.e("GiftCardProcessor", "getGiftCardRegisterData- unable to get card Object");
            return;
        }
        GiftCardRegisterResponseData giftCardRegisterResponseData2 = c2.ad().getGiftCardRegisterDataTA(giftCardRegisterRequestData);
        if (giftCardRegisterResponseData2.getErrorCode() == 0) {
            iGiftCardRegisterCallback.onSuccess(giftCardRegisterResponseData2);
            return;
        }
        iGiftCardRegisterCallback.onFail(giftCardRegisterResponseData2.getErrorCode(), giftCardRegisterResponseData2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void a(byte[] arrby, byte[] arrby2, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
        try {
            Log.d("GiftCardProcessor", "startGiftCardPay()");
            if (arrby2 == null || iPayCallback == null || securedObject == null) {
                if (iPayCallback != null) {
                    Log.e("GiftCardProcessor", "startGiftCardPay- invalid inputs: data is null");
                    iPayCallback.onFail("GIFT", -5);
                }
                if (arrby2 == null) {
                    Log.e("GiftCardProcessor", "startGiftCardPay- invalid inputs: giftCardTzEncData is null");
                    return;
                }
                if (securedObject == null) {
                    Log.e("GiftCardProcessor", "startGiftCardPay- invalid inputs: secObj is null");
                    return;
                }
                Log.e("GiftCardProcessor", "startGiftCardPay- invalid inputs: callback is null");
                return;
            }
        }
        catch (RemoteException remoteException) {
            Log.c("GiftCardProcessor", remoteException.getMessage(), remoteException);
            return;
        }
        c c2 = this.M(null);
        if (c2 == null || c2.ad() == null) {
            if (iPayCallback != null) {
                iPayCallback.onFail("GIFT", -1);
            }
            Log.e("GiftCardProcessor", "startGiftCardPay - unable to get card Object");
            return;
        }
        GiftCardDetail giftCardDetail = c2.ad().extractGiftCardDetailTA(arrby, arrby2, securedObject, true);
        if (giftCardDetail.getErrorCode() == 0) {
            Log.d("GiftCardProcessor", "startGiftCardPay - extract giftCardDetail success");
        }
        Log.d("GiftCardProcessor", "startGiftCardPay - invoking onExtractGiftCardDetail cb");
        iPayCallback.onExtractGiftCardDetail(giftCardDetail);
        c2.ad().setCardTzEncData(arrby2);
        if (n.q(this.mContext) != null) {
            n.q(this.mContext).a(securedObject, payConfig, iPayCallback, true);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void b(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
        GiftCardRegisterResponseData giftCardRegisterResponseData = new GiftCardRegisterResponseData();
        try {
            Log.d("GiftCardProcessor", "getGiftCardTzEncData()");
            if (giftCardRegisterRequestData == null || iGiftCardRegisterCallback == null) {
                if (iGiftCardRegisterCallback != null) {
                    Log.e("GiftCardProcessor", "getGiftCardTzEncData- invalid inputs: data is null");
                    iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
                    return;
                }
                Log.e("GiftCardProcessor", "getGiftCardTzEncData- invalid inputs: callback is null");
                return;
            }
            if (giftCardRegisterRequestData.getGiftCardData() == null || giftCardRegisterRequestData.getServerCaCert() == null || giftCardRegisterRequestData.getServerEncCert() == null || giftCardRegisterRequestData.getServerVerCert() == null) {
                Log.e("GiftCardProcessor", "getGiftCardTzEncData- invalid data");
                iGiftCardRegisterCallback.onFail(-5, giftCardRegisterResponseData);
                return;
            }
        }
        catch (RemoteException remoteException) {
            Log.c("GiftCardProcessor", remoteException.getMessage(), remoteException);
            return;
        }
        c c2 = this.M(null);
        if (c2 == null || c2.ad() == null) {
            iGiftCardRegisterCallback.onFail(-1, giftCardRegisterResponseData);
            Log.e("GiftCardProcessor", "getGiftCardTzEncData- unable to get card Object");
            return;
        }
        GiftCardRegisterResponseData giftCardRegisterResponseData2 = c2.ad().getGiftCardTzEncDataTA(giftCardRegisterRequestData);
        if (giftCardRegisterResponseData2.getErrorCode() == 0) {
            iGiftCardRegisterCallback.onSuccess(giftCardRegisterResponseData2);
            return;
        }
        iGiftCardRegisterCallback.onFail(giftCardRegisterResponseData2.getErrorCode(), giftCardRegisterResponseData2);
    }
}

