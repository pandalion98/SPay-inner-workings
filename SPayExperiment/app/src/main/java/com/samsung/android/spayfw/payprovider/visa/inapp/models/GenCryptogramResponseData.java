/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Locale
 */
package com.samsung.android.spayfw.payprovider.visa.inapp.models;

import com.samsung.android.spayfw.b.Log;
import java.util.Locale;

public class GenCryptogramResponseData {
    private CryptogramInfo cryptogramInfo;
    private PaymentInstrument paymentInstrument;
    private TokenInfo tokenInfo;
    private String vPaymentDataID;

    public CryptogramInfo getCryptogramInfo() {
        return this.cryptogramInfo;
    }

    public PaymentInstrument getPaymentInstrument() {
        return this.paymentInstrument;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public String getvPaymentDataID() {
        return this.vPaymentDataID;
    }

    public void setCryptogramInfo(CryptogramInfo cryptogramInfo) {
        this.cryptogramInfo = cryptogramInfo;
    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public void setvPaymentDataID(String string) {
        this.vPaymentDataID = string;
    }

    public static class CryptogramInfo {
        private String cryptogram;
        private String eci;

        public String getCryptogram() {
            return this.cryptogram;
        }

        public String getEci() {
            return this.eci;
        }

        public void setCryptogram(String string) {
            this.cryptogram = string;
        }

        public void setEci(String string) {
            this.eci = string;
        }
    }

    public static class EncTokenInfo {
        private String token;

        public String getToken() {
            return this.token;
        }

        public void setToken(String string) {
            this.token = string;
        }
    }

    public static class ExpirationDate {
        private String month;
        private String year;

        public String getMonth() {
            return this.month;
        }

        public String getYear() {
            return this.year;
        }

        public void setMonth(String string) {
            this.month = string;
        }

        public void setYear(String string) {
            this.year = string;
        }
    }

    public static class PaymentInstrument {
        private String last4;
        private PaymentType paymentType;

        public String getLast4() {
            return this.last4;
        }

        public PaymentType getPaymentType() {
            return this.paymentType;
        }

        public void setLast4(String string) {
            this.last4 = string;
        }

        public void setPaymentType(PaymentType paymentType) {
            this.paymentType = paymentType;
        }
    }

    public static class PaymentType {
        private String cardBrand;
        private String productType;

        public String getCardBrand() {
            return this.cardBrand;
        }

        public String getProductType() {
            return this.productType;
        }

        public void setCardBrand(String string) {
            this.cardBrand = string;
        }

        public void setProductType(String string) {
            this.productType = string;
        }
    }

    public static class TokenInfo {
        private String encTokenInfo;
        private ExpirationDate expirationDate;
        private String last4;

        public String getEncTokenInfo() {
            return this.encTokenInfo;
        }

        public ExpirationDate getExpirationDate() {
            return this.expirationDate;
        }

        public String getLast4() {
            return this.last4;
        }

        public String getTokenExpirationDate() {
            if (this.expirationDate == null || this.expirationDate.getMonth() == null || this.expirationDate.getYear() == null) {
                return null;
            }
            try {
                int n2 = Integer.parseInt((String)this.expirationDate.getMonth());
                int n3 = Integer.parseInt((String)this.expirationDate.getYear());
                StringBuilder stringBuilder = new StringBuilder();
                Locale locale = Locale.US;
                Object[] arrobject = new Object[]{n2};
                StringBuilder stringBuilder2 = stringBuilder.append(String.format((Locale)locale, (String)"%02d", (Object[])arrobject));
                Locale locale2 = Locale.US;
                Object[] arrobject2 = new Object[]{n3 % 100};
                String string = stringBuilder2.append(String.format((Locale)locale2, (String)"%02d", (Object[])arrobject2)).toString();
                return string;
            }
            catch (NumberFormatException numberFormatException) {
                Log.c("GenCryptogramResponseData", numberFormatException.getMessage(), numberFormatException);
                return null;
            }
        }

        public void setEncTokenInfo(String string) {
            this.encTokenInfo = string;
        }

        public void setExpirationDate(ExpirationDate expirationDate) {
            this.expirationDate = expirationDate;
        }

        public void setLast4(String string) {
            this.last4 = string;
        }
    }

}

