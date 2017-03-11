package com.samsung.android.spayfw.payprovider.mastercard;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionService;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

public class McProvider extends PaymentNetworkProvider {
    private static final String JSON_KEY_ACTION = "action";
    private static final String JSON_KEY_EXTRA_TEXT = "extraTextValue";
    private static final String JSON_KEY_PKGNAME = "packageName";
    private static final String TAG = "McProvider";
    public static final String TDS_TAG_ERROR = "e_";
    public static final String TDS_TAG_INFO = "i_";
    private static boolean initCleanupDone;
    private static String mEmailHash;
    private McTokenManager mMcTokenManager;
    private MCTransactionService mTransactionService;

    public McProvider(Context context, String str) {
        super(context, str);
        McTAController.createOnlyInstance(context);
        this.mTAController = McTAController.getInstance();
        this.mMcTokenManager = new McTokenManager(context);
        this.mTransactionService = new MCTransactionService(context);
    }

    public McProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        this(context, str);
        initCleanup(context);
        if (providerTokenKey != null) {
            McTdsManager.getInstance(providerTokenKey.cm());
        }
    }

    public static synchronized Context getContext() {
        Context aB;
        synchronized (McProvider.class) {
            aB = PaymentFrameworkApp.aB();
        }
        return aB;
    }

    public CertificateInfo[] getDeviceCertificates() {
        return null;
    }

    public void updateRequestStatus(ProviderRequestStatus providerRequestStatus) {
        if (providerRequestStatus != null) {
            Log.m287i(TAG, "updateRequestStatus Status :" + providerRequestStatus.ci() + " Type:" + providerRequestStatus.getRequestType());
            if (providerRequestStatus.ci() == 0 && providerRequestStatus.getRequestType() == 3 && providerRequestStatus.ck() != null && providerRequestStatus.ck().cn() != null) {
                Log.m287i(TAG, "updateRequestStatus Provision Complete");
                this.mMcTokenManager.setUniqueTokenReference(providerRequestStatus.ck().cn());
                return;
            }
            return;
        }
        Log.m286e(TAG, "ProviderRequestStatus is null");
    }

    public ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        ProviderRequestData enrollmentMcData = this.mMcTokenManager.getEnrollmentMcData(enrollCardInfo, billingInfo);
        if (enrollmentMcData.getErrorCode() != 0) {
            Log.m286e(TAG, "Failed to create Enrollment Request" + enrollmentMcData.getErrorCode());
        } else {
            Bundle bundle = new Bundle();
            if (enrollCardInfo.getUserEmail() != null) {
                mEmailHash = getEmailAddressHash(enrollCardInfo.getUserEmail());
                bundle.putString("emailHash", mEmailHash);
            }
            bundle.putString("appId", getAppId());
            enrollmentMcData.m823e(bundle);
        }
        return enrollmentMcData;
    }

    public byte[] handleApdu(byte[] bArr, Bundle bundle) {
        return this.mTransactionService.processApdu(bArr, bundle);
    }

    public SelectCardResult selectCard() {
        return this.mTransactionService.selectCard(this.mProviderTokenKey);
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        return this.mMcTokenManager.updateMcCerts(certificateInfoArr);
    }

    public ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        return this.mMcTokenManager.provisionToken(providerRequestData.ch(), i);
    }

    public ProviderRequestData getReplenishmentRequestData() {
        return null;
    }

    public ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return this.mMcTokenManager.updateTokenData(this.mProviderTokenKey, jsonObject, tokenStatus);
    }

    public ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    public boolean authenticateTransaction(SecuredObject securedObject) {
        return this.mTransactionService.authenticateTransaction(securedObject);
    }

    public boolean prepareMstPay() {
        return this.mTransactionService.prepareMstPay();
    }

    public boolean startMstPay(int i, byte[] bArr) {
        return this.mTransactionService.startMstPay(i, bArr);
    }

    public void stopMstPay(boolean z) {
        this.mTransactionService.stopMstPay();
    }

    public void clearCard() {
        Log.m287i(TAG, "clearCard");
        this.mTransactionService.clearCard();
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return this.mMcTokenManager.getProvisionRequestData(provisionTokenInfo);
    }

    public void delete() {
        this.mMcTokenManager.deleteToken(this.mProviderTokenKey);
    }

    public boolean prepareNfcPay() {
        return true;
    }

    public Bundle stopNfcPay(int i) {
        Log.m287i(TAG, "stopNfcPay : " + i);
        return this.mTransactionService.stopNfcPay(i);
    }

    protected void loadTA() {
        this.mTransactionService.loadTA();
    }

    protected void unloadTA() {
        this.mTransactionService.unloadTA();
    }

    private String getEmailAddressHash(String str) {
        return CryptoUtils.convertbyteToHexString(CryptoUtils.getAccountIdHash(str));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.samsung.android.spayfw.payprovider.CasdParameters getCasdParameters() {
        /*
        r6 = this;
        r2 = 0;
        r0 = com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController.getInstance();	 Catch:{ Exception -> 0x0031 }
        r6.mTAController = r0;	 Catch:{ Exception -> 0x0031 }
        r0 = r6.mTAController;	 Catch:{ Exception -> 0x0031 }
        r0 = (com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController) r0;	 Catch:{ Exception -> 0x0031 }
        r0.loadTA();	 Catch:{ Exception -> 0x0043, all -> 0x003f }
        r3 = r0.getCasdParameters();	 Catch:{ Exception -> 0x0043, all -> 0x003f }
        if (r3 == 0) goto L_0x004e;
    L_0x0014:
        r1 = new com.samsung.android.spayfw.payprovider.a;	 Catch:{ Exception -> 0x0043, all -> 0x003f }
        r1.<init>();	 Catch:{ Exception -> 0x0043, all -> 0x003f }
        r2 = r3.hpk;	 Catch:{ Exception -> 0x0049, all -> 0x003f }
        r4 = 2;
        r2 = android.util.Base64.encodeToString(r2, r4);	 Catch:{ Exception -> 0x0049, all -> 0x003f }
        r1.ao(r2);	 Catch:{ Exception -> 0x0049, all -> 0x003f }
        r2 = r3.huid;	 Catch:{ Exception -> 0x0049, all -> 0x003f }
        r3 = 2;
        r2 = android.util.Base64.encodeToString(r2, r3);	 Catch:{ Exception -> 0x0049, all -> 0x003f }
        r1.ap(r2);	 Catch:{ Exception -> 0x0049, all -> 0x003f }
    L_0x002d:
        r6.unloadMcTAController(r0);
    L_0x0030:
        return r1;
    L_0x0031:
        r0 = move-exception;
        r1 = r2;
    L_0x0033:
        r0.printStackTrace();	 Catch:{ all -> 0x003a }
        r6.unloadMcTAController(r2);
        goto L_0x0030;
    L_0x003a:
        r0 = move-exception;
    L_0x003b:
        r6.unloadMcTAController(r2);
        throw r0;
    L_0x003f:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
        goto L_0x003b;
    L_0x0043:
        r1 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x0033;
    L_0x0049:
        r2 = move-exception;
        r5 = r2;
        r2 = r0;
        r0 = r5;
        goto L_0x0033;
    L_0x004e:
        r1 = r2;
        goto L_0x002d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.McProvider.getCasdParameters():com.samsung.android.spayfw.payprovider.a");
    }

    private String getAppId() {
        return "SAMSUNGPAY1";
    }

    public boolean setCasdCertificate(String str) {
        boolean z = false;
        McTAController mcTAController = null;
        try {
            mcTAController = McTAController.getInstance();
            mcTAController.loadTA();
            if (mcTAController.writeCasdCert(Base64.decode(str, 2)) == 0) {
                z = true;
                return z;
            }
            unloadMcTAController(mcTAController);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            unloadMcTAController(mcTAController);
        }
    }

    public int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        int i = -2;
        if (this.mProviderTokenKey == null) {
            Log.m286e(McUtils.TAG_TDS, "providerTokenKey is null..");
        } else {
            try {
                i = McTdsManager.getInstance(this.mProviderTokenKey.cm()).getTransactionData(bundle, transactionResponse);
            } catch (NullPointerException e) {
                Log.m286e(McUtils.TAG_TDS, "getTransactionData: NPE occured: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return i;
    }

    protected TransactionDetails processTransactionData(Object obj) {
        Log.m287i(McUtils.TAG_TDS, "processTransactionData: ");
        if (obj != null && (obj instanceof TransactionDetails)) {
            return (TransactionDetails) obj;
        }
        Log.m286e(McUtils.TAG_TDS, "processTransactionData: Invalid data pased");
        return null;
    }

    public static String getEmailHash() {
        return mEmailHash;
    }

    protected void init() {
    }

    protected void interruptMstPay() {
        Log.m285d(TAG, "MCProvider: interruptMstPay");
        this.mTransactionService.interruptMstPay();
    }

    public boolean getPayReadyState() {
        return true;
    }

    private static synchronized void initCleanup(Context context) {
        synchronized (McProvider.class) {
            if (!initCleanupDone) {
                Log.m287i(TAG, "initCleanup: performing one time cleanup operations");
                long deleteStaleEnrollmentData = McTokenManager.deleteStaleEnrollmentData(context);
                if (deleteStaleEnrollmentData > 0) {
                    Log.m287i(TAG, "initCleanup: deleted entries: " + deleteStaleEnrollmentData);
                }
                initCleanupDone = true;
            }
        }
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        McPayDataHandler mcPayDataHandler = new McPayDataHandler();
        DSRPOutputData payInfoData = this.mTransactionService.getPayInfoData(mcPayDataHandler.convertToDsrpInput(inAppDetailedTransactionInfo));
        if (payInfoData != null) {
            return mcPayDataHandler.convertToPaymentGatewayFormat(inAppDetailedTransactionInfo.cd(), inAppDetailedTransactionInfo.getNonce(), payInfoData);
        }
        Log.m286e(TAG, "Failed to create dsrpOutput !!!");
        throw new PaymentProviderException(-36);
    }

    public boolean isPayAllowedForPresentationMode(int i) {
        if (i == 2) {
            return isMstSupported(this.mProviderTokenKey);
        }
        return true;
    }

    public boolean isMstSupported(ProviderTokenKey providerTokenKey) {
        return this.mTransactionService.isMSTSupportedByProfile(providerTokenKey);
    }

    public boolean isDsrpBlobMissing() {
        boolean z = !this.mTransactionService.isDSRPSupportedByProfile(this.mProviderTokenKey);
        Log.m285d(TAG, "isDsrpBlobMissing: mcprovider: ret = " + z);
        return z;
    }

    public void reconstructMissingDsrpBlob() {
        Log.m285d(TAG, "reconstructMissingDsrpBlob");
        this.mTransactionService.validateDSRPProfile(this.mProviderTokenKey);
        Log.m285d(TAG, "reconstructAlternateProfile");
        this.mTransactionService.validateAlternateProfile(this.mProviderTokenKey);
    }

    public String encryptUserSignature(byte[] bArr) {
        if (bArr == null) {
            Log.m285d(TAG, "encryptUserSignature : input data null");
            return null;
        }
        byte[] processSignatureData = this.mMcTokenManager.processSignatureData(bArr, 1);
        if (processSignatureData != null) {
            return Base64.encodeToString(processSignatureData, 2);
        }
        return null;
    }

    public byte[] decryptUserSignature(String str) {
        if (str == null) {
            Log.m285d(TAG, "decryptUserSignature : input data null");
            return null;
        }
        byte[] processSignatureData = this.mMcTokenManager.processSignatureData(Base64.decode(str, 2), 2);
        if (processSignatureData != null) {
            return processSignatureData;
        }
        return null;
    }

    public void beginPay(boolean z, boolean z2) {
        Log.m285d(TAG, "beginPay authenticated " + z);
        this.mTransactionService.initTransaction(z);
    }

    public void endPay() {
        Log.m285d(TAG, "endTransaction");
    }

    public ProviderResponseData processIdvOptionsData(IdvMethod idvMethod) {
        String str = null;
        Log.m285d(TAG, "processIdvOptionsData: ");
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        if (!(idvMethod == null || idvMethod.getData() == null || !IdvMethod.IDV_TYPE_APP.equals(idvMethod.getType()))) {
            String asString;
            String asString2;
            JsonObject asJsonObject = new JsonParser().parse(idvMethod.getData()).getAsJsonObject();
            if (asJsonObject.get(JSON_KEY_ACTION) != null) {
                asString = asJsonObject.get(JSON_KEY_ACTION).getAsString();
                Log.m285d(TAG, "intentAction : " + asString);
            } else {
                Log.m290w(TAG, "intentAction : is null ");
                asString = null;
            }
            if (asJsonObject.get(JSON_KEY_PKGNAME) != null) {
                asString2 = asJsonObject.get(JSON_KEY_PKGNAME).getAsString();
                Log.m285d(TAG, "Package Name : " + asString2);
            } else {
                Log.m290w(TAG, "intentAction : is null ");
                asString2 = null;
            }
            if (asJsonObject.get(JSON_KEY_EXTRA_TEXT) != null) {
                str = asJsonObject.get(JSON_KEY_EXTRA_TEXT).getAsString();
                Log.m285d(TAG, "Old Payload URL : " + str);
                str = Base64.encodeToString(Base64.decode(str, 2), 2);
                Log.m285d(TAG, "New Payload URL : " + str);
            }
            Bundle bundle = new Bundle();
            bundle.putString(PaymentFramework.EXTRA_INTENT_ACTION, asString);
            bundle.putString(JSON_KEY_PKGNAME, asString2);
            bundle.putString(PaymentFramework.EXTRA_PAYLOAD_DATA, str);
            providerResponseData.m1058e(bundle);
        }
        return providerResponseData;
    }

    private void unloadMcTAController(McTAController mcTAController) {
        try {
            mcTAController.unloadTA();
        } catch (Exception e) {
            Log.m286e(TAG, "unloadMcTAController : McTAController is null");
        }
    }

    public Bundle getTokenMetaData() {
        int issuerCountryCode = this.mTransactionService.getIssuerCountryCode(this.mProviderTokenKey);
        if (issuerCountryCode == -2) {
            Log.m286e(TAG, "Error obtaining CRM Country !!!");
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString(PaymentFramework.EXTRA_TOKEN_METADATA_ISSUER_COUNTRY_CODE, Integer.toHexString(issuerCountryCode));
        Log.m285d(TAG, "crmCountryCode = " + bundle.toString());
        return bundle;
    }
}
