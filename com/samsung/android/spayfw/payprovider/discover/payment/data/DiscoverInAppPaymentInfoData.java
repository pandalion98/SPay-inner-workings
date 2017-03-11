package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.appinterface.IdvMethod;
import org.json.JSONException;
import org.json.JSONObject;

public class DiscoverInAppPaymentInfoData {
    C0509a mDcInAppPaymentInfoDataBuilder;

    /* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppPaymentInfoData.a */
    public static class C0509a {
        private static C0509a wf;
        private String amount;
        private String cardholder_name;
        private String cryptogram;
        private String currency_code;
        private String eci_indicator;
        private String tokenPAN;
        private String tokenPanExpiry;
        private String utc;

        public static synchronized C0509a dX() {
            C0509a c0509a;
            synchronized (C0509a.class) {
                if (wf == null) {
                    wf = new C0509a();
                }
                c0509a = wf;
            }
            return c0509a;
        }

        private C0509a() {
        }

        public C0509a aH(String str) {
            this.eci_indicator = str;
            return this;
        }

        public C0509a aI(String str) {
            this.tokenPAN = str;
            return this;
        }

        public C0509a aJ(String str) {
            this.tokenPanExpiry = str;
            return this;
        }

        public C0509a aK(String str) {
            this.cryptogram = str;
            return this;
        }

        public C0509a aL(String str) {
            this.amount = str;
            return this;
        }

        public C0509a aM(String str) {
            this.currency_code = str;
            return this;
        }

        public C0509a aN(String str) {
            this.utc = str;
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

        public DiscoverInAppPaymentInfoData dY() {
            return new DiscoverInAppPaymentInfoData();
        }
    }

    private DiscoverInAppPaymentInfoData(C0509a c0509a) {
        this.mDcInAppPaymentInfoDataBuilder = c0509a;
    }

    public JSONObject getJSONObject() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(IdvMethod.EXTRA_AMOUNT, this.mDcInAppPaymentInfoDataBuilder.getAmount());
            jSONObject.put("currency_code", this.mDcInAppPaymentInfoDataBuilder.getCurrency_code());
            jSONObject.put("utc", this.mDcInAppPaymentInfoDataBuilder.getUtc());
            jSONObject.put("cardholder_name", this.mDcInAppPaymentInfoDataBuilder.getCardholder_name());
            jSONObject.put("eci_indicator", this.mDcInAppPaymentInfoDataBuilder.getEci_indicator());
            jSONObject.put("tokenPAN", this.mDcInAppPaymentInfoDataBuilder.getTokenPAN());
            jSONObject.put("tokenPanExpiration", this.mDcInAppPaymentInfoDataBuilder.getTokenPanExpiry());
            jSONObject.put("cryptogram", this.mDcInAppPaymentInfoDataBuilder.getCryptogram());
            return jSONObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
