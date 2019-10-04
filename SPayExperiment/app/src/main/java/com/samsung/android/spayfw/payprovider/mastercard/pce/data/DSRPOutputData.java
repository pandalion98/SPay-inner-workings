/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramType;

public class DSRPOutputData {
    private static final String DEFAULT_ECI_INDICATOR = "5";
    private int atc = 0;
    private byte[] cryptoGram = null;
    private int cryptoGramType = CryptogramType.UCAF.ordinal();
    private long currencyCode = 0L;
    private String eciValue;
    private byte[] expiryDate = null;
    private String pan = null;
    private byte panSequenceNumber = 0;
    private byte[] par = null;
    private int transactionAmount = 0;
    private byte[] transactionCryptogramData;
    private int ucafVersion = 0;
    private byte[] unpredictableNumber = null;

    public int getAtc() {
        return this.atc;
    }

    public byte[] getCryptoGram() {
        return this.cryptoGram;
    }

    public int getCryptoGramType() {
        return this.cryptoGramType;
    }

    public long getCurrencyCode() {
        return this.currencyCode;
    }

    public String getEciValue() {
        if (this.eciValue == null) {
            return DEFAULT_ECI_INDICATOR;
        }
        return this.eciValue;
    }

    public byte[] getExpiryDate() {
        return this.expiryDate;
    }

    public String getPan() {
        return this.pan;
    }

    public int getPanSequenceNumber() {
        return this.panSequenceNumber;
    }

    public byte[] getPar() {
        return this.par;
    }

    public long getTransactionAmount() {
        return this.transactionAmount;
    }

    public byte[] getTransactionCryptogramData() {
        return this.transactionCryptogramData;
    }

    public int getUcafVersion() {
        return this.ucafVersion;
    }

    public byte[] getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setAtc(int n2) {
        this.atc = n2;
    }

    public void setCryptoGram(byte[] arrby) {
        this.cryptoGram = arrby;
    }

    public void setCryptoGramType(CryptogramType cryptogramType) {
        this.cryptoGramType = cryptogramType.ordinal();
    }

    public void setCurrencyCode(int n2) {
        this.currencyCode = n2;
    }

    public void setEciValue(String string) {
        this.eciValue = string;
    }

    public void setExpiryDate(byte[] arrby) {
        this.expiryDate = arrby;
    }

    public void setPan(String string) {
        this.pan = string;
    }

    public void setPanSequenceNumber(int n2) {
        this.panSequenceNumber = (byte)n2;
    }

    public void setPar(byte[] arrby) {
        this.par = arrby;
    }

    public void setTransactionAmount(long l2) {
        this.transactionAmount = (int)l2;
    }

    public void setTransactionCryptogramData(byte[] arrby) {
        this.transactionCryptogramData = arrby;
    }

    public void setUcafVersion(int n2) {
        this.ucafVersion = n2;
    }

    public void setUnpredictableNumber(byte[] arrby) {
        this.unpredictableNumber = arrby;
    }
}

