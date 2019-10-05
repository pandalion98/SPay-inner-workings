/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  com.google.gson.JsonObject
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.payprovider.b;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.ExtractCardDetailResult;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAException;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.h;

public class a
extends PaymentNetworkProvider {
    private static f pj = null;
    private static PlccTAController zg;
    private byte[] zh = null;

    public a(Context context, String string, f f2) {
        super(context, string);
        this.mContext = context;
        this.mTAController = PlccTAController.createOnlyInstance(this.mContext);
        zg = (PlccTAController)this.mTAController;
    }

    @Override
    protected boolean authenticateTransaction(SecuredObject securedObject) {
        if (zg == null) {
            Log.e("GiftCardPayProvider", "TAController is null");
            return false;
        }
        try {
            Log.d("GiftCardPayProvider", "Calling Plcc TA Controller Authenticate Transaction");
            boolean bl = zg.authenticateTransaction(securedObject.getSecureObjectData());
            return bl;
        }
        catch (PlccTAException plccTAException) {
            Log.c("GiftCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return false;
        }
    }

    @Override
    protected void clearCard() {
        this.zh = null;
    }

    @Override
    protected e createToken(String string, c c2, int n2) {
        return null;
    }

    @Override
    public void delete() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected GiftCardDetail extractGiftCardDetail(byte[] arrby, byte[] arrby2) {
        GiftCardDetail giftCardDetail = new GiftCardDetail();
        giftCardDetail.setErrorCode(-1);
        if (zg == null) {
            Log.e("GiftCardPayProvider", "TAController is null");
            return giftCardDetail;
        }
        try {
            ExtractCardDetailResult extractCardDetailResult = zg.extractGiftCardDetail(arrby, arrby2);
            if (extractCardDetailResult == null || extractCardDetailResult.getErrorCode() != 0) return giftCardDetail;
            giftCardDetail.setCardnumber(extractCardDetailResult.getCardnumber());
            giftCardDetail.setPin(extractCardDetailResult.getPin());
            giftCardDetail.setBarcodeContent(extractCardDetailResult.getBarcodeContent());
            giftCardDetail.setErrorCode(0);
            return giftCardDetail;
        }
        catch (InterruptedException interruptedException) {
            Log.c("GiftCardPayProvider", interruptedException.getMessage(), interruptedException);
            return giftCardDetail;
        }
        catch (PlccTAException plccTAException) {
            Log.c("GiftCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return giftCardDetail;
        }
    }

    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }

    @Override
    protected CertificateInfo[] getDeviceCertificates() {
        return new CertificateInfo[0];
    }

    @Override
    protected c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public GiftCardRegisterResponseData getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        GiftCardRegisterResponseData giftCardRegisterResponseData;
        byte[] arrby;
        block6 : {
            block5 : {
                giftCardRegisterResponseData = new GiftCardRegisterResponseData();
                if (zg == null) {
                    Log.e("GiftCardPayProvider", "TAController is null");
                    giftCardRegisterResponseData.setErrorCode(-1);
                    return giftCardRegisterResponseData;
                }
                try {
                    PlccTAController plccTAController = zg;
                    plccTAController.setPlccServerCerts("GIFT", giftCardRegisterRequestData.getServerVerCert(), giftCardRegisterRequestData.getServerEncCert(), giftCardRegisterRequestData.getServerCaCert());
                    PlccTAController plccTAController2 = zg;
                    PlccTAController.TACerts tACerts = plccTAController2.getAllCerts("GIFT");
                    if (tACerts != null) {
                        giftCardRegisterResponseData.setDeviceSignCert(tACerts.signcert);
                        giftCardRegisterResponseData.setDeviceEncryptCert(tACerts.encryptcert);
                        giftCardRegisterResponseData.setDeviceDrk(tACerts.drk);
                        long l2 = h.am(this.mContext);
                        Log.d("GiftCardPayProvider", "Network Time = " + l2);
                        PlccTAController plccTAController3 = zg;
                        arrby = plccTAController3.utility_enc4Server_Transport("GIFT", giftCardRegisterRequestData.getGiftCardData(), l2);
                        if (arrby == null) break block5;
                        giftCardRegisterResponseData.setErrorCode(0);
                        break block6;
                    }
                    giftCardRegisterResponseData.setErrorCode(-1);
                    return giftCardRegisterResponseData;
                }
                catch (PlccTAException plccTAException) {
                    Log.c("GiftCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
                    giftCardRegisterResponseData.setErrorCode(-1);
                    return giftCardRegisterResponseData;
                }
            }
            giftCardRegisterResponseData.setErrorCode(-1);
        }
        giftCardRegisterResponseData.setGiftCardEncryptedData(arrby);
        return giftCardRegisterResponseData;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public GiftCardRegisterResponseData getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        GiftCardRegisterResponseData giftCardRegisterResponseData = new GiftCardRegisterResponseData();
        if (zg == null) {
            Log.e("GiftCardPayProvider", "TAController is null");
            giftCardRegisterResponseData.setErrorCode(-1);
            return giftCardRegisterResponseData;
        }
        try {
            PlccTAController plccTAController = zg;
            plccTAController.setPlccServerCerts("GIFT", giftCardRegisterRequestData.getServerVerCert(), giftCardRegisterRequestData.getServerEncCert(), giftCardRegisterRequestData.getServerCaCert());
            PlccTAController plccTAController2 = zg;
            byte[] arrby = plccTAController2.addCard("GIFT", giftCardRegisterRequestData.getGiftCardData());
            if (arrby != null) {
                giftCardRegisterResponseData.setErrorCode(0);
            } else {
                giftCardRegisterResponseData.setErrorCode(-1);
            }
            giftCardRegisterResponseData.setGiftCardEncryptedData(arrby);
            return giftCardRegisterResponseData;
        }
        catch (PlccTAException plccTAException) {
            Log.c("GiftCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            giftCardRegisterResponseData.setErrorCode(-1);
            return giftCardRegisterResponseData;
        }
    }

    @Override
    public boolean getPayReadyState() {
        return true;
    }

    @Override
    protected c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    @Override
    protected c getReplenishmentRequestData() {
        return null;
    }

    public String getTaid() {
        return PlccTAController.getInstance().getTAInfo().getTAId();
    }

    @Override
    protected int getTransactionData(Bundle bundle, i i2) {
        return 0;
    }

    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        return new byte[0];
    }

    @Override
    protected void init() {
    }

    @Override
    protected void interruptMstPay() {
    }

    @Override
    protected void loadTA() {
        zg.loadTA();
        Log.i("GiftCardPayProvider", "load real TA");
    }

    @Override
    protected boolean prepareMstPay() {
        return true;
    }

    @Override
    public boolean prepareNfcPay() {
        return false;
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        return null;
    }

    @Override
    protected e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public SelectCardResult selectCard() {
        if (zg == null) {
            Log.e("GiftCardPayProvider", "TAController is null");
            return null;
        }
        try {
            return new SelectCardResult(this.getTaid(), zg.getNonce(32));
        }
        catch (PlccTAException plccTAException) {
            Log.c("GiftCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return null;
        }
    }

    @Override
    public void setCardTzEncData(byte[] arrby) {
        this.zh = arrby;
    }

    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        return false;
    }

    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        if (zg == null) {
            Log.e("GiftCardPayProvider", "TAController is null");
            return false;
        }
        try {
            boolean bl = zg.mstTransmit(this.zh, n2, arrby);
            return bl;
        }
        catch (InterruptedException interruptedException) {
            Log.c("GiftCardPayProvider", interruptedException.getMessage(), interruptedException);
            return false;
        }
        catch (PlccTAException plccTAException) {
            Log.c("GiftCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected void stopMstPay(boolean bl) {
        Log.i("GiftCardPayProvider", "stopMstPay: start ");
        if (zg == null) {
            Log.e("GiftCardPayProvider", "TAController is null");
            return;
        }
        try {
            zg.clearMstData();
        }
        catch (PlccTAException plccTAException) {
            Log.c("GiftCardPayProvider", plccTAException.getMessage(), (Throwable)((Object)plccTAException));
        }
        Log.i("GiftCardPayProvider", "stopMstPay: end ");
    }

    @Override
    protected Bundle stopNfcPay(int n2) {
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", (short)1);
        return bundle;
    }

    @Override
    protected void unloadTA() {
        zg.unloadTA();
        Log.i("GiftCardPayProvider", "unload real TA");
    }

    @Override
    protected e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }
}

