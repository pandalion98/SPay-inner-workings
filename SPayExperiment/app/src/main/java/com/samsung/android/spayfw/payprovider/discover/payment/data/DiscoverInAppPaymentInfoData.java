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
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import org.json.JSONException;
import org.json.JSONObject;

public class DiscoverInAppPaymentInfoData {
    a mDcInAppPaymentInfoDataBuilder;

    private DiscoverInAppPaymentInfoData(a a2) {
        this.mDcInAppPaymentInfoDataBuilder = a2;
    }

    public JSONObject getJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("amount", (Object)this.mDcInAppPaymentInfoDataBuilder.getAmount());
            jSONObject.put("currency_code", (Object)this.mDcInAppPaymentInfoDataBuilder.getCurrency_code());
            jSONObject.put("utc", (Object)this.mDcInAppPaymentInfoDataBuilder.getUtc());
            jSONObject.put("cardholder_name", (Object)this.mDcInAppPaymentInfoDataBuilder.getCardholder_name());
            jSONObject.put("eci_indicator", (Object)this.mDcInAppPaymentInfoDataBuilder.getEci_indicator());
            jSONObject.put("tokenPAN", (Object)this.mDcInAppPaymentInfoDataBuilder.getTokenPAN());
            jSONObject.put("tokenPanExpiration", (Object)this.mDcInAppPaymentInfoDataBuilder.getTokenPanExpiry());
            jSONObject.put("cryptogram", (Object)this.mDcInAppPaymentInfoDataBuilder.getCryptogram());
            return jSONObject;
        }
        catch (JSONException jSONException) {
            jSONException.printStackTrace();
            return null;
        }
    }

    public static class a {
        private static a wf;
        private String amount;
        private String cardholder_name;
        private String cryptogram;
        private String currency_code;
        private String eci_indicator;
        private String tokenPAN;
        private String tokenPanExpiry;
        private String utc;

        private a() {
        }

        public static a dX() {
            Class<a> class_ = a.class;
            synchronized (a.class) {
                if (wf == null) {
                    wf = new a();
                }
                a a2 = wf;
                // ** MonitorExit[var2] (shouldn't be in output)
                return a2;
            }
        }

        public a aH(String string) {
            this.eci_indicator = string;
            return this;
        }

        public a aI(String string) {
            this.tokenPAN = string;
            return this;
        }

        public a aJ(String string) {
            this.tokenPanExpiry = string;
            return this;
        }

        public a aK(String string) {
            this.cryptogram = string;
            return this;
        }

        public a aL(String string) {
            this.amount = string;
            return this;
        }

        public a aM(String string) {
            this.currency_code = string;
            return this;
        }

        public a aN(String string) {
            this.utc = string;
            return this;
        }

        public DiscoverInAppPaymentInfoData dY() {
            return new DiscoverInAppPaymentInfoData(this);
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
    }

}

