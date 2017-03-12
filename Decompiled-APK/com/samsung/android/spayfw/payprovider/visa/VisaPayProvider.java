package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.PaymentFrameworkRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetails;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetailsDao;
import com.samsung.android.spayfw.payprovider.visa.inapp.InAppPayment;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.GenCryptogramResponseData;
import com.samsung.android.spayfw.payprovider.visa.transaction.VisaPayTransactionManager;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.visa.tainterface.VisaTAController;
import java.util.List;
import org.bouncycastle.asn1.eac.EACTags;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.b */
public class VisaPayProvider extends PaymentNetworkProvider {
    private static ProviderTokenKey pj;
    private static PaymentFrameworkRequester ps;
    private static VisaTokenDetailsDao zG;
    private static VisaTAController zH;
    private static boolean zK;
    private VisaPayProviderService zE;
    private VisaPayProviderSdk zF;
    private boolean zI;
    private boolean zJ;
    private EnrollCardInfo zL;
    private long zM;

    static {
        zG = null;
        zK = false;
        pj = null;
    }

    public VisaPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.zI = false;
        this.zJ = false;
        this.zL = null;
        this.zM = -1;
        this.mContext = context;
        this.mTAController = VisaTAController.bv(this.mContext);
        zH = (VisaTAController) this.mTAController;
        if (zG == null) {
            zG = new VisaTokenDetailsDao(this.mContext);
        }
    }

    public void setPaymentFrameworkRequester(PaymentFrameworkRequester paymentFrameworkRequester) {
        ps = paymentFrameworkRequester;
    }

    protected void loadTA() {
        zH.loadTA();
        Log.m287i("VisaPayProvider", "load real TA");
    }

    protected void unloadTA() {
        zH.unloadTA();
        Log.m287i("VisaPayProvider", "unload real TA");
    }

    public CertificateInfo[] getDeviceCertificates() {
        return this.zE.getCertificates();
    }

    public ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        if (zK) {
            ProviderRequestData b = this.zF.m1097b(enrollCardInfo, billingInfo);
            this.zL = enrollCardInfo;
            this.zL.incrementRefCount();
            return b;
        }
        Log.m286e("VisaPayProvider", "server cert is not set. so return empty data");
        b = new ProviderRequestData();
        b.setErrorCode(0);
        b.m822a(new JsonObject());
        return b;
    }

    public void stopMstPay(boolean z) {
        this.zI = false;
        if (this.zJ) {
            Log.m287i("VisaPayProvider", "mIgnoreToInformVisaSdk flag is set so no need to inform sdk abt stopMst");
            this.zJ = false;
            this.zE.stopMstPay();
            return;
        }
        Log.m285d("VisaPayProvider", "stopMstPay: stop visa mst pay");
        Log.m287i("VisaPayProvider", "stopMstPay: SDK start: " + System.currentTimeMillis());
        this.zF.stopMstPay(z);
        Log.m287i("VisaPayProvider", "stopMstPay: SDK end: " + System.currentTimeMillis());
        this.zE.stopMstPay();
        if (z) {
            eD();
        }
        pj = null;
    }

    public byte[] handleApdu(byte[] bArr, Bundle bundle) {
        return this.zF.processApdu(bArr, bundle);
    }

    public void clearCard() {
    }

    public SelectCardResult selectCard() {
        SelectCardResult selectCardResult = null;
        Object obj = 1;
        this.zF.selectCard(m753e(this.mProviderTokenKey));
        String i = this.zE.m1105i(null);
        if (i != null) {
            byte[] nonce = this.zE.getNonce();
            if (nonce != null) {
                selectCardResult = new SelectCardResult(i, nonce);
                pj = this.mProviderTokenKey;
                obj = null;
            }
        }
        if (obj != null) {
            Log.m286e("VisaPayProvider", "selectCard error occured");
        }
        return selectCardResult;
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        zK = this.zE.m1102a(certificateInfoArr);
        return zK;
    }

    public ProviderRequestData getReplenishmentRequestData() {
        return this.zF.m1089a(m753e(this.mProviderTokenKey));
    }

    private TokenKey m753e(ProviderTokenKey providerTokenKey) {
        if (providerTokenKey != null) {
            return new TokenKey(providerTokenKey.cm());
        }
        return null;
    }

    public ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        ProviderResponseData a;
        Throwable e;
        try {
            a = this.zF.m1090a(str, providerRequestData.ch());
            if (a != null) {
                try {
                    if (a.getErrorCode() == 0) {
                        this.mProviderTokenKey = a.getProviderTokenKey();
                    }
                } catch (TokenInvalidException e2) {
                    e = e2;
                    try {
                        Log.m284c("VisaPayProvider", e.getMessage(), e);
                        if (this.zL != null) {
                            this.zL.decrementRefCount();
                            this.zL = null;
                        }
                        return a;
                    } catch (Throwable th) {
                        if (this.zL != null) {
                            this.zL.decrementRefCount();
                            this.zL = null;
                        }
                    }
                }
            }
            this.mProviderTokenKey.setTrTokenId(str);
            setupReplenishAlarm();
            if (this.zL != null) {
                this.zL.decrementRefCount();
                this.zL = null;
            }
        } catch (Throwable e3) {
            e = e3;
            a = null;
            Log.m284c("VisaPayProvider", e.getMessage(), e);
            if (this.zL != null) {
                this.zL.decrementRefCount();
                this.zL = null;
            }
            return a;
        }
        return a;
    }

    public void setupReplenishAlarm() {
        this.zF.m1098b(this.mProviderTokenKey.getTrTokenId(), this.mProviderTokenKey, ps);
    }

    protected void replenishAlarmExpired() {
        if (this.mProviderTokenKey == null) {
            Log.m286e("VisaPayProvider", "cannot fire replenishment, providerTokenKey is null");
        } else if (ps != null) {
            ps.m311a(this.mProviderTokenKey);
        }
    }

    protected ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        String trTokenId = this.mProviderTokenKey.getTrTokenId();
        Log.m285d("VisaPayProvider", "replenishToken: trTokenId = " + trTokenId);
        return this.zF.m1091a(trTokenId, this.mProviderTokenKey, jsonObject, tokenStatus, ps);
    }

    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
        if (this.mProviderTokenKey == null || this.mProviderTokenKey.cn() == null) {
            Log.m286e("VisaPayProvider", "checkIfReplenishmentNeeded :  token key is null");
            return;
        }
        String trTokenId = this.mProviderTokenKey.getTrTokenId();
        Log.m285d("VisaPayProvider", "checkIfReplenishmentNeeded: id " + trTokenId);
        if (trTokenId != null) {
            VisaTokenDetails aZ = zG.aZ(trTokenId);
            String f = this.zF.m1099f(this.mProviderTokenKey);
            if (f == null) {
                Log.m286e("VisaPayProvider", "checkIfReplenishmentNeeded :  tokenStatus is null");
                return;
            }
            Log.m285d("VisaPayProvider", "checkIfReplenishmentNeeded :  tokenStatus " + f);
            if (!f.equals(TokenStatus.ACTIVE)) {
                Log.m286e("VisaPayProvider", "Not Replenishing as Token is Suspended or Pending: " + f);
            } else if (aZ != null) {
                Log.m285d("VisaPayProvider", "checkIfReplenishmentNeeded: repplenish ts = " + aZ.eK() + " current ts = " + System.currentTimeMillis() + " maxPmts = " + aZ.getMaxPmts() + " replPmts = " + aZ.eI());
                if (aZ.eK() - System.currentTimeMillis() <= 0 || aZ.getMaxPmts() <= aZ.eI()) {
                    ps.m311a(this.mProviderTokenKey);
                }
            }
        }
    }

    protected ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        int a = this.zF.m1087a(this.mProviderTokenKey, jsonObject, tokenStatus, ps);
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(a);
        providerResponseData.setProviderTokenKey(this.mProviderTokenKey);
        return providerResponseData;
    }

    public boolean authenticateTransaction(SecuredObject securedObject) {
        boolean authenticateTransaction = this.zE.authenticateTransaction(securedObject);
        if (!authenticateTransaction) {
            Log.m286e("VisaPayProvider", "authenticateTransaction:failed");
        }
        return authenticateTransaction;
    }

    public boolean prepareMstPay() {
        boolean prepareMstPay = this.zF.prepareMstPay();
        if (!prepareMstPay) {
            Log.m286e("VisaPayProvider", "prepareMstPay: unable to prepare mst data in TA");
        }
        return prepareMstPay;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        boolean startMstPay = this.zE.startMstPay(i, bArr);
        if (!startMstPay) {
            Log.m286e("VisaPayProvider", "startPayMst: failed to transmit mst data");
        }
        return startMstPay;
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        Bundle extraEnrollData;
        if (!(provisionTokenInfo == null || provisionTokenInfo.getActivationParams() == null)) {
            String str = (String) provisionTokenInfo.getActivationParams().get(ActivationData.CARD_INFO_CVV);
            if (this.zL != null && (this.zL instanceof EnrollCardPanInfo)) {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) this.zL;
                if (enrollCardPanInfo.getCVV() != null) {
                    provisionTokenInfo.getActivationParams().put(ActivationData.CARD_INFO_CVV, enrollCardPanInfo.getCVV());
                }
            } else if (this.zL != null && (this.zL instanceof EnrollCardReferenceInfo)) {
                Log.m287i("VisaPayProvider", "Provisioning by cardReferenceInfo !!!");
                extraEnrollData = ((EnrollCardReferenceInfo) this.zL).getExtraEnrollData();
                if (extraEnrollData != null) {
                    String string = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_CVV);
                    String string2 = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_EXP_MM);
                    String string3 = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_EXP_YY);
                    str = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_ZIP);
                    Log.m287i("VisaPayProvider", "Provisioning by cardReferenceInfo: ZIP " + str + ", exp " + string2 + "/" + string3 + ", cvv " + string);
                    if (string != null) {
                        provisionTokenInfo.getActivationParams().put(ActivationData.CARD_INFO_CVV, string);
                    }
                    if (str != null) {
                        provisionTokenInfo.getActivationParams().put(ActivationData.CARD_INFO_BILLING_ZIP, str);
                    }
                    if (string2 != null) {
                        provisionTokenInfo.getActivationParams().put(ActivationData.EXPIRATION_DATE_MONTH, string2);
                    }
                    if (string3 != null) {
                        provisionTokenInfo.getActivationParams().put(ActivationData.EXPIRATION_DATE_YEAR, string3);
                    }
                }
            }
        }
        extraEnrollData = new Bundle();
        extraEnrollData.putSerializable("riskData", RiskDataCollector.m1083a(this.mContext, provisionTokenInfo, this.zE));
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.m823e(extraEnrollData);
        return providerRequestData;
    }

    public void delete() {
        Log.m285d("VisaPayProvider", "delete: tokenKey = " + this.mProviderTokenKey);
        if (this.zL != null) {
            this.zL.decrementRefCount();
            this.zL = null;
        }
        if (this.mProviderTokenKey != null) {
            String cn = this.mProviderTokenKey.cn();
            if (cn == null || zG.ba(cn) == null) {
                Log.m285d("VisaPayProvider", "delete: token already deleted from VisaPayProvider database : " + cn);
                return;
            }
            this.zF.deleteToken(new TokenKey(this.mProviderTokenKey.cm()));
            zG.bb(cn);
        }
    }

    public boolean prepareNfcPay() {
        return true;
    }

    public Bundle stopNfcPay(int i) {
        Bundle a = this.zF.m1088a(i, pj);
        if (a.getShort("nfcApduErrorCode") == (short) 2) {
            eD();
            pj = null;
            if (this.zI) {
                this.zJ = true;
            }
        }
        return a;
    }

    public int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        if (this.mProviderTokenKey == null || bundle == null || transactionResponse == null) {
            Log.m286e("VisaPayProvider", "getTransactionData : invalid input ");
            return -4;
        }
        String str = null;
        VisaTokenDetails aX = this.zF.aX(this.mProviderTokenKey.cn());
        if (aX != null && aX.eL() > 0) {
            str = String.valueOf(aX.eL());
        }
        this.zM = Utils.am(this.mContext);
        new VisaPayTransactionManager().m1153a(this.mContext, this.mProviderTokenKey, str, bundle, transactionResponse);
        return 0;
    }

    protected TransactionDetails processTransactionData(Object obj) {
        TransactionDetails a = this.zE.m1101a(this.mProviderTokenKey, obj);
        if (a != null) {
            VisaTokenDetails aX = this.zF.aX(this.mProviderTokenKey.cn());
            if (aX != null) {
                if (this.zM == -1) {
                    Log.m286e("VisaPayProvider", "unable to update transaction fetch time ");
                } else {
                    aX.m1120A(this.zM);
                }
                zG.m1119d(aX);
                this.zF.m1093a(aX);
                Log.m285d("VisaPayProvider", "processTransactionData : updated transaction fetch time " + this.zM);
            }
        }
        this.zM = -1;
        return a;
    }

    protected void init() {
        Log.m285d("VisaPayProvider", "init ");
        this.zE = new VisaPayProviderService(this.mContext, zH);
        this.zF = new VisaPayProviderSdk(this.mContext, zG);
    }

    protected void onPaySwitch(int i, int i2) {
        Log.m287i("VisaPayProvider", "switch payment method from MST to NFC");
        this.zI = true;
    }

    protected void interruptMstPay() {
        this.zE.interruptMstPay();
    }

    public boolean getPayReadyState() {
        boolean z = false;
        if (!(this.mProviderTokenKey == null || this.mProviderTokenKey.cn() == null)) {
            z = this.zF.aW(this.mProviderTokenKey.cn());
        }
        Log.m285d("VisaPayProvider", " getPayReadyState: key" + this.mProviderTokenKey + "ret: " + z);
        return z;
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        return this.zF.isReplenishDataAvailable(jsonObject);
    }

    public void setPayAuthenticationMode(String str) {
        this.zF.setPayAuthenticationMode(str);
    }

    private void eD() {
        if (pj == null) {
            Log.m287i("VisaPayProvider", "no provider Token key. ignore updateAndCheckReplenishStatus ");
            return;
        }
        String cn = pj.cn();
        Log.m285d("VisaPayProvider", "updateAndCheckReplenishStatus: provideKey: " + pj.cm());
        if (cn != null) {
            this.zF.m1095a(cn, pj, ps);
        }
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        if (inAppDetailedTransactionInfo == null || pj == null) {
            Log.m286e("VisaPayProvider", " generateInAppPaymentPayload: input is null or mSelectedTokenKey is null");
            throw new PaymentProviderException(-36);
        }
        Log.m285d("VisaPayProvider", " generateInAppPaymentPayload: start " + System.currentTimeMillis());
        VisaTokenDetails ba = zG.ba(pj.cn());
        if (ba == null) {
            Log.m286e("VisaPayProvider", " generateInAppPaymentPayload: tokenDetails is null");
            throw new PaymentProviderException(-36);
        }
        TokenKey e = m753e(pj);
        GenCryptogramResponseData cryptogramInfo = InAppPayment.getCryptogramInfo(this.mContext, ba.getTrTokenId(), this.zF.m1092a(e, inAppDetailedTransactionInfo.getContextId(), inAppDetailedTransactionInfo.isRecurring()));
        if (cryptogramInfo == null) {
            Log.m286e("VisaPayProvider", " getCryptogramInfo: error");
            throw new PaymentProviderException(-36);
        } else if (cryptogramInfo.getTokenInfo() == null) {
            Log.m286e("VisaPayProvider", " getTokenInfo: null");
            throw new PaymentProviderException(-36);
        } else {
            Object buildInAppPaymentData = InAppPayment.buildInAppPaymentData(this.mContext, inAppDetailedTransactionInfo, cryptogramInfo);
            if (buildInAppPaymentData == null) {
                Log.m286e("VisaPayProvider", " buildInAppPaymentData: error");
                throw new PaymentProviderException(-36);
            }
            String toJson = new GsonBuilder().disableHtmlEscaping().create().toJson(buildInAppPaymentData);
            if (toJson == null) {
                Log.m286e("VisaPayProvider", " buildInAppPaymentData: json error");
                throw new PaymentProviderException(-36);
            }
            Log.m285d("VisaPayProvider", "json string: " + toJson);
            String encTokenInfo = cryptogramInfo.getTokenInfo().getEncTokenInfo();
            if (encTokenInfo == null) {
                Log.m286e("VisaPayProvider", " encTokenInfo: null");
                throw new PaymentProviderException(-36);
            }
            byte[] b;
            if (inAppDetailedTransactionInfo.cd() == null) {
                b = this.zE.m1104b(toJson, encTokenInfo, inAppDetailedTransactionInfo.getNonce());
            } else {
                List bE = Utils.bE(inAppDetailedTransactionInfo.cd());
                if (bE == null || bE.isEmpty()) {
                    Log.m286e("VisaPayProvider", "buildInAppPaymentData: cannot get certificate");
                    throw new PaymentProviderException(-36);
                }
                b = this.zE.m1103a(toJson, encTokenInfo, inAppDetailedTransactionInfo.getNonce(), (byte[]) bE.get(0), (byte[]) bE.get(1));
            }
            if (b == null || b.length <= 0) {
                Log.m286e("VisaPayProvider", "payload empty");
                throw new PaymentProviderException(-36);
            }
            Log.m285d("VisaPayProvider", "generateInAppPaymentPayload: payload length: " + b.length);
            this.zF.m1094a(e, inAppDetailedTransactionInfo.isRecurring(), true);
            eD();
            Log.m285d("VisaPayProvider", "generateInAppPaymentPayload: end: " + System.currentTimeMillis());
            return b;
        }
    }

    public ProviderResponseData processIdvOptionsData(IdvMethod idvMethod) {
        String str = null;
        Log.m285d("VisaPayProvider", "processIdvOptionsData: ");
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        if (!(idvMethod == null || idvMethod.getData() == null)) {
            String asString;
            String substring;
            if (IdvMethod.IDV_TYPE_APP.equals(idvMethod.getType())) {
                JsonObject asJsonObject = new JsonParser().parse(idvMethod.getData()).getAsJsonObject();
                if (asJsonObject.get("source") != null) {
                    asString = asJsonObject.get("source").getAsString();
                    if (asString != null) {
                        substring = asString.substring(0, asString.indexOf(EACTags.DYNAMIC_AUTHENTIFICATION_TEMPLATE));
                        asString = asString.replaceAll("\\|", "\\.");
                        Log.m285d("VisaPayProvider", "Source: " + asString + " Package Name : " + substring);
                    } else {
                        Log.m290w("VisaPayProvider", "Source is null after converting as string");
                        substring = null;
                    }
                } else {
                    Log.m290w("VisaPayProvider", "Source is missing in App2App IDV data");
                    substring = null;
                    asString = null;
                }
                if (asJsonObject.get("requestPayload") != null) {
                    str = asJsonObject.get("requestPayload").getAsString();
                    Log.m285d("VisaPayProvider", "Old Payload URL : " + str);
                    str = Base64.encodeToString(Base64.decode(str, 2), 2);
                    Log.m285d("VisaPayProvider", "New Payload URL : " + str);
                } else {
                    Log.m290w("VisaPayProvider", "Bank app payload is missing in App2App IDV data");
                }
                Bundle bundle = new Bundle();
                bundle.putString(PaymentFramework.EXTRA_INTENT_ACTION, asString);
                bundle.putString(PaymentFramework.EXTRA_PACKAGENAME, substring);
                bundle.putString(PaymentFramework.EXTRA_PAYLOAD_DATA, str);
                providerResponseData.m1058e(bundle);
            } else if (IdvMethod.IDV_TYPE_CODE_ONLINEBANKING.equals(idvMethod.getType())) {
                JsonObject asJsonObject2 = new JsonParser().parse(idvMethod.getData()).getAsJsonObject();
                if (asJsonObject2.get("source") != null) {
                    asString = asJsonObject2.get("source").getAsString();
                    if (asString != null) {
                        substring = asString.substring(0, asString.indexOf(EACTags.DYNAMIC_AUTHENTIFICATION_TEMPLATE));
                        str = asString.substring(asString.indexOf(EACTags.DYNAMIC_AUTHENTIFICATION_TEMPLATE) + 1);
                        Log.m285d("VisaPayProvider", "amount: " + substring + " currenyCode : " + str);
                    } else {
                        Log.m290w("VisaPayProvider", "Source is null after converting as string");
                        substring = null;
                    }
                } else {
                    Log.m290w("VisaPayProvider", "Source is missing in CODE_ONLINE_BANKING IDV data");
                    substring = null;
                }
                Bundle bundle2 = new Bundle();
                bundle2.putString(IdvMethod.EXTRA_AMOUNT, substring);
                bundle2.putString(IdvMethod.EXTRA_CURRENCY_CODE, str);
                providerResponseData.m1058e(bundle2);
            }
        }
        return providerResponseData;
    }

    protected ProviderRequestData getVerifyIdvRequestData(VerifyIdvInfo verifyIdvInfo) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (verifyIdvInfo.getIntent() != null) {
            String stringExtra = verifyIdvInfo.getIntent().getStringExtra("authenticationCodeResponse");
            Bundle bundle = new Bundle();
            bundle.putString("tac", stringExtra);
            providerRequestData.m823e(bundle);
        } else {
            Log.m285d("VisaPayProvider", "No Intent Data");
        }
        return providerRequestData;
    }

    public String encryptUserSignature(byte[] bArr) {
        String str = null;
        if (bArr != null) {
            try {
                byte[] storeData = zH.storeData(bArr);
                if (storeData != null) {
                    str = Base64.encodeToString(storeData, 2);
                }
            } catch (Throwable e) {
                Log.m286e("VisaPayProvider", "encryptUserSignature Error occured while gettting encypted data from TA");
                Log.m284c("VisaPayProvider", e.getMessage(), e);
            }
        }
        return str;
    }

    public byte[] decryptUserSignature(String str) {
        byte[] bArr = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                bArr = zH.retrieveFromStorage(Base64.decode(str, 2));
            } catch (Throwable e) {
                Log.m286e("VisaPayProvider", "decryptUserSignature Error occured while gettting decrypted data from TA");
                Log.m284c("VisaPayProvider", e.getMessage(), e);
            }
        }
        return bArr;
    }

    public boolean isPayAllowedForPresentationMode(int i) {
        if (this.mProviderTokenKey != null) {
            return this.zF.m1096a(m753e(this.mProviderTokenKey), i);
        }
        Log.m286e("VisaPayProvider", "ProviderTokenKey is null");
        return false;
    }

    public Bundle getTokenMetaData() {
        Log.m285d("VisaPayProvider", "getTokenMetaData ");
        if (this.zF == null) {
            Log.m286e("VisaPayProvider", "mSdk is null");
            return null;
        } else if (this.mProviderTokenKey != null) {
            return this.zF.getTokenMetaData(m753e(this.mProviderTokenKey));
        } else {
            Log.m286e("VisaPayProvider", "tokenKey is null");
            return null;
        }
    }
}
