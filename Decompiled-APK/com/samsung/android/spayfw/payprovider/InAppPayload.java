package com.samsung.android.spayfw.payprovider;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import org.bouncycastle.jce.X509KeyUsage;

public class InAppPayload {
    private static final transient String METHOD_3DS = "3DS";
    private static final transient String TAG = "InAppPayload";
    private static final transient String TYPE = "S";
    private static final transient String VERSION_OF_3DS_STRUCTURE = "100";
    private BillingAddress billing_address;
    private String card_last4digits;
    private CertificateInfo[] certificates;
    @SerializedName("3DS")
    private Algorithm3DS ds;
    private String merchant_ref;
    private String method;
    private boolean recurring_payment;
    private String transaction_type;

    public class Algorithm3DS {
        private String data;
        private String type;
        private String version;

        public Algorithm3DS(String str, String str2, String str3) {
            this.data = str3;
            this.version = str2;
            this.type = str;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String str) {
            this.version = str;
        }

        public String getData() {
            return this.data;
        }

        public void setData(String str) {
            this.data = str;
        }
    }

    public class BillingAddress {
        private String city;
        private String country;
        private String state_province;
        private String street;
        private String zip_postal_code;

        public BillingAddress(String str, String str2, String str3, String str4, String str5) {
            this.street = str;
            this.state_province = str2;
            this.zip_postal_code = str3;
            this.city = str4;
            this.country = str5;
        }

        public String getStreet() {
            return this.street;
        }

        public void setStreet(String str) {
            this.street = str;
        }

        public String getStateProvince() {
            return this.state_province;
        }

        public void setStateProvince(String str) {
            this.state_province = str;
        }

        public String getZip() {
            return this.zip_postal_code;
        }

        public void setZip(String str) {
            this.zip_postal_code = str;
        }

        public String getCity() {
            return this.city;
        }

        public void setCity(String str) {
            this.city = str;
        }

        public String getCountry() {
            return this.country;
        }

        public void setCountry(String str) {
            this.country = str;
        }
    }

    public InAppPayload(InAppTransactionInfo inAppTransactionInfo, byte[] bArr, CertificateInfo[] certificateInfoArr) {
        Log.m285d(TAG, "Entered In App Payload");
        if (inAppTransactionInfo == null || bArr == null || bArr.length <= 0) {
            Log.m286e(TAG, "Invalid input");
            return;
        }
        Log.m285d(TAG, "Inputs not null");
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
                Log.m285d(TAG, "Billing Address Set");
            }
            this.ds = new Algorithm3DS();
            this.ds.type = TYPE;
            this.ds.version = VERSION_OF_3DS_STRUCTURE;
            this.ds.data = new String(bArr);
            if (certificateInfoArr != null) {
                this.certificates = certificateInfoArr;
            }
        } catch (Exception e) {
            Log.m285d(TAG, "Exception when forming InAppPayload object");
            e.printStackTrace();
        }
    }

    public String toJsonString() {
        return new GsonBuilder().disableHtmlEscaping().excludeFieldsWithModifiers(X509KeyUsage.digitalSignature).create().toJson((Object) this);
    }
}
