package com.samsung.android.spayfw.payprovider.discover.payment.data;

public class DiscoverInAppCryptoData {
    public static final String DEFAULT_ECI_INDICATOR = "4";
    private byte[] mCryptogram;
    private InAppCryptogramType mCryptogramType;
    private long mCurrencyCode;
    private String mEciValue;
    private byte[] mExpiryDate;
    private String mPan;
    private long mTransactionAmount;

    public enum InAppCryptogramType {
        CRYPTO_TYPE_3DS,
        CRYPTO_TYPE_EMV
    }

    public DiscoverInAppCryptoData() {
        this.mPan = null;
        this.mExpiryDate = null;
        this.mCryptogram = null;
        this.mTransactionAmount = 0;
        this.mCurrencyCode = 0;
        this.mCryptogramType = InAppCryptogramType.CRYPTO_TYPE_3DS;
    }

    public byte[] getCryptogram() {
        return this.mCryptogram;
    }

    public void setCryptogram(byte[] bArr) {
        this.mCryptogram = bArr;
    }

    public String getPan() {
        return this.mPan;
    }

    public void setPan(String str) {
        this.mPan = str;
    }

    public byte[] getExpiryDate() {
        return this.mExpiryDate;
    }

    public void setExpiryDate(byte[] bArr) {
        this.mExpiryDate = bArr;
    }

    public InAppCryptogramType getCryptoGramType() {
        return this.mCryptogramType;
    }

    public void setCryptoGramType(InAppCryptogramType inAppCryptogramType) {
        this.mCryptogramType = inAppCryptogramType;
    }

    public long getTransactionAmount() {
        return this.mTransactionAmount;
    }

    public void setTransactionAmount(long j) {
        this.mTransactionAmount = j;
    }

    public long getCurrencyCode() {
        return this.mCurrencyCode;
    }

    public void setCurrencyCode(int i) {
        this.mCurrencyCode = (long) i;
    }

    public String getEciValue() {
        return this.mEciValue == null ? DEFAULT_ECI_INDICATOR : this.mEciValue;
    }

    public void setEciValue(String str) {
        this.mEciValue = str;
    }
}
