package com.samsung.android.visasdk.storage.model;

import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.paywave.model.ExpirationDate;
import com.samsung.android.visasdk.paywave.model.Mst;
import com.samsung.android.visasdk.paywave.model.StaticParams;

public class DbTokenInfo {
    private long _id;
    private String appPrgrmId;
    private byte[] bouncy_submarine;
    private int car_data_offset;
    private byte[] decimalized_crypto_data;
    private String dynamicDki;
    private byte[] dynamicKey;
    private byte[] encTokenInfo;
    private String encryptionMetaData;
    private int locate_sad_offset;
    private byte[] macLeftKey;
    private byte[] macRightKey;
    private Mst mst;
    private int nic;
    private ExpirationDate paymentInstrumentExpirationDate;
    private String paymentInstrumentLast4;
    private int sdad_length;
    private int sdad_offset;
    private int sdad_rec;
    private int sdad_sfi;
    private StaticParams staticParams;
    private ExpirationDate tokenExpirationDate;
    private String tokenId;
    private String tokenLast4;
    private String tokenStatus;
    private String vEnrollmentId;
    private String vProvisionedTokenID;
    private String vTokenRequesterId;

    private DbTokenInfo() {
    }

    public DbTokenInfo(ProvisionResponse provisionResponse, String str, String str2, String str3) {
    }

    public TokenKey getTokenKey() {
        return new TokenKey(this._id);
    }

    public void setTokenKey(TokenKey tokenKey) {
        this._id = tokenKey.getTokenId();
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setTokenStatus(String str) {
        this.tokenStatus = str;
    }

    public String getPaymentInstrumentLast4() {
        return this.paymentInstrumentLast4;
    }

    public void setPaymentInstrumentLast4(String str) {
        this.paymentInstrumentLast4 = str;
    }

    public ExpirationDate getPaymentInstrumentExpirationDate() {
        return this.paymentInstrumentExpirationDate;
    }

    public void setPaymentInstrumentExpirationDate(ExpirationDate expirationDate) {
        this.paymentInstrumentExpirationDate = expirationDate;
    }

    public ExpirationDate getTokenExpirationDate() {
        return this.tokenExpirationDate;
    }

    public void setTokenExpirationDate(ExpirationDate expirationDate) {
        this.tokenExpirationDate = expirationDate;
    }

    public String getAppPrgrmId() {
        return this.appPrgrmId;
    }

    public void setAppPrgrmId(String str) {
        this.appPrgrmId = str;
    }

    public String getDynamicDki() {
        return this.dynamicDki;
    }

    public void setDynamicDki(String str) {
        this.dynamicDki = str;
    }

    public String getTokenLast4() {
        return this.tokenLast4;
    }

    public void setTokenLast4(String str) {
        this.tokenLast4 = str;
    }

    public StaticParams getStaticParams() {
        return this.staticParams;
    }

    public void setStaticParams(StaticParams staticParams) {
        this.staticParams = staticParams;
    }

    public String getvTokenRequesterId() {
        return this.vTokenRequesterId;
    }

    public void setvTokenRequesterId(String str) {
        this.vTokenRequesterId = str;
    }

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public void setEncryptionMetaData(String str) {
        this.encryptionMetaData = str;
    }

    public Mst getMst() {
        return this.mst;
    }

    public void setMst(Mst mst) {
        this.mst = mst;
    }
}
