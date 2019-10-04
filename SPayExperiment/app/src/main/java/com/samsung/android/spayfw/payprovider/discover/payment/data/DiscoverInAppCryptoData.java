/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

public class DiscoverInAppCryptoData {
    public static final String DEFAULT_ECI_INDICATOR = "4";
    private byte[] mCryptogram = null;
    private InAppCryptogramType mCryptogramType = InAppCryptogramType.wc;
    private long mCurrencyCode = 0L;
    private String mEciValue;
    private byte[] mExpiryDate = null;
    private String mPan = null;
    private long mTransactionAmount = 0L;

    public InAppCryptogramType getCryptoGramType() {
        return this.mCryptogramType;
    }

    public byte[] getCryptogram() {
        return this.mCryptogram;
    }

    public long getCurrencyCode() {
        return this.mCurrencyCode;
    }

    public String getEciValue() {
        if (this.mEciValue == null) {
            return DEFAULT_ECI_INDICATOR;
        }
        return this.mEciValue;
    }

    public byte[] getExpiryDate() {
        return this.mExpiryDate;
    }

    public String getPan() {
        return this.mPan;
    }

    public long getTransactionAmount() {
        return this.mTransactionAmount;
    }

    public void setCryptoGramType(InAppCryptogramType inAppCryptogramType) {
        this.mCryptogramType = inAppCryptogramType;
    }

    public void setCryptogram(byte[] arrby) {
        this.mCryptogram = arrby;
    }

    public void setCurrencyCode(int n2) {
        this.mCurrencyCode = n2;
    }

    public void setEciValue(String string) {
        this.mEciValue = string;
    }

    public void setExpiryDate(byte[] arrby) {
        this.mExpiryDate = arrby;
    }

    public void setPan(String string) {
        this.mPan = string;
    }

    public void setTransactionAmount(long l2) {
        this.mTransactionAmount = l2;
    }

    public static final class InAppCryptogramType
    extends Enum<InAppCryptogramType> {
        public static final /* enum */ InAppCryptogramType wc = new InAppCryptogramType();
        public static final /* enum */ InAppCryptogramType wd = new InAppCryptogramType();
        private static final /* synthetic */ InAppCryptogramType[] we;

        static {
            InAppCryptogramType[] arrinAppCryptogramType = new InAppCryptogramType[]{wc, wd};
            we = arrinAppCryptogramType;
        }

        public static InAppCryptogramType valueOf(String string) {
            return (InAppCryptogramType)Enum.valueOf(InAppCryptogramType.class, (String)string);
        }

        public static InAppCryptogramType[] values() {
            return (InAppCryptogramType[])we.clone();
        }
    }

}

