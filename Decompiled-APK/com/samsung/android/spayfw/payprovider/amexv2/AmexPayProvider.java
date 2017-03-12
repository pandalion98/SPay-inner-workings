package com.samsung.android.spayfw.payprovider.amexv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenCloseResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenInAppResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.model.TokenPersoResponse;
import com.americanexpress.mobilepayments.hceclient.model.TokenRefreshStatusResponse;
import com.americanexpress.mobilepayments.hceclient.service.AmexPaySaturn;
import com.americanexpress.mobilepayments.hceclient.service.AmexPaySaturnImpl;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.PaymentFrameworkRequester;
import com.samsung.android.spayfw.fraud.FraudDataProvider;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.RiskDataParam;
import com.samsung.android.spayfw.payprovider.TokenReplenishAlarm;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.amex.AmexUtils;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexCommands.ProcessDataJwsJwe.JsonOperation;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAController;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.LLVARByteUtil;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardSchema;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;
import org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.a */
public class AmexPayProvider extends PaymentNetworkProvider {
    private static PaymentFrameworkRequester ps;
    private static boolean qF;
    private EnrollCardInfo kQ;
    private BillingInfo mBillingInfo;
    private Context mContext;
    private ProviderTokenKey pj;
    private CertificateInfo pl;
    private CertificateInfo pm;
    private boolean pn;
    private boolean po;
    private SharedPreferences pp;
    private boolean pr;
    private AmexTAController qA;
    private AmexPaySaturn qB;
    private String qC;
    private String qD;
    private Map<String, String> qE;
    private boolean qG;
    private boolean qH;
    private boolean qI;
    final String qu;
    final String qv;
    final String qw;
    final String qx;
    final String qy;
    final String qz;

    static {
        qF = false;
    }

    public AmexPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.qu = IdvMethod.EXTRA_AMOUNT;
        this.qv = "currency_code";
        this.qw = "utc";
        this.qx = "cardholder_name";
        this.qy = "eci_indicator";
        this.qz = "5";
        this.pj = null;
        this.pn = false;
        this.po = false;
        this.qG = false;
        this.qH = false;
        this.pr = false;
        this.qI = false;
        if (context == null) {
            Log.m286e("AmexPayProvider", "AmexPayProvider : ERROR: context is null");
            return;
        }
        this.mContext = context;
        this.mTAController = AmexTAController.m810D(this.mContext);
        this.qA = (AmexTAController) this.mTAController;
        this.qB = new AmexPaySaturnImpl();
        this.pp = this.mContext.getSharedPreferences("AmexStorage", 0);
        this.qE = new HashMap();
    }

    private void aB(String str) {
        if (str == null) {
            Log.m286e("AmexPayProvider", "createConfigMap : ERROR: Invalid key-value pair string, cannot create congfig map");
            return;
        }
        for (String split : str.split(",")) {
            String[] split2 = split.split("=");
            if (split2 != null && split2.length == 2) {
                this.qE.put(split2[0], split2[1]);
            }
        }
        Log.m285d("AmexPayProvider", "createConfigMap: Config Map created");
    }

    private static final short m790a(byte[] bArr, short s) {
        return (short) ((((short) bArr[s]) << 8) + (((short) bArr[(short) (s + 1)]) & GF2Field.MASK));
    }

    private static String au(String str) {
        if (str == null) {
            Log.m286e("AmexPayProvider", "Token Status received is null");
            return null;
        } else if (HCEClientConstants.API_INDEX_TOKEN_OPEN.equals(str)) {
            return HCEClientConstants.API_INDEX_TOKEN_OPEN;
        } else {
            if (HCEClientConstants.API_INDEX_TOKEN_PERSO.equals(str)) {
                return Constants.SERVICE_CODE_LENGTH;
            }
            if (Constants.SERVICE_CODE_LENGTH.equals(str)) {
                return HCEClientConstants.API_INDEX_TOKEN_PERSO;
            }
            Log.m286e("AmexPayProvider", "Token Status translation failure");
            return null;
        }
    }

    public static final synchronized JsonObject m793c(JsonObject jsonObject) {
        JsonObject jsonObject2;
        synchronized (AmexPayProvider.class) {
            TreeMap treeMap = new TreeMap();
            for (Entry entry : jsonObject.entrySet()) {
                Object obj = (JsonElement) entry.getValue();
                if (((JsonElement) entry.getValue()).isJsonObject()) {
                    obj = AmexPayProvider.m793c(obj.getAsJsonObject());
                }
                treeMap.put(entry.getKey(), obj);
            }
            jsonObject2 = new JsonObject();
            for (Entry entry2 : treeMap.entrySet()) {
                jsonObject2.add((String) entry2.getKey(), (JsonElement) entry2.getValue());
            }
        }
        return jsonObject2;
    }

    protected void init() {
        if (this.mProviderTokenKey != null) {
            String string = this.pp.getString(this.mProviderTokenKey.cn() + "_config_string", null);
            if (string != null) {
                aB(string);
                return;
            } else {
                Log.m286e("AmexPayProvider", "init : ERROR: Cannot find config entry for this token");
                return;
            }
        }
        Log.m285d("AmexPayProvider", "init : Token key is null");
    }

    protected void loadTA() {
        this.qA.loadTA();
        Log.m285d("AmexPayProvider", "loadTA : Load AMEX TA");
    }

    protected void unloadTA() {
        this.qA.unloadTA();
        Log.m285d("AmexPayProvider", "unloadTA : Unload AMEX TA");
    }

    protected synchronized CertificateInfo[] getDeviceCertificates() {
        CertificateInfo[] certificateInfoArr;
        Log.m285d("AmexPayProvider", "getDeviceCertificates : Enter");
        try {
            AmexTAController.AmexTAController cA = this.qA.cA();
        } catch (Throwable e) {
            Log.m284c("AmexPayProvider", e.getMessage(), e);
            cA = null;
        }
        if (cA != null) {
            if (!(cA.deviceCertificate == null || cA.deviceSigningCertificate == null)) {
                certificateInfoArr = new CertificateInfo[2];
                certificateInfoArr[0] = new CertificateInfo();
                certificateInfoArr[0].setUsage(CertificateInfo.CERT_USAGE_CA);
                certificateInfoArr[0].setAlias("DeviceCert");
                certificateInfoArr[0].setContent(cA.deviceCertificate);
                certificateInfoArr[1] = new CertificateInfo();
                certificateInfoArr[1].setUsage(CertificateInfo.CERT_USAGE_VER);
                certificateInfoArr[1].setAlias("Amex-DeviceSigningCert");
                certificateInfoArr[1].setContent(cA.deviceSigningCertificate);
                Log.m285d("AmexPayProvider", "getDeviceCertificates : Exit");
            }
        }
        Log.m286e("AmexPayProvider", "getDeviceCertificates : ERROR: Null device certificates");
        certificateInfoArr = null;
        Log.m285d("AmexPayProvider", "getDeviceCertificates : Exit");
        return certificateInfoArr;
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        CharSequence charSequence = "tsp_rsa";
        String str = "tsp_ecc";
        for (int i = 0; i < certificateInfoArr.length; i++) {
            if (certificateInfoArr[i].getAlias().contains(charSequence)) {
                this.pl = certificateInfoArr[i];
            } else if (certificateInfoArr[i].getAlias().equals(str)) {
                this.pm = certificateInfoArr[i];
            }
        }
        return true;
    }

    private long cx() {
        long am = Utils.am(this.mContext) / 1000;
        Log.m285d("AmexPayProvider", "getRealTimeInSeconds : Time in seconds = " + am);
        return am;
    }

    protected synchronized ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        ProviderRequestData providerRequestData;
        providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(-2);
        Log.m285d("AmexPayProvider", "getEnrollmentRequestData : Enter");
        if (this.pl == null) {
            providerRequestData.setErrorCode(0);
            providerRequestData.m822a(new JsonObject());
            providerRequestData.m823e(m792b(enrollCardInfo));
        } else if (enrollCardInfo == null) {
            Log.m286e("AmexPayProvider", "getEnrollmentRequestData : ERROR: Invalid or Incomplete card information, cannot continue with enrollment");
            providerRequestData.setErrorCode(-4);
        } else {
            JsonElement jsonObject = new JsonObject();
            if (enrollCardInfo instanceof EnrollCardPanInfo) {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) enrollCardInfo;
                if (enrollCardPanInfo.getPAN() == null) {
                    providerRequestData.setErrorCode(-4);
                } else if (!(enrollCardPanInfo.getPAN() == null || enrollCardPanInfo.getPAN().isEmpty())) {
                    jsonObject.addProperty("accountNumber", enrollCardPanInfo.getPAN());
                }
            } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
                EnrollCardReferenceInfo enrollCardReferenceInfo = (EnrollCardReferenceInfo) enrollCardInfo;
                if (!(enrollCardReferenceInfo.getReferenceType() == null || !enrollCardReferenceInfo.getReferenceType().equals(EnrollCardReferenceInfo.CARD_REF_TYPE_ID) || enrollCardReferenceInfo.getExtraEnrollData() == null)) {
                    jsonObject.addProperty("accountRefId", enrollCardReferenceInfo.getExtraEnrollData().getString(EnrollCardReferenceInfo.CARD_REFERENCE_ID));
                }
            }
            if (enrollCardInfo.getCardEntryMode() != null) {
                if (enrollCardInfo.getCardEntryMode().equals(EnrollCardInfo.CARD_ENTRY_MODE_MANUAL)) {
                    jsonObject.addProperty("accountInputMethod", HCEClientConstants.API_INDEX_TOKEN_OPEN);
                } else if (enrollCardInfo.getCardEntryMode().equals(EnrollCardInfo.CARD_ENTRY_MODE_OCR)) {
                    jsonObject.addProperty("accountInputMethod", Constants.SERVICE_CODE_LENGTH);
                } else {
                    jsonObject.addProperty("accountInputMethod", HCEClientConstants.API_INDEX_TOKEN_PERSO);
                }
                if (enrollCardInfo.getCardEntryMode().equals(EnrollCardInfo.CARD_ENTRY_MODE_FILE)) {
                    jsonObject.addProperty("onFileIndicator", Boolean.valueOf(true));
                } else {
                    jsonObject.addProperty("onFileIndicator", Boolean.valueOf(false));
                }
            }
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.add("accountData", jsonObject);
            JsonElement jsonObject3 = new JsonObject();
            try {
                AmexTAController.AmexTAController cA = this.qA.cA();
                if (cA.deviceEncryptionCertificate != null) {
                    jsonObject3.addProperty("deviceEncryptionPublicKeyCert", cA.deviceEncryptionCertificate);
                }
                if (cA.deviceCertificate != null) {
                    jsonObject3.addProperty("devicePublicKeyCert", cA.deviceCertificate);
                }
                if (cA.deviceSigningCertificate != null) {
                    jsonObject3.addProperty("deviceSigningPublicKeyCert", cA.deviceSigningCertificate);
                }
                try {
                    AmexTAController.AmexTAController c = this.qA.m817c(this.pl.getContent(), jsonObject2.toString(), AmexPayProvider.m793c(jsonObject).toString() + AmexPayProvider.m793c(jsonObject3).toString());
                    JsonObject jsonObject4 = new JsonObject();
                    jsonObject4.addProperty("encryptedData", c.encryptedRequestData);
                    jsonObject4.add("secureDeviceData", jsonObject3);
                    jsonObject4.addProperty("encryptionParameters", c.encryptionParams);
                    jsonObject4.addProperty("accountDataSignature", c.requestDataSignature);
                    providerRequestData.m822a(jsonObject4);
                    this.mBillingInfo = billingInfo;
                    enrollCardInfo.incrementRefCount();
                    this.kQ = enrollCardInfo;
                    providerRequestData.m823e(m792b(enrollCardInfo));
                    providerRequestData.setErrorCode(0);
                    Log.m285d("AmexPayProvider", "accountData : " + AmexPayProvider.m793c(jsonObject).toString());
                    Log.m285d("AmexPayProvider", "encryptedData : " + AmexPayProvider.m793c(jsonObject2).toString());
                    Log.m285d("AmexPayProvider", "secureDeviceData : " + AmexPayProvider.m793c(jsonObject3).toString());
                    Log.m285d("AmexPayProvider", "enrollRequest : " + AmexPayProvider.m793c(jsonObject4).toString());
                } catch (Throwable e) {
                    Log.m286e("AmexPayProvider", "getEnrollmentRequestData : ERROR: Unable to Sign enrollment data");
                    Log.m284c("AmexPayProvider", e.getMessage(), e);
                }
            } catch (AmexTAException e2) {
                Log.m286e("AmexPayProvider", "getEnrollmentRequestData : ERROR: Unable to get device certificates");
                e2.printStackTrace();
            }
        }
        Log.m285d("AmexPayProvider", "getEnrollmentRequestData : Exit");
        return providerRequestData;
    }

    protected synchronized ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        ProviderRequestData providerRequestData;
        Log.m285d("AmexPayProvider", "getProvisionRequestData : Enter");
        providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(-2);
        if (this.pl == null) {
            providerRequestData.setErrorCode(0);
            providerRequestData.m822a(new JsonObject());
        } else if (this.kQ == null || this.mBillingInfo == null) {
            providerRequestData.setErrorCode(-4);
            Log.m286e("AmexPayProvider", "getProvisionRequestData : ERROR: Card data is null");
        } else {
            JsonElement jsonObject = new JsonObject();
            JsonElement jsonObject2 = new JsonObject();
            if (this.mBillingInfo.getCity() != null) {
                jsonObject2.addProperty("city", this.mBillingInfo.getCity());
            }
            if (this.mBillingInfo.getCountry() != null) {
                jsonObject2.addProperty("country", this.mBillingInfo.getCountry());
            }
            if (this.mBillingInfo.getStreet1() != null) {
                jsonObject2.addProperty("addressLine1", this.mBillingInfo.getStreet1());
            }
            if (this.mBillingInfo.getStreet2() != null) {
                jsonObject2.addProperty("addressLine2", this.mBillingInfo.getStreet2());
            }
            if (this.mBillingInfo.getState() != null) {
                jsonObject2.addProperty("state", this.mBillingInfo.getState());
            }
            if (this.mBillingInfo.getZip() != null) {
                jsonObject2.addProperty("zipCode", this.mBillingInfo.getZip());
            }
            jsonObject.add("billingAddress", jsonObject2);
            JsonElement jsonObject3 = new JsonObject();
            if (this.kQ instanceof EnrollCardPanInfo) {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) this.kQ;
                jsonObject3.addProperty("cardExpiry", enrollCardPanInfo.getExpMonth() + "/" + enrollCardPanInfo.getExpYear());
                jsonObject3.addProperty("cid", enrollCardPanInfo.getCVV());
                jsonObject.add("cardVerificationValues", jsonObject3);
            }
            jsonObject.addProperty("cardholderName", this.kQ.getName());
            JsonObject jsonObject4 = new JsonObject();
            jsonObject4.add("accountData", jsonObject);
            JsonElement jsonObject5 = new JsonObject();
            this.qD = "ProvReqID_" + System.currentTimeMillis();
            byte[] d = LLVARByteUtil.m820d(new String[]{LLVARUtil.PLAIN_TEXT + this.qD});
            int open = this.qA.open(d);
            if (open < 0) {
                Log.m286e("AmexPayProvider", "getProvisionRequestData : ERROR: AmexTAController open failed = " + open);
            } else {
                byte[] bArr = new byte[384];
                int initializeSecureChannel = this.qA.initializeSecureChannel(null, bArr);
                if (initializeSecureChannel < 0) {
                    Log.m286e("AmexPayProvider", "getProvisionRequestData : ERROR: AmexTAController initializeSecureChannel failed = " + initializeSecureChannel);
                } else {
                    String str;
                    AmexTAController.AmexTAController c;
                    bArr = Arrays.copyOfRange(bArr, 0, initializeSecureChannel);
                    Log.m285d("AmexPayProvider", "getProvisionRequestData : Ephemeral Key in LLVAR  = " + new String(bArr));
                    byte[][] llVarToBytes = LLVARByteUtil.llVarToBytes(bArr);
                    if (llVarToBytes == null || llVarToBytes[0] == null) {
                        Log.m285d("AmexPayProvider", "getProvisionRequestData : ephemeralPublicKeyBytes or ephemeralPublicKeyBytes[0] is null");
                        str = null;
                    } else {
                        str = new String(llVarToBytes[0]);
                    }
                    Log.m285d("AmexPayProvider", "getProvisionRequestData : Success calling TA initializeSecureChannel, eccPublicKey = " + str);
                    jsonObject5.addProperty("ephemeralPublicKey", str);
                    try {
                        AmexTAController.AmexTAController cA = this.qA.cA();
                        if (cA.deviceCertificate != null) {
                            jsonObject5.addProperty("devicePublicKeyCert", cA.deviceCertificate);
                        }
                        if (cA.deviceSigningCertificate != null) {
                            jsonObject5.addProperty("deviceSigningPublicKeyCert", cA.deviceSigningCertificate);
                        }
                        if (cA.deviceEncryptionCertificate != null) {
                            jsonObject5.addProperty("deviceEncryptionPublicKeyCert", cA.deviceEncryptionCertificate);
                        }
                    } catch (AmexTAException e) {
                        Log.m286e("AmexPayProvider", "getProvisionRequestData : ERROR: Unable to get device certificates");
                        e.printStackTrace();
                    }
                    TokenRefreshStatusResponse tokenRefreshStatus = this.qB.tokenRefreshStatus(cx(), null);
                    if (tokenRefreshStatus != null && ((!TextUtils.isEmpty(tokenRefreshStatus.getReasonCode()) && tokenRefreshStatus.getReasonCode().equalsIgnoreCase("12")) || tokenRefreshStatus.getReasonCode().equalsIgnoreCase(HCEClientConstants.API_INDEX_TOKEN_REFRESH_STATUS))) {
                        Log.m287i("AmexPayProvider", "getProvisionRequestData : Trying a one time recovery for fatal cases");
                        TokenCloseResponse tokenClose = this.qB.tokenClose();
                        if (tokenClose != null) {
                            Log.m285d("AmexPayProvider", "tokenClose returns :" + tokenClose.getDetailCode());
                        }
                        tokenRefreshStatus = this.qB.tokenRefreshStatus(cx(), null);
                    }
                    if (tokenRefreshStatus != null) {
                        jsonObject5.addProperty("clientAPIVersion", tokenRefreshStatus.getClientVersion());
                    }
                    str = AmexPayProvider.m793c(jsonObject).toString() + AmexPayProvider.m793c(jsonObject5).toString();
                    Log.m285d("AmexPayProvider", "getProvisionRequestData : dataToBeSigned " + str);
                    try {
                        c = this.qA.m817c(this.pl.getContent(), jsonObject4.toString(), str);
                    } catch (Throwable e2) {
                        Log.m284c("AmexPayProvider", e2.getMessage(), e2);
                        c = null;
                    }
                    if (c == null) {
                        Log.m286e("AmexPayProvider", "getProvisionRequestData : ERROR: Unable to sign the request data");
                    } else {
                        if (this.qA.close(d) != 0) {
                            Log.m286e("AmexPayProvider", "getProvisionRequestData : ERROR: TA close failed");
                        }
                        JsonObject jsonObject6 = new JsonObject();
                        jsonObject6.addProperty("encryptedData", c.encryptedRequestData);
                        jsonObject6.add("secureDeviceData", jsonObject5);
                        jsonObject6.addProperty("encryptionParameters", c.encryptionParams);
                        jsonObject6.addProperty("accountDataSignature", c.requestDataSignature);
                        jsonObject2 = new JsonObject();
                        jsonObject2.addProperty("imei", DeviceInfo.getDeviceImei(this.mContext));
                        jsonObject2.addProperty("serial", DeviceInfo.getDeviceSerialNumber());
                        jsonObject2.addProperty("msisdn", DeviceInfo.getMsisdn(this.mContext));
                        jsonObject6.add("deviceData", jsonObject2);
                        providerRequestData.m822a(jsonObject6);
                        providerRequestData.m823e(m789a(provisionTokenInfo));
                        providerRequestData.setErrorCode(0);
                        Log.m287i("AmexPayProvider", "accountData : " + AmexPayProvider.m793c(jsonObject).toString());
                        Log.m287i("AmexPayProvider", "encryptedData : " + AmexPayProvider.m793c(jsonObject4).toString());
                        Log.m287i("AmexPayProvider", "secureDeviceData : " + AmexPayProvider.m793c(jsonObject5).toString());
                        Log.m287i("AmexPayProvider", "enrollRequest : " + AmexPayProvider.m793c(jsonObject6).toString());
                    }
                }
            }
        }
        Log.m285d("AmexPayProvider", "getProvisionRequestData : Exit");
        return providerRequestData;
    }

    protected synchronized ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        ProviderResponseData providerResponseData;
        Log.m285d("AmexPayProvider", "createToken : Enter");
        providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(-2);
        JsonObject ch = providerRequestData.ch();
        if (ch == null) {
            try {
                Log.m285d("AmexPayProvider", "createToken : ERROR: Invalid input - responseData");
                providerResponseData.setErrorCode(-4);
                if (this.kQ != null) {
                    this.kQ.decrementRefCount();
                }
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", "createToken : ERROR: " + e.getMessage(), e);
                if (this.kQ != null) {
                    this.kQ.decrementRefCount();
                }
            } catch (Throwable th) {
                if (this.kQ != null) {
                    this.kQ.decrementRefCount();
                }
            }
        } else {
            JsonObject asJsonObject = ch.getAsJsonObject("secureTokenData");
            if (asJsonObject == null) {
                Log.m286e("AmexPayProvider", "createToken : ERROR: secureTokenData is null");
                providerResponseData.setErrorCode(-4);
                if (this.kQ != null) {
                    this.kQ.decrementRefCount();
                }
            } else {
                String walletId;
                String asString = asJsonObject.get("initializationVector").getAsString();
                String asString2 = asJsonObject.get("encryptedTokenData").getAsString();
                String asString3 = asJsonObject.get("encryptedTokenDataHMAC").getAsString();
                String replace = (asJsonObject.get("cloudPublicKeyCert").getAsString() + (this.pm != null ? this.pm.getContent() : BuildConfig.FLAVOR)).replace("\n", BuildConfig.FLAVOR);
                if (this.kQ != null) {
                    walletId = this.kQ.getWalletId();
                } else {
                    walletId = ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID);
                }
                if (walletId == null) {
                    Log.m286e("AmexPayProvider", "createToken : ERROR: Wallet Id is NULL");
                    providerResponseData.setErrorCode(-2);
                    if (this.kQ != null) {
                        this.kQ.decrementRefCount();
                    }
                } else {
                    byte[] objectsToLLVar = LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + replace, LLVARUtil.HEX_STRING + Utils.encodeHex(walletId.getBytes()));
                    if (this.qB.tokenOpen(OperationMode.PROVISION, str).getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                        Log.m285d("AmexPayProvider", "createToken : tokenChannelUpdateResponse = " + this.qA.updateSecureChannel(objectsToLLVar, null));
                        TokenPersoResponse tokenPerso = this.qB.tokenPerso(HexUtils.getSafePrintChars(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + asString2, LLVARUtil.PLAIN_TEXT + asString, LLVARUtil.PLAIN_TEXT + asString3)));
                        TokenOperationStatus tokenClose = this.qB.tokenClose();
                        if (!tokenPerso.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                            Log.m286e("AmexPayProvider", "createToken : ERROR: tokenPerso failed on SDK");
                            providerResponseData.setErrorCode(-2);
                            if (this.kQ != null) {
                                this.kQ.decrementRefCount();
                            }
                        } else if (tokenClose.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                            Log.m285d("AmexPayProvider", "createToken : SecureTokenData signature len : " + tokenPerso.getTokenDataSignature().length());
                            Log.m285d("AmexPayProvider", "createToken : SecureTokenData signature : " + AmexUtils.byteArrayToHex(tokenPerso.getTokenDataSignature().getBytes()));
                            Log.m287i("AmexPayProvider", "createToken : TokenConfiguration = " + tokenPerso.getTokenConfiguration());
                            this.pp.edit().putString(str + "_config_string", tokenPerso.getTokenConfiguration()).apply();
                            aB(tokenPerso.getTokenConfiguration());
                            JsonElement jsonObject = new JsonObject();
                            AmexTAController.AmexTAController cA = this.qA.cA();
                            if (cA.deviceCertificate != null) {
                                jsonObject.addProperty("devicePublicKeyCert", cA.deviceCertificate);
                            }
                            if (cA.deviceSigningCertificate != null) {
                                jsonObject.addProperty("deviceSigningPublicKeyCert", cA.deviceSigningCertificate);
                            }
                            walletId = BuildConfig.FLAVOR;
                            asString3 = ch.get("encryptionParameters").getAsString();
                            String asString4 = ch.get("encryptedData").getAsString();
                            if (!(asString3 == null || asString3.isEmpty() || asString4 == null || asString4.isEmpty())) {
                                walletId = this.qA.m818o(asString4, asString3);
                            }
                            providerResponseData.setProviderTokenKey(new ProviderTokenKey(str));
                            ch = new JsonObject();
                            ch.addProperty("secureTokenDataSignature", tokenPerso.getTokenDataSignature());
                            ch.add("secureDeviceData", jsonObject);
                            ch.addProperty("accountRefId", walletId);
                            providerResponseData.m1057b(ch);
                            providerResponseData.setErrorCode(0);
                            av(str);
                            if (this.kQ != null) {
                                this.kQ.decrementRefCount();
                            }
                        } else {
                            Log.m286e("AmexPayProvider", "createToken : ERROR: tokenClose failed on SDK");
                            providerResponseData.setErrorCode(-2);
                            if (this.kQ != null) {
                                this.kQ.decrementRefCount();
                            }
                        }
                    } else {
                        Log.m286e("AmexPayProvider", "createToken : ERROR: tokenOpen failed on SDK");
                        providerResponseData.setErrorCode(-2);
                        if (this.kQ != null) {
                            this.kQ.decrementRefCount();
                        }
                    }
                }
            }
        }
        Log.m285d("AmexPayProvider", "createToken : Exit");
        return providerResponseData;
    }

    public synchronized SelectCardResult selectCard() {
        SelectCardResult selectCardResult = null;
        synchronized (this) {
            Log.m285d("AmexPayProvider", "selectCard : Enter");
            if (this.mProviderTokenKey != null) {
                if (getPayReadyState()) {
                    if (this.pn) {
                        Log.m286e("AmexPayProvider", "selectCard : ERROR: Select Card called before previous Payment did not complete. This must never happen");
                        this.qG = false;
                        this.pr = false;
                        stopMstPay(false);
                    }
                    try {
                        this.qA.initializeSecuritySetup();
                        byte[] nonce = this.qA.getNonce(32);
                        if (nonce == null) {
                            Log.m286e("AmexPayProvider", "selectCard : ERROR: getNonce returned null");
                        } else {
                            SelectCardResult selectCardResult2 = new SelectCardResult(AmexTAController.cz().getTAInfo().getTAId(), nonce);
                            this.pj = this.mProviderTokenKey;
                            Log.m285d("AmexPayProvider", "selectCard : Calling Token Open");
                            TokenOperationStatus tokenOpen = this.qB.tokenOpen(OperationMode.PAYMENT, this.pj.cn());
                            if (tokenOpen.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                                Log.m285d("AmexPayProvider", "selectCard : set card selected status on tokenOpen success");
                                qF = true;
                            } else {
                                Log.m286e("AmexPayProvider", "selectCard : ERROR: tokenOpen failed on SDK " + tokenOpen.getReasonCode());
                            }
                            selectCardResult = selectCardResult2;
                        }
                    } catch (Throwable e) {
                        Log.m284c("AmexPayProvider", e.getMessage(), e);
                    }
                } else {
                    Log.m286e("AmexPayProvider", "selectCard : ERROR: Can not pay since LUPC reached zero or token status not active");
                }
            }
        }
        return selectCardResult;
    }

    public synchronized void beginPay(boolean z, boolean z2) {
    }

    public synchronized void endPay() {
    }

    protected synchronized boolean authenticateTransaction(SecuredObject securedObject) {
        boolean z;
        Log.m285d("AmexPayProvider", "authenticateTransaction : Enter");
        z = true;
        if (!this.qB.tokenSetCDCVM(securedObject.getSecureObjectData()).getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
            Log.m286e("AmexPayProvider", "authenticateTransaction : ERROR: tokenSetCDCVM failed on SDK");
            z = false;
        }
        Log.m285d("AmexPayProvider", "authenticateTransaction : Exit");
        return z;
    }

    boolean cq() {
        Log.m285d("AmexPayProvider", "startPayment : Enter");
        if (this.pn) {
            Log.m286e("AmexPayProvider", "startPayment : Payment already in progress");
        } else {
            this.po = false;
            this.qG = false;
            this.qI = false;
            this.qH = false;
            this.pn = true;
        }
        Log.m285d("AmexPayProvider", "startPayment : Exit");
        return true;
    }

    public synchronized boolean prepareNfcPay() {
        Log.m285d("AmexPayProvider", "prepareNfcPay : Enter");
        cq();
        this.qG = true;
        Log.m285d("AmexPayProvider", "prepareNfcPay : Exit");
        return this.qG;
    }

    public synchronized byte[] handleApdu(byte[] bArr, Bundle bundle) {
        byte[] bArr2 = null;
        synchronized (this) {
            long currentTimeMillis = System.currentTimeMillis();
            Log.m285d("AmexPayProvider", "handleApdu: Enter: " + currentTimeMillis);
            if (!this.pn) {
                Log.m286e("AmexPayProvider", "handleApdu: ERROR: handleApdu must never be called when there is already a pending NFC");
            } else if (bArr == null) {
                Log.m286e("AmexPayProvider", "Error: apduBuffer received is NULL");
            } else {
                Log.m288m("AmexPayProvider", "HandlAPDU - Request = " + Utils.encodeHex(bArr));
                TokenAPDUResponse tokenAPDU = this.qB.tokenAPDU(bArr);
                if (!tokenAPDU.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                    Log.m286e("AmexPayProvider", "tokenAPDU failed on SDK");
                }
                bArr2 = tokenAPDU.getOutBuffer();
                if (AmexPayProvider.m790a(bArr, (short) 0) == ISO7816.GENERATE_AC && bArr2 != null) {
                    byte[] bArr3 = new byte[]{bArr2[bArr2.length - 2], bArr2[bArr2.length - 1]};
                    short s = (short) (((short) bArr3[1]) + (((short) bArr3[0]) * SkeinMac.SKEIN_256));
                    if (s == ISO7816.SW_NO_ERROR) {
                        Log.m285d("AmexPayProvider", "set amex nfc payment to true");
                        this.pr = true;
                    } else {
                        Log.m285d("AmexPayProvider", "amex nfc payment response code: " + s);
                    }
                    Log.m288m("AmexPayProvider", "HandlAPDU - Response = " + Utils.encodeHex(bArr2));
                }
                long currentTimeMillis2 = System.currentTimeMillis();
                Log.m287i("AmexPayProvider", "handleApdu: end: " + currentTimeMillis2 + "Time Taken = " + ((currentTimeMillis2 - currentTimeMillis) + 1));
            }
        }
        return bArr2;
    }

    protected synchronized Bundle stopNfcPay(int i) {
        Bundle bundle;
        short s = (short) 1;
        bundle = new Bundle();
        Log.m285d("AmexPayProvider", "stopNfcPay : Enter");
        if (!this.pn || !this.qG) {
            Log.m285d("AmexPayProvider", "stopNfcPay : ERROR: Stop NFC Pay called when payment is not in progress");
        } else if (this.pr) {
            s = (short) 2;
        } else {
            s = i == 4 ? (short) 4 : (short) 3;
            if (i == 4) {
                this.pr = false;
                bundle.putShort("nfcApduErrorCode", (short) 4);
            }
        }
        bundle.putShort("nfcApduErrorCode", s);
        this.pn = false;
        this.qH = false;
        this.pr = false;
        this.qG = false;
        Log.m285d("AmexPayProvider", "stopNfcPay: Exit reason : " + i + " ISO ret = " + s);
        return bundle;
    }

    protected synchronized void onPaySwitch(int i, int i2) {
        Log.m285d("AmexPayProvider", "onPaySwitch : Enter");
        super.onPaySwitch(i, i2);
        if (i == 1 && i2 == 2) {
            Log.m286e("AmexPayProvider", "onPaySwitch : ERROR: Payment mode switching from NFC to MST. Must never happen");
        } else {
            this.qH = true;
        }
        Log.m285d("AmexPayProvider", "onPaySwitch : Exit");
    }

    protected synchronized boolean prepareMstPay() {
        Log.m285d("AmexPayProvider", "prepareMstPay : Enter");
        cq();
        this.po = true;
        if (!this.qB.tokenMST().getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
            Log.m286e("AmexPayProvider", "prepareMstPay : ERROR: tokenMST failed on SDK");
            stopMstPay(false);
        }
        Log.m285d("AmexPayProvider", "prepareMstPay : Exit");
        return this.po;
    }

    public synchronized boolean startMstPay(int i, byte[] bArr) {
        boolean z;
        Log.m285d("AmexPayProvider", "startMstPay : Enter");
        z = false;
        if (this.pn) {
            Log.m285d("AmexPayProvider", "startMstPay : input config " + Arrays.toString(bArr));
            try {
                z = this.qA.m813a(i, bArr);
                if (!z) {
                    Log.m286e("AmexPayProvider", "startMstPay: ERROR: MST transmission failed");
                }
            } catch (Exception e) {
                Log.m286e("AmexPayProvider", "startMstPay: ERROR: MST transmission exception : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.m286e("AmexPayProvider", "startMstPay: ERROR: startMstPay must never happen when there is already a pending MST");
        }
        Log.m285d("AmexPayProvider", "startMstPay : Exit");
        return z;
    }

    protected synchronized void stopMstPay(boolean z) {
        Log.m285d("AmexPayProvider", "stopMstPay: Enter");
        this.pn = false;
        this.po = false;
        this.qH = false;
        Log.m285d("AmexPayProvider", "stopMstPay: Exit");
    }

    public synchronized void setPaymentFrameworkRequester(PaymentFrameworkRequester paymentFrameworkRequester) {
        ps = paymentFrameworkRequester;
    }

    private void av(String str) {
        Log.m285d("AmexPayProvider", "handleReplenishment:  Enter for token : " + str);
        if (qF) {
            Log.m286e("AmexPayProvider", "handleReplenishment : token is open, cannot replenish now, retry later");
        } else if (TextUtils.isEmpty(str)) {
            Log.m286e("AmexPayProvider", "handleReplenishment : ERROR: invalid Token ref ID ");
        } else {
            TokenRefreshStatusResponse tokenRefreshStatus = this.qB.tokenRefreshStatus(cx(), str);
            if (tokenRefreshStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                String pFTokenStatus = getPFTokenStatus();
                Log.m285d("AmexPayProvider", "handleReplenishment : pfTokenState : " + pFTokenStatus);
                String tokenState = tokenRefreshStatus.getTokenState();
                Log.m285d("AmexPayProvider", "handleReplenishment : tokenState : " + tokenState);
                if (tokenState == null) {
                    Log.m286e("AmexPayProvider", "handleReplenishment : ERROR: Token State is null");
                } else {
                    int lupcCount = tokenRefreshStatus.getLupcCount();
                    boolean isRefreshRequired = tokenRefreshStatus.isRefreshRequired();
                    long lupcRefreshCheckBack = tokenRefreshStatus.getLupcRefreshCheckBack();
                    Log.m285d("AmexPayProvider", "handleReplenishment : nfcLupcCount : " + lupcCount);
                    Log.m285d("AmexPayProvider", "handleReplenishment : refreshRequired : " + isRefreshRequired);
                    Log.m285d("AmexPayProvider", "handleReplenishment : checkBack : " + lupcRefreshCheckBack);
                    if (!isRefreshRequired || lupcCount != 0 || tokenRefreshStatus.getMaxATC() != 0 || tokenRefreshStatus.getTokenDataVersion() != null) {
                        this.pp.edit().putBoolean(str + "_reperso_required", false).apply();
                        if (tokenState.equals(Constants.SERVICE_CODE_LENGTH) && pFTokenStatus.equalsIgnoreCase(TokenStatus.SUSPENDED)) {
                            Log.m286e("AmexPayProvider", "handleReplenishment : ERROR: Token Suspended cannot trigger replenishment");
                        } else if (tokenState.equals(HCEClientConstants.API_INDEX_TOKEN_PERSO)) {
                            Log.m286e("AmexPayProvider", "handleReplenishment : ERROR: Token Suspended or in pending state, cannot trigger replenishment");
                        } else if (ax(str)) {
                            Log.m287i("AmexPayProvider", "handleReplenishment : Replenish Retry Alarm Set");
                        } else {
                            ProviderTokenKey providerTokenKey = new ProviderTokenKey(str);
                            providerTokenKey.setTrTokenId(str);
                            if (isRefreshRequired) {
                                Log.m287i("AmexPayProvider", "handleReplenishment : replenish token now");
                                cy();
                            } else {
                                long am = (1000 * lupcRefreshCheckBack) + Utils.am(this.mContext);
                                Log.m285d("AmexPayProvider", "handleReplenishment : keyReplenishTs : " + am);
                                TokenReplenishAlarm.m1070a(this.mContext, am, providerTokenKey);
                            }
                        }
                    } else if (pFTokenStatus.equalsIgnoreCase(TokenStatus.ACTIVE)) {
                        this.pp.edit().putBoolean(str + "_reperso_required", true).apply();
                        Log.m286e("AmexPayProvider", "handleReplenishment : ERROR: Token needs Re-perso");
                        cy();
                    } else {
                        Log.m286e("AmexPayProvider", "handleReplenishment : Not triggerring Re-perso as token is in " + pFTokenStatus + " state");
                    }
                }
            } else {
                Log.m286e("AmexPayProvider", "handleReplenishment : ERROR: tokenRefreshStatus failed on SDK" + tokenRefreshStatus.getReasonCode());
            }
        }
        Log.m285d("AmexPayProvider", "handleReplenishment:  Exit");
    }

    public synchronized void setupReplenishAlarm() {
        Log.m285d("AmexPayProvider", "Entered setup Replenish Alarm");
        String cn = this.mProviderTokenKey.cn();
        if (cn == null) {
            Log.m286e("AmexPayProvider", "TrTokenId is null");
        } else {
            av(cn);
        }
    }

    protected synchronized void replenishAlarmExpired() {
        if (this.mProviderTokenKey == null) {
            Log.m286e("AmexPayProvider", "replenishAlarmExpired : ERROR: cannot check replenishment, providerTokenKey is null");
        } else {
            av(this.mProviderTokenKey.cn());
        }
    }

    private void cy() {
        Log.m285d("AmexPayProvider", "fireReplenishmentRequest : Enter");
        if (this.mProviderTokenKey == null) {
            Log.m286e("AmexPayProvider", "fireReplenishmentRequest : ERROR: providerTokenKey is null");
        } else if (ps == null) {
            Log.m286e("AmexPayProvider", "fireReplenishmentRequest : ERROR: Uninitialized Common Framework requester");
        } else if (qF) {
            Log.m285d("AmexPayProvider", "fireReplenishmentRequest : Card selected, retry later");
        } else {
            ps.m311a(this.mProviderTokenKey);
        }
        Log.m285d("AmexPayProvider", "fireReplenishmentRequest : Exit");
    }

    private Bundle m789a(ProvisionTokenInfo provisionTokenInfo) {
        int parseInt;
        ArrayList arrayList = new ArrayList();
        Bundle bundle = new Bundle();
        List arrayList2 = new ArrayList();
        bundle.putSerializable("riskData", arrayList);
        Map activationParams = provisionTokenInfo.getActivationParams();
        arrayList.add(new RiskDataParam("networkOperator", DeviceInfo.getNetworkOperatorName(this.mContext)));
        arrayList.add(new RiskDataParam("networkType", Utils.aj(this.mContext) ? "wifi" : "cellular"));
        arrayList.add(new RiskDataParam("ipAddress", DeviceInfo.getLocalIpAddress()));
        String id = TimeZone.getDefault().getID();
        if (!(id == null || id.isEmpty())) {
            arrayList.add(new RiskDataParam("deviceTimezone", id));
        }
        arrayList.add(new RiskDataParam("timezoneSetByCarrier", Boolean.valueOf(DeviceInfo.getAutoTimeZone(this.mContext))));
        Location lastKnownLocation = DeviceInfo.getLastKnownLocation(this.mContext);
        if (lastKnownLocation != null) {
            arrayList.add(new RiskDataParam("deviceLatitude", lastKnownLocation.getLatitude() + BuildConfig.FLAVOR));
            arrayList.add(new RiskDataParam("deviceLongitude", lastKnownLocation.getLongitude() + BuildConfig.FLAVOR));
        }
        arrayList.add(new RiskDataParam("locale", Locale.getDefault().getLanguage() + HCEClientConstants.TAG_KEY_SEPARATOR + Locale.getDefault().getCountry()));
        FraudDataProvider fraudDataProvider = new FraudDataProvider(this.mContext);
        arrayList.add(new RiskDataParam(ActivationData.DEVICE_SCORE, Integer.valueOf(fraudDataProvider.m742E(CNCCCommands.CMD_CNCC_CMD_UNKNOWN).nk)));
        arrayList.add(new RiskDataParam(ActivationData.WALLET_ACCOUNT_SCORE, activationParams.get(ActivationData.WALLET_ACCOUNT_SCORE)));
        try {
            id = (String) activationParams.get(ActivationData.USER_ACCOUNT_FIRST_CREATED_IN_DAYS);
            if (!(id == null || id.isEmpty())) {
                parseInt = Integer.parseInt(id);
                String str = (parseInt / 7) + BuildConfig.FLAVOR;
                arrayList.add(new RiskDataParam("accountTenureOnFile", str));
                arrayList.add(new RiskDataParam("accountIdTenure", str));
                if (parseInt < 30) {
                    arrayList2.add("LT");
                }
            }
        } catch (Throwable e) {
            Log.m284c("AmexPayProvider", e.getMessage(), e);
        }
        try {
            id = (String) activationParams.get(ActivationData.WALLET_ACCOUNT_FIRST_CREATED_IN_DAYS);
            if (!(id == null || id.isEmpty())) {
                arrayList.add(new RiskDataParam("walletAccountTenure", (Integer.parseInt(id) / 7) + BuildConfig.FLAVOR));
            }
        } catch (Throwable e2) {
            Log.m284c("AmexPayProvider", e2.getMessage(), e2);
        }
        arrayList.add(new RiskDataParam("countryOnDevice", DeviceInfo.getDeviceCountry()));
        arrayList.add(new RiskDataParam("countryonAccountId", activationParams.get(ActivationData.WALLET_ACCOUNT_COUNTRY_CODE)));
        arrayList.add(new RiskDataParam(ActivationData.TOTAL_REGISTERED_DEVICES_FOR_ACCOUNT, activationParams.get(ActivationData.TOTAL_REGISTERED_DEVICES_FOR_ACCOUNT)));
        arrayList.add(new RiskDataParam(ActivationData.TOTAL_DEVICES_WITH_TOKEN_FOR_ACCOUNT, activationParams.get(ActivationData.TOTAL_DEVICES_WITH_TOKEN_FOR_ACCOUNT)));
        arrayList.add(new RiskDataParam("activeTokensCountForUser", activationParams.get(ActivationData.WALLET_ACC_ACTIVE_TOKENS_GIVEN_DEVICE)));
        arrayList.add(new RiskDataParam("daysSinceLastWalletActivity", activationParams.get(ActivationData.LAST_ACCOUNT_ACTIVITY_IN_DAYS)));
        arrayList.add(new RiskDataParam("walletTransactionsCount", activationParams.get(ActivationData.LAST_12_MONTHS_TRANSACTION_COUNT)));
        arrayList.add(new RiskDataParam(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS, activationParams.get(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS)));
        arrayList.add(new RiskDataParam(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS, activationParams.get(ActivationData.FIRST_TOKEN_REGISTERED_IN_WEEKS)));
        try {
            id = (String) activationParams.get(ActivationData.WALLET_ACCOUNT_DEVICE_BINDING_AGE_IN_DAYS);
            if (!(id == null || id.isEmpty())) {
                parseInt = Integer.parseInt(id);
                arrayList.add(new RiskDataParam("ageOfAcctOnDevice", (parseInt / 7) + BuildConfig.FLAVOR));
                if (parseInt < 30) {
                    arrayList2.add("GD");
                }
            }
        } catch (Throwable e22) {
            Log.m284c("AmexPayProvider", e22.getMessage(), e22);
        }
        try {
            arrayList.add(new RiskDataParam("noOfProvisioningAttempts", Integer.valueOf(fraudDataProvider.m743x(1))));
            arrayList.add(new RiskDataParam("tokensOnDeviceScore", Integer.valueOf(fraudDataProvider.m744y(CNCCCommands.CMD_CNCC_CMD_UNKNOWN))));
            parseInt = fraudDataProvider.m743x(30) + 1;
            Log.m285d("AmexPayProvider", "provAttempts : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XC");
            }
            if (parseInt >= 3) {
                arrayList2.add(PaymentFramework.CARD_BRAND_MASTERCARD);
            }
            parseInt = fraudDataProvider.m739B(30);
            Log.m285d("AmexPayProvider", "billingAddress : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XZ");
            }
            if (parseInt >= 3) {
                arrayList2.add("MZ");
            }
            parseInt = fraudDataProvider.m745z(30);
            Log.m285d("AmexPayProvider", "lastNames : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XN");
            }
            if (parseInt >= 3) {
                arrayList2.add("MN");
            }
            parseInt = fraudDataProvider.m740C(30);
            Log.m285d("AmexPayProvider", "resetCount : " + parseInt);
            if (parseInt >= 10) {
                arrayList2.add("XR");
            }
            try {
                id = (String) activationParams.get(ActivationData.WALLET_ACCOUNT_CARD_BINDING_AGE_IN_DAYS);
                if (!(id == null || id.isEmpty() || Integer.parseInt(id) >= 30)) {
                    arrayList2.add("LP");
                }
            } catch (Throwable e222) {
                Log.m284c("AmexPayProvider", e222.getMessage(), e222);
            }
            if (DeviceInfo.isVpnConnected(this.mContext) || DeviceInfo.isProxyEnabled(this.mContext)) {
                arrayList2.add("GV");
            }
            arrayList.add(new RiskDataParam("reasonCodes", arrayList2));
        } catch (Throwable e2222) {
            Log.m284c("AmexPayProvider", e2222.getMessage(), e2222);
        }
        return bundle;
    }

    protected synchronized ProviderRequestData getReplenishmentRequestData() {
        ProviderRequestData providerRequestData;
        String str = null;
        synchronized (this) {
            Log.m285d("AmexPayProvider", "getReplenishmentRequestData : Enter");
            providerRequestData = new ProviderRequestData();
            providerRequestData.setErrorCode(-2);
            if (this.mProviderTokenKey == null) {
                providerRequestData.setErrorCode(-4);
                Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: Invalid token");
            } else if (qF) {
                Log.m286e("AmexPayProvider", "getReplenishmentRequestData : Card selected and in use, retry later");
            } else {
                JsonObject jsonObject = new JsonObject();
                String cn = this.mProviderTokenKey.cn();
                Log.m285d("AmexPayProvider", "getReplenishmentRequestData : Token Ref Id = " + cn);
                byte[] d = LLVARByteUtil.m820d(new String[]{LLVARUtil.PLAIN_TEXT + cn});
                int open = this.qA.open(d);
                if (open < 0) {
                    Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: TA open failed = " + open);
                    providerRequestData.setErrorCode(-6);
                } else {
                    byte[] bArr = new byte[384];
                    int initializeSecureChannel = this.qA.initializeSecureChannel(null, bArr);
                    if (initializeSecureChannel < 0) {
                        Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA initializeSecureChannel failed = " + initializeSecureChannel);
                        providerRequestData.setErrorCode(-6);
                    } else {
                        bArr = Arrays.copyOfRange(bArr, 0, initializeSecureChannel);
                        Log.m285d("AmexPayProvider", "getReplenishmentRequestData : Ephemeral Key in LLVAR  = " + new String(bArr));
                        byte[][] llVarToBytes = LLVARByteUtil.llVarToBytes(bArr);
                        if (llVarToBytes == null || llVarToBytes[0] == null) {
                            Log.m285d("AmexPayProvider", "ephemeralPublicKeyBytes or ephemeralPublicKeyBytes[0] is null");
                        } else {
                            str = new String(llVarToBytes[0]);
                        }
                        Log.m285d("AmexPayProvider", "getReplenishmentRequestData : eccPublicKey = " + str);
                        try {
                            AmexTAController.AmexTAController cA = this.qA.cA();
                            if (cA == null) {
                                Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA getDeviceCertificates failed");
                                providerRequestData.setErrorCode(-6);
                            } else {
                                TokenRefreshStatusResponse tokenRefreshStatus = this.qB.tokenRefreshStatus(cx(), cn);
                                if (tokenRefreshStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                                    Log.m285d("AmexPayProvider", "getReplenishmentRequestData : getClientVersion = " + tokenRefreshStatus.getClientVersion());
                                    Log.m285d("AmexPayProvider", "getReplenishmentRequestData : getTokenDataVersion = " + tokenRefreshStatus.getTokenDataVersion());
                                    Log.m285d("AmexPayProvider", "getReplenishmentRequestData : getMaxATC = " + tokenRefreshStatus.getMaxATC());
                                    Log.m285d("AmexPayProvider", "getReplenishmentRequestData : getLupcCount = " + tokenRefreshStatus.getLupcCount());
                                    JsonElement jsonObject2 = new JsonObject();
                                    if (this.pp.getBoolean(cn + "_reperso_required", false)) {
                                        JsonObject jsonObject3 = new JsonObject();
                                        jsonObject2.addProperty("ephemeral_public_key", str);
                                        jsonObject2.addProperty("amex_device_public_key", cA.deviceCertificate);
                                        jsonObject2.addProperty("amex_device_signing_public_key", cA.deviceSigningCertificate);
                                        jsonObject2.addProperty("client_api_version", tokenRefreshStatus.getClientVersion());
                                        jsonObject2.addProperty("token_data_version", BuildConfig.FLAVOR);
                                        jsonObject2.addProperty("maximum_atc", Integer.toString(tokenRefreshStatus.getMaxATC()));
                                        jsonObject2.addProperty("remaining_lupc_count", Integer.toString(tokenRefreshStatus.getLupcCount()));
                                        jsonObject3.add("security_data", jsonObject2);
                                        Log.m287i("AmexPayProvider", "getReplenishmentRequestData : TSP Enc cert : " + this.pl.getContent());
                                        try {
                                            str = AmexPayProvider.m793c(jsonObject3).toString();
                                            Log.m287i("AmexPayProvider", "getReplenishmentRequestData : inputJwsString : " + str);
                                            byte[] a = this.qA.m814a(JsonOperation.JWS, null, str.getBytes(), this.pl.getContent());
                                            if (a == null) {
                                                Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processDataJwsJwe failed on JWS");
                                                providerRequestData.setErrorCode(-6);
                                            } else {
                                                byte[] a2 = this.qA.m814a(JsonOperation.JWE, this.qC.getBytes(), null, this.pl.getContent());
                                                if (a2 == null) {
                                                    Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processDataJwsJwe failed on JWE");
                                                    providerRequestData.setErrorCode(-6);
                                                } else {
                                                    Log.m285d("AmexPayProvider", "getReplenishmentRequestData : JWS String: " + new String(a));
                                                    Log.m285d("AmexPayProvider", "getReplenishmentRequestData : JWE String: " + new String(a2));
                                                    JsonElement jsonObject4 = new JsonObject();
                                                    jsonObject4.addProperty("securityDataSignature", new String(a));
                                                    jsonObject.addProperty("encryptedPayload", new String(a2));
                                                    jsonObject.addProperty("persoVersion", TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
                                                    jsonObject.add("signatureData", jsonObject4);
                                                }
                                            }
                                        } catch (AmexTAException e) {
                                            Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: Cannot perform JWS/JWE on Input data");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        jsonObject2.addProperty("ephemeralPublicKey", str);
                                        if (cA.deviceCertificate != null) {
                                            jsonObject2.addProperty("devicePublicKeyCert", cA.deviceCertificate);
                                        }
                                        if (cA.deviceSigningCertificate != null) {
                                            jsonObject2.addProperty("deviceSigningPublicKeyCert", cA.deviceSigningCertificate);
                                        }
                                        jsonObject2.addProperty("clientAPIVersion", tokenRefreshStatus.getClientVersion());
                                        jsonObject2.addProperty("tokenDataVersion", tokenRefreshStatus.getTokenDataVersion());
                                        jsonObject2.addProperty("maxATC", Integer.valueOf(tokenRefreshStatus.getMaxATC()));
                                        jsonObject2.addProperty("remainingLUPCCount", Integer.valueOf(tokenRefreshStatus.getLupcCount()));
                                        try {
                                            AmexTAController.AmexTAController c = this.qA.m817c(this.pl.getContent(), null, null);
                                            if (c == null) {
                                                Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processRequestData failed");
                                                providerRequestData.setErrorCode(-2);
                                            } else {
                                                jsonObject.addProperty("encryptedData", c.encryptedRequestData);
                                                jsonObject.addProperty("encryptionParameters", c.encryptionParams);
                                                jsonObject.add("secureDeviceData", jsonObject2);
                                                jsonObject.addProperty("responseCode", tokenRefreshStatus.getReasonCode());
                                                jsonObject.addProperty("detailCode", tokenRefreshStatus.getDetailCode());
                                                jsonObject.addProperty("detailMessage", tokenRefreshStatus.getDetailMessage());
                                                jsonObject.addProperty("secureTokenDataSignature", "null");
                                            }
                                        } catch (AmexTAException e2) {
                                            Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: AmexTA processRequestData failed");
                                            providerRequestData.setErrorCode(-2);
                                            e2.printStackTrace();
                                        }
                                    }
                                    if (this.qA.close(d) != 0) {
                                        Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: TA close failed");
                                        providerRequestData.setErrorCode(-6);
                                    } else {
                                        providerRequestData.m822a(AmexPayProvider.m793c(jsonObject));
                                        providerRequestData.setErrorCode(0);
                                    }
                                } else {
                                    Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: tokenRefreshStatus failed on SDK");
                                    providerRequestData.setErrorCode(-2);
                                }
                            }
                        } catch (AmexTAException e22) {
                            Log.m286e("AmexPayProvider", "getReplenishmentRequestData : ERROR: Cannot fetch device certificates");
                            providerRequestData.setErrorCode(-6);
                            e22.printStackTrace();
                        }
                    }
                }
            }
            Log.m285d("AmexPayProvider", "getReplenishmentRequestData : Exit");
        }
        return providerRequestData;
    }

    protected synchronized ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        ProviderResponseData providerResponseData;
        Log.m287i("AmexPayProvider", "replenishToken : Enter");
        providerResponseData = new ProviderResponseData();
        try {
            providerResponseData.setErrorCode(0);
            if (jsonObject == null) {
                Log.m286e("AmexPayProvider", "replenishToken : ERROR: Input Data is NULL");
                providerResponseData.setErrorCode(-4);
            } else {
                String asString;
                String asString2;
                String asString3;
                String str;
                String str2;
                JsonObject asJsonObject = jsonObject.getAsJsonObject("secureTokenData");
                if (asJsonObject != null) {
                    asString = asJsonObject.get("initializationVector").getAsString();
                    asString2 = asJsonObject.get("encryptedTokenData").getAsString();
                    asString3 = asJsonObject.get("encryptedTokenDataHMAC").getAsString();
                    str = asString2;
                    str2 = asString;
                    asString2 = asJsonObject.get("cloudPublicKeyCert").getAsString();
                    asString = asString3;
                } else {
                    Log.m286e("AmexPayProvider", "replenishToken : secureTokenData is NULL");
                    asString2 = null;
                    asString = null;
                    str = null;
                    str2 = null;
                }
                if (this.kQ != null) {
                    asString3 = this.kQ.getWalletId();
                } else {
                    asString3 = ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID);
                }
                if (asString3 == null) {
                    Log.m286e("AmexPayProvider", "replenishToken : ERROR: Wallet Id is NULL");
                    providerResponseData.setErrorCode(-2);
                } else if (AmexPayProvider.au(jsonObject.get(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS).getAsString()) == null) {
                    Log.m286e("AmexPayProvider", "replenishToken : ERROR: Token Status is NULL");
                    providerResponseData.setErrorCode(-2);
                } else {
                    String str3;
                    StringBuilder append = new StringBuilder().append(asString2);
                    if (this.pm == null) {
                        str3 = BuildConfig.FLAVOR;
                    } else {
                        str3 = this.pm.getContent();
                    }
                    str3 = append.append(str3).toString();
                    asString2 = this.mProviderTokenKey.cn();
                    if (this.qB.tokenOpen(OperationMode.PROVISION, asString2).getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                        str3 = str3.replace("\n", BuildConfig.FLAVOR);
                        if (this.qB.tokenChannelUpdate(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + str3, LLVARUtil.HEX_STRING + Utils.encodeHex(asString3.getBytes()))).getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                            TokenPersoResponse tokenPerso = this.qB.tokenPerso(HexUtils.getSafePrintChars(LLVARUtil.objectsToLLVar(LLVARUtil.PLAIN_TEXT + str, LLVARUtil.PLAIN_TEXT + str2, LLVARUtil.PLAIN_TEXT + asString)));
                            TokenOperationStatus tokenClose = this.qB.tokenClose();
                            if (tokenPerso.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                                this.pp.edit().putString(asString2 + "_config_string", tokenPerso.getTokenConfiguration()).apply();
                                aB(tokenPerso.getTokenConfiguration());
                                if (tokenClose.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                                    if (getPFTokenStatus() != TokenStatus.ACTIVE) {
                                        setPFTokenStatus(TokenStatus.ACTIVE);
                                    }
                                    JsonElement jsonObject2 = new JsonObject();
                                    try {
                                        AmexTAController.AmexTAController cA = this.qA.cA();
                                        if (cA.deviceCertificate != null) {
                                            jsonObject2.addProperty("devicePublicKeyCert", cA.deviceCertificate);
                                        }
                                        if (cA.deviceSigningCertificate != null) {
                                            jsonObject2.addProperty("deviceSigningPublicKeyCert", cA.deviceSigningCertificate);
                                        }
                                        providerResponseData.setProviderTokenKey(new ProviderTokenKey(asString2));
                                        JsonObject jsonObject3 = new JsonObject();
                                        if (this.pp.getBoolean(asString2 + "_reperso_required", false)) {
                                            jsonObject3.addProperty("persoVersion", TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
                                            JsonElement jsonObject4 = new JsonObject();
                                            jsonObject4.addProperty("secureTokenDataSignature", tokenPerso.getTokenDataSignature());
                                            jsonObject3.add("signatureData", jsonObject4);
                                        } else {
                                            jsonObject3.addProperty("secureTokenDataSignature", tokenPerso.getTokenDataSignature());
                                        }
                                        jsonObject3.add("secureDeviceData", jsonObject2);
                                        providerResponseData.m1057b(jsonObject3);
                                        this.pp.edit().remove(asString2 + "_replenish_retry").apply();
                                        ps.m312b(this.mProviderTokenKey);
                                        av(asString2);
                                    } catch (AmexTAException e) {
                                        providerResponseData.setErrorCode(-2);
                                    }
                                } else {
                                    Log.m286e("AmexPayProvider", "replenishToken : ERROR: tokenClose failed on SDK");
                                    providerResponseData.setErrorCode(-2);
                                }
                            } else {
                                Log.m286e("AmexPayProvider", "replenishToken : ERROR: tokenPerso failed on SDK");
                                providerResponseData.setErrorCode(-2);
                            }
                        } else {
                            Log.m286e("AmexPayProvider", "replenishToken : ERROR: tokenChannelUpdate failed on SDK");
                            providerResponseData.setErrorCode(-2);
                        }
                    } else {
                        Log.m286e("AmexPayProvider", "replenishToken : ERROR: tokenOpen failed on SDK");
                        providerResponseData.setErrorCode(-2);
                    }
                }
            }
        } catch (Throwable e2) {
            Log.m284c("AmexPayProvider", e2.getMessage(), e2);
            cy();
            providerResponseData.setErrorCode(-2);
        }
        return providerResponseData;
    }

    protected synchronized ProviderRequestData getVerifyIdvRequestData(VerifyIdvInfo verifyIdvInfo) {
        ProviderRequestData providerRequestData;
        providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (verifyIdvInfo == null) {
            try {
                providerRequestData.setErrorCode(-4);
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                providerRequestData.setErrorCode(-2);
            }
        } else {
            AmexTAController.AmexTAController c;
            JsonElement jsonObject = new JsonObject();
            try {
                AmexTAController.AmexTAController cA = this.qA.cA();
                if (cA.deviceCertificate != null) {
                    jsonObject.addProperty("devicePublicKeyCert", cA.deviceCertificate);
                }
                if (cA.deviceSigningCertificate != null) {
                    jsonObject.addProperty("deviceSigningPublicKeyCert", cA.deviceSigningCertificate);
                }
            } catch (Exception e2) {
                providerRequestData.setErrorCode(-2);
                e2.printStackTrace();
            }
            try {
                c = this.qA.m817c(this.pl.getContent(), null, verifyIdvInfo.getValue());
            } catch (Throwable e3) {
                providerRequestData.setErrorCode(-2);
                Log.m284c("AmexPayProvider", e3.getMessage(), e3);
                c = null;
            }
            if (c == null) {
                providerRequestData.setErrorCode(-2);
            } else {
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("authenticationCodeSignature", c.requestDataSignature);
                jsonObject2.add("secureDeviceData", jsonObject);
                providerRequestData.m822a(jsonObject2);
            }
        }
        return providerRequestData;
    }

    protected synchronized ProviderRequestData getDeleteRequestData(Bundle bundle) {
        ProviderRequestData providerRequestData;
        Log.m285d("AmexPayProvider", "getDeleteRequestData : Enter");
        providerRequestData = new ProviderRequestData();
        JsonObject jsonObject = new JsonObject();
        if (this.mProviderTokenKey == null) {
            Log.m286e("AmexPayProvider", "getDeleteRequestData : ERROR: Token not found");
        } else {
            String cn = this.mProviderTokenKey.cn();
            if (cn == null) {
                cn = this.mProviderTokenKey.getTrTokenId();
            }
            Log.m285d("AmexPayProvider", "getDeleteRequestData : tokenRefID : " + cn);
            TokenRefreshStatusResponse tokenRefreshStatus = this.qB.tokenRefreshStatus(cx(), cn);
            if (tokenRefreshStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                Log.m285d("AmexPayProvider", "getDeleteRequestData : ClientVersion = " + tokenRefreshStatus.getClientVersion());
                Log.m285d("AmexPayProvider", "getDeleteRequestData : TokenDataVersion = " + tokenRefreshStatus.getTokenDataVersion());
                JsonElement jsonObject2 = new JsonObject();
                try {
                    AmexTAController.AmexTAController cA = this.qA.cA();
                    if (cA.deviceCertificate != null) {
                        jsonObject2.addProperty("devicePublicKeyCert", cA.deviceCertificate);
                    }
                    if (cA.deviceSigningCertificate != null) {
                        jsonObject2.addProperty("deviceSigningPublicKeyCert", cA.deviceSigningCertificate);
                    }
                    jsonObject2.addProperty("clientAPIVersion", tokenRefreshStatus.getClientVersion());
                    jsonObject2.addProperty("tokenDataVersion", tokenRefreshStatus.getTokenDataVersion());
                    Log.m285d("AmexPayProvider", "getDeleteRequestData : token status : " + tokenRefreshStatus.getReasonCode() + " : " + tokenRefreshStatus.getDetailCode());
                    jsonObject.addProperty("tokenRefId", cn);
                    jsonObject.addProperty("responseCode", tokenRefreshStatus.getReasonCode());
                    jsonObject.addProperty("detailCode", tokenRefreshStatus.getDetailCode());
                    String jsonObject3 = AmexPayProvider.m793c(jsonObject).toString();
                    Log.m288m("AmexPayProvider", "getDeleteRequestData : Sorted JSON Data" + jsonObject3);
                    try {
                        AmexTAController.AmexTAController c = this.qA.m817c(this.pl.getContent(), null, jsonObject3);
                        if (c == null) {
                            Log.m286e("AmexPayProvider", "getDeleteRequestData : ERROR: Signed data is null");
                        } else {
                            jsonObject.addProperty("secureTokenDataSignature", c.requestDataSignature);
                            jsonObject.add("secureDeviceData", jsonObject2);
                            jsonObject3 = this.pp.getString(cn + "_transaction_json_data", null);
                            cn = this.pp.getString(cn + "_transaction_param", null);
                            if (jsonObject3 == null || cn == null) {
                                Log.m286e("AmexPayProvider", "getDeleteRequestData : WARNING: Access key not found");
                            } else {
                                try {
                                    cn = this.qA.m818o(jsonObject3, cn);
                                    if (cn == null) {
                                        Log.m286e("AmexPayProvider", "getDeleteRequestData : ERROR: Cannot decrypt Access Key");
                                    }
                                    AmexTAController.AmexTAController c2 = this.qA.m817c(this.pl.getContent(), new String(Base64.decode(cn, 2)), null);
                                    if (c2 != null) {
                                        jsonObject.addProperty("encryptedData", c2.encryptedRequestData);
                                        jsonObject.addProperty("encryptionParameters", c2.encryptionParams);
                                    } else {
                                        Log.m286e("AmexPayProvider", "getDeleteRequestData : ERROR: Cannot prepare Access Key payload");
                                    }
                                } catch (Throwable e) {
                                    Log.m284c("AmexPayProvider", "getDeleteRequestData : ERROR: Invalid encryptedData" + e.getMessage(), e);
                                }
                            }
                            providerRequestData.m822a(jsonObject);
                            providerRequestData.setErrorCode(0);
                        }
                    } catch (Throwable e2) {
                        Log.m284c("AmexPayProvider", "getDeleteRequestData : ERROR: Unable to sign data" + e2.getMessage(), e2);
                    }
                } catch (AmexTAException e3) {
                    Log.m286e("AmexPayProvider", "getDeleteRequestData : ERROR: Unable to get device certificates");
                    e3.printStackTrace();
                }
            } else {
                Log.m286e("AmexPayProvider", "getDeleteRequestData : ERROR: tokenRefreshStatus failed on SDK");
            }
        }
        Log.m285d("AmexPayProvider", "getDeleteRequestData : Exit");
        return providerRequestData;
    }

    public synchronized void delete() {
        Log.m285d("AmexPayProvider", "delete : Enter");
        if (this.kQ != null) {
            this.kQ.decrementRefCount();
        } else {
            Log.m285d("AmexPayProvider", "delete : Card info is null, do nothing");
        }
        Log.m285d("AmexPayProvider", "delete : Exit");
    }

    protected synchronized ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        ProviderResponseData providerResponseData;
        JsonObject jsonObject2 = new JsonObject();
        providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        jsonObject2.addProperty("responseCode", HCEClientConstants.HEX_ZERO_BYTE);
        Log.m285d("AmexPayProvider", "updateTokenStatus : Enter");
        if (tokenStatus == null) {
            providerResponseData.setErrorCode(-4);
        } else if (this.mProviderTokenKey != null) {
            String cn = this.mProviderTokenKey.cn();
            Log.m285d("AmexPayProvider", "updateTokenStatus : Token Ref ID = " + cn);
            TokenRefreshStatusResponse tokenRefreshStatus = this.qB.tokenRefreshStatus(cx(), cn);
            if (tokenRefreshStatus != null && tokenRefreshStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                String tokenState = tokenRefreshStatus.getTokenState();
                Log.m285d("AmexPayProvider", "updateTokenStatus : tokenState from sdk: " + tokenState);
                if (tokenStatus.getCode().equals(TokenStatus.ACTIVE)) {
                    if (tokenRefreshStatus.isRefreshRequired() && tokenRefreshStatus.getLupcCount() == 0 && tokenRefreshStatus.getMaxATC() == 0 && tokenRefreshStatus.getTokenDataVersion() == null) {
                        Log.m285d("AmexPayProvider", "updateTokenStatus : Triggerring a Re-perso for token : " + cn + " which was in " + getPFTokenStatus() + " state");
                        this.pp.edit().putBoolean(cn + "_reperso_required", true).apply();
                    } else if (tokenState == null) {
                        Log.m286e("AmexPayProvider", "updateTokenStatus : ERROR: Token State is null");
                        providerResponseData.setErrorCode(-2);
                    } else if (tokenState.equals(HCEClientConstants.API_INDEX_TOKEN_PERSO)) {
                        Log.m285d("AmexPayProvider", "updateTokenStatus : Activating Token");
                    } else if (tokenState.equals(Constants.SERVICE_CODE_LENGTH)) {
                        Log.m285d("AmexPayProvider", "updateTokenStatus : Resuming Token");
                    } else if (tokenState.equals(HCEClientConstants.API_INDEX_TOKEN_OPEN)) {
                        Log.m285d("AmexPayProvider", "updateTokenStatus : Token already in active state, nothing to do");
                    }
                    cy();
                } else if (!tokenStatus.getCode().equals(TokenStatus.SUSPENDED) && !tokenStatus.getCode().equals(TokenStatus.DISPOSED)) {
                    Log.m286e("AmexPayProvider", "updateTokenStatus : ERROR: Unknown Token Status : " + tokenStatus.getCode());
                    providerResponseData.setErrorCode(-5);
                } else if (TextUtils.isEmpty(cn)) {
                    Log.m285d("AmexPayProvider", "Nothing to delete : ");
                } else if (this.qB.tokenOpen(OperationMode.LCM, cn).getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                    TokenOperationStatus tokenLCM;
                    if (tokenStatus.getCode().equals(TokenStatus.SUSPENDED)) {
                        Log.m285d("AmexPayProvider", "updateTokenStatus : Suspending Token");
                        setPFTokenStatus(TokenStatus.SUSPENDED);
                        tokenLCM = this.qB.tokenLCM(StateMode.SUSPEND);
                    } else {
                        Log.m285d("AmexPayProvider", "updateTokenStatus : Deleting Token");
                        setPFTokenStatus(TokenStatus.DISPOSED);
                        tokenLCM = this.qB.tokenLCM(StateMode.DELETE);
                        this.pp.edit().remove(cn + "_transaction_json_data").remove(cn + "_replenish_retry").apply();
                        this.qA.aC(cn);
                    }
                    TokenReplenishAlarm.m1071a(this.mContext, this.mProviderTokenKey);
                    if (tokenLCM != null) {
                        Log.m285d("AmexPayProvider", "updateTokenStatus : LCM status = " + tokenLCM.getReasonCode() + " : " + tokenLCM.getDetailCode());
                        if (!tokenLCM.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                            providerResponseData.setErrorCode(-2);
                        }
                        jsonObject2.addProperty("responseCode", tokenLCM.getReasonCode());
                    } else {
                        Log.m286e("AmexPayProvider", "updateTokenStatus : ERROR: Error processing tokenLCM");
                        providerResponseData.setErrorCode(-2);
                    }
                    if (!this.qB.tokenClose().getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                        Log.m286e("AmexPayProvider", "updateTokenStatus : ERROR: tokenClose failed on SDK");
                        providerResponseData.setErrorCode(-2);
                    }
                } else {
                    Log.m286e("AmexPayProvider", "updateTokenStatus : ERROR: tokenOpen failed on SDK");
                    providerResponseData.setErrorCode(-2);
                }
            } else if (tokenStatus.getCode().equals(TokenStatus.ACTIVE) || tokenStatus.getCode().equals(TokenStatus.SUSPENDED)) {
                providerResponseData.setErrorCode(-5);
                Log.m286e("AmexPayProvider", "updateTokenStatus : ERROR: Unknown Token : " + cn);
            } else {
                Log.m286e("AmexPayProvider", "updateTokenStatus : ERROR: Token already deleted");
            }
        } else if (!tokenStatus.getCode().equals(TokenStatus.DISPOSED)) {
            Log.m285d("AmexPayProvider", "updateTokenStatus : ERROR: Invalid token, Token may not exist");
            providerResponseData.setErrorCode(-4);
        }
        providerResponseData.m1057b(jsonObject2);
        Log.m285d("AmexPayProvider", "updateTokenStatus : Exit");
        return providerResponseData;
    }

    public synchronized int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        return AmexTransactionManager.m803a(this.mContext, this.qA, this.pl, this.pp).m806a(this.mProviderTokenKey, bundle, transactionResponse);
    }

    protected synchronized TransactionDetails processTransactionData(Object obj) {
        return AmexTransactionManager.m803a(this.mContext, this.qA, this.pl, this.pp).m807a(this.mProviderTokenKey, obj);
    }

    private Bundle m792b(EnrollCardInfo enrollCardInfo) {
        Bundle bundle;
        Throwable e;
        try {
            bundle = new Bundle();
            try {
                if (enrollCardInfo.getUserEmail() != null) {
                    bundle.putString("emailHash", AmexUtils.az(enrollCardInfo.getUserEmail()));
                }
                bundle.putString("appId", this.mContext.getPackageName());
            } catch (Exception e2) {
                e = e2;
                Log.m284c("AmexPayProvider", e.getMessage(), e);
                return bundle;
            }
        } catch (Throwable e3) {
            Throwable th = e3;
            bundle = null;
            e = th;
            Log.m284c("AmexPayProvider", e.getMessage(), e);
            return bundle;
        }
        return bundle;
    }

    protected synchronized void clearCard() {
        Log.m285d("AmexPayProvider", "clearCard : Enter");
        if (qF) {
            qF = false;
            TokenOperationStatus tokenClose = this.qB.tokenClose();
            if (!tokenClose.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                Log.m286e("AmexPayProvider", "clearCard : ERROR : tokenClose failed on SDK " + tokenClose.getReasonCode());
            } else if (this.pj != null) {
                Log.m287i("AmexPayProvider", "clearCard : check if replenishment required");
                av(this.pj.cn());
            }
        }
        Log.m285d("AmexPayProvider", "clearCard : Card selection cleared");
        this.pj = null;
        Log.m285d("AmexPayProvider", "clearCard : Exit");
    }

    protected synchronized void interruptMstPay() {
    }

    public synchronized boolean getPayReadyState() {
        boolean z = false;
        synchronized (this) {
            Log.m285d("AmexPayProvider", "getPayReadyState : Enter");
            if (this.mProviderTokenKey == null) {
                Log.m286e("AmexPayProvider", "getPayReadyState : ERROR : No token to check for readiness");
            } else if (this.pj != null) {
                Log.m286e("AmexPayProvider", "getPayReadyState : WARNING : Token Busy");
                z = true;
            } else {
                String cn = this.mProviderTokenKey.cn();
                Log.m285d("AmexPayProvider", "getPayReadyState : Token ID :  " + cn);
                TokenRefreshStatusResponse tokenRefreshStatus = this.qB.tokenRefreshStatus(cx(), cn);
                if (tokenRefreshStatus.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                    int lupcCount = tokenRefreshStatus.getLupcCount();
                    boolean isRefreshRequired = tokenRefreshStatus.isRefreshRequired();
                    Log.m285d("AmexPayProvider", "getPayReadyState : LUPC Count : " + lupcCount);
                    if (isRefreshRequired && lupcCount == 0 && tokenRefreshStatus.getMaxATC() == 0 && tokenRefreshStatus.getTokenDataVersion() == null) {
                        this.pp.edit().putBoolean(cn + "_reperso_required", true);
                        Log.m286e("AmexPayProvider", "getPayReadyState : ERROR : Token needs Re-perso");
                        cy();
                    } else {
                        this.pp.edit().putBoolean(cn + "_reperso_required", false);
                        if (lupcCount == 0) {
                            Log.m286e("AmexPayProvider", "getPayReadyState : ERROR : Token unavailable for payment");
                        } else if (TextUtils.isEmpty(tokenRefreshStatus.getTokenState()) || tokenRefreshStatus.getTokenState().equalsIgnoreCase(HCEClientConstants.API_INDEX_TOKEN_OPEN)) {
                            if (isRefreshRequired) {
                                Log.m285d("AmexPayProvider", "getPayReadyState : WARNING : Token needs to be replenished");
                            }
                            z = true;
                        } else {
                            Log.m286e("AmexPayProvider", "getPayReadyState : ERROR : Token unavailable for payment :" + tokenRefreshStatus.getTokenState());
                        }
                    }
                } else {
                    Log.m286e("AmexPayProvider", "getPayReadyState : ERROR : tokenRefreshStatus failed on SDK " + tokenRefreshStatus.getReasonCode());
                }
            }
            Log.m285d("AmexPayProvider", "getPayReadyState : Exit");
        }
        return z;
    }

    public synchronized void updateRequestStatus(ProviderRequestStatus providerRequestStatus) {
        Log.m285d("AmexPayProvider", "updateRequestStatus : Enter: Req type = " + providerRequestStatus.getRequestType() + ", Req status = " + providerRequestStatus.ci());
        if (providerRequestStatus.ck() != null) {
            Log.m285d("AmexPayProvider", "updateRequestStatus : Token key string = " + providerRequestStatus.ck().cn());
        }
        if (providerRequestStatus.getRequestType() != 23) {
            switch (providerRequestStatus.ci()) {
                case RequestedCertificate.certificate /*-1*/:
                    if (providerRequestStatus.ck() != null) {
                        this.qA.aC(this.qD);
                    }
                    if (providerRequestStatus.getRequestType() == 11 && providerRequestStatus.cj().equals(ErrorResponseData.ERROR_CODE_REPLENISH_EXCEEDED) && providerRequestStatus.ck() != null) {
                        m791a(providerRequestStatus.ck(), true);
                        ax(providerRequestStatus.ck().cn());
                        break;
                    }
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                    if (providerRequestStatus.getRequestType() == 3 && providerRequestStatus.ck() != null) {
                        this.qA.m819q(this.qD, providerRequestStatus.ck().cn());
                        break;
                    }
                    break;
                default:
                    Log.m286e("AmexPayProvider", "Error in updating status");
                    break;
            }
        }
        Log.m285d("AmexPayProvider", "updateRequestStatus : Exit");
    }

    public synchronized void checkIfReplenishmentNeeded(TransactionData transactionData) {
        Log.m285d("AmexPayProvider", "checkIfReplenishmentNeeded : Enter");
        av(this.mProviderTokenKey.cn());
        Log.m285d("AmexPayProvider", "checkIfReplenishmentNeeded : Exit");
    }

    public synchronized boolean isReplenishDataAvailable(JsonObject jsonObject) {
        boolean z;
        JsonElement jsonElement = jsonObject.get("certificateIdentifier");
        if (jsonElement == null) {
            Log.m285d("AmexPayProvider", "isReplenishDataAvailable: Certificate Id is not present");
            this.qC = BuildConfig.FLAVOR;
        } else {
            this.qC = jsonElement.getAsString();
            Log.m285d("AmexPayProvider", "isReplenishDataAvailable: Certificate Id is  present : " + this.qC);
        }
        if (jsonObject.getAsJsonObject("secureTokenData") == null) {
            Log.m285d("AmexPayProvider", "isReplenishDataAvailable: returns : false");
            z = false;
        } else {
            Log.m285d("AmexPayProvider", "isReplenishDataAvailable: returns : true");
            z = true;
        }
        return z;
    }

    protected synchronized boolean allowPaymentRetry() {
        Log.m285d("AmexPayProvider", "allowPaymentRetry : returns : true");
        return true;
    }

    private void m791a(ProviderTokenKey providerTokenKey, boolean z) {
        String str = null;
        String string = this.pp.getString(providerTokenKey.cn() + "_replenish_retry", null);
        Log.m286e("AmexPayProvider", "incrementReplenishRetryCount : " + z);
        if (Utils.al(this.mContext)) {
            str = string;
        } else {
            z = false;
        }
        if (z) {
            str = "4|" + ((Utils.am(this.mContext) + 86400000) + 600000);
        } else {
            int i;
            long j;
            long am = Utils.am(this.mContext);
            if (str != null) {
                int parseInt = Integer.parseInt(str.split("|")[0]);
                switch (parseInt) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        i = parseInt + 1;
                        j = am + 600000;
                        break;
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        i = parseInt + 1;
                        j = 5400000 + am;
                        break;
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        i = parseInt + 1;
                        j = 10800000 + am;
                        break;
                    default:
                        i = 4;
                        j = am + 86400000;
                        break;
                }
            }
            Log.m290w("AmexPayProvider", "Retry Data is Empty");
            i = 1;
            j = am + 600000;
            str = i + "|" + j;
            Log.m287i("AmexPayProvider", "Retry Count :" + i);
        }
        this.pp.edit().putString(providerTokenKey.cn() + "_replenish_retry", str).apply();
    }

    private boolean ax(String str) {
        boolean z = true;
        Log.m285d("AmexPayProvider", "setupReplenishRetryAlarm : Enter");
        String string = this.pp.getString(str + "_replenish_retry", null);
        if (string != null) {
            try {
                String[] split = string.split("\\|");
                for (String str2 : split) {
                    Log.m285d("AmexPayProvider", "setupReplenishRetryAlarm : retryParts : " + str2);
                }
                long parseLong = Long.parseLong(split[1]);
                Log.m285d("AmexPayProvider", "setupReplenishRetryAlarm : retryTime : " + parseLong);
                ProviderTokenKey providerTokenKey = new ProviderTokenKey(str);
                providerTokenKey.setTrTokenId(str);
                TokenReplenishAlarm.m1070a(this.mContext, parseLong, providerTokenKey);
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", "setupReplenishRetryAlarm : ERROR: " + e.getMessage(), e);
                z = false;
            }
        } else {
            Log.m290w("AmexPayProvider", "setupReplenishRetryAlarm : WARNING: Retry data is empty, unable to set alarm");
            z = false;
        }
        Log.m285d("AmexPayProvider", "setupReplenishRetryAlarm : Exit");
        return z;
    }

    private byte[] generateRndBytes(int i) {
        if (i < 1) {
            Log.m285d("AmexPayProvider", "generateRndBytes : ERROR: Invalid input length");
            return null;
        }
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    protected synchronized byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        byte[] bArr;
        bArr = null;
        Log.m285d("AmexPayProvider", "generateInAppPaymentPayload: Enter: " + System.currentTimeMillis());
        if (cq()) {
            this.qI = true;
            AmexTAController.AmexTAController amexTAController = new AmexTAController.AmexTAController();
            amexTAController.merchantCertificate = inAppDetailedTransactionInfo.cd();
            amexTAController.txnAttributes = new HashMap();
            amexTAController.txnAttributes.put(IdvMethod.EXTRA_AMOUNT, inAppDetailedTransactionInfo.getAmount());
            amexTAController.txnAttributes.put("currency_code", inAppDetailedTransactionInfo.getCurrencyCode());
            amexTAController.txnAttributes.put("utc", String.valueOf(Utils.am(this.mContext) / 1000));
            if (!(inAppDetailedTransactionInfo.getCardholderName() == null || inAppDetailedTransactionInfo.getCardholderName().isEmpty())) {
                amexTAController.txnAttributes.put("cardholder_name", inAppDetailedTransactionInfo.getCardholderName());
            }
            amexTAController.txnAttributes.put("eci_indicator", "5");
            TokenInAppResponse tokenInApp = this.qB.tokenInApp(Utils.encodeHex(generateRndBytes(4)), amexTAController.toString());
            if (tokenInApp.getReasonCode().equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                Log.m288m("AmexPayProvider", "generateInAppPaymentPayload : InApp Payload : " + tokenInApp.getPaymentPayload());
                bArr = tokenInApp.getPaymentPayload().getBytes();
            } else {
                Log.m286e("AmexPayProvider", "generateInAppPaymentPayload : ERROR: tokenInApp failed on SDK");
            }
        } else {
            Log.m286e("AmexPayProvider", "generateInAppPaymentPayload : ERROR: Start Payment Failed");
        }
        this.qI = false;
        Log.m285d("AmexPayProvider", "generateInAppPaymentPayload: end: " + System.currentTimeMillis());
        return bArr;
    }

    public synchronized String encryptUserSignature(byte[] bArr) {
        String str;
        Log.m285d("AmexPayProvider", "encryptUserSignature : Enter");
        if (bArr == null) {
            str = null;
        } else {
            try {
                str = this.qA.m812a(com.samsung.android.spayfw.payprovider.amexv2.tzsvc.Utils.toBase64(bArr), true);
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", "encryptUserSignature : ERROR: Cannot encrypt User signature data" + e.getMessage(), e);
                str = null;
            }
        }
        Log.m285d("AmexPayProvider", "encryptUserSignature : Exit");
        return str;
    }

    public synchronized byte[] decryptUserSignature(String str) {
        byte[] bArr;
        Log.m285d("AmexPayProvider", "decryptUserSignature : Enter");
        bArr = null;
        if (!TextUtils.isEmpty(str)) {
            try {
                Object a = this.qA.m812a(str, false);
                if (!TextUtils.isEmpty(a)) {
                    bArr = com.samsung.android.spayfw.payprovider.amexv2.tzsvc.Utils.fromBase64(a);
                }
            } catch (Throwable e) {
                Log.m284c("AmexPayProvider", "decryptUserSignature : ERROR: Cannot decrypt User signature data" + e.getMessage(), e);
            }
        }
        Log.m285d("AmexPayProvider", "decryptUserSignature : Exit");
        return bArr;
    }

    public synchronized boolean isPayAllowedForPresentationMode(int i) {
        boolean z;
        z = true;
        Log.m285d("AmexPayProvider", "isPayAllowedForPresentationMode : Enter with presentation mode as : " + i);
        if (i == 2) {
            Log.m285d("AmexPayProvider", "isPayAllowedForPresentationMode : Enter (CARD_PRESENT_MODE_MST)");
            String str = HCEClientConstants.MST_SUPPORTED;
            if (this.qE.size() == 0) {
                if (this.mProviderTokenKey != null) {
                    str = this.mProviderTokenKey.cn();
                    Log.m285d("AmexPayProvider", "isPayAllowedForPresentationMode : token Ref ID : " + str);
                    str = this.pp.getString(str + "_config_string", null);
                    if (str != null) {
                        aB(str);
                    } else {
                        Log.m286e("AmexPayProvider", "isPayAllowedForPresentationMode : ERROR: cannot find config for this token");
                    }
                } else {
                    Log.m286e("AmexPayProvider", "isPayAllowedForPresentationMode : ERROR: Invalid token reference");
                }
            }
            Log.m285d("AmexPayProvider", "isPayAllowedForPresentationMode : Token Config Map -> " + this.qE.toString());
            if (this.qE.containsKey(HCEClientConstants.MST_SUPPORTED)) {
                z = Boolean.parseBoolean((String) this.qE.get(HCEClientConstants.MST_SUPPORTED));
            }
            Log.m285d("AmexPayProvider", "isPayAllowedForPresentationMode : returning " + z);
        }
        return z;
    }
}
