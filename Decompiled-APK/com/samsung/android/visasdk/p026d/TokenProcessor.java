package com.samsung.android.visasdk.p026d;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.data.EnrollPanRequest;
import com.samsung.android.visasdk.facade.data.ProvisionAckRequest;
import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.ProvisioningStatus;
import com.samsung.android.visasdk.facade.data.ReplenishAckRequest;
import com.samsung.android.visasdk.facade.data.ReplenishRequest;
import com.samsung.android.visasdk.facade.data.Signature;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenMetaData;
import com.samsung.android.visasdk.facade.data.TokenStatus;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.p023a.Utils;
import com.samsung.android.visasdk.p024b.CryptoManager;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.ICC;
import com.samsung.android.visasdk.paywave.model.ODAData;
import com.samsung.android.visasdk.paywave.model.PaymentData;
import com.samsung.android.visasdk.paywave.model.QVSDCWithODA;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.samsung.android.visasdk.storage.DbEnhancedTokenInfoDao;
import com.samsung.android.visasdk.storage.DbTvlDao;
import com.samsung.android.visasdk.storage.model.DbSugarData;
import com.visa.tainterface.VisaTAException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.bouncycastle.crypto.macs.SkeinMac;

/* renamed from: com.samsung.android.visasdk.d.a */
public class TokenProcessor {
    private static TokenProcessor mTokenProcessor;
    private CryptoManager Dl;
    private DbTvlDao Et;
    private DbEnhancedTokenInfoDao Gw;

    static {
        mTokenProcessor = null;
    }

    public static synchronized TokenProcessor at(Context context) {
        TokenProcessor tokenProcessor;
        synchronized (TokenProcessor.class) {
            if (mTokenProcessor == null) {
                mTokenProcessor = new TokenProcessor(context);
                if (mTokenProcessor == null) {
                    throw new InitializationException("token processor cannot be initialized");
                }
            }
            tokenProcessor = mTokenProcessor;
        }
        return tokenProcessor;
    }

    TokenProcessor(Context context) {
        this.Dl = null;
        this.Gw = null;
        this.Et = null;
        this.Dl = CryptoManager.fV();
        if (this.Dl == null) {
            throw new InitializationException("crypto manager cannot be initialized");
        }
        this.Gw = new DbEnhancedTokenInfoDao(context);
        if (this.Gw == null) {
            throw new InitializationException("db cannot be initialized and used");
        }
        this.Et = new DbTvlDao(context);
        if (this.Et == null) {
            throw new InitializationException("cannot access db");
        }
    }

    public EnrollPanRequest constructEnrollRequest(byte[] bArr) {
        byte[] bArr2 = null;
        if (bArr == null || bArr.length <= 0) {
            Log.m1300d("TokenProcessor", "enroll request data cannot be null");
            return null;
        }
        try {
            if (this.Dl != null) {
                bArr2 = this.Dl.m1297b(bArr, false);
            }
            EnrollPanRequest enrollPanRequest = new EnrollPanRequest();
            enrollPanRequest.setEncPaymentInstrument(new String(bArr2));
            return enrollPanRequest;
        } catch (VisaTAException e) {
            Log.m1301e("TokenProcessor", "cannot prepare vts data");
            e.printStackTrace();
            return null;
        }
    }

    public TokenKey storeProvisionedToken(ProvisionResponse provisionResponse, String str) {
        TokenInfo tokenInfo = provisionResponse.getTokenInfo();
        try {
            String bH = this.Dl.bH(tokenInfo.getEncTokenInfo());
            String bH2 = this.Dl.bH(tokenInfo.getHceData().getDynParams().getEncKeyInfo());
            if (provisionResponse.getODAData() != null) {
                QVSDCWithODA a = m1305a(provisionResponse.getODAData(), tokenInfo);
                if (a != null) {
                    provisionResponse.getTokenInfo().getHceData().getStaticParams().getQVSDCData().setqVSDCWithODA(a);
                }
            }
            return this.Gw.m1364a(provisionResponse, str, Utils.hexStringToBytes(bH2), Utils.hexStringToBytes(bH));
        } catch (Exception e) {
            Log.m1301e("TokenProcessor", " cannot store provisioned token");
            throw new TokenInvalidException("cannot store provisioned token");
        }
    }

    public ProvisionAckRequest constructProvisionAck(TokenKey tokenKey) {
        DbSugarData d = this.Gw.m1368d(tokenKey);
        if (d == null) {
            throw new TokenInvalidException("cannot get data with the given token");
        }
        ProvisionAckRequest provisionAckRequest = new ProvisionAckRequest();
        provisionAckRequest.setApi(d.getApi());
        provisionAckRequest.setProvisioningStatus(ProvisioningStatus.SUCCESS.toString());
        return provisionAckRequest;
    }

    public void deleteToken(TokenKey tokenKey) {
        this.Gw.deleteToken(tokenKey);
    }

    public String getTokenStatus(TokenKey tokenKey) {
        return this.Gw.getTokenStatus(tokenKey);
    }

    public boolean updateTokenStatus(TokenKey tokenKey, TokenStatus tokenStatus) {
        return this.Gw.updateTokenStatus(tokenKey, tokenStatus);
    }

    private TokenInfo m1306g(TokenKey tokenKey) {
        DbSugarData d = this.Gw.m1368d(tokenKey);
        if (d == null) {
            Log.m1301e("TokenProcessor", "sugar data not found");
            return null;
        }
        DynParams dynParams = new DynParams();
        dynParams.setApi(d.getApi());
        dynParams.setSc(d.getSequenceCounter());
        HceData hceData = new HceData();
        hceData.setDynParams(dynParams);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setHceData(hceData);
        return tokenInfo;
    }

    public ReplenishRequest constructReplenishRequest(TokenKey tokenKey) {
        ReplenishRequest replenishRequest = new ReplenishRequest();
        TokenInfo g = m1306g(tokenKey);
        if (g == null) {
            return null;
        }
        DynParams dynParams = g.getHceData().getDynParams();
        List a = this.Et.m1373a(tokenKey, g.getHceData().getDynParams().getApi());
        g.getHceData().getDynParams().setTvl(a);
        replenishRequest.setTokenInfo(g);
        String str = BuildConfig.FLAVOR;
        if (a == null || a.isEmpty()) {
            Log.m1300d("TokenProcessor", "tvls empty ");
        } else {
            str = (String) a.get(a.size() - 1);
        }
        str = dynParams.getApi() + dynParams.getSc() + str;
        Log.m1300d("TokenProcessor", "replenish request mactext " + str);
        if (str.length() > 511) {
            str = str.substring(0, SkeinMac.SKEIN_512);
        }
        try {
            CryptoManager cryptoManager = this.Dl;
            byte[] j = CryptoManager.m1293j(this.Gw.m1370f(tokenKey), str.getBytes());
            Signature signature = new Signature();
            signature.setMac(Utils.m1285o(j));
            replenishRequest.setSignature(signature);
            replenishRequest.setEncryptionMetaData(this.Gw.m1369e(tokenKey));
            return replenishRequest;
        } catch (VisaTAException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ReplenishAckRequest constructReplenishAcknowledgementRequest(TokenKey tokenKey) {
        ReplenishAckRequest replenishAckRequest = new ReplenishAckRequest();
        replenishAckRequest.setTokenInfo(m1306g(tokenKey));
        return replenishAckRequest;
    }

    public boolean processReplenishmentResponse(TokenKey tokenKey, TokenInfo tokenInfo) {
        try {
            DynParams dynParams = tokenInfo.getHceData().getDynParams();
            dynParams.setEncKeyInfo(this.Dl.bH(dynParams.getEncKeyInfo()));
            return this.Gw.m1366a(tokenKey, tokenInfo);
        } catch (VisaTAException e) {
            e.printStackTrace();
            Log.m1301e("TokenProcessor", "cannot store the key");
            return false;
        }
    }

    public static String m1304A(String str, String str2) {
        if (str == null || str2 == null || str.isEmpty() || str2.isEmpty()) {
            Log.m1301e("TokenProcessor", "Input is empty");
            return null;
        }
        Calendar.getInstance(Locale.US).get(1);
        Calendar.getInstance(Locale.US).get(2);
        try {
            int parseInt = Integer.parseInt(str);
            int parseInt2 = Integer.parseInt(str2);
            Calendar instance = Calendar.getInstance(Locale.US);
            if (instance == null) {
                Log.m1301e("TokenProcessor", "calendar is null");
                return null;
            }
            instance.set(parseInt, parseInt2 - 1, 1);
            instance.set(5, instance.getActualMaximum(5));
            Date time = instance.getTime();
            if (time == null) {
                Log.m1301e("TokenProcessor", "date is null");
                return null;
            }
            String format = new SimpleDateFormat("yyMMdd", Locale.US).format(time);
            Log.m1300d("TokenProcessor", "formattedDate " + format);
            return format;
        } catch (NumberFormatException e) {
            Log.m1301e("TokenProcessor", "Cannot convert integer");
            return null;
        }
    }

    private QVSDCWithODA m1305a(ODAData oDAData, TokenInfo tokenInfo) {
        if (tokenInfo == null || tokenInfo.getExpirationDate() == null || tokenInfo.getExpirationDate().getMonth() == null || tokenInfo.getExpirationDate().getYear() == null) {
            Log.m1301e("TokenProcessor", "tokenInfo null");
            return null;
        } else if (oDAData == null || oDAData.getTokenBinPubKeyCert() == null || oDAData.getIccPubKeyCert() == null) {
            Log.m1301e("TokenProcessor", "odaData null");
            return null;
        } else {
            QVSDCWithODA qVSDCWithODA = new QVSDCWithODA();
            qVSDCWithODA.setAfl(oDAData.getAppFileLocator());
            qVSDCWithODA.setAip(oDAData.getAppProfile());
            String A = TokenProcessor.m1304A(tokenInfo.getExpirationDate().getYear(), tokenInfo.getExpirationDate().getMonth());
            if (A == null) {
                Log.m1301e("TokenProcessor", "expiryDate null");
                return null;
            }
            qVSDCWithODA.setAppExpDate(A);
            qVSDCWithODA.setCapki(oDAData.getCaPubKeyIndex());
            try {
                if (!(oDAData.getTokenBinPubKeyCert().getCertificate() == null || oDAData.getTokenBinPubKeyCert().getCertificate().isEmpty())) {
                    Log.m1300d("TokenProcessor", "base64 issuer cert: " + oDAData.getTokenBinPubKeyCert().getCertificate());
                    A = Utils.m1285o(Base64.decode(oDAData.getTokenBinPubKeyCert().getCertificate().getBytes(), 0));
                    qVSDCWithODA.setIPubkCert(A);
                    Log.m1300d("TokenProcessor", "decodedHexString issuer cert: " + A);
                }
                try {
                    if (!(oDAData.getTokenBinPubKeyCert().getRemainder() == null || oDAData.getTokenBinPubKeyCert().getRemainder().isEmpty())) {
                        Log.m1300d("TokenProcessor", "issuer cert:  remainder: " + oDAData.getTokenBinPubKeyCert().getRemainder());
                        A = Utils.m1285o(Base64.decode(oDAData.getTokenBinPubKeyCert().getRemainder().getBytes(), 0));
                        Log.m1300d("TokenProcessor", "decodedHexString issuer cert remainder: " + A);
                        qVSDCWithODA.setIPubkRem(A);
                    }
                    qVSDCWithODA.setIPubkExpo(oDAData.getTokenBinPubKeyCert().getExponent());
                    qVSDCWithODA.setIPubkExpirationDate(oDAData.getTokenBinPubKeyCert().getExpirationDate());
                    ICC icc = new ICC();
                    try {
                        String o;
                        if (!(oDAData.getIccPubKeyCert().getCertificate() == null || oDAData.getIccPubKeyCert().getCertificate().isEmpty())) {
                            Log.m1300d("TokenProcessor", "base64 iccPubKeyCert: " + oDAData.getIccPubKeyCert().getCertificate());
                            o = Utils.m1285o(Base64.decode(oDAData.getIccPubKeyCert().getCertificate().getBytes(), 0));
                            Log.m1300d("TokenProcessor", "decodedHexString iccPubKeyCert: " + o);
                            icc.setIccPubKCert(o);
                        }
                        try {
                            if (!(oDAData.getIccPubKeyCert().getRemainder() == null || oDAData.getIccPubKeyCert().getRemainder().isEmpty())) {
                                Log.m1300d("TokenProcessor", " iccPubKeyCert:  remainder: " + oDAData.getIccPubKeyCert().getRemainder());
                                o = Utils.m1285o(Base64.decode(oDAData.getIccPubKeyCert().getRemainder().getBytes(), 0));
                                Log.m1300d("TokenProcessor", "decodedHexString IccPubKeyCert remainder: " + o);
                                icc.setIccPubKRem(o);
                            }
                            icc.setIccPubKExpo(oDAData.getIccPubKeyCert().getExponent());
                            icc.setIccPubKExpirationDate(oDAData.getIccPubKeyCert().getExpirationDate());
                            try {
                                icc.setEncIccCRTPrivKey(CryptoManager.fV().bH(oDAData.getEnciccPrivateKey()));
                                qVSDCWithODA.setIcc(icc);
                                return qVSDCWithODA;
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.m1301e("TokenProcessor", " cannot store provisioned encIccCRTPrivKey");
                                return null;
                            }
                        } catch (IllegalArgumentException e2) {
                            e2.printStackTrace();
                            Log.m1301e("TokenProcessor", "base64 decode failed for iccPubKeyCert remainder");
                            return null;
                        }
                    } catch (IllegalArgumentException e22) {
                        e22.printStackTrace();
                        Log.m1301e("TokenProcessor", "base64 decode failed for iccPubKeyCert");
                        return null;
                    }
                } catch (IllegalArgumentException e222) {
                    e222.printStackTrace();
                    Log.m1301e("TokenProcessor", "base64 decode failed for iPubKeyCert remainder");
                    return null;
                }
            } catch (IllegalArgumentException e2222) {
                e2222.printStackTrace();
                Log.m1301e("TokenProcessor", "base64 decode failed for iPubKeyCert");
                return null;
            }
        }
    }

    public boolean isMstSupported(TokenKey tokenKey) {
        return this.Gw.isMstSupported(tokenKey);
    }

    public Bundle getTokenMetaData(TokenKey tokenKey) {
        Bundle bundle = null;
        Log.m1300d("TokenProcessor", "getTokenMetaData");
        if (tokenKey == null) {
            Log.m1301e("TokenProcessor", "invalid param tokenKey");
        } else {
            PaymentData b = this.Gw.m1367b(tokenKey, false);
            if (b == null) {
                Log.m1301e("TokenProcessor", "invalid data");
            } else if (b.getTokenInfo() == null || b.getTokenInfo().getHceData() == null || b.getTokenInfo().getHceData().getStaticParams() == null || b.getTokenInfo().getHceData().getStaticParams().getQVSDCData() == null) {
                Log.m1301e("TokenProcessor", "invalid data");
            } else if (b.getTokenInfo().getHceData().getStaticParams().getQVSDCData().getCountryCode() == null) {
                Log.m1301e("TokenProcessor", "invalid getCountryCode");
            } else {
                bundle = new Bundle();
                String countryCode = b.getTokenInfo().getHceData().getStaticParams().getQVSDCData().getCountryCode();
                if (countryCode != null) {
                    try {
                        countryCode = Integer.toString(Integer.parseInt(countryCode));
                        Log.m1300d("TokenProcessor", "metaData countryCode " + countryCode);
                        bundle.putString(TokenMetaData.QVSDC_ISSUER_COUNTRY_CODE, countryCode);
                    } catch (NumberFormatException e) {
                        Log.m1301e("TokenProcessor", "invalid issuerCountryCode");
                    }
                }
            }
        }
        return bundle;
    }
}
