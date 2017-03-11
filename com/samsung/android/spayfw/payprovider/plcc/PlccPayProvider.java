package com.samsung.android.spayfw.payprovider.plcc;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.PayConfig;
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
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.payprovider.plcc.util.SequenceUtils;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class PlccPayProvider extends PaymentNetworkProvider {
    private static final String TAG = "PlccPayProvider";
    private static PlccTAController mPlccTac;
    private Context mContext;
    private PlccPayProviderSdk mSdk;

    static {
        mPlccTac = null;
    }

    public PlccPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.mContext = context;
        this.mTAController = PlccTAController.createOnlyInstance(this.mContext);
        mPlccTac = (PlccTAController) this.mTAController;
        this.mSdk = new PlccPayProviderSdk(context);
    }

    public boolean authenticateTransaction(SecuredObject securedObject) {
        return this.mSdk.authenticateTransaction(securedObject.getSecureObjectData());
    }

    public ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setProviderTokenKey(new ProviderTokenKey(str));
        this.mSdk.addCard(str, str, providerRequestData.ch());
        return providerResponseData;
    }

    public ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (enrollCardInfo == null || billingInfo == null) {
            providerRequestData.setErrorCode(-4);
            Log.m286e(TAG, "getEnrollmentRequestData: invalid input ");
            return providerRequestData;
        }
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            providerRequestData.m822a(buildJsonFromCard((EnrollCardPanInfo) enrollCardInfo, billingInfo));
        }
        Bundle bundle = new Bundle();
        if (enrollCardInfo.getUserEmail() != null) {
            bundle.putString("emailHash", getEmailAddressHash(enrollCardInfo.getUserEmail()));
        }
        providerRequestData.m823e(bundle);
        return providerRequestData;
    }

    private JsonObject buildJsonFromCard(EnrollCardPanInfo enrollCardPanInfo, BillingInfo billingInfo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pan", enrollCardPanInfo.getPAN());
        jsonObject.addProperty(ActivationData.CARD_INFO_CVV, enrollCardPanInfo.getCVV());
        Log.m285d(TAG, "debug cvv = " + enrollCardPanInfo.getCVV());
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("encrypted", this.mSdk.utilityEnc4ServerTransport(jsonObject.toString()));
        return jsonObject2;
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    public ProviderRequestData getReplenishmentRequestData() {
        return null;
    }

    public byte[] handleApdu(byte[] bArr, Bundle bundle) {
        return null;
    }

    public boolean prepareMstPay() {
        return true;
    }

    public ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    public SelectCardResult selectCard() {
        if (this.mProviderTokenKey == null) {
            return null;
        }
        String cn = this.mProviderTokenKey.cn();
        SelectCardResult selectCardResult = new SelectCardResult(this.mSdk.getTaid(cn), this.mSdk.getNonce(32));
        this.mSdk.selectCard(cn);
        return selectCardResult;
    }

    public void clearCard() {
        this.mSdk.clearCard();
    }

    public CertificateInfo[] getDeviceCertificates() {
        HashMap deviceCertificates = this.mSdk.getDeviceCertificates();
        if (deviceCertificates == null) {
            return null;
        }
        String convertToPem = Util.convertToPem((byte[]) deviceCertificates.get(PlccConstants.CERT_ENC));
        String convertToPem2 = Util.convertToPem((byte[]) deviceCertificates.get(PlccConstants.CERT_SIGN));
        String convertToPem3 = Util.convertToPem((byte[]) deviceCertificates.get(PlccConstants.CERT_CA));
        if (convertToPem == null || convertToPem2 == null || convertToPem3 == null) {
            return null;
        }
        CertificateInfo[] certificateInfoArr = new CertificateInfo[3];
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setAlias(CertificateInfo.CERT_USAGE_ENC);
        certificateInfo.setContent(convertToPem);
        certificateInfo.setUsage(CertificateInfo.CERT_USAGE_ENC);
        certificateInfoArr[0] = certificateInfo;
        certificateInfo = new CertificateInfo();
        certificateInfo.setAlias(CertificateInfo.CERT_USAGE_SIG);
        certificateInfo.setContent(convertToPem2);
        certificateInfo.setUsage(CertificateInfo.CERT_USAGE_SIG);
        certificateInfoArr[1] = certificateInfo;
        certificateInfo = new CertificateInfo();
        certificateInfo.setAlias(CertificateInfo.CERT_USAGE_CA);
        certificateInfo.setContent(convertToPem3);
        certificateInfo.setUsage(CertificateInfo.CERT_USAGE_CA);
        certificateInfoArr[2] = certificateInfo;
        return certificateInfoArr;
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        byte[] bArr = null;
        if (certificateInfoArr == null || certificateInfoArr.length == 0) {
            return false;
        }
        byte[] bArr2 = null;
        byte[] bArr3 = null;
        for (CertificateInfo certificateInfo : certificateInfoArr) {
            String replace = certificateInfo.getContent().replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR);
            if (CertificateInfo.CERT_USAGE_ENC.equals(certificateInfo.getUsage())) {
                bArr3 = Base64.decode(replace, 0);
            } else if (CertificateInfo.CERT_USAGE_VER.equals(certificateInfo.getUsage())) {
                bArr2 = Base64.decode(replace, 0);
            } else if (CertificateInfo.CERT_USAGE_CA.equals(certificateInfo.getUsage())) {
                bArr = Base64.decode(replace, 0);
            }
        }
        if (bArr3 == null || bArr2 == null || bArr == null) {
            return false;
        }
        this.mSdk.setPlccServerCerts(bArr2, bArr3, bArr);
        return true;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        try {
            Log.m287i(TAG, "Mstpayconfig length = " + bArr.length);
            return this.mSdk.startMstTransmit(i, bArr);
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return false;
        }
    }

    public ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        ProviderResponseData providerResponseData = new ProviderResponseData();
        if (this.mSdk == null || this.mProviderTokenKey == null) {
            return null;
        }
        this.mSdk.updateCard(this.mProviderTokenKey.cn(), tokenStatus.getCode());
        providerResponseData.setProviderTokenKey(this.mProviderTokenKey);
        return providerResponseData;
    }

    public PayConfig getPayConfig() {
        PayConfig payConfig = null;
        if (this.mProviderTokenKey == null) {
            return payConfig;
        }
        String cn = this.mProviderTokenKey.cn();
        PayConfig payConfig2 = new PayConfig();
        try {
            payConfig2.setMstPayConfig(SequenceUtils.buildSequenceFromString(this.mSdk.getPayConfig(cn)));
            return payConfig2;
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return payConfig;
        }
    }

    public int getPayConfigTransmitTime(boolean z) {
        return 23;
    }

    public void stopMstPay(boolean z) {
        this.mSdk.stopMstTransmit();
    }

    public boolean prepareNfcPay() {
        return false;
    }

    public Bundle stopNfcPay(int i) {
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", (short) 1);
        return bundle;
    }

    public void updateSequenceConfig(HashMap<String, String> hashMap) {
        this.mSdk.updateSequenceConfig(hashMap);
    }

    protected void loadTA() {
        this.mSdk.loadTA();
    }

    protected void unloadTA() {
        this.mSdk.unloadTA();
    }

    private String getEmailAddressHash(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            Log.m285d(TAG, "getEmailAddressHash: digest " + Arrays.toString(digest));
            String encodeToString = Base64.encodeToString(digest, 11);
            Log.m285d(TAG, "getEmailAddressHash: emailAddress " + str + " Hash: " + encodeToString);
            return encodeToString;
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return null;
        }
    }

    public int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        return 0;
    }

    protected TransactionDetails processTransactionData(Object obj) {
        return null;
    }

    protected void init() {
    }

    public void delete() {
        if (this.mProviderTokenKey != null) {
            this.mSdk.removeCard(this.mProviderTokenKey.cn());
        } else {
            Log.m285d(TAG, "Provider Key null");
        }
    }

    protected void interruptMstPay() {
    }

    public boolean getPayReadyState() {
        return true;
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }

    public String getMerchantId() {
        String str = null;
        try {
            for (PlccCard plccCard : this.mSdk.listCard()) {
                String merchantId;
                if (Objects.equals(plccCard.getProviderKey(), this.mProviderTokenKey.cn())) {
                    merchantId = plccCard.getMerchantId();
                } else {
                    merchantId = str;
                }
                str = merchantId;
            }
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
        }
        return str;
    }

    public String encryptUserSignature(byte[] bArr) {
        String str = null;
        if (bArr != null) {
            try {
                byte[] storeData = mPlccTac.storeData(bArr);
                if (storeData != null) {
                    str = Base64.encodeToString(storeData, 2);
                }
            } catch (Throwable e) {
                Log.m286e(TAG, "encryptUserSignature Error occured while gettting encypted data from TA");
                Log.m284c(TAG, e.getMessage(), e);
            }
        }
        return str;
    }

    public byte[] decryptUserSignature(String str) {
        byte[] bArr = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                bArr = mPlccTac.retrieveFromStorage(Base64.decode(str, 2));
            } catch (Throwable e) {
                Log.m286e(TAG, "decryptUserSignature Error occured while gettting decrypted data from TA");
                Log.m284c(TAG, e.getMessage(), e);
            }
        }
        return bArr;
    }
}
