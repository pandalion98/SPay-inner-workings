/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

import org.json.JSONException;
import org.json.JSONObject;

public class McInAppPaymentInfoData {
    McInAppPaymentInfoDataBuilder mMcInAppPaymentInfoDataBuilder;

    private McInAppPaymentInfoData(McInAppPaymentInfoDataBuilder mcInAppPaymentInfoDataBuilder) {
        this.mMcInAppPaymentInfoDataBuilder = mcInAppPaymentInfoDataBuilder;
    }

    public JSONObject getJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("amount", (Object)this.mMcInAppPaymentInfoDataBuilder.getAmount());
            jSONObject.put("currency_code", (Object)this.mMcInAppPaymentInfoDataBuilder.getCurrency_code());
            jSONObject.put("utc", (Object)this.mMcInAppPaymentInfoDataBuilder.getUtc());
            jSONObject.put("cardholder_name", (Object)this.mMcInAppPaymentInfoDataBuilder.getCardholder_name());
            jSONObject.put("eci_indicator", (Object)this.mMcInAppPaymentInfoDataBuilder.getEci_indicator());
            jSONObject.put("tokenPAN", (Object)this.mMcInAppPaymentInfoDataBuilder.getTokenPAN());
            jSONObject.put("tokenPanExpiration", (Object)this.mMcInAppPaymentInfoDataBuilder.getTokenPanExpiry());
            jSONObject.put("cryptogram", (Object)this.mMcInAppPaymentInfoDataBuilder.getCryptogram());
            return jSONObject;
        }
        catch (JSONException jSONException) {
            jSONException.printStackTrace();
            return null;
        }
    }

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

        private McInAppPaymentInfoDataBuilder() {
        }

        public static McInAppPaymentInfoDataBuilder getInstance() {
            Class<McInAppPaymentInfoDataBuilder> class_ = McInAppPaymentInfoDataBuilder.class;
            synchronized (McInAppPaymentInfoDataBuilder.class) {
                if (mInstance == null) {
                    mInstance = new McInAppPaymentInfoDataBuilder();
                }
                McInAppPaymentInfoDataBuilder mcInAppPaymentInfoDataBuilder = mInstance;
                // ** MonitorExit[var2] (shouldn't be in output)
                return mcInAppPaymentInfoDataBuilder;
            }
        }

        public McInAppPaymentInfoData build() {
            return new McInAppPaymentInfoData(this);
        }

        public String getAmount() {
            return this.amount;
        }

        public String getCardholder_name() {
            return this.cardholder_name;
        }

        public String getCryptogram() {
            return this.cryptogram;
        }

        public String getCurrency_code() {
            return this.currency_code;
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

        public String getUtc() {
            return this.utc;
        }

        public McInAppPaymentInfoDataBuilder setAmount(String string) {
            this.amount = string;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setCardholder_name(String string) {
            this.cardholder_name = string;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setCryptogram(String string) {
            this.cryptogram = string;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setCurrency_code(String string) {
            this.currency_code = string;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setEci_indicator(String string) {
            this.eci_indicator = string;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setTokenPAN(String string) {
            this.tokenPAN = string;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setTokenPanExpiry(String string) {
            this.tokenPanExpiry = string;
            return this;
        }

        public McInAppPaymentInfoDataBuilder setUtc(String string) {
            this.utc = string;
            return this;
        }
    }

}

