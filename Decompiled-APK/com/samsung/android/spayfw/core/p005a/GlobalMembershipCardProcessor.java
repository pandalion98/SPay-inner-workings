package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Base64;
import com.samsung.android.spayfw.appinterface.ExtractGlobalMembershipCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardDetail;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Account;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.Token;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPConstants;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.storage.ServerCertsStorage;
import com.samsung.android.spayfw.storage.ServerCertsStorage.ServerCertsDb.ServerCertsColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.core.a.g */
public class GlobalMembershipCardProcessor extends Processor {
    public static final boolean DEBUG;
    private static GlobalMembershipCardProcessor lg;
    private ServerCertsStorage lh;
    private HashSet<String> li;

    /* renamed from: com.samsung.android.spayfw.core.a.g.a */
    static class GlobalMembershipCardProcessor {
        public byte[] lj;
        public byte[] lk;
        public byte[] ll;

        public GlobalMembershipCardProcessor(byte[] bArr, byte[] bArr2, byte[] bArr3) {
            this.lj = null;
            this.lk = null;
            this.ll = null;
            this.lj = bArr;
            this.lk = bArr2;
            this.ll = bArr3;
        }
    }

    static {
        DEBUG = Utils.DEBUG;
    }

    public static final synchronized GlobalMembershipCardProcessor m411o(Context context) {
        GlobalMembershipCardProcessor globalMembershipCardProcessor;
        synchronized (GlobalMembershipCardProcessor.class) {
            try {
                if (lg == null) {
                    lg = new GlobalMembershipCardProcessor(context);
                }
                globalMembershipCardProcessor = lg;
            } catch (Throwable e) {
                Log.m284c("GlobalMembershipCardProcessor", e.getMessage(), e);
                globalMembershipCardProcessor = null;
            }
        }
        return globalMembershipCardProcessor;
    }

    private GlobalMembershipCardProcessor(Context context) {
        super(context);
        this.li = new HashSet();
        this.lh = ServerCertsStorage.ad(this.mContext);
        this.li.add("AU");
        this.li.add("BR");
        this.li.add("SG");
        this.li.add("ES");
        this.li.add("MY");
        this.li.add("TH");
        this.li.add("CH");
        this.li.add("TW");
        this.li.add("RU");
        this.li.add("AE");
        this.li.add("SE");
    }

    public void m412a(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
        GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
        if (globalMembershipCardRegisterRequestData == null || iGlobalMembershipCardRegisterCallback == null) {
            if (iGlobalMembershipCardRegisterCallback != null) {
                try {
                    Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: invalid inputs: data is null");
                    iGlobalMembershipCardRegisterCallback.onFail(-5, globalMembershipCardRegisterResponseData);
                    return;
                } catch (Throwable e) {
                    Log.m284c("GlobalMembershipCardProcessor", e.getMessage(), e);
                    return;
                }
            }
            Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: invalid inputs: callback is null");
        } else if (aZ()) {
            Log.m285d("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData() [" + globalMembershipCardRegisterRequestData + "]");
            if (globalMembershipCardRegisterRequestData.getGlobalMembershipCardData() == null || TextUtils.isEmpty(globalMembershipCardRegisterRequestData.getUserId()) || TextUtils.isEmpty(globalMembershipCardRegisterRequestData.getPartnerId())) {
                Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: invalid data");
                iGlobalMembershipCardRegisterCallback.onFail(-5, globalMembershipCardRegisterResponseData);
                return;
            }
            if (globalMembershipCardRegisterRequestData.allServerCertsNotNull()) {
                m415a(globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getServerCaCert(), globalMembershipCardRegisterRequestData.getServerEncCert(), globalMembershipCardRegisterRequestData.getServerVerCert());
            } else {
                GlobalMembershipCardProcessor O = m409O(globalMembershipCardRegisterRequestData.getPartnerId());
                if (O == null) {
                    Log.m285d("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: serverCerts are null and not stored.");
                } else {
                    globalMembershipCardRegisterRequestData.setServerCaCert(O.lj);
                    globalMembershipCardRegisterRequestData.setServerEncCert(O.lk);
                    globalMembershipCardRegisterRequestData.setServerVerCert(O.ll);
                }
            }
            Card aY = aY();
            if (aY == null || aY.ad() == null) {
                iGlobalMembershipCardRegisterCallback.onFail(-1, globalMembershipCardRegisterResponseData);
                Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData: unable to get card Object");
                return;
            }
            globalMembershipCardRegisterResponseData = aY.ad().getGlobalMembershipCardRegisterDataTA(globalMembershipCardRegisterRequestData);
            if (globalMembershipCardRegisterResponseData.getErrorCode() == 0) {
                iGlobalMembershipCardRegisterCallback.onSuccess(globalMembershipCardRegisterResponseData);
            } else {
                iGlobalMembershipCardRegisterCallback.onFail(globalMembershipCardRegisterResponseData.getErrorCode(), globalMembershipCardRegisterResponseData);
            }
        } else {
            globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
            Log.m285d("GlobalMembershipCardProcessor", "getGlobalMembershipCardRegisterData() calls onFail. this country doesn't support GlobalMembership");
            if (iGlobalMembershipCardRegisterCallback != null) {
                iGlobalMembershipCardRegisterCallback.onFail(PaymentFramework.RESULT_CODE_FAIL_CARD_NOT_SUPPORTED, globalMembershipCardRegisterResponseData);
            }
        }
    }

    public void m416b(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
        GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
        if (globalMembershipCardRegisterRequestData == null || iGlobalMembershipCardRegisterCallback == null) {
            if (iGlobalMembershipCardRegisterCallback != null) {
                try {
                    Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: invalid inputs: data is null");
                    iGlobalMembershipCardRegisterCallback.onFail(-5, globalMembershipCardRegisterResponseData);
                    return;
                } catch (Throwable e) {
                    Log.m284c("GlobalMembershipCardProcessor", e.getMessage(), e);
                    return;
                }
            }
            Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: invalid inputs: callback is null");
        } else if (aZ()) {
            Log.m285d("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData() [" + globalMembershipCardRegisterRequestData + "]");
            if (globalMembershipCardRegisterRequestData.getGlobalMembershipCardData() == null) {
                Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: invalid data");
                iGlobalMembershipCardRegisterCallback.onFail(-5, globalMembershipCardRegisterResponseData);
            } else if (TextUtils.isEmpty(globalMembershipCardRegisterRequestData.getTokenId())) {
                Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: tokenId is empty");
                iGlobalMembershipCardRegisterCallback.onFail(-5, globalMembershipCardRegisterResponseData);
            } else {
                GlobalMembershipCardProcessor O = m409O(globalMembershipCardRegisterRequestData.getPartnerId());
                if (O == null) {
                    Log.m285d("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: serverCerts are null and not stored.");
                } else {
                    globalMembershipCardRegisterRequestData.setServerCaCert(O.lj);
                    globalMembershipCardRegisterRequestData.setServerEncCert(O.lk);
                    globalMembershipCardRegisterRequestData.setServerVerCert(O.ll);
                }
                Card aY = aY();
                if (aY == null || aY.ad() == null) {
                    iGlobalMembershipCardRegisterCallback.onFail(-1, globalMembershipCardRegisterResponseData);
                    Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData: unable to get card Object");
                    return;
                }
                globalMembershipCardRegisterResponseData = aY.ad().getGlobalMembershipCardTzEncDataTA(globalMembershipCardRegisterRequestData);
                if (globalMembershipCardRegisterResponseData.getErrorCode() == 0) {
                    m410a(globalMembershipCardRegisterRequestData.getTokenId(), globalMembershipCardRegisterRequestData.getPartnerId(), globalMembershipCardRegisterRequestData.getUserId());
                    iGlobalMembershipCardRegisterCallback.onSuccess(globalMembershipCardRegisterResponseData);
                    return;
                }
                iGlobalMembershipCardRegisterCallback.onFail(globalMembershipCardRegisterResponseData.getErrorCode(), globalMembershipCardRegisterResponseData);
            }
        } else {
            globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
            Log.m285d("GlobalMembershipCardProcessor", "getGlobalMembershipCardTzEncData() calls onFail. this country doesn't support GlobalMembership");
            if (iGlobalMembershipCardRegisterCallback != null) {
                iGlobalMembershipCardRegisterCallback.onFail(PaymentFramework.RESULT_CODE_FAIL_CARD_NOT_SUPPORTED, globalMembershipCardRegisterResponseData);
            }
        }
    }

    public void m414a(List<ExtractGlobalMembershipCardDetailRequest> list, IGlobalMembershipCardExtractDetailCallback iGlobalMembershipCardExtractDetailCallback) {
        if (list != null) {
            try {
                if (list.size() > 0 && iGlobalMembershipCardExtractDetailCallback != null) {
                    if (aZ()) {
                        Log.m285d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail() " + list);
                        String[] strArr = new String[list.size()];
                        byte[][] bArr = new byte[list.size()][];
                        Card card = null;
                        int i = 0;
                        for (ExtractGlobalMembershipCardDetailRequest extractGlobalMembershipCardDetailRequest : list) {
                            if (TextUtils.isEmpty(extractGlobalMembershipCardDetailRequest.getTokenId()) || extractGlobalMembershipCardDetailRequest.getTzEncData() == null) {
                                Log.m286e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: invalid inputs: data value is null");
                                iGlobalMembershipCardExtractDetailCallback.onFail(-5);
                                return;
                            }
                            Log.m285d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: tokenId [" + extractGlobalMembershipCardDetailRequest.getTokenId() + "] [" + Arrays.toString(extractGlobalMembershipCardDetailRequest.getTzEncData()) + "]");
                            Card N = m408N(extractGlobalMembershipCardDetailRequest.getTokenId());
                            if (N == null || N.ad() == null) {
                                Log.m286e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: unable to get card Object");
                                iGlobalMembershipCardExtractDetailCallback.onFail(-1);
                                return;
                            }
                            strArr[i] = extractGlobalMembershipCardDetailRequest.getTokenId();
                            bArr[i] = extractGlobalMembershipCardDetailRequest.getTzEncData();
                            i++;
                            card = N;
                        }
                        List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetailTA = card.ad().extractGlobalMembershipCardDetailTA(strArr, bArr);
                        if (extractGlobalMembershipCardDetailTA == null || extractGlobalMembershipCardDetailTA.size() <= 0) {
                            Log.m286e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: return data is null");
                            iGlobalMembershipCardExtractDetailCallback.onFail(-1);
                        }
                        for (GlobalMembershipCardDetail globalMembershipCardDetail : extractGlobalMembershipCardDetailTA) {
                            if (globalMembershipCardDetail.getErrorCode() != 0) {
                                Log.m286e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: response error [" + globalMembershipCardDetail.getErrorCode() + "]");
                                iGlobalMembershipCardExtractDetailCallback.onFail(globalMembershipCardDetail.getErrorCode());
                                return;
                            } else if (DEBUG) {
                                Log.m285d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail response[" + globalMembershipCardDetail + "]");
                            }
                        }
                        iGlobalMembershipCardExtractDetailCallback.onSuccess(extractGlobalMembershipCardDetailTA);
                        return;
                    }
                    GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
                    Log.m285d("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail() calls onFail. this country doesn't support GlobalMembership");
                    if (iGlobalMembershipCardExtractDetailCallback != null) {
                        iGlobalMembershipCardExtractDetailCallback.onFail(PaymentFramework.RESULT_CODE_FAIL_CARD_NOT_SUPPORTED);
                        return;
                    }
                    return;
                }
            } catch (Throwable e) {
                Log.m284c("GlobalMembershipCardProcessor", e.getMessage(), e);
                return;
            }
        }
        if (iGlobalMembershipCardExtractDetailCallback != null) {
            Log.m286e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: invalid inputs: data is null");
            iGlobalMembershipCardExtractDetailCallback.onFail(-5);
            return;
        }
        Log.m286e("GlobalMembershipCardProcessor", "extractGlobalMembershipCardDetail: invalid inputs: callback is null");
    }

    public void m413a(String str, byte[] bArr, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
        try {
            Log.m285d("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay is not supported yet");
            if (bArr == null || iPayCallback == null) {
                if (iPayCallback != null) {
                    Log.m286e("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay: invalid inputs: data is null");
                    iPayCallback.onFail(str, -5);
                    return;
                }
                Log.m286e("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay: invalid inputs: callback is null");
            } else if (aZ()) {
                iPayCallback.onFail(str, DSRPConstants.DSRP_INPUT_CURRENCY_CODE_MAX);
            } else {
                GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = new GlobalMembershipCardRegisterResponseData();
                Log.m285d("GlobalMembershipCardProcessor", "startGlobalMembershipCardPay() calls onFail. this country doesn't support GlobalMembership");
                if (iPayCallback != null) {
                    iPayCallback.onFail(str, PaymentFramework.RESULT_CODE_FAIL_CARD_NOT_SUPPORTED);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void m410a(String str, String str2, String str3) {
        if (this.iJ == null) {
            Log.m285d("GlobalMembershipCardProcessor", "Initializing Samsung Account - userId = " + str3);
            this.iJ = Account.m551a(this.mContext, str3);
        }
        if (this.iJ == null) {
            Log.m286e("GlobalMembershipCardProcessor", "unable to create account");
            return;
        }
        try {
            Card card = new Card(this.mContext, PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP, PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP, PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP, 0);
            Token token = new Token();
            token.setTokenStatus(TokenStatus.ACTIVE);
            token.setTokenId(str);
            ProviderTokenKey providerTokenKey = new ProviderTokenKey(str);
            token.m663c(providerTokenKey);
            card.m576a(token);
            card.setEnrollmentId(str);
            this.iJ.m556a(card);
            if (card.ad() != null) {
                Log.m285d("GlobalMembershipCardProcessor", "Set Provider Token Key for Global Membership Card");
                card.ad().setProviderTokenKey(providerTokenKey);
            }
            TokenRecord tokenRecord = new TokenRecord(str);
            tokenRecord.setUserId(str3);
            tokenRecord.setTrTokenId(str);
            tokenRecord.m1255j(0);
            tokenRecord.setCardBrand(PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP);
            tokenRecord.setTokenStatus(TokenStatus.ACTIVE);
            tokenRecord.setTokenRefId(str2);
            this.jJ.m1227c(tokenRecord);
            if (DEBUG) {
                Log.m285d("GlobalMembershipCardProcessor", "addCardToken. add partnerId [" + str2 + "], tokenId[" + str + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Card aY() {
        try {
            return new Card(this.mContext, PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP, PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP, PaymentFramework.CARD_BRAND_GLOBAL_MEMBERSHIP, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Card m408N(String str) {
        Card card = null;
        if (this.iJ == null) {
            Log.m285d("GlobalMembershipCardProcessor", "getGlobalMembershipCardObject. Initializing Samsung Account - null userId ");
            this.iJ = Account.m551a(this.mContext, null);
        }
        if (this.iJ == null) {
            Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardObject. unable to create account");
        } else {
            card = this.iJ.m559r(str);
            if (card == null) {
                Log.m286e("GlobalMembershipCardProcessor", "getGlobalMembershipCardObject. failed to find Card");
            }
        }
        return card;
    }

    protected boolean m415a(String str, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        Log.m285d("GlobalMembershipCardProcessor", "saveReceivedCerts: partnerId:" + str);
        if (TextUtils.isEmpty(str) || bArr == null || bArr2 == null || bArr3 == null) {
            Log.m286e("GlobalMembershipCardProcessor", "saveReceivedCerts: invalid input");
        } else if (this.lh == null) {
            Log.m290w("GlobalMembershipCardProcessor", "saveReceivedCerts: mServerCertsDb null, cant add server certs to Db");
        } else {
            CertificateInfo certificateInfo = new CertificateInfo();
            certificateInfo.setContent(Utils.convertToPem(bArr));
            certificateInfo.setAlias(CertificateInfo.CERT_USAGE_CA);
            certificateInfo.setUsage(CertificateInfo.CERT_USAGE_CA);
            CertificateInfo certificateInfo2 = new CertificateInfo();
            certificateInfo2.setContent(Utils.convertToPem(bArr2));
            certificateInfo2.setAlias(CertificateInfo.CERT_USAGE_ENC);
            certificateInfo2.setUsage(CertificateInfo.CERT_USAGE_ENC);
            CertificateInfo certificateInfo3 = new CertificateInfo();
            certificateInfo3.setContent(Utils.convertToPem(bArr3));
            certificateInfo3.setAlias(CertificateInfo.CERT_USAGE_VER);
            certificateInfo3.setUsage(CertificateInfo.CERT_USAGE_VER);
            this.lh.m1219a(str, certificateInfo);
            this.lh.m1219a(str, certificateInfo2);
            this.lh.m1219a(str, certificateInfo3);
        }
        return false;
    }

    private GlobalMembershipCardProcessor m409O(String str) {
        List<CertificateInfo> a = this.lh.m1220a(ServerCertsColumn.CARD_TYPE, str);
        if (a != null && a.size() > 0) {
            byte[] bArr = null;
            byte[] bArr2 = null;
            byte[] bArr3 = null;
            for (CertificateInfo certificateInfo : a) {
                byte[] bArr4;
                String replace = certificateInfo.getContent().replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR);
                if (CertificateInfo.CERT_USAGE_ENC.equals(certificateInfo.getUsage())) {
                    byte[] bArr5 = bArr;
                    bArr = bArr2;
                    bArr2 = Base64.decode(replace, 0);
                    bArr4 = bArr5;
                } else if (CertificateInfo.CERT_USAGE_VER.equals(certificateInfo.getUsage())) {
                    bArr2 = bArr3;
                    bArr4 = bArr;
                    bArr = Base64.decode(replace, 0);
                } else if (CertificateInfo.CERT_USAGE_CA.equals(certificateInfo.getUsage())) {
                    bArr4 = Base64.decode(replace, 0);
                    bArr = bArr2;
                    bArr2 = bArr3;
                } else {
                    bArr4 = bArr;
                    bArr = bArr2;
                    bArr2 = bArr3;
                }
                bArr3 = bArr2;
                bArr2 = bArr;
                bArr = bArr4;
            }
            if (bArr3 != null && bArr2 != null && bArr != null) {
                return new GlobalMembershipCardProcessor(bArr, bArr3, bArr2);
            }
            Log.m286e("GlobalMembershipCardProcessor", "getServerCerts : failed to get certificates. cert value is null");
            return null;
        } else if (DEBUG) {
            Log.m286e("GlobalMembershipCardProcessor", "getServerCerts : failed to get certificates [" + str + "]");
            return null;
        } else {
            Log.m286e("GlobalMembershipCardProcessor", "getServerCerts : failed to get certificates");
            return null;
        }
    }

    private boolean aZ() {
        return this.li.contains(Utils.fP().toUpperCase());
    }
}
