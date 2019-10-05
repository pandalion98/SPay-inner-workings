/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.annotations.SerializedName
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

public class InAppPayload {
    private static final transient String METHOD_3DS = "3DS";
    private static final transient String TAG = "InAppPayload";
    private static final transient String TYPE = "S";
    private static final transient String VERSION_OF_3DS_STRUCTURE = "100";
    private BillingAddress billing_address;
    private String card_last4digits;
    private CertificateInfo[] certificates;
    @SerializedName(value="3DS")
    private Algorithm3DS ds;
    private String merchant_ref;
    private String method;
    private boolean recurring_payment;
    private String transaction_type;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public InAppPayload(InAppTransactionInfo inAppTransactionInfo, byte[] arrby, CertificateInfo[] arrcertificateInfo) {
        Log.d(TAG, "Entered In App Payload");
        if (inAppTransactionInfo == null || arrby == null || arrby.length <= 0) {
            Log.e(TAG, "Invalid input");
            return;
        }
        Log.d(TAG, "Inputs not null");
        try {
            this.method = METHOD_3DS;
            this.card_last4digits = inAppTransactionInfo.getFPANLast4Digits();
            this.merchant_ref = inAppTransactionInfo.getMerchantRefId();
            this.recurring_payment = inAppTransactionInfo.isRecurring();
            BillingInfo billingInfo = inAppTransactionInfo.getBillingInfo();
            if (billingInfo != null) {
                this.billing_address = new BillingAddress();
                this.billing_address.city = billingInfo.getCity();
                this.billing_address.country = billingInfo.getCountry();
                this.billing_address.street = billingInfo.getStreet1();
                this.billing_address.zip_postal_code = billingInfo.getZip();
                this.billing_address.state_province = billingInfo.getState();
                Log.d(TAG, "Billing Address Set");
            }
            this.ds = new Algorithm3DS();
            this.ds.type = TYPE;
            this.ds.version = VERSION_OF_3DS_STRUCTURE;
            this.ds.data = new String(arrby);
            if (arrcertificateInfo == null) return;
            this.certificates = arrcertificateInfo;
            return;
        }
        catch (Exception exception) {
            Log.d(TAG, "Exception when forming InAppPayload object");
            exception.printStackTrace();
            return;
        }
    }

    public String toJsonString() {
        return new GsonBuilder().disableHtmlEscaping().excludeFieldsWithModifiers(new int[]{128}).create().toJson((Object)this);
    }

    public class Algorithm3DS {
        private String data;
        private String type;
        private String version;

        public Algorithm3DS() {
        }

        public Algorithm3DS(String string, String string2, String string3) {
            this.data = string3;
            this.version = string2;
            this.type = string;
        }

        public String getData() {
            return this.data;
        }

        public String getType() {
            return this.type;
        }

        public String getVersion() {
            return this.version;
        }

        public void setData(String string) {
            this.data = string;
        }

        public void setType(String string) {
            this.type = string;
        }

        public void setVersion(String string) {
            this.version = string;
        }
    }

    public class BillingAddress {
        private String city;
        private String country;
        private String state_province;
        private String street;
        private String zip_postal_code;

        public BillingAddress() {
        }

        public BillingAddress(String string, String string2, String string3, String string4, String string5) {
            this.street = string;
            this.state_province = string2;
            this.zip_postal_code = string3;
            this.city = string4;
            this.country = string5;
        }

        public String getCity() {
            return this.city;
        }

        public String getCountry() {
            return this.country;
        }

        public String getStateProvince() {
            return this.state_province;
        }

        public String getStreet() {
            return this.street;
        }

        public String getZip() {
            return this.zip_postal_code;
        }

        public void setCity(String string) {
            this.city = string;
        }

        public void setCountry(String string) {
            this.country = string;
        }

        public void setStateProvince(String string) {
            this.state_province = string;
        }

        public void setStreet(String string) {
            this.street = string;
        }

        public void setZip(String string) {
            this.zip_postal_code = string;
        }
    }

}

