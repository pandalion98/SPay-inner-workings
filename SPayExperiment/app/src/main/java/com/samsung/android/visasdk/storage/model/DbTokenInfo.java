/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
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

    public DbTokenInfo(ProvisionResponse provisionResponse, String string, String string2, String string3) {
    }

    public String getAppPrgrmId() {
        return this.appPrgrmId;
    }

    public String getDynamicDki() {
        return this.dynamicDki;
    }

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public Mst getMst() {
        return this.mst;
    }

    public ExpirationDate getPaymentInstrumentExpirationDate() {
        return this.paymentInstrumentExpirationDate;
    }

    public String getPaymentInstrumentLast4() {
        return this.paymentInstrumentLast4;
    }

    public StaticParams getStaticParams() {
        return this.staticParams;
    }

    public ExpirationDate getTokenExpirationDate() {
        return this.tokenExpirationDate;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public TokenKey getTokenKey() {
        return new TokenKey(this._id);
    }

    public String getTokenLast4() {
        return this.tokenLast4;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public String getvTokenRequesterId() {
        return this.vTokenRequesterId;
    }

    public void setAppPrgrmId(String string) {
        this.appPrgrmId = string;
    }

    public void setDynamicDki(String string) {
        this.dynamicDki = string;
    }

    public void setEncryptionMetaData(String string) {
        this.encryptionMetaData = string;
    }

    public void setMst(Mst mst) {
        this.mst = mst;
    }

    public void setPaymentInstrumentExpirationDate(ExpirationDate expirationDate) {
        this.paymentInstrumentExpirationDate = expirationDate;
    }

    public void setPaymentInstrumentLast4(String string) {
        this.paymentInstrumentLast4 = string;
    }

    public void setStaticParams(StaticParams staticParams) {
        this.staticParams = staticParams;
    }

    public void setTokenExpirationDate(ExpirationDate expirationDate) {
        this.tokenExpirationDate = expirationDate;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public void setTokenKey(TokenKey tokenKey) {
        this._id = tokenKey.getTokenId();
    }

    public void setTokenLast4(String string) {
        this.tokenLast4 = string;
    }

    public void setTokenStatus(String string) {
        this.tokenStatus = string;
    }

    public void setvTokenRequesterId(String string) {
        this.vTokenRequesterId = string;
    }
}

