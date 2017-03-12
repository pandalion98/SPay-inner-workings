package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import com.samsung.android.spayfw.appinterface.IdvMethod;
import org.json.JSONException;
import org.json.JSONObject;

public class McInAppPaymentInfoData {
    McInAppPaymentInfoDataBuilder mMcInAppPaymentInfoDataBuilder;

    public static class McInAppPaymentInfoDataBuilder {
        private static McInAppPaymentInfoDataBuilder mInstance;
        private String amount;
        private String cardholder_name;
        private String cryptogram;
        private String currency_code;
        private String eci_indicator;
        private String tokenPAN;
        private String tokenPanExpiry;
        private String utc;

        public static synchronized McInAppPaymentInfoDataBuilder getInstance() {
            McInAppPaymentInfoDataBuilder mcInAppPaymentInfoDataBuilder;
            synchronized (McInAppPaymentInfoDataBuilder.class) {
                if (mInstance == null) {
                    mInstance = new McInAppPaymentInfoDataBuilder();
                }
                mcInAppPaymentInfoDataBuilder = mInstance;
            }
            return mcInAppPaymentInfoDataBuilder;
        }

        private McInAppPaymentInfoDataBuilder() {
        }

        public McInAppPaymentInfoDataBuilder setEci_indicator(String str) {
            this.eci_indicator = str;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setTokenPAN(String str) {
            this.tokenPAN = str;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setTokenPanExpiry(String str) {
            this.tokenPanExpiry = str;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setCryptogram(String str) {
            this.cryptogram = str;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setAmount(String str) {
            this.amount = str;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setCurrency_code(String str) {
            this.currency_code = str;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setUtc(String str) {
            this.utc = str;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setCardholder_name(String str) {
            this.cardholder_name = str;
            return this;
        }

        public String getEci_indicator() {
            return this.eci_indicator;
        }

        public String getTokenPAN() {
            return this.tokenPAN;
        }

        public String getTokenPanExpiry() {
            return this.tokenPanExpiry;
        }

        public String getCryptogram() {
            return this.cryptogram;
        }

        public String getAmount() {
            return this.amount;
        }

        public String getCurrency_code() {
            return this.currency_code;
        }

        public String getUtc() {
            return this.utc;
        }

        public String getCardholder_name() {
            return this.cardholder_name;
        }

        public McInAppPaymentInfoData build() {
            return new McInAppPaymentInfoData();
        }
    }

    private McInAppPaymentInfoData(McInAppPaymentInfoDataBuilder mcInAppPaymentInfoDataBuilder) {
        this.mMcInAppPaymentInfoDataBuilder = mcInAppPaymentInfoDataBuilder;
    }

    public JSONObject getJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(IdvMethod.EXTRA_AMOUNT, this.mMcInAppPaymentInfoDataBuilder.getAmount());
            jSONObject.put("currency_code", this.mMcInAppPaymentInfoDataBuilder.getCurrency_code());
            jSONObject.put("utc", this.mMcInAppPaymentInfoDataBuilder.getUtc());
            jSONObject.put("cardholder_name", this.mMcInAppPaymentInfoDataBuilder.getCardholder_name());
            jSONObject.put("eci_indicator", this.mMcInAppPaymentInfoDataBuilder.getEci_indicator());
            jSONObject.put("tokenPAN", this.mMcInAppPaymentInfoDataBuilder.getTokenPAN());
            jSONObject.put("tokenPanExpiration", this.mMcInAppPaymentInfoDataBuilder.getTokenPanExpiry());
            jSONObject.put("cryptogram", this.mMcInAppPaymentInfoDataBuilder.getCryptogram());
            return jSONObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
