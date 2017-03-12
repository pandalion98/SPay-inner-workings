package com.samsung.android.spayfw.payprovider.visa.inapp.models;

import com.samsung.android.spayfw.p002b.Log;
import java.util.Locale;

public class GenCryptogramResponseData {
    private CryptogramInfo cryptogramInfo;
    private PaymentInstrument paymentInstrument;
    private TokenInfo tokenInfo;
    private String vPaymentDataID;

    public static class CryptogramInfo {
        private String cryptogram;
        private String eci;

        public String getCryptogram() {
            return this.cryptogram;
        }

        public void setCryptogram(String str) {
            this.cryptogram = str;
        }

        public String getEci() {
            return this.eci;
        }

        public void setEci(String str) {
            this.eci = str;
        }
    }

    public static class EncTokenInfo {
        private String token;

        public String getToken() {
            return this.token;
        }

        public void setToken(String str) {
            this.token = str;
        }
    }

    public static class ExpirationDate {
        private String month;
        private String year;

        public String getMonth() {
            return this.month;
        }

        public void setMonth(String str) {
            this.month = str;
        }

        public String getYear() {
            return this.year;
        }

        public void setYear(String str) {
            this.year = str;
        }
    }

    public static class PaymentInstrument {
        private String last4;
        private PaymentType paymentType;

        public String getLast4() {
            return this.last4;
        }

        public void setLast4(String str) {
            this.last4 = str;
        }

        public PaymentType getPaymentType() {
            return this.paymentType;
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

        public void setCardBrand(String str) {
            this.cardBrand = str;
        }

        public String getProductType() {
            return this.productType;
        }

        public void setProductType(String str) {
            this.productType = str;
        }
    }

    public static class TokenInfo {
        private String encTokenInfo;
        private ExpirationDate expirationDate;
        private String last4;

        public String getLast4() {
            return this.last4;
        }

        public void setLast4(String str) {
            this.last4 = str;
        }

        public String getEncTokenInfo() {
            return this.encTokenInfo;
        }

        public void setEncTokenInfo(String str) {
            this.encTokenInfo = str;
        }

        public ExpirationDate getExpirationDate() {
            return this.expirationDate;
        }

        public void setExpirationDate(ExpirationDate expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getTokenExpirationDate() {
            String str = null;
            if (!(this.expirationDate == null || this.expirationDate.getMonth() == null || this.expirationDate.getYear() == null)) {
                try {
                    int parseInt = Integer.parseInt(this.expirationDate.getMonth());
                    int parseInt2 = Integer.parseInt(this.expirationDate.getYear());
                    str = String.format(Locale.US, "%02d", new Object[]{Integer.valueOf(parseInt)}) + String.format(Locale.US, "%02d", new Object[]{Integer.valueOf(parseInt2 % 100)});
                } catch (Throwable e) {
                    Log.m284c("GenCryptogramResponseData", e.getMessage(), e);
                }
            }
            return str;
        }
    }

    public String getvPaymentDataID() {
        return this.vPaymentDataID;
    }

    public void setvPaymentDataID(String str) {
        this.vPaymentDataID = str;
    }

    public PaymentInstrument getPaymentInstrument() {
        return this.paymentInstrument;
    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public CryptogramInfo getCryptogramInfo() {
        return this.cryptogramInfo;
    }

    public void setCryptogramInfo(CryptogramInfo cryptogramInfo) {
        this.cryptogramInfo = cryptogramInfo;
    }
}
