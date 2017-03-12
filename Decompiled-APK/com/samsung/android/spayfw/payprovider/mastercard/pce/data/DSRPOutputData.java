package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public class DSRPOutputData {
    private static final String DEFAULT_ECI_INDICATOR = "5";
    private int atc;
    private byte[] cryptoGram;
    private int cryptoGramType;
    private long currencyCode;
    private String eciValue;
    private byte[] expiryDate;
    private String pan;
    private byte panSequenceNumber;
    private byte[] par;
    private int transactionAmount;
    private byte[] transactionCryptogramData;
    private int ucafVersion;
    private byte[] unpredictableNumber;

    public DSRPOutputData() {
        this.pan = null;
        this.panSequenceNumber = (byte) 0;
        this.expiryDate = null;
        this.cryptoGram = null;
        this.ucafVersion = 0;
        this.transactionAmount = 0;
        this.currencyCode = 0;
        this.atc = 0;
        this.unpredictableNumber = null;
        this.cryptoGramType = CryptogramType.UCAF.ordinal();
        this.par = null;
    }

    public byte[] getCryptoGram() {
        return this.cryptoGram;
    }

    public void setCryptoGram(byte[] bArr) {
        this.cryptoGram = bArr;
    }

    public byte[] getTransactionCryptogramData() {
        return this.transactionCryptogramData;
    }

    public void setTransactionCryptogramData(byte[] bArr) {
        this.transactionCryptogramData = bArr;
    }

    public String getPan() {
        return this.pan;
    }

    public void setPan(String str) {
        this.pan = str;
    }

    public int getPanSequenceNumber() {
        return this.panSequenceNumber;
    }

    public void setPanSequenceNumber(int i) {
        this.panSequenceNumber = (byte) i;
    }

    public byte[] getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(byte[] bArr) {
        this.expiryDate = bArr;
    }

    public int getCryptoGramType() {
        return this.cryptoGramType;
    }

    public void setCryptoGramType(CryptogramType cryptogramType) {
        this.cryptoGramType = cryptogramType.ordinal();
    }

    public int getUcafVersion() {
        return this.ucafVersion;
    }

    public void setUcafVersion(int i) {
        this.ucafVersion = i;
    }

    public long getTransactionAmount() {
        return (long) this.transactionAmount;
    }

    public void setTransactionAmount(long j) {
        this.transactionAmount = (int) j;
    }

    public long getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(int i) {
        this.currencyCode = (long) i;
    }

    public int getAtc() {
        return this.atc;
    }

    public void setAtc(int i) {
        this.atc = i;
    }

    public byte[] getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setUnpredictableNumber(byte[] bArr) {
        this.unpredictableNumber = bArr;
    }

    public String getEciValue() {
        return this.eciValue == null ? DEFAULT_ECI_INDICATOR : this.eciValue;
    }

    public void setEciValue(String str) {
        this.eciValue = str;
    }

    public byte[] getPar() {
        return this.par;
    }

    public void setPar(byte[] bArr) {
        this.par = bArr;
    }
}
