/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.util.Base64
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.text.SimpleDateFormat
 *  java.util.Calendar
 *  java.util.Date
 *  java.util.List
 *  java.util.Locale
 */
package com.samsung.android.visasdk.d;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.samsung.android.visasdk.a.b;
import com.samsung.android.visasdk.facade.data.EnrollPanRequest;
import com.samsung.android.visasdk.facade.data.ProvisionAckRequest;
import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.ProvisioningStatus;
import com.samsung.android.visasdk.facade.data.ReplenishAckRequest;
import com.samsung.android.visasdk.facade.data.ReplenishRequest;
import com.samsung.android.visasdk.facade.data.Signature;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenStatus;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.ExpirationDate;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.ICC;
import com.samsung.android.visasdk.paywave.model.ICCPubKeyCert;
import com.samsung.android.visasdk.paywave.model.ODAData;
import com.samsung.android.visasdk.paywave.model.PaymentData;
import com.samsung.android.visasdk.paywave.model.QVSDCData;
import com.samsung.android.visasdk.paywave.model.QVSDCWithODA;
import com.samsung.android.visasdk.paywave.model.StaticParams;
import com.samsung.android.visasdk.paywave.model.TokenBinPubKeyCert;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.samsung.android.visasdk.storage.e;
import com.samsung.android.visasdk.storage.model.DbSugarData;
import com.visa.tainterface.VisaTAException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class a {
    private static a mTokenProcessor = null;
    private com.samsung.android.visasdk.b.b Dl = com.samsung.android.visasdk.b.b.fV();
    private e Et = null;
    private com.samsung.android.visasdk.storage.b Gw = null;

    a(Context context) {
        if (this.Dl == null) {
            throw new InitializationException("crypto manager cannot be initialized");
        }
        this.Gw = new com.samsung.android.visasdk.storage.b(context);
        if (this.Gw == null) {
            throw new InitializationException("db cannot be initialized and used");
        }
        this.Et = new e(context);
        if (this.Et == null) {
            throw new InitializationException("cannot access db");
        }
    }

    public static String A(String string, String string2) {
        int n2;
        int n3;
        if (string == null || string2 == null || string.isEmpty() || string2.isEmpty()) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "Input is empty");
            return null;
        }
        Calendar.getInstance((Locale)Locale.US).get(1);
        Calendar.getInstance((Locale)Locale.US).get(2);
        try {
            n2 = Integer.parseInt((String)string);
            n3 = Integer.parseInt((String)string2);
        }
        catch (NumberFormatException numberFormatException) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "Cannot convert integer");
            return null;
        }
        Calendar calendar = Calendar.getInstance((Locale)Locale.US);
        if (calendar == null) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "calendar is null");
            return null;
        }
        calendar.set(n2, n3 - 1, 1);
        calendar.set(5, calendar.getActualMaximum(5));
        Date date = calendar.getTime();
        if (date == null) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "date is null");
            return null;
        }
        String string3 = new SimpleDateFormat("yyMMdd", Locale.US).format(date);
        com.samsung.android.visasdk.c.a.d("TokenProcessor", "formattedDate " + string3);
        return string3;
    }

    private QVSDCWithODA a(ODAData oDAData, TokenInfo tokenInfo) {
        ICC iCC;
        QVSDCWithODA qVSDCWithODA;
        block16 : {
            block15 : {
                block14 : {
                    block13 : {
                        if (tokenInfo == null || tokenInfo.getExpirationDate() == null || tokenInfo.getExpirationDate().getMonth() == null || tokenInfo.getExpirationDate().getYear() == null) {
                            com.samsung.android.visasdk.c.a.e("TokenProcessor", "tokenInfo null");
                            return null;
                        }
                        if (oDAData == null || oDAData.getTokenBinPubKeyCert() == null || oDAData.getIccPubKeyCert() == null) {
                            com.samsung.android.visasdk.c.a.e("TokenProcessor", "odaData null");
                            return null;
                        }
                        qVSDCWithODA = new QVSDCWithODA();
                        qVSDCWithODA.setAfl(oDAData.getAppFileLocator());
                        qVSDCWithODA.setAip(oDAData.getAppProfile());
                        String string = a.A(tokenInfo.getExpirationDate().getYear(), tokenInfo.getExpirationDate().getMonth());
                        if (string == null) {
                            com.samsung.android.visasdk.c.a.e("TokenProcessor", "expiryDate null");
                            return null;
                        }
                        qVSDCWithODA.setAppExpDate(string);
                        qVSDCWithODA.setCapki(oDAData.getCaPubKeyIndex());
                        try {
                            if (oDAData.getTokenBinPubKeyCert().getCertificate() == null || oDAData.getTokenBinPubKeyCert().getCertificate().isEmpty()) break block13;
                            com.samsung.android.visasdk.c.a.d("TokenProcessor", "base64 issuer cert: " + oDAData.getTokenBinPubKeyCert().getCertificate());
                            String string2 = b.o(Base64.decode((byte[])oDAData.getTokenBinPubKeyCert().getCertificate().getBytes(), (int)0));
                            qVSDCWithODA.setIPubkCert(string2);
                            com.samsung.android.visasdk.c.a.d("TokenProcessor", "decodedHexString issuer cert: " + string2);
                        }
                        catch (IllegalArgumentException illegalArgumentException) {
                            illegalArgumentException.printStackTrace();
                            com.samsung.android.visasdk.c.a.e("TokenProcessor", "base64 decode failed for iPubKeyCert");
                            return null;
                        }
                    }
                    try {
                        if (oDAData.getTokenBinPubKeyCert().getRemainder() == null || oDAData.getTokenBinPubKeyCert().getRemainder().isEmpty()) break block14;
                        com.samsung.android.visasdk.c.a.d("TokenProcessor", "issuer cert:  remainder: " + oDAData.getTokenBinPubKeyCert().getRemainder());
                        String string = b.o(Base64.decode((byte[])oDAData.getTokenBinPubKeyCert().getRemainder().getBytes(), (int)0));
                        com.samsung.android.visasdk.c.a.d("TokenProcessor", "decodedHexString issuer cert remainder: " + string);
                        qVSDCWithODA.setIPubkRem(string);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        illegalArgumentException.printStackTrace();
                        com.samsung.android.visasdk.c.a.e("TokenProcessor", "base64 decode failed for iPubKeyCert remainder");
                        return null;
                    }
                }
                qVSDCWithODA.setIPubkExpo(oDAData.getTokenBinPubKeyCert().getExponent());
                qVSDCWithODA.setIPubkExpirationDate(oDAData.getTokenBinPubKeyCert().getExpirationDate());
                iCC = new ICC();
                try {
                    if (oDAData.getIccPubKeyCert().getCertificate() == null || oDAData.getIccPubKeyCert().getCertificate().isEmpty()) break block15;
                    com.samsung.android.visasdk.c.a.d("TokenProcessor", "base64 iccPubKeyCert: " + oDAData.getIccPubKeyCert().getCertificate());
                    String string = b.o(Base64.decode((byte[])oDAData.getIccPubKeyCert().getCertificate().getBytes(), (int)0));
                    com.samsung.android.visasdk.c.a.d("TokenProcessor", "decodedHexString iccPubKeyCert: " + string);
                    iCC.setIccPubKCert(string);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    illegalArgumentException.printStackTrace();
                    com.samsung.android.visasdk.c.a.e("TokenProcessor", "base64 decode failed for iccPubKeyCert");
                    return null;
                }
            }
            try {
                if (oDAData.getIccPubKeyCert().getRemainder() == null || oDAData.getIccPubKeyCert().getRemainder().isEmpty()) break block16;
                com.samsung.android.visasdk.c.a.d("TokenProcessor", " iccPubKeyCert:  remainder: " + oDAData.getIccPubKeyCert().getRemainder());
                String string = b.o(Base64.decode((byte[])oDAData.getIccPubKeyCert().getRemainder().getBytes(), (int)0));
                com.samsung.android.visasdk.c.a.d("TokenProcessor", "decodedHexString IccPubKeyCert remainder: " + string);
                iCC.setIccPubKRem(string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                illegalArgumentException.printStackTrace();
                com.samsung.android.visasdk.c.a.e("TokenProcessor", "base64 decode failed for iccPubKeyCert remainder");
                return null;
            }
        }
        iCC.setIccPubKExpo(oDAData.getIccPubKeyCert().getExponent());
        iCC.setIccPubKExpirationDate(oDAData.getIccPubKeyCert().getExpirationDate());
        try {
            iCC.setEncIccCRTPrivKey(com.samsung.android.visasdk.b.b.fV().bH(oDAData.getEnciccPrivateKey()));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            com.samsung.android.visasdk.c.a.e("TokenProcessor", " cannot store provisioned encIccCRTPrivKey");
            return null;
        }
        qVSDCWithODA.setIcc(iCC);
        return qVSDCWithODA;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static a at(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (mTokenProcessor != null) return mTokenProcessor;
            mTokenProcessor = new a(context);
            if (mTokenProcessor != null) return mTokenProcessor;
            throw new InitializationException("token processor cannot be initialized");
        }
    }

    private TokenInfo g(TokenKey tokenKey) {
        DbSugarData dbSugarData = this.Gw.d(tokenKey);
        if (dbSugarData == null) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "sugar data not found");
            return null;
        }
        DynParams dynParams = new DynParams();
        dynParams.setApi(dbSugarData.getApi());
        dynParams.setSc(dbSugarData.getSequenceCounter());
        HceData hceData = new HceData();
        hceData.setDynParams(dynParams);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setHceData(hceData);
        return tokenInfo;
    }

    public EnrollPanRequest constructEnrollRequest(byte[] arrby) {
        byte[] arrby2;
        block4 : {
            byte[] arrby3;
            if (arrby == null || arrby.length <= 0) {
                com.samsung.android.visasdk.c.a.d("TokenProcessor", "enroll request data cannot be null");
                return null;
            }
            try {
                com.samsung.android.visasdk.b.b b2 = this.Dl;
                arrby2 = null;
                if (b2 == null) break block4;
            }
            catch (VisaTAException visaTAException) {
                com.samsung.android.visasdk.c.a.e("TokenProcessor", "cannot prepare vts data");
                visaTAException.printStackTrace();
                return null;
            }
            arrby2 = arrby3 = this.Dl.b(arrby, false);
        }
        EnrollPanRequest enrollPanRequest = new EnrollPanRequest();
        enrollPanRequest.setEncPaymentInstrument(new String(arrby2));
        return enrollPanRequest;
    }

    public ProvisionAckRequest constructProvisionAck(TokenKey tokenKey) {
        DbSugarData dbSugarData = this.Gw.d(tokenKey);
        if (dbSugarData == null) {
            throw new TokenInvalidException("cannot get data with the given token");
        }
        ProvisionAckRequest provisionAckRequest = new ProvisionAckRequest();
        provisionAckRequest.setApi(dbSugarData.getApi());
        provisionAckRequest.setProvisioningStatus(ProvisioningStatus.SUCCESS.toString());
        return provisionAckRequest;
    }

    public ReplenishAckRequest constructReplenishAcknowledgementRequest(TokenKey tokenKey) {
        ReplenishAckRequest replenishAckRequest = new ReplenishAckRequest();
        replenishAckRequest.setTokenInfo(this.g(tokenKey));
        return replenishAckRequest;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ReplenishRequest constructReplenishRequest(TokenKey tokenKey) {
        byte[] arrby;
        ReplenishRequest replenishRequest = new ReplenishRequest();
        TokenInfo tokenInfo = this.g(tokenKey);
        if (tokenInfo == null) {
            return null;
        }
        DynParams dynParams = tokenInfo.getHceData().getDynParams();
        List<String> list = this.Et.a(tokenKey, tokenInfo.getHceData().getDynParams().getApi());
        tokenInfo.getHceData().getDynParams().setTvl(list);
        replenishRequest.setTokenInfo(tokenInfo);
        String string = "";
        if (list != null && !list.isEmpty()) {
            string = (String)list.get(-1 + list.size());
        } else {
            com.samsung.android.visasdk.c.a.d("TokenProcessor", "tvls empty ");
        }
        String string2 = dynParams.getApi() + dynParams.getSc() + string;
        com.samsung.android.visasdk.c.a.d("TokenProcessor", "replenish request mactext " + string2);
        if (string2.length() > 511) {
            string2 = string2.substring(0, 512);
        }
        try {
            arrby = com.samsung.android.visasdk.b.b.j(this.Gw.f(tokenKey), string2.getBytes());
        }
        catch (VisaTAException visaTAException) {
            visaTAException.printStackTrace();
            return null;
        }
        Signature signature = new Signature();
        signature.setMac(b.o(arrby));
        replenishRequest.setSignature(signature);
        replenishRequest.setEncryptionMetaData(this.Gw.e(tokenKey));
        return replenishRequest;
    }

    public void deleteToken(TokenKey tokenKey) {
        this.Gw.deleteToken(tokenKey);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Bundle getTokenMetaData(TokenKey tokenKey) {
        Bundle bundle = null;
        com.samsung.android.visasdk.c.a.d("TokenProcessor", "getTokenMetaData");
        if (tokenKey == null) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "invalid param tokenKey");
            return bundle;
        }
        PaymentData paymentData = this.Gw.b(tokenKey, false);
        if (paymentData == null) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "invalid data");
            return null;
        }
        if (paymentData.getTokenInfo() == null || paymentData.getTokenInfo().getHceData() == null || paymentData.getTokenInfo().getHceData().getStaticParams() == null || paymentData.getTokenInfo().getHceData().getStaticParams().getQVSDCData() == null) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "invalid data");
            return null;
        }
        if (paymentData.getTokenInfo().getHceData().getStaticParams().getQVSDCData().getCountryCode() == null) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "invalid getCountryCode");
            return null;
        }
        bundle = new Bundle();
        String string = paymentData.getTokenInfo().getHceData().getStaticParams().getQVSDCData().getCountryCode();
        if (string == null) return bundle;
        try {
            String string2 = Integer.toString((int)Integer.parseInt((String)string));
            com.samsung.android.visasdk.c.a.d("TokenProcessor", "metaData countryCode " + string2);
            bundle.putString("qvsdc_issuercountrycode", string2);
            return bundle;
        }
        catch (NumberFormatException numberFormatException) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "invalid issuerCountryCode");
            return bundle;
        }
    }

    public String getTokenStatus(TokenKey tokenKey) {
        return this.Gw.getTokenStatus(tokenKey);
    }

    public boolean isMstSupported(TokenKey tokenKey) {
        return this.Gw.isMstSupported(tokenKey);
    }

    public boolean processReplenishmentResponse(TokenKey tokenKey, TokenInfo tokenInfo) {
        try {
            DynParams dynParams = tokenInfo.getHceData().getDynParams();
            dynParams.setEncKeyInfo(this.Dl.bH(dynParams.getEncKeyInfo()));
        }
        catch (VisaTAException visaTAException) {
            visaTAException.printStackTrace();
            com.samsung.android.visasdk.c.a.e("TokenProcessor", "cannot store the key");
            return false;
        }
        return this.Gw.a(tokenKey, tokenInfo);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public TokenKey storeProvisionedToken(ProvisionResponse provisionResponse, String string) {
        String string2;
        QVSDCWithODA qVSDCWithODA;
        String string3;
        TokenInfo tokenInfo = provisionResponse.getTokenInfo();
        try {
            string3 = this.Dl.bH(tokenInfo.getEncTokenInfo());
            string2 = this.Dl.bH(tokenInfo.getHceData().getDynParams().getEncKeyInfo());
            if (provisionResponse.getODAData() == null) return this.Gw.a(provisionResponse, string, b.hexStringToBytes(string2), b.hexStringToBytes(string3));
            qVSDCWithODA = this.a(provisionResponse.getODAData(), tokenInfo);
            if (qVSDCWithODA == null) return this.Gw.a(provisionResponse, string, b.hexStringToBytes(string2), b.hexStringToBytes(string3));
        }
        catch (Exception exception) {
            com.samsung.android.visasdk.c.a.e("TokenProcessor", " cannot store provisioned token");
            throw new TokenInvalidException("cannot store provisioned token");
        }
        provisionResponse.getTokenInfo().getHceData().getStaticParams().getQVSDCData().setqVSDCWithODA(qVSDCWithODA);
        return this.Gw.a(provisionResponse, string, b.hexStringToBytes(string2), b.hexStringToBytes(string3));
    }

    public boolean updateTokenStatus(TokenKey tokenKey, TokenStatus tokenStatus) {
        return this.Gw.updateTokenStatus(tokenKey, tokenStatus);
    }
}

