/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.Message
 *  android.os.RemoteException
 *  com.google.gson.JsonObject
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.LoyaltyCardDetail;
import com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest;
import com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.f;
import com.samsung.android.spayfw.core.i;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Instruction;
import com.samsung.android.spayfw.remoteservice.tokenrequester.n;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import java.util.ArrayList;
import java.util.List;

public class l
extends o {
    private static l lu = null;

    private l(Context context) {
        super(context);
    }

    private static List<Instruction> a(c c2, UpdateLoyaltyCardInfo updateLoyaltyCardInfo) {
        ArrayList arrayList = new ArrayList();
        for (com.samsung.android.spayfw.appinterface.Instruction instruction : updateLoyaltyCardInfo.getInstructions()) {
            String string = instruction.getValue();
            if (instruction.isEncrypt() && (string = c2.ad().prepareLoyaltyDataForServerTA(string)) == null) {
                Log.e("LoyaltyCardProcessor", "updateLoyaltyCard: Error getting encrypted value");
                continue;
            }
            Instruction instruction2 = new Instruction();
            instruction2.setOp(instruction.getOp());
            instruction2.setPath(instruction.getPath());
            instruction2.setValue(string);
            arrayList.add((Object)instruction2);
        }
        return arrayList;
    }

    private void d(PaymentDetailsRecord paymentDetailsRecord) {
        i i2 = PaymentFrameworkApp.az();
        if (i2 != null) {
            Log.d("LoyaltyCardProcessor", "Post PAYFW_OPT_ANALYTICS_REPORT request");
            i2.sendMessage(j.a(21, paymentDetailsRecord, null));
            return;
        }
        Log.e("LoyaltyCardProcessor", "HANDLER IS NOT INITIAILIZED");
    }

    public static l p(Context context) {
        Class<l> class_ = l.class;
        synchronized (l.class) {
            if (lu == null) {
                lu = new l(context);
            }
            l l2 = lu;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return l2;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void a(ExtractLoyaltyCardDetailRequest var1_1, IExtractLoyaltyCardDetailResponseCallback var2_2) {
        Log.i("LoyaltyCardProcessor", "extractLoyaltyCardDetails()");
        if (var1_1 == null || var2_2 == null) ** GOTO lbl5
        try {
            block17 : {
                block16 : {
                    if (var1_1.getTokenId() != null && !var1_1.getTokenId().isEmpty() && var1_1.getCardRefID() != null && var1_1.getCardRefID().length > 0 && var1_1.getTzEncData() != null && var1_1.getTzEncData().length > 0) ** GOTO lbl28
lbl5: // 2 sources:
                    if (var1_1 != null) break block16;
                    Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(request) is null!");
                    ** GOTO lbl24
                }
                if (var1_1.getTokenId() != null && !var1_1.getTokenId().isEmpty()) break block17;
                Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(tokenId) is null!");
                ** GOTO lbl24
            }
            if (var1_1.getCardRefID() != null && var1_1.getCardRefID().length > 0) ** GOTO lbl20
            Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(cardRefId) is null!");
            ** GOTO lbl24
        }
        catch (RemoteException var3_3) {
            block18 : {
                Log.c("LoyaltyCardProcessor", var3_3.getMessage(), var3_3);
                f.j(this.mContext).ap();
                break block18;
lbl20: // 1 sources:
                if (var1_1.getTzEncData() == null || var1_1.getTzEncData().length <= 0) {
                    Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(cardData) is null!");
                } else {
                    Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(callback) is null!");
                }
lbl24: // 5 sources:
                if (var2_2 != null) {
                    var2_2.onFail(-5);
                }
                f.j(this.mContext).ap();
                return;
lbl28: // 1 sources:
                Log.d("LoyaltyCardProcessor", "token id = " + var1_1.getTokenId());
                if (this.iJ == null) {
                    Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: Failed to initialize account");
                    var2_2.onFail(-1);
                    return;
                }
                var4_4 = this.iJ.r(var1_1.getTokenId());
                if (var4_4 == null || var4_4.ac() == null) {
                    Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: Invalid tokenId");
                    var2_2.onFail(-6);
                    f.j(this.mContext).ap();
                    return;
                }
                var5_5 = var4_4.ad().extractLoyaltyCardDetailTA(var1_1);
                if (var5_5 != null) {
                    if (var5_5.getErrorCode() == 0) {
                        var6_6 = new LoyaltyCardDetail();
                        var7_7 = new Bundle();
                        if (var5_5.getCardnumber() != null) {
                            var7_7.putString("cardNumber", var5_5.getCardnumber());
                            Log.d("LoyaltyCardProcessor", "Card Number = " + var5_5.getCardnumber());
                        }
                        if (var5_5.getBarcodeContent() != null) {
                            var7_7.putString("barcodeContent", var5_5.getBarcodeContent());
                            Log.d("LoyaltyCardProcessor", "Barcode Content = " + var5_5.getBarcodeContent());
                        }
                        if (var5_5.getImgSessionKey() != null) {
                            var7_7.putString("imgSessionKey", var5_5.getImgSessionKey());
                            Log.d("LoyaltyCardProcessor", "Image Session Key = " + var5_5.getImgSessionKey());
                        }
                        if (var5_5.getExtraContent() != null) {
                            var7_7.putString("acTokenExtra", var5_5.getExtraContent());
                            Log.d("LoyaltyCardProcessor", "Extra content = " + var5_5.getExtraContent());
                        }
                        var6_6.setCardDetailbundle(var7_7);
                        var2_2.onSuccess(var6_6);
                    } else {
                        var2_2.onFail(var5_5.getErrorCode());
                    }
                } else {
                    Log.e("LoyaltyCardProcessor", " extractLoyaltyCardDetailTA returns null");
                    var2_2.onFail(-36);
                }
                f.j(this.mContext).ap();
            }
            Log.i("LoyaltyCardProcessor", "extractLoyaltyCardDetails() end");
            return;
        }
    }

    public void a(LoyaltyCardShowRequest loyaltyCardShowRequest, IPayCallback iPayCallback) {
        String string;
        Log.d("LoyaltyCardProcessor", "startLoyaltyCardPay() start");
        if (loyaltyCardShowRequest == null || iPayCallback == null || loyaltyCardShowRequest.getTokenId() == null) {
            if (iPayCallback != null) {
                String string2 = null;
                if (loyaltyCardShowRequest != null) {
                    string2 = loyaltyCardShowRequest.getTokenId();
                }
                iPayCallback.onFail(string2, -5);
            }
            Log.e("LoyaltyCardProcessor", "inputs are null!");
            return;
        }
        c c2 = this.iJ.r(loyaltyCardShowRequest.getTokenId());
        if (c2 == null || c2.ac() == null) {
            Log.e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: Invalid tokenId");
            iPayCallback.onFail(loyaltyCardShowRequest.getTokenId(), -6);
            return;
        }
        PaymentDetailsRecord paymentDetailsRecord = new PaymentDetailsRecord();
        paymentDetailsRecord.setPaymentType(c.y(c2.getCardBrand()));
        paymentDetailsRecord.setBarcodeAttempted("ATTEMPTED");
        paymentDetailsRecord.setTrTokenId(loyaltyCardShowRequest.getTokenId());
        if (loyaltyCardShowRequest.getExtras() != null && (string = loyaltyCardShowRequest.getExtras().getString("cardName")) != null) {
            paymentDetailsRecord.setCardName(string);
        }
        this.d(paymentDetailsRecord);
        iPayCallback.onFinish(loyaltyCardShowRequest.getTokenId(), 0, null);
        Log.d("LoyaltyCardProcessor", "startLoyaltyCardPay() end");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void a(UpdateLoyaltyCardInfo var1_1, IUpdateLoyaltyCardCallback var2_2) {
        Log.d("LoyaltyCardProcessor", "updateLoyaltyCard() start");
        if (var1_1 == null) ** GOTO lbl5
        try {
            block10 : {
                if (var1_1.getInstructions() != null && !var1_1.getInstructions().isEmpty() && var1_1.getCardId() != null && !var1_1.getCardId().isEmpty() && var1_1.getTokenId() != null && !var1_1.getTokenId().isEmpty() && var2_2 != null) break block10;
lbl5: // 2 sources:
                if (var2_2 != null) {
                    var2_2.onFail(-5);
                }
                Log.e("LoyaltyCardProcessor", "Input Validation failed: cardInfo = " + var1_1 + "; cb = " + var2_2);
                if (var1_1 == null) return;
                Log.e("LoyaltyCardProcessor", "Input Validation failed: cardInfo.toString = " + var1_1.toString());
                return;
            }
            if (this.iJ == null) {
                Log.e("LoyaltyCardProcessor", "updateLoyaltyCard: Failed to initialize account");
                var2_2.onFail(-1);
                return;
            }
            var4_4 = this.iJ.r(var1_1.getTokenId());
            if (var4_4 == null || var4_4.ac() == null) {
                Log.e("LoyaltyCardProcessor", "updateLoyaltyCard: Invalid tokenId");
                var2_2.onFail(-6);
                return;
            }
            ** GOTO lbl25
        }
        catch (RemoteException var3_3) {
            block11 : {
                Log.c("LoyaltyCardProcessor", var3_3.getMessage(), var3_3);
                break block11;
lbl25: // 1 sources:
                var5_5 = l.a(var4_4, var1_1);
                var6_6 = this.lQ.b(var1_1.getCardId(), var5_5);
                var6_6.bf(this.P(var4_4.getCardBrand()));
                var6_6.a(new a(var1_1, var2_2));
                Log.i("LoyaltyCardProcessor", "updateLoyaltyCard: update loyalty card request made for cardId : " + var1_1.getCardId());
            }
            Log.d("LoyaltyCardProcessor", "updateLoyaltyCard() end");
            return;
        }
    }

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<Data>, n> {
        UpdateLoyaltyCardInfo lv;
        IUpdateLoyaltyCardCallback lw;

        public a(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback) {
            this.lv = updateLoyaltyCardInfo;
            this.lw = iUpdateLoyaltyCardCallback;
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        public void a(int var1_1, com.samsung.android.spayfw.remoteservice.c<Data> var2_2) {
            Log.i("LoyaltyCardProcessor", "UpdateLoyaltyCardProcessorCallback: onRequestComplete:  " + var1_1);
            switch (var1_1) {
                default: {
                    var3_3 = -1;
                    var4_4 = null;
                    break;
                }
                case 200: {
                    if (var2_2 == null || var2_2.getResult() == null || var2_2.getResult().getData() == null) {
                        Log.e("LoyaltyCardProcessor", "onRequestComplete: invalid response from server");
                        var3_3 = -204;
                        var4_4 = null;
                        break;
                    }
                    var6_5 = l.this.iJ.r(this.lv.getTokenId());
                    if (var6_5 == null || var6_5.ac() == null) {
                        Log.e("LoyaltyCardProcessor", "onRequestComplete: Invalid tokenId");
                        var3_3 = -6;
                        var4_4 = null;
                        break;
                    }
                    var4_4 = var6_5.ad().updateLoyaltyCardTA(var2_2.getResult().getData());
                    var3_3 = var4_4 == null ? -36 : 0;
                }
                case 204: 
                case 404: 
                case 410: {
                    var3_3 = -207;
                    var4_4 = null;
                    break;
                }
                case 500: {
                    var3_3 = -205;
                    var4_4 = null;
                    break;
                }
                case 0: 
                case 503: {
                    var3_3 = -201;
                    var4_4 = null;
                    break;
                }
                case -2: {
                    var3_3 = -206;
                    var4_4 = null;
                    break;
                }
            }
            if (var3_3 != 0) ** GOTO lbl41
            try {
                this.lw.onSuccess(var4_4);
                return;
lbl41: // 1 sources:
                this.lw.onFail(var3_3);
                return;
            }
            catch (RemoteException var5_6) {
                Log.c("LoyaltyCardProcessor", var5_6.getMessage(), var5_6);
                return;
            }
        }

        @Override
        public void a(int n2, ServerCertificates serverCertificates, n n3) {
            Log.d("LoyaltyCardProcessor", "onCertsReceived: called for UpdateLoyaltyCardProcessorCallback");
            c c2 = l.this.iJ.r(this.lv.getTokenId());
            if (c2 == null) {
                Log.e("LoyaltyCardProcessor", "UpdateLoyaltyCardProcessorCallback : cannot get Card object :" + this.lv.getTokenId());
                try {
                    this.lw.onFail(-6);
                    return;
                }
                catch (RemoteException remoteException) {
                    Log.c("LoyaltyCardProcessor", remoteException.getMessage(), remoteException);
                    return;
                }
            }
            if (l.this.a(c2.getEnrollmentId(), c2, serverCertificates)) {
                n3.k(l.a(c2, this.lv));
                n3.bf(l.this.P(c2.getCardBrand()));
                n3.a(this);
                Log.i("LoyaltyCardProcessor", "update loyalty card request successfully sent after server cert update");
                return;
            }
            Log.e("LoyaltyCardProcessor", "UpdateLoyaltyCardProcessorCallback: Server certificate update failed");
            try {
                this.lw.onFail(-1);
                return;
            }
            catch (RemoteException remoteException) {
                Log.c("LoyaltyCardProcessor", remoteException.getMessage(), remoteException);
                return;
            }
        }
    }

}

