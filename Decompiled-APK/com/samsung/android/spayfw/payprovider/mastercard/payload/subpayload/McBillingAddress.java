package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import com.samsung.android.spayfw.p002b.Log;

public class McBillingAddress {
    public static final int INVALID_BILLING_ADDRESS = 0;
    private static final int MAX_CITY_LEN = 32;
    private static final int MAX_LINE1_LEN = 64;
    private static final int MAX_LINE2_LEN = 64;
    private static final int MAX_STATE_LEN = 12;
    private static final int MAX_ZIPCODE_LEN = 16;
    private static final String TAG = "McBillingAddress";
    public static final int VALID_BILLING_ADDRESS = 1;
    private String city;
    private String country;
    private String countrySubdivision;
    private String line1;
    private String line2;
    private String postalCode;

    public void setLine1(String str) {
        this.line1 = str;
    }

    public void setLine2(String str) {
        this.line2 = str;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setCountrySubdivision(String str) {
        this.countrySubdivision = str;
    }

    public void setPostalCode(String str) {
        this.postalCode = str;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public String getLine1() {
        return this.line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountrySubdivision() {
        return this.countrySubdivision;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCountry() {
        return this.country;
    }

    public void trimBillingAddress() {
        if (this.line1 != null) {
            this.line1 = this.line1.trim();
            if (this.line1.length() > MAX_LINE2_LEN) {
                this.line1 = this.line1.substring(INVALID_BILLING_ADDRESS, MAX_LINE2_LEN);
            }
        }
        if (this.line2 != null) {
            this.line2 = this.line2.trim();
            if (this.line2.length() > MAX_LINE2_LEN) {
                this.line2 = this.line2.substring(INVALID_BILLING_ADDRESS, MAX_LINE2_LEN);
            }
        }
        if (this.city != null) {
            this.city = this.city.trim();
            if (this.city.length() > MAX_CITY_LEN) {
                this.city = this.city.substring(INVALID_BILLING_ADDRESS, MAX_CITY_LEN);
            }
        }
        if (this.countrySubdivision != null) {
            this.countrySubdivision = this.countrySubdivision.trim();
            if (this.countrySubdivision.length() > MAX_STATE_LEN) {
                this.countrySubdivision = this.countrySubdivision.substring(INVALID_BILLING_ADDRESS, MAX_STATE_LEN);
            }
        }
        if (this.postalCode != null) {
            this.postalCode = this.postalCode.trim();
            if (this.postalCode.length() > MAX_ZIPCODE_LEN) {
                this.postalCode = this.postalCode.substring(INVALID_BILLING_ADDRESS, MAX_ZIPCODE_LEN);
            }
        }
        Log.m285d(TAG, this.line1 + ", " + this.line2 + ", " + this.city + ", " + this.countrySubdivision + ", " + this.postalCode + ", " + this.country);
    }
}
