package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.util.Base64;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardResult;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Account;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.FactoryResetDetector;
import com.samsung.android.spayfw.core.PaymentFrameworkApp.C0409a;
import com.samsung.android.spayfw.core.RequestDataBuilder;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.core.Token;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.CasdParameters;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.EnrollRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EventReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.storage.ServerCertsStorage.ServerCertsDb.ServerCertsColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.TAController;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.d */
public class CardEnroller extends Processor {
    private static final String[] CARD_ENTRY_MODES;
    private static final String[] CARD_PRESENTATION_MODES;
    private static final String kN;
    private static final String[] kO;
    protected final IEnrollCardCallback kP;
    protected EnrollCardInfo kQ;
    protected BillingInfo mBillingInfo;

    /* renamed from: com.samsung.android.spayfw.core.a.d.a */
    private class CardEnroller extends C0413a<Response<EnrollmentResponseData>, EnrollRequest> {
        IEnrollCardCallback kP;
        EnrollCardInfo kQ;
        String kR;
        final /* synthetic */ CardEnroller kS;
        BillingInfo mBillingInfo;

        public CardEnroller(CardEnroller cardEnroller, String str, EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
            this.kS = cardEnroller;
            this.kP = iEnrollCardCallback;
            this.kQ = enrollCardInfo;
            this.mBillingInfo = billingInfo;
            this.kR = str;
        }

        public void m366a(int i, Response<EnrollmentResponseData> response) {
            EnrollCardResult enrollCardResult;
            int i2 = -3;
            int i3 = -11;
            Log.m285d("CardEnroller", "EnrollCallback:onRequestComplete: code: " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    enrollCardResult = null;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    enrollCardResult = null;
                    break;
                case 201:
                    if (response != null && response.getResult() != null && ((EnrollmentResponseData) response.getResult()).getId() != null) {
                        String id = ((EnrollmentResponseData) response.getResult()).getId();
                        if (this.kS.iJ.m558q(this.kR) != null) {
                            this.kS.iJ.m558q(this.kR).setEnrollmentId(id);
                            Card q = this.kS.iJ.m558q(id);
                            if (q != null) {
                                TokenRecord tokenRecord = new TokenRecord(id);
                                tokenRecord.setUserId(Account.m551a(this.kS.mContext, null).getAccountId());
                                tokenRecord.m1255j(q.ab());
                                tokenRecord.setCardBrand(q.getCardBrand());
                                tokenRecord.setTokenStatus(TokenStatus.PENDING_ENROLLED);
                                this.kS.jJ.m1227c(tokenRecord);
                                q.ad().setEnrollmentId(id);
                                FactoryResetDetector.ai();
                                Token token = new Token();
                                token.setTokenStatus(tokenRecord.getTokenStatus());
                                q.m576a(token);
                                enrollCardResult = ResponseDataBuilder.m629a(this.kS.mContext, (EnrollmentResponseData) response.getResult());
                                i2 = 0;
                                break;
                            }
                            Log.m286e("CardEnroller", "EnrollCallback:onRequestComplete: unable to find the card ");
                            i2 = -1;
                            enrollCardResult = null;
                            break;
                        }
                        Log.m286e("CardEnroller", "EnrollCallback:onRequestComplete: unable to find the card ");
                        i2 = -1;
                        enrollCardResult = null;
                        break;
                    }
                    Log.m286e("CardEnroller", "EnrollCallback:onRequestComplete: invalid response from server");
                    enrollCardResult = null;
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_OUTPUT_INVALID;
                    break;
                    break;
                case 204:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_CARD_NOT_SUPPORTED;
                    enrollCardResult = null;
                    break;
                case 400:
                    ErrorResponseData fa = response.fa();
                    if (fa != null && fa.getCode() != null) {
                        String code = fa.getCode();
                        if (!ErrorResponseData.ERROR_CODE_PAN_NOT_ELIGIBLE.equals(code)) {
                            if (ErrorResponseData.ERROR_CODE_PAN_ALREADY_ENROLLED.equals(code)) {
                                this.kS.iJ.m560s(this.kR);
                                i3 = -3;
                            } else if (!ErrorResponseData.ERROR_CODE_INVALID_DATA.equals(code)) {
                                i3 = ErrorResponseData.ERROR_CODE_PROVISION_EXCEEDED.equals(code) ? -13 : ErrorResponseData.ERROR_CODE_DEVICE_TOKEN_MAX_LIMIT_REACHED.equals(code) ? PaymentFramework.RESULT_CODE_FAIL_DEVICE_TOKENS_MAX_LIMIT_REACHED : -1;
                            }
                        }
                        enrollCardResult = null;
                        i2 = i3;
                        break;
                    }
                    i2 = -1;
                    enrollCardResult = null;
                    break;
                case 409:
                    if (response != null && response.getResult() != null && ((EnrollmentResponseData) response.getResult()).getId() != null) {
                        Card q2 = this.kS.iJ.m558q(this.kR);
                        if (q2 != null) {
                            String id2 = ((EnrollmentResponseData) response.getResult()).getId();
                            EnrollCardResult a = ResponseDataBuilder.m629a(this.kS.mContext, (EnrollmentResponseData) response.getResult());
                            if (this.kS.jJ.bp(id2) != null) {
                                enrollCardResult = a;
                                break;
                            }
                            q2.setEnrollmentId(id2);
                            TokenRecord tokenRecord2 = new TokenRecord(id2);
                            tokenRecord2.setUserId(Account.m551a(this.kS.mContext, null).getAccountId());
                            tokenRecord2.m1255j(q2.ab());
                            tokenRecord2.setCardBrand(q2.getCardBrand());
                            tokenRecord2.setTokenStatus(TokenStatus.PENDING_ENROLLED);
                            this.kS.jJ.m1227c(tokenRecord2);
                            q2.ad().setEnrollmentId(id2);
                            Token token2 = new Token();
                            token2.setTokenStatus(tokenRecord2.getTokenStatus());
                            q2.m576a(token2);
                            Log.m285d("CardEnroller", "EnrollCallback:onRequestComplete: add a new token record " + tokenRecord2.dump());
                            i2 = 0;
                            enrollCardResult = a;
                            break;
                        }
                        Log.m286e("CardEnroller", "EnrollCallback:unable to find the card in PF");
                        i2 = -1;
                        enrollCardResult = null;
                        break;
                    }
                    Log.m286e("CardEnroller", "EnrollCallback:onRequestComplete: invalid response from server");
                    enrollCardResult = null;
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_OUTPUT_INVALID;
                    break;
                    break;
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    enrollCardResult = null;
                    break;
                default:
                    i2 = -1;
                    enrollCardResult = null;
                    break;
            }
            if (!(enrollCardResult == null || enrollCardResult.getEnrollmentId() == null || (enrollCardResult.getTnC() != null && !enrollCardResult.getTnC().isEmpty()))) {
                TokenRecord bp = this.kS.jJ.bp(enrollCardResult.getEnrollmentId());
                if (bp != null) {
                    bp.m1252b(System.currentTimeMillis());
                    this.kS.jJ.m1230d(bp);
                }
            }
            if (i2 == 0) {
                try {
                    Log.m287i("CardEnroller", "EnrollCallback:onRequestComplete: invoking app callback");
                    if (ConfigurationManager.m581h(this.kS.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID) == null) {
                        Log.m287i("CardEnroller", "Wallet ID Set: " + ConfigurationManager.m581h(this.kS.mContext).setConfig(PaymentFramework.CONFIG_WALLET_ID, this.kQ.getWalletId()));
                    }
                    this.kP.onSuccess(enrollCardResult);
                } catch (Throwable e) {
                    Log.m284c("CardEnroller", e.getMessage(), e);
                    if (response != null) {
                        ResponseDataBuilder.m641a((EnrollmentResponseData) response.getResult(), enrollCardResult);
                    } else {
                        ResponseDataBuilder.m641a(null, enrollCardResult);
                    }
                    this.kS.clearSensitiveData();
                    return;
                } catch (Throwable e2) {
                    Throwable th = e2;
                    if (response != null) {
                        ResponseDataBuilder.m641a((EnrollmentResponseData) response.getResult(), enrollCardResult);
                    } else {
                        ResponseDataBuilder.m641a(null, enrollCardResult);
                    }
                    this.kS.clearSensitiveData();
                }
            } else {
                Log.m286e("CardEnroller", "EnrollCard Failed - Error Code = " + i2 + "code: " + i);
                this.kP.onFail(i2, enrollCardResult);
                this.kS.iJ.m560s(this.kR);
            }
            if (response != null) {
                ResponseDataBuilder.m641a((EnrollmentResponseData) response.getResult(), enrollCardResult);
            } else {
                ResponseDataBuilder.m641a(null, enrollCardResult);
            }
            this.kS.clearSensitiveData();
        }

        public boolean m370a(int i, String str) {
            Log.m285d("CardEnroller", "onCasdUpdate : called for EnrollCallback");
            if (str == null || str.isEmpty()) {
                Log.m286e("CardEnroller", "CASD certificate is null. Enrollment aborted");
                try {
                    this.kP.onFail(-43, null);
                    return false;
                } catch (Throwable e) {
                    Log.m284c("CardEnroller", e.getMessage(), e);
                    return false;
                }
            }
            Card q = this.kS.iJ.m558q(this.kR);
            if (q == null || q.ad() == null) {
                Log.m286e("CardEnroller", "no card, CASD aborted");
                try {
                    this.kP.onFail(-43, null);
                    return false;
                } catch (Throwable e2) {
                    Log.m284c("CardEnroller", e2.getMessage(), e2);
                    return false;
                }
            }
            Log.m285d("CardEnroller", "onCasdUpdate : " + q.getCardBrand());
            boolean casdCertificate = q.ad().setCasdCertificate(str);
            Log.m285d("CardEnroller", "onCasdUpdate : " + casdCertificate);
            if (casdCertificate) {
                EventReport eventReport = new EventReport();
                eventReport.setCategory(EventReport.EVENT_CATEGORY_SECURITY);
                eventReport.setCode(EventReport.EVENT_CODE_CASD_UPDATE);
                eventReport.setSource(EventReport.EVENT_SOURCE_PF);
                eventReport.setDescription(EventReport.EVENT_DESCRIPTION_CASD_UPDATED);
                this.kS.lQ.m1133a(Card.m574y(q.getCardBrand()), eventReport).fe();
                return true;
            }
            ErrorReport errorReport = new ErrorReport();
            errorReport.setSeverity(ErrorReport.ERROR_SEVERITY_ERROR);
            errorReport.setCode(TokenReport.ERROR_TRUSTED_APP);
            errorReport.setDescription(ErrorReport.EVENT_DESCRIPTION_CASD_UPDATE_FAILED);
            this.kS.lQ.m1132a(Card.m574y(q.getCardBrand()), errorReport).fe();
            Log.m286e("CardEnroller", "CASD certificate update faild. Enrollment aborted");
            try {
                this.kP.onFail(-43, null);
                return false;
            } catch (Throwable e22) {
                Log.m284c("CardEnroller", e22.getMessage(), e22);
                return false;
            }
        }

        public void m367a(int i, ServerCertificates serverCertificates, EnrollRequest enrollRequest) {
            JsonObject jsonObject = null;
            Log.m285d("CardEnroller", "onCertsReceived: called for EnrollCallback");
            Card q = this.kS.iJ.m558q(this.kR);
            if (q == null) {
                Log.m286e("CardEnroller", "unable to find card object");
                try {
                    this.kP.onFail(-1, null);
                } catch (Throwable e) {
                    Log.m284c("CardEnroller", e.getMessage(), e);
                }
            } else if (this.kS.m334a(this.kR, q, serverCertificates)) {
                ProviderRequestData enrollmentRequestDataTA = q.ad().getEnrollmentRequestDataTA(this.kQ, this.mBillingInfo);
                if (enrollmentRequestDataTA != null && enrollmentRequestDataTA.getErrorCode() == 0) {
                    jsonObject = enrollmentRequestDataTA.ch();
                }
                if (jsonObject != null || (this.kQ instanceof EnrollCardReferenceInfo)) {
                    RequestDataBuilder.m626a(enrollmentRequestDataTA, (EnrollmentRequestData) enrollRequest.eT(), this.kS.aV());
                    enrollRequest.bf(this.kS.m329P(q.getCardBrand()));
                    enrollRequest.m836a((C0413a) this);
                    Log.m287i("CardEnroller", "enrollCard request successfully sent after server cert update");
                    return;
                }
                Log.m290w("CardEnroller", "provider data is null");
                try {
                    this.kP.onFail(-1, null);
                } catch (Throwable e2) {
                    Log.m284c("CardEnroller", e2.getMessage(), e2);
                }
                this.kS.iJ.m560s(this.kR);
            } else {
                Log.m286e("CardEnroller", "Server certificate update failed. Enrollment aborted");
                try {
                    this.kP.onFail(-1, null);
                } catch (Throwable e22) {
                    Log.m284c("CardEnroller", e22.getMessage(), e22);
                }
            }
        }
    }

    static {
        kN = TAController.getEfsDirectory() + "/salt";
        CARD_ENTRY_MODES = new String[]{EnrollCardInfo.CARD_ENTRY_MODE_MANUAL, EnrollCardInfo.CARD_ENTRY_MODE_OCR, IdvMethod.IDV_TYPE_APP, EnrollCardInfo.CARD_ENTRY_MODE_FILE};
        CARD_PRESENTATION_MODES = new String[]{EnrollCardInfo.CARD_PRESENTATION_MODE_NFC, EnrollCardInfo.CARD_PRESENTATION_MODE_MST, EnrollCardInfo.CARD_PRESENTATION_MODE_ECM, EnrollCardInfo.CARD_PRESENTATION_MODE_ALL};
        kO = new String[]{EnrollCardReferenceInfo.CARD_REF_TYPE_APP2APP, EnrollCardReferenceInfo.CARD_REF_TYPE_ID, EnrollCardReferenceInfo.CARD_REF_TYPE_COF};
    }

    private static boolean isValidMode(String[] strArr, String str) {
        for (String equals : strArr) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public CardEnroller(Context context, EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
        super(context);
        this.kQ = enrollCardInfo;
        this.mBillingInfo = billingInfo;
        this.kP = iEnrollCardCallback;
    }

    public void process() {
        if (m374a(this.kQ, this.kP)) {
            Log.m285d("CardEnroller", this.kQ.toString());
            if (this.iJ == null) {
                this.iJ = Account.m551a(this.mContext, this.kQ.getUserId());
            }
            if (this.iJ == null) {
                Log.m286e("CardEnroller", "unable to create account");
                this.kP.onFail(-1, null);
                clearSensitiveData();
                return;
            } else if (this.iJ.m557p(this.kQ.getUserId())) {
                Card a;
                ConfigurationManager.m581h(this.mContext).setConfig(PaymentFramework.CONFIG_USER_ID, this.kQ.getUserId());
                try {
                    a = this.iJ.m555a(this.kQ);
                } catch (Throwable e) {
                    Log.m284c("CardEnroller", e.getMessage(), e);
                    if (e.getErrorCode() == 2) {
                        this.kP.onFail(-42, null);
                    } else {
                        this.kP.onFail(-41, null);
                    }
                    clearSensitiveData();
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    a = null;
                }
                if (a == null) {
                    Log.m286e("CardEnroller", "unable to add new card");
                    this.kP.onFail(-10, null);
                    clearSensitiveData();
                    return;
                }
                ProviderRequestData providerRequestData;
                if (this.lh != null) {
                    Log.m287i("CardEnroller", "Server Certs DB not null");
                    Log.m285d("CardEnroller", "Card Brand = " + a.getCardBrand());
                    List a2 = this.lh.m1220a(ServerCertsColumn.CARD_TYPE, a.getCardBrand());
                    if (a2 == null || a2.isEmpty()) {
                        Log.m287i("CardEnroller", "No certs stored for current card");
                    } else {
                        Log.m287i("CardEnroller", "Certificates exist for : " + a.getCardBrand());
                        a.ad().setServerCertificates((CertificateInfo[]) a2.toArray(new CertificateInfo[a2.size()]));
                    }
                }
                String valueOf = String.valueOf(System.currentTimeMillis());
                a.setEnrollmentId(valueOf);
                a.ad().setPaymentFrameworkRequester(new C0409a());
                CasdParameters casdParameters = a.ad().getCasdParameters();
                CertificateInfo[] deviceCertificatesTA = a.ad().getDeviceCertificatesTA();
                if (deviceCertificatesTA == null || deviceCertificatesTA.length <= 0) {
                    Log.m290w("CardEnroller", "getCerts returns null");
                }
                String P = m329P(a.getCardBrand());
                if (P == null || P.isEmpty()) {
                    providerRequestData = null;
                } else {
                    JsonObject jsonObject;
                    providerRequestData = a.ad().getEnrollmentRequestDataTA(this.kQ, this.mBillingInfo);
                    if (providerRequestData == null || providerRequestData.getErrorCode() != 0) {
                        jsonObject = null;
                    } else {
                        jsonObject = providerRequestData.ch();
                    }
                    if (jsonObject == null && !(this.kQ instanceof EnrollCardReferenceInfo)) {
                        Log.m286e("CardEnroller", "provider data is null");
                        try {
                            this.kP.onFail(-1, null);
                            clearSensitiveData();
                        } catch (Throwable e3) {
                            Log.m284c("CardEnroller", e3.getMessage(), e3);
                        }
                        this.iJ.m560s(valueOf);
                        return;
                    }
                }
                DeviceInfo defaultDeviceInfo = DeviceInfo.getDefaultDeviceInfo(this.mContext);
                String sppId = this.kQ.getSppId();
                String gcmId = this.kQ.getGcmId();
                Log.m285d("CardEnroller", "spp id= " + sppId);
                Log.m285d("CardEnroller", "gcm id= " + gcmId);
                if (sppId == null || sppId.isEmpty()) {
                    defaultDeviceInfo.setSppId("89235dce-9e25-11e4-89d3-123b93f75cba");
                } else {
                    defaultDeviceInfo.setSppId(sppId);
                }
                if (!(gcmId == null || gcmId.isEmpty())) {
                    defaultDeviceInfo.setGcmId(gcmId);
                }
                EnrollmentRequestData a3 = RequestDataBuilder.m624a(this.kQ, deviceCertificatesTA, defaultDeviceInfo, providerRequestData, a, aV());
                FraudDataCollector x = FraudDataCollector.m718x(this.mContext);
                if (x != null) {
                    x.m721a(this.kQ, this.mBillingInfo);
                } else {
                    Log.m285d("CardEnroller", "Collector: buildFCardRecord cannot get data");
                }
                m373a(a3);
                if (this.kQ instanceof EnrollCardPanInfo) {
                    EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) this.kQ;
                    if (enrollCardPanInfo.getCVV() != null) {
                        a.setSecurityCode(m379g(enrollCardPanInfo.getCVV(), a.getCardBrand()));
                    }
                }
                EnrollRequest a4 = this.lQ.m1128a(Card.m574y(a.getCardBrand()), a3);
                sppId = ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID);
                Log.m285d("CardEnroller", "Current Wallet ID = " + sppId);
                String walletId = this.kQ.getWalletId();
                if (sppId == null) {
                    a4.bl(walletId);
                } else if (!sppId.equals(walletId)) {
                    Log.m286e("CardEnroller", "Current Wallet ID not match");
                    this.kP.onFail(PaymentFramework.RESULT_CODE_FAIL_WALLET_ID_MISMATCH, null);
                    return;
                }
                a4.bf(P);
                if (casdParameters != null) {
                    a4.m844s(casdParameters.cb(), casdParameters.ca());
                }
                a4.bk(this.iJ.m562u(a.getCardBrand()));
                a4.m836a(new CardEnroller(this, valueOf, this.kQ, this.mBillingInfo, this.kP));
                Log.m287i("CardEnroller", "enrollCard request successfully sent");
                return;
            } else {
                Log.m286e("CardEnroller", "account ids are not same ");
                this.kP.onFail(-5, null);
                clearSensitiveData();
                return;
            }
        }
        if (this.kP != null) {
            this.kP.onFail(-5, null);
        }
        clearSensitiveData();
        Log.m286e("CardEnroller", "validateEnrollCardRequest failed");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final byte[] getSalt() {
        /*
        r7 = this;
        r1 = 0;
        r2 = new java.io.File;
        r0 = kN;
        r2.<init>(r0);
        r0 = r2.exists();
        if (r0 == 0) goto L_0x0087;
    L_0x000e:
        r0 = "CardEnroller";
        r3 = "Salt present, fetching now.";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r3);
        r3 = new java.io.FileReader;	 Catch:{ Exception -> 0x004b }
        r3.<init>(r2);	 Catch:{ Exception -> 0x004b }
        r2 = 0;
        r4 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x003d, all -> 0x005b }
        r4.<init>(r3);	 Catch:{ Throwable -> 0x003d, all -> 0x005b }
        r5 = 0;
        r0 = r4.readLine();	 Catch:{ Throwable -> 0x0067, all -> 0x00e6 }
        r0 = com.samsung.android.spayfw.utils.Utils.decodeHex(r0);	 Catch:{ Throwable -> 0x0067, all -> 0x00e6 }
        if (r4 == 0) goto L_0x0030;
    L_0x002b:
        if (r1 == 0) goto L_0x0057;
    L_0x002d:
        r4.close();	 Catch:{ Throwable -> 0x0038, all -> 0x005b }
    L_0x0030:
        if (r3 == 0) goto L_0x0037;
    L_0x0032:
        if (r1 == 0) goto L_0x0063;
    L_0x0034:
        r3.close();	 Catch:{ Throwable -> 0x005e }
    L_0x0037:
        return r0;
    L_0x0038:
        r4 = move-exception;
        r5.addSuppressed(r4);	 Catch:{ Throwable -> 0x003d, all -> 0x005b }
        goto L_0x0030;
    L_0x003d:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x003f }
    L_0x003f:
        r2 = move-exception;
        r6 = r2;
        r2 = r0;
        r0 = r6;
    L_0x0043:
        if (r3 == 0) goto L_0x004a;
    L_0x0045:
        if (r2 == 0) goto L_0x0083;
    L_0x0047:
        r3.close();	 Catch:{ Throwable -> 0x007e }
    L_0x004a:
        throw r0;	 Catch:{ Exception -> 0x004b }
    L_0x004b:
        r0 = move-exception;
        r2 = "CardEnroller";
        r3 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r0);
    L_0x0055:
        r0 = r1;
        goto L_0x0037;
    L_0x0057:
        r4.close();	 Catch:{ Throwable -> 0x003d, all -> 0x005b }
        goto L_0x0030;
    L_0x005b:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0043;
    L_0x005e:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x004b }
        goto L_0x0037;
    L_0x0063:
        r3.close();	 Catch:{ Exception -> 0x004b }
        goto L_0x0037;
    L_0x0067:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0069 }
    L_0x0069:
        r2 = move-exception;
        r6 = r2;
        r2 = r0;
        r0 = r6;
    L_0x006d:
        if (r4 == 0) goto L_0x0074;
    L_0x006f:
        if (r2 == 0) goto L_0x007a;
    L_0x0071:
        r4.close();	 Catch:{ Throwable -> 0x0075, all -> 0x005b }
    L_0x0074:
        throw r0;	 Catch:{ Throwable -> 0x003d, all -> 0x005b }
    L_0x0075:
        r4 = move-exception;
        r2.addSuppressed(r4);	 Catch:{ Throwable -> 0x003d, all -> 0x005b }
        goto L_0x0074;
    L_0x007a:
        r4.close();	 Catch:{ Throwable -> 0x003d, all -> 0x005b }
        goto L_0x0074;
    L_0x007e:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x004b }
        goto L_0x004a;
    L_0x0083:
        r3.close();	 Catch:{ Exception -> 0x004b }
        goto L_0x004a;
    L_0x0087:
        r0 = "CardEnroller";
        r3 = "No Salt, generating now.";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r3);
        r3 = new java.security.SecureRandom;
        r3.<init>();
        r0 = 32;
        r0 = new byte[r0];
        r3.nextBytes(r0);
        r3 = new java.io.FileWriter;	 Catch:{ Exception -> 0x00bc }
        r3.<init>(r2);	 Catch:{ Exception -> 0x00bc }
        r2 = 0;
        r4 = com.samsung.android.spayfw.utils.Utils.encodeHex(r0);	 Catch:{ Throwable -> 0x00cc, all -> 0x00e3 }
        r3.write(r4);	 Catch:{ Throwable -> 0x00cc, all -> 0x00e3 }
        r4 = "CardEnroller";
        r5 = "Salt Storage Success";
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);	 Catch:{ Throwable -> 0x00cc, all -> 0x00e3 }
        if (r3 == 0) goto L_0x0037;
    L_0x00b0:
        if (r1 == 0) goto L_0x00c7;
    L_0x00b2:
        r3.close();	 Catch:{ Throwable -> 0x00b6 }
        goto L_0x0037;
    L_0x00b6:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x00bc }
        goto L_0x0037;
    L_0x00bc:
        r0 = move-exception;
        r2 = "CardEnroller";
        r3 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r0);
        goto L_0x0055;
    L_0x00c7:
        r3.close();	 Catch:{ Exception -> 0x00bc }
        goto L_0x0037;
    L_0x00cc:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x00ce }
    L_0x00ce:
        r2 = move-exception;
        r6 = r2;
        r2 = r0;
        r0 = r6;
    L_0x00d2:
        if (r3 == 0) goto L_0x00d9;
    L_0x00d4:
        if (r2 == 0) goto L_0x00df;
    L_0x00d6:
        r3.close();	 Catch:{ Throwable -> 0x00da }
    L_0x00d9:
        throw r0;	 Catch:{ Exception -> 0x00bc }
    L_0x00da:
        r3 = move-exception;
        r2.addSuppressed(r3);	 Catch:{ Exception -> 0x00bc }
        goto L_0x00d9;
    L_0x00df:
        r3.close();	 Catch:{ Exception -> 0x00bc }
        goto L_0x00d9;
    L_0x00e3:
        r0 = move-exception;
        r2 = r1;
        goto L_0x00d2;
    L_0x00e6:
        r0 = move-exception;
        r2 = r1;
        goto L_0x006d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.d.getSalt():byte[]");
    }

    private String m379g(String str, String str2) {
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty() || !Utils.fP().equals("BR")) {
            return null;
        }
        if (str2.equals(PaymentFramework.CARD_BRAND_VISA)) {
            return "000";
        }
        Random random = new Random(System.currentTimeMillis());
        while (true) {
            String str3;
            int nextInt = random.nextInt(LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
            if (nextInt / 10 == 0) {
                str3 = HCEClientConstants.HEX_ZERO_BYTE + Integer.toString(nextInt);
            } else if (nextInt / 100 == 0) {
                str3 = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE + Integer.toString(nextInt);
            } else {
                str3 = Integer.toString(nextInt);
            }
            if (str3.equals(str) || str3.equals("000")) {
                Log.m290w("CardEnroller", "Code equals Avoid Code or Invalid Code. Retry.");
            } else {
                Log.m287i("CardEnroller", "Code Generated");
                return str3;
            }
        }
    }

    private boolean m376a(EnrollCardPanInfo enrollCardPanInfo) {
        if (enrollCardPanInfo.getCVV() == null || enrollCardPanInfo.getCVV().isEmpty()) {
            Log.m286e("CardEnroller", "CVV is null or empty");
            return false;
        } else if (enrollCardPanInfo.getPAN() == null || enrollCardPanInfo.getPAN().isEmpty()) {
            Log.m286e("CardEnroller", "PAN is null or empty");
            return false;
        } else if (enrollCardPanInfo.getName() != null && !enrollCardPanInfo.getName().isEmpty()) {
            return true;
        } else {
            Log.m286e("CardEnroller", "Name is null or empty");
            return false;
        }
    }

    private boolean m377a(EnrollCardReferenceInfo enrollCardReferenceInfo) {
        if (!CardEnroller.isValidMode(kO, enrollCardReferenceInfo.getReferenceType())) {
            Log.m286e("CardEnroller", "validateEnrollCardRequest: card Reference type incorrect! = " + enrollCardReferenceInfo.getReferenceType());
            return false;
        } else if (enrollCardReferenceInfo.getExtraEnrollData() != null) {
            return true;
        } else {
            Log.m286e("CardEnroller", "Extra Enroll Data is null or empty");
            return false;
        }
    }

    private boolean m375a(EnrollCardLoyaltyInfo enrollCardLoyaltyInfo) {
        if (enrollCardLoyaltyInfo.getLoyaltyInfo() != null && !enrollCardLoyaltyInfo.getLoyaltyInfo().isEmpty()) {
            return true;
        }
        Log.m286e("CardEnroller", "Loyalty cardInfo is null or empty");
        return false;
    }

    private boolean m374a(EnrollCardInfo enrollCardInfo, IEnrollCardCallback iEnrollCardCallback) {
        if (enrollCardInfo == null || iEnrollCardCallback == null) {
            Log.m286e("CardEnroller", "validateEnrollCardRequest: input is null !");
            return false;
        } else if (enrollCardInfo.getApplicationId() == null || enrollCardInfo.getUserId() == null || enrollCardInfo.getGcmId() == null || enrollCardInfo.getSppId() == null || enrollCardInfo.getWalletId() == null || enrollCardInfo.getUserEmail() == null) {
            if (this.mBillingInfo == null || this.mBillingInfo.getZip() == null || this.mBillingInfo.getZip().length() <= 16) {
                return false;
            }
            Log.m286e("CardEnroller", "validateEnrollCardRequest: zipcode invalid! too long = " + this.mBillingInfo.getZip());
            return false;
        } else if (!CardEnroller.isValidMode(CARD_ENTRY_MODES, enrollCardInfo.getCardEntryMode())) {
            Log.m286e("CardEnroller", "validateEnrollCardRequest: entry mode incorrect! = " + enrollCardInfo.getCardEntryMode());
            return false;
        } else if (!CardEnroller.isValidMode(CARD_PRESENTATION_MODES, enrollCardInfo.getCardPresentationMode())) {
            Log.m286e("CardEnroller", "validateEnrollCardRequest: presentation mode incorrect! = " + enrollCardInfo.getCardPresentationMode());
            return false;
        } else if (enrollCardInfo instanceof EnrollCardPanInfo) {
            return m376a((EnrollCardPanInfo) enrollCardInfo);
        } else {
            if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
                return m377a((EnrollCardReferenceInfo) enrollCardInfo);
            }
            if (enrollCardInfo instanceof EnrollCardLoyaltyInfo) {
                return m375a((EnrollCardLoyaltyInfo) enrollCardInfo);
            }
            Log.m286e("CardEnroller", "validateEnrollCardRequest: UNKNOWN ENROLL CARD CLASS");
            return false;
        }
    }

    private void m373a(EnrollmentRequestData enrollmentRequestData) {
        String str;
        String str2;
        EnrollCardPanInfo enrollCardPanInfo;
        String str3;
        String str4 = null;
        try {
            if (this.mBillingInfo == null || this.mBillingInfo.getZip() == null || this.mBillingInfo.getZip().isEmpty()) {
                str = null;
            } else {
                Log.m285d("CardEnroller", "ZipCode : " + this.mBillingInfo.getZip());
                str = m371K(this.mBillingInfo.getZip());
            }
            str2 = str;
        } catch (Throwable e) {
            Log.m284c("CardEnroller", e.getMessage(), e);
            str2 = null;
        }
        try {
            if (this.kQ instanceof EnrollCardPanInfo) {
                enrollCardPanInfo = (EnrollCardPanInfo) this.kQ;
                if (!(enrollCardPanInfo.getName() == null || enrollCardPanInfo.getName().isEmpty())) {
                    String[] split = enrollCardPanInfo.getName().split(" ");
                    if (split != null) {
                        Log.m285d("CardEnroller", "Last Name : " + split[split.length - 1]);
                        str = m371K(split[split.length - 1]);
                        str3 = str;
                        if (this.kQ instanceof EnrollCardPanInfo) {
                            enrollCardPanInfo = (EnrollCardPanInfo) this.kQ;
                            if (!(enrollCardPanInfo.getPAN() == null || enrollCardPanInfo.getPAN().isEmpty())) {
                                str = enrollCardPanInfo.getPAN();
                                str = str.substring(str.length() - 4);
                                Log.m285d("CardEnroller", "Last 4 : " + str);
                                if (str != null) {
                                    str4 = m371K(str);
                                }
                            }
                        }
                        enrollmentRequestData.getCard().setRiskData(str2, str3, str4);
                    }
                }
            }
            str = null;
            str3 = str;
        } catch (Throwable e2) {
            Log.m284c("CardEnroller", e2.getMessage(), e2);
            str3 = null;
        }
        try {
            if (this.kQ instanceof EnrollCardPanInfo) {
                enrollCardPanInfo = (EnrollCardPanInfo) this.kQ;
                str = enrollCardPanInfo.getPAN();
                str = str.substring(str.length() - 4);
                Log.m285d("CardEnroller", "Last 4 : " + str);
                if (str != null) {
                    str4 = m371K(str);
                }
            }
        } catch (Throwable e22) {
            Log.m284c("CardEnroller", e22.getMessage(), e22);
        }
        enrollmentRequestData.getCard().setRiskData(str2, str3, str4);
    }

    private String aV() {
        if (this.kQ instanceof EnrollCardReferenceInfo) {
            EnrollCardReferenceInfo enrollCardReferenceInfo = (EnrollCardReferenceInfo) this.kQ;
            if (enrollCardReferenceInfo.getExtraEnrollData() != null) {
                return enrollCardReferenceInfo.getExtraEnrollData().getString(EnrollCardReferenceInfo.CARD_REFERENCE_ID);
            }
        }
        return null;
    }

    private void clearSensitiveData() {
        if (this.kQ != null) {
            this.kQ.decrementRefCount();
        }
    }

    private String m371K(String str) {
        Log.m285d("CardEnroller", "generateRiskDataHash : " + str);
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] bytes = str.getBytes("UTF-8");
            instance.update(bytes);
            byte[] digest = instance.digest();
            String encodeHex = Utils.encodeHex(bytes);
            String str2 = Utils.encodeHex(digest) + "00000001";
            digest = getSalt();
            String str3 = null;
            int i = 2000;
            while (i > 0) {
                try {
                    str2 = Utils.m1278n(encodeHex, str2);
                    Utils.xor(digest, Utils.decodeHex(str2));
                    str3 = Utils.encodeHex(digest);
                    i--;
                } catch (Exception e) {
                    return str3;
                }
            }
            String str4 = new String(Base64.encode(Utils.decodeHex(str3), 2));
            Log.m285d("CardEnroller", "generateRiskDataHash : " + str4);
            return str4;
        } catch (Exception e2) {
            return null;
        }
    }
}
