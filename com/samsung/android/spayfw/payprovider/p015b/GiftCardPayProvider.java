package com.samsung.android.spayfw.payprovider.p015b;

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
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.ExtractCardDetailResult;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController.TACerts;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.Utils;

/* renamed from: com.samsung.android.spayfw.payprovider.b.a */
public class GiftCardPayProvider extends PaymentNetworkProvider {
    private static ProviderTokenKey pj;
    private static PlccTAController zg;
    private byte[] zh;

    static {
        pj = null;
    }

    public GiftCardPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.zh = null;
        this.mContext = context;
        this.mTAController = PlccTAController.createOnlyInstance(this.mContext);
        zg = (PlccTAController) this.mTAController;
    }

    public GiftCardRegisterResponseData getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        GiftCardRegisterResponseData giftCardRegisterResponseData = new GiftCardRegisterResponseData();
        if (zg == null) {
            Log.m286e("GiftCardPayProvider", "TAController is null");
            giftCardRegisterResponseData.setErrorCode(-1);
        } else {
            try {
                PlccTAController plccTAController = zg;
                PlccTAController plccTAController2 = zg;
                plccTAController.setPlccServerCerts(PlccTAController.CARD_BRAND_GIFT, giftCardRegisterRequestData.getServerVerCert(), giftCardRegisterRequestData.getServerEncCert(), giftCardRegisterRequestData.getServerCaCert());
                plccTAController = zg;
                plccTAController2 = zg;
                TACerts allCerts = plccTAController.getAllCerts(PlccTAController.CARD_BRAND_GIFT);
                if (allCerts != null) {
                    giftCardRegisterResponseData.setDeviceSignCert(allCerts.signcert);
                    giftCardRegisterResponseData.setDeviceEncryptCert(allCerts.encryptcert);
                    giftCardRegisterResponseData.setDeviceDrk(allCerts.drk);
                    long am = Utils.am(this.mContext);
                    Log.m285d("GiftCardPayProvider", "Network Time = " + am);
                    plccTAController = zg;
                    PlccTAController plccTAController3 = zg;
                    byte[] utility_enc4Server_Transport = plccTAController.utility_enc4Server_Transport(PlccTAController.CARD_BRAND_GIFT, giftCardRegisterRequestData.getGiftCardData(), am);
                    if (utility_enc4Server_Transport != null) {
                        giftCardRegisterResponseData.setErrorCode(0);
                    } else {
                        giftCardRegisterResponseData.setErrorCode(-1);
                    }
                    giftCardRegisterResponseData.setGiftCardEncryptedData(utility_enc4Server_Transport);
                } else {
                    giftCardRegisterResponseData.setErrorCode(-1);
                }
            } catch (Throwable e) {
                Log.m284c("GiftCardPayProvider", e.getMessage(), e);
                giftCardRegisterResponseData.setErrorCode(-1);
            }
        }
        return giftCardRegisterResponseData;
    }

    public GiftCardRegisterResponseData getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        GiftCardRegisterResponseData giftCardRegisterResponseData = new GiftCardRegisterResponseData();
        if (zg == null) {
            Log.m286e("GiftCardPayProvider", "TAController is null");
            giftCardRegisterResponseData.setErrorCode(-1);
        } else {
            try {
                PlccTAController plccTAController = zg;
                PlccTAController plccTAController2 = zg;
                plccTAController.setPlccServerCerts(PlccTAController.CARD_BRAND_GIFT, giftCardRegisterRequestData.getServerVerCert(), giftCardRegisterRequestData.getServerEncCert(), giftCardRegisterRequestData.getServerCaCert());
                plccTAController = zg;
                plccTAController2 = zg;
                byte[] addCard = plccTAController.addCard(PlccTAController.CARD_BRAND_GIFT, giftCardRegisterRequestData.getGiftCardData());
                if (addCard != null) {
                    giftCardRegisterResponseData.setErrorCode(0);
                } else {
                    giftCardRegisterResponseData.setErrorCode(-1);
                }
                giftCardRegisterResponseData.setGiftCardEncryptedData(addCard);
            } catch (Throwable e) {
                Log.m284c("GiftCardPayProvider", e.getMessage(), e);
                giftCardRegisterResponseData.setErrorCode(-1);
            }
        }
        return giftCardRegisterResponseData;
    }

    public void setCardTzEncData(byte[] bArr) {
        this.zh = bArr;
    }

    public SelectCardResult selectCard() {
        if (zg == null) {
            Log.m286e("GiftCardPayProvider", "TAController is null");
            return null;
        }
        SelectCardResult selectCardResult;
        try {
            selectCardResult = new SelectCardResult(getTaid(), zg.getNonce(32));
        } catch (Throwable e) {
            Log.m284c("GiftCardPayProvider", e.getMessage(), e);
            selectCardResult = null;
        }
        return selectCardResult;
    }

    public String getTaid() {
        return PlccTAController.getInstance().getTAInfo().getTAId();
    }

    protected void clearCard() {
        this.zh = null;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        boolean z = false;
        if (zg == null) {
            Log.m286e("GiftCardPayProvider", "TAController is null");
        } else {
            try {
                z = zg.mstTransmit(this.zh, i, bArr);
            } catch (Throwable e) {
                Log.m284c("GiftCardPayProvider", e.getMessage(), e);
            } catch (Throwable e2) {
                Log.m284c("GiftCardPayProvider", e2.getMessage(), e2);
            }
        }
        return z;
    }

    protected void stopMstPay(boolean z) {
        Log.m287i("GiftCardPayProvider", "stopMstPay: start ");
        if (zg == null) {
            Log.m286e("GiftCardPayProvider", "TAController is null");
            return;
        }
        try {
            zg.clearMstData();
        } catch (Throwable e) {
            Log.m284c("GiftCardPayProvider", e.getMessage(), e);
        }
        Log.m287i("GiftCardPayProvider", "stopMstPay: end ");
    }

    protected GiftCardDetail extractGiftCardDetail(byte[] bArr, byte[] bArr2) {
        GiftCardDetail giftCardDetail = new GiftCardDetail();
        giftCardDetail.setErrorCode(-1);
        if (zg == null) {
            Log.m286e("GiftCardPayProvider", "TAController is null");
        } else {
            try {
                ExtractCardDetailResult extractGiftCardDetail = zg.extractGiftCardDetail(bArr, bArr2);
                if (extractGiftCardDetail != null && extractGiftCardDetail.getErrorCode() == 0) {
                    giftCardDetail.setCardnumber(extractGiftCardDetail.getCardnumber());
                    giftCardDetail.setPin(extractGiftCardDetail.getPin());
                    giftCardDetail.setBarcodeContent(extractGiftCardDetail.getBarcodeContent());
                    giftCardDetail.setErrorCode(0);
                }
            } catch (Throwable e) {
                Log.m284c("GiftCardPayProvider", e.getMessage(), e);
            } catch (Throwable e2) {
                Log.m284c("GiftCardPayProvider", e2.getMessage(), e2);
            }
        }
        return giftCardDetail;
    }

    protected boolean authenticateTransaction(SecuredObject securedObject) {
        boolean z = false;
        if (zg == null) {
            Log.m286e("GiftCardPayProvider", "TAController is null");
        } else {
            try {
                Log.m285d("GiftCardPayProvider", "Calling Plcc TA Controller Authenticate Transaction");
                z = zg.authenticateTransaction(securedObject.getSecureObjectData());
            } catch (Throwable e) {
                Log.m284c("GiftCardPayProvider", e.getMessage(), e);
            }
        }
        return z;
    }

    public boolean getPayReadyState() {
        return true;
    }

    protected boolean prepareMstPay() {
        return true;
    }

    protected void init() {
    }

    public void delete() {
    }

    protected CertificateInfo[] getDeviceCertificates() {
        return new CertificateInfo[0];
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        return false;
    }

    protected ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        return null;
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    protected ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        return null;
    }

    protected void interruptMstPay() {
    }

    public boolean prepareNfcPay() {
        return false;
    }

    public byte[] handleApdu(byte[] bArr, Bundle bundle) {
        return new byte[0];
    }

    protected Bundle stopNfcPay(int i) {
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", (short) 1);
        return bundle;
    }

    protected ProviderRequestData getReplenishmentRequestData() {
        return null;
    }

    protected ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    protected ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    protected int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        return 0;
    }

    protected TransactionDetails processTransactionData(Object obj) {
        return null;
    }

    protected void loadTA() {
        zg.loadTA();
        Log.m287i("GiftCardPayProvider", "load real TA");
    }

    protected void unloadTA() {
        zg.unloadTA();
        Log.m287i("GiftCardPayProvider", "unload real TA");
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }
}
