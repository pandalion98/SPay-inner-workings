package com.samsung.android.spayfw.payprovider.discover;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
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
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TokenReplenishAlarm;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.discover.accttxns.DcAcctTransactionsManager;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcCardMasterDaoImpl;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;
import com.samsung.android.spayfw.payprovider.discover.payment.DiscoverTransactionService;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.DcTokenManager;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAController;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.a */
public class DiscoverPayProvider extends PaymentNetworkProvider {
    private PaymentFrameworkRequester ps;
    private long se;
    private DcTokenManager sf;
    private DiscoverTransactionService sg;
    private DcAcctTransactionsManager sh;
    private DcTAController sj;
    private ProviderTokenKey sk;
    private CertificateInfo[] sl;
    private boolean sm;

    public DiscoverPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.se = 120000;
        this.sf = null;
        this.sg = null;
        this.sh = null;
        this.sj = null;
        this.sk = null;
        this.sl = null;
        this.sm = false;
        this.sf = new DcTokenManager(context);
        this.sg = new DiscoverTransactionService(context);
        this.sh = new DcAcctTransactionsManager(context);
        this.sj = DcTAController.m1039E(context);
        this.mTAController = this.sj;
        this.sk = this.mProviderTokenKey;
        Log.m285d("DCSDK_DiscoverPayProvider", "Key is null? " + (providerTokenKey == null ? "true" : "false"));
        Log.m285d("DCSDK_DiscoverPayProvider", "Key is null? " + (this.mProviderTokenKey == null ? "true" : "false"));
    }

    protected void init() {
        Log.m287i("DCSDK_DiscoverPayProvider", "init");
    }

    public void setPaymentFrameworkRequester(PaymentFrameworkRequester paymentFrameworkRequester) {
        this.ps = paymentFrameworkRequester;
    }

    public void delete() {
        Log.m287i("DCSDK_DiscoverPayProvider", "delete");
        this.sf.m1023d(this.mProviderTokenKey);
    }

    protected CertificateInfo[] getDeviceCertificates() {
        Log.m287i("DCSDK_DiscoverPayProvider", "getDeviceCertificates");
        return null;
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        Log.m287i("DCSDK_DiscoverPayProvider", "setServerCertificates");
        Log.m285d("DCSDK_DiscoverPayProvider", "setServerCertificates: size: " + certificateInfoArr.length);
        for (CertificateInfo certificateInfo : certificateInfoArr) {
            Log.m285d("DCSDK_DiscoverPayProvider", "Usage: " + certificateInfo.getUsage() + ", Alias: " + certificateInfo.getAlias());
        }
        this.sl = certificateInfoArr;
        return true;
    }

    protected ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        Log.m287i("DCSDK_DiscoverPayProvider", "getEnrollmentRequestData");
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            Log.m287i("DCSDK_DiscoverPayProvider", "Regular Enrollment");
            return this.sf.m1016a((EnrollCardPanInfo) enrollCardInfo, billingInfo, m824J(1));
        } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
            Log.m287i("DCSDK_DiscoverPayProvider", "Push Enrollemnt");
            r0 = this.sf.m1021b((EnrollCardReferenceInfo) enrollCardInfo);
            Log.m287i("DCSDK_DiscoverPayProvider", "Provider Req Data: " + r0.ch().toString());
            return r0;
        } else {
            Log.m286e("DCSDK_DiscoverPayProvider", "EnrollCardInfo is of Invalid type" + enrollCardInfo.getEnrollType());
            r0 = new ProviderRequestData();
            r0.m822a(null);
            r0.setErrorCode(-4);
            return r0;
        }
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        Log.m287i("DCSDK_DiscoverPayProvider", "getProvisionRequestData");
        return this.sf.m1022c(provisionTokenInfo);
    }

    protected ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        Log.m287i("DCSDK_DiscoverPayProvider", "createToken");
        return this.sf.m1019a(str, providerRequestData.ch(), i, m824J(2));
    }

    public String encryptUserSignature(byte[] bArr) {
        if (bArr != null) {
            return this.sf.m1024j(bArr);
        }
        Log.m286e("DCSDK_DiscoverPayProvider", "encryptUserSignature : input data null");
        return null;
    }

    public byte[] decryptUserSignature(String str) {
        if (str != null) {
            return this.sf.aO(str);
        }
        Log.m286e("DCSDK_DiscoverPayProvider", "decryptUserSignature : input data null");
        return null;
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        Log.m287i("DCSDK_DiscoverPayProvider", "isReplenishDataAvailable");
        return this.sf.isReplenishDataAvailable(jsonObject);
    }

    protected ProviderRequestData getReplenishmentRequestData() {
        Log.m287i("DCSDK_DiscoverPayProvider", "getReplenishmentRequestData");
        return this.sf.m1017a(this.mProviderTokenKey, m824J(1));
    }

    protected ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        Log.m287i("DCSDK_DiscoverPayProvider", "replenishToken");
        ProviderResponseData a = this.sf.m1020a(this.mProviderTokenKey.getTrTokenId(), this.mProviderTokenKey, jsonObject, tokenStatus, m824J(2));
        if (a.getErrorCode() == 0) {
            cE();
        }
        return a;
    }

    protected ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        Log.m287i("DCSDK_DiscoverPayProvider", "updateTokenStatus");
        return this.sf.m1018a(this.mProviderTokenKey, jsonObject, tokenStatus);
    }

    protected int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        Log.m287i("DCSDK_DiscoverPayProvider", "getTransactionData");
        return this.sh.m852a(this.mProviderTokenKey.cm(), bundle, transactionResponse);
    }

    protected TransactionDetails processTransactionData(Object obj) {
        Log.m287i("DCSDK_DiscoverPayProvider", "processTransactionData");
        return this.sh.m853a(this.mProviderTokenKey.cm(), obj, m824J(2));
    }

    public boolean getPayReadyState() {
        this.sk = this.mProviderTokenKey;
        boolean s = this.sg.m996s(this.sk.cm());
        Log.m287i("DCSDK_DiscoverPayProvider", "getPayReadyState: " + s);
        cD();
        return s;
    }

    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
        Log.m287i("DCSDK_DiscoverPayProvider", "checkIfReplenishmentNeeded");
        this.sk = this.mProviderTokenKey;
        cD();
    }

    public void setupReplenishAlarm() {
        Log.m287i("DCSDK_DiscoverPayProvider", "setupReplenishAlarm");
        this.sk = this.mProviderTokenKey;
        cD();
    }

    protected void replenishAlarmExpired() {
        Log.m287i("DCSDK_DiscoverPayProvider", "replenishAlarmExpired");
        this.sk = this.mProviderTokenKey;
        cE();
        cF();
        cD();
    }

    protected void loadTA() {
        Log.m287i("DCSDK_DiscoverPayProvider", "loadTA: start, timestamp " + System.currentTimeMillis());
        Log.m287i("DCSDK_DiscoverPayProvider", "loadTA: end, result " + this.sj.loadTA() + ", timestamp " + System.currentTimeMillis());
    }

    protected void unloadTA() {
        Log.m287i("DCSDK_DiscoverPayProvider", "unloadTA: start, timestamp " + System.currentTimeMillis());
        this.sj.unloadTA();
        Log.m287i("DCSDK_DiscoverPayProvider", "unloadTA: unloadTA end, timestamp " + System.currentTimeMillis());
    }

    public void updateRequestStatus(ProviderRequestStatus providerRequestStatus) {
        Log.m287i("DCSDK_DiscoverPayProvider", "updateRequestStatus: Req Type - " + providerRequestStatus.getRequestType() + ", RequestStatus - " + providerRequestStatus.ci());
        switch (providerRequestStatus.getRequestType()) {
            case CertStatus.UNREVOKED /*11*/:
                cE();
                if (providerRequestStatus.ci() != 0) {
                    Log.m286e("DCSDK_DiscoverPayProvider", "Replenishment Request Failed");
                    cF();
                    aE(providerRequestStatus.ck().cn());
                    return;
                }
                this.se = 120000;
                Log.m287i("DCSDK_DiscoverPayProvider", "Replenishment successful. Set mReplenishmentRetryWait to DEFAULT_REPLENISH_RETRY_DELTA");
                this.ps.m312b(providerRequestStatus.ck());
            default:
        }
    }

    protected SelectCardResult selectCard() {
        Log.m287i("DCSDK_DiscoverPayProvider", "selectCard: start, timestamp " + System.currentTimeMillis());
        if (this.mProviderTokenKey == null) {
            Log.m286e("DCSDK_DiscoverPayProvider", "selectCard: wrong token - null.");
            return null;
        }
        long cm = this.mProviderTokenKey.cm();
        Log.m285d("DCSDK_DiscoverPayProvider", "selectCard:  load card by token key, key = " + cm);
        SelectCardResult q = this.sg.m995q(cm);
        if (q == null) {
            Log.m286e("DCSDK_DiscoverPayProvider", "selectCard: cannot select card for token " + cm);
        }
        Log.m287i("DCSDK_DiscoverPayProvider", "selectCard: end , timestamp " + System.currentTimeMillis());
        return q;
    }

    protected boolean authenticateTransaction(SecuredObject securedObject) {
        Log.m287i("DCSDK_DiscoverPayProvider", "authenticateTransaction: start , timestamp " + System.currentTimeMillis());
        if (securedObject == null) {
            Log.m286e("DCSDK_DiscoverPayProvider", "authenticateTransaction: can't authenticate transaction, secure object is null, returned false.");
            return false;
        }
        boolean a = this.sg.m993a(securedObject, DiscoverCDCVM.aG(getAuthType()));
        if (!a) {
            Log.m286e("DCSDK_DiscoverPayProvider", "authenticateTransaction: authenticateTransaction failed.");
        }
        Log.m287i("DCSDK_DiscoverPayProvider", "authenticateTransaction:  end, result " + a + ", timestamp " + System.currentTimeMillis());
        return a;
    }

    protected void clearCard() {
        Log.m287i("DCSDK_DiscoverPayProvider", "clearCard: start, timestamp " + System.currentTimeMillis());
        this.sg.clearCard();
        cD();
        Log.m287i("DCSDK_DiscoverPayProvider", "clearCard:  end, timestamp " + System.currentTimeMillis());
    }

    public void beginPay(boolean z, boolean z2) {
        Log.m287i("DCSDK_DiscoverPayProvider", "prepareNfcPay: start, timestamp " + System.currentTimeMillis());
        boolean prepareNfcPay = this.sg.prepareNfcPay();
        if (!prepareNfcPay) {
            Log.m286e("DCSDK_DiscoverPayProvider", "prepareNfcPay: init nfc transaction failed.");
        }
        Log.m287i("DCSDK_DiscoverPayProvider", "prepareNfcPay:  end, result " + prepareNfcPay + ", timestamp " + System.currentTimeMillis());
    }

    protected boolean prepareNfcPay() {
        return true;
    }

    protected byte[] handleApdu(byte[] bArr, Bundle bundle) {
        Log.m287i("DCSDK_DiscoverPayProvider", "handleApdu: start, timestamp " + System.currentTimeMillis());
        if (bArr == null) {
            Log.m286e("DCSDK_DiscoverPayProvider", "handleApdu: can't process apdu, apdu buffer is null, return null.");
            return null;
        } else if (bArr.length == 0) {
            Log.m286e("DCSDK_DiscoverPayProvider", "handleApdu: can't process apdu, apdu buffer is empty, return null.");
            return null;
        } else {
            byte[] h = this.sg.m994h(bArr);
            Log.m287i("DCSDK_DiscoverPayProvider", "handleApdu: end, timestamp " + System.currentTimeMillis());
            return h;
        }
    }

    protected Bundle stopNfcPay(int i) {
        Log.m287i("DCSDK_DiscoverPayProvider", "stopNfcPay: start, reason " + i + ", timestamp " + System.currentTimeMillis());
        short K = this.sg.m992K(i);
        Log.m287i("DCSDK_DiscoverPayProvider", "stopNfcPay: end, result " + K + ", timestamp " + System.currentTimeMillis());
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", K);
        return bundle;
    }

    protected boolean prepareMstPay() {
        Log.m287i("DCSDK_DiscoverPayProvider", "prepareMstPay: start, timestamp " + System.currentTimeMillis());
        boolean prepareMstPay = this.sg.prepareMstPay();
        if (!prepareMstPay) {
            Log.m286e("DCSDK_DiscoverPayProvider", "prepareMstPay: mst tracks data preparation failed ");
        }
        Log.m287i("DCSDK_DiscoverPayProvider", "prepareMstPay: end, result " + prepareMstPay + ", timestamp " + System.currentTimeMillis());
        return prepareMstPay;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        Log.m287i("DCSDK_DiscoverPayProvider", "startMstPay: start, timestamp " + System.currentTimeMillis());
        boolean startMstPay = this.sg.startMstPay(i, bArr);
        if (!startMstPay) {
            Log.m286e("DCSDK_DiscoverPayProvider", "prepareMstPay: mst transmission failed ");
        }
        Log.m287i("DCSDK_DiscoverPayProvider", "startMstPay: end, timestamp" + System.currentTimeMillis() + ", result " + startMstPay);
        return startMstPay;
    }

    protected void interruptMstPay() {
        Log.m287i("DCSDK_DiscoverPayProvider", "interruptMstPay: start, timestamp " + System.currentTimeMillis());
        this.sg.interruptMstPay();
        Log.m287i("DCSDK_DiscoverPayProvider", "interruptMstPay: end, timestamp " + System.currentTimeMillis());
    }

    protected void stopMstPay(boolean z) {
        Log.m287i("DCSDK_DiscoverPayProvider", "stopMstPay: start, timestamp " + System.currentTimeMillis() + "mstTransmissionStatus " + z);
        this.sg.stopMstPay(z);
        Log.m287i("DCSDK_DiscoverPayProvider", "stopMstPay:  end, timestamp " + System.currentTimeMillis());
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        Log.m287i("DCSDK_DiscoverPayProvider", "generateInAppPaymentPayload: start, timestamp " + System.currentTimeMillis());
        byte[] generateInAppPaymentPayload = this.sg.generateInAppPaymentPayload(inAppDetailedTransactionInfo);
        Log.m287i("DCSDK_DiscoverPayProvider", "generateInAppPaymentPayload: end, timestamp " + System.currentTimeMillis());
        return generateInAppPaymentPayload;
    }

    public static synchronized Context cC() {
        Context aB;
        synchronized (DiscoverPayProvider.class) {
            aB = PaymentFrameworkApp.aB();
        }
        return aB;
    }

    private boolean cD() {
        DcCardMaster dcCardMaster;
        long remainingOtpkCount;
        boolean z;
        Log.m287i("DCSDK_DiscoverPayProvider", "checkAndTriggerReplenishment...");
        try {
            dcCardMaster = (DcCardMaster) new DcCardMasterDaoImpl(this.mContext).getData(this.mProviderTokenKey.cm());
        } catch (DcDbException e) {
            e.printStackTrace();
            dcCardMaster = null;
        } catch (NullPointerException e2) {
            e2.printStackTrace();
            Log.m286e("DCSDK_DiscoverPayProvider", "Nullpointer Exception in checkAndTriggerReplenishment");
            return false;
        }
        if (dcCardMaster != null) {
            remainingOtpkCount = dcCardMaster.getRemainingOtpkCount();
            long replenishmentThreshold = dcCardMaster.getReplenishmentThreshold();
            Log.m285d("DCSDK_DiscoverPayProvider", "checkAndTriggerReplenishment: otpkBal - " + remainingOtpkCount + ", Threshold - " + replenishmentThreshold);
            if (remainingOtpkCount <= replenishmentThreshold) {
                String tokenId = dcCardMaster.getTokenId();
                Log.m285d("DCSDK_DiscoverPayProvider", "Requesting Replenishment: " + tokenId);
                aE(tokenId);
            } else {
                Log.m285d("DCSDK_DiscoverPayProvider", "Requesting Replenishment: No need for replenishment");
                cE();
            }
        } else {
            Log.m285d("DCSDK_DiscoverPayProvider", "Requesting Replenishment: cardMaster is null");
            cE();
            remainingOtpkCount = 0;
        }
        if (remainingOtpkCount > 0) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    private boolean aE(String str) {
        Log.m287i("DCSDK_DiscoverPayProvider", "startReplenishAlarm: " + this.sm);
        if (!this.sm) {
            ProviderTokenKey providerTokenKey = new ProviderTokenKey(str);
            providerTokenKey.setTrTokenId(str);
            TokenReplenishAlarm.m1070a(this.mContext, this.se + Utils.am(this.mContext), providerTokenKey);
            this.ps.m311a(this.mProviderTokenKey);
            this.sm = true;
            Log.m285d("DCSDK_DiscoverPayProvider", "startReplenishAlarm: started alarm - " + this.se);
        }
        return this.sm;
    }

    private void cE() {
        Log.m287i("DCSDK_DiscoverPayProvider", "cancelReplenishmentAlarm: " + this.sm);
        if (this.sm) {
            TokenReplenishAlarm.m1071a(this.mContext, this.mProviderTokenKey);
            this.sm = false;
        }
    }

    private void cF() {
        this.se += 120000;
        if (this.se > 1200000) {
            this.se = 1200000;
        }
    }

    private byte[] aF(String str) {
        Log.m285d("DCSDK_DiscoverPayProvider", "getDiscoverCert with usage type : " + str);
        for (CertificateInfo certificateInfo : this.sl) {
            if (certificateInfo.getContent() != null && str.equals(certificateInfo.getUsage())) {
                return Base64.decode(certificateInfo.getContent().replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR).getBytes(), 0);
            }
        }
        Log.m286e("DCSDK_DiscoverPayProvider", "getDiscoverCert: No Cert Found for usage type " + str);
        return null;
    }

    public List<byte[]> m824J(int i) {
        if (i == 1 || i == 2) {
            Object aF = aF(CertificateInfo.CERT_USAGE_ENC);
            Object aF2 = aF(CertificateInfo.CERT_USAGE_CA);
            Object aF3 = aF(CertificateInfo.CERT_USAGE_SIG);
            if (aF == null || aF2 == null || aF3 == null) {
                Log.m286e("DCSDK_DiscoverPayProvider", "getDiscoverCertChain: One of the Server cert is null");
                return null;
            }
            List<byte[]> arrayList = new ArrayList();
            arrayList.add(0, aF2);
            if (1 == i) {
                arrayList.add(1, aF);
            } else if (2 == i) {
                arrayList.add(1, aF3);
            } else {
                Log.m286e("DCSDK_DiscoverPayProvider", "getDiscoverCertChain: Invalid usage");
                return null;
            }
            return arrayList;
        }
        Log.m286e("DCSDK_DiscoverPayProvider", "getDiscoverCertChain: Invalid usage");
        return null;
    }

    public Bundle isTransactionComplete() {
        return this.sg.isTransactionComplete();
    }
}
