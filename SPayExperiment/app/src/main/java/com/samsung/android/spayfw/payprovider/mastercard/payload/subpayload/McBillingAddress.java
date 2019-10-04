/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import com.samsung.android.spayfw.b.c;

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

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCountrySubdivision() {
        return this.countrySubdivision;
    }

    public String getLine1() {
        return this.line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setCity(String string) {
        this.city = string;
    }

    public void setCountry(String string) {
        this.country = string;
    }

    public void setCountrySubdivision(String string) {
        this.countrySubdivision = string;
    }

    public void setLine1(String string) {
        this.line1 = string;
    }

    public void setLine2(String string) {
        this.line2 = string;
    }

    public void setPostalCode(String string) {
        this.postalCode = string;
    }

    public void trimBillingAddress() {
        if (this.line1 != null) {
            this.line1 = this.line1.trim();
            if (this.line1.length() > 64) {
                this.line1 = this.line1.substring(0, 64);
            }
        }
        if (this.line2 != null) {
            this.line2 = this.line2.trim();
            if (this.line2.length() > 64) {
                this.line2 = this.line2.substring(0, 64);
            }
        }
        if (this.city != null) {
            this.city = this.city.trim();
            if (this.city.length() > 32) {
                this.city = this.city.substring(0, 32);
            }
        }
        if (this.countrySubdivision != null) {
            this.countrySubdivision = this.countrySubdivision.trim();
            if (this.countrySubdivision.length() > 12) {
                this.countrySubdivision = this.countrySubdivision.substring(0, 12);
            }
        }
        if (this.postalCode != null) {
            this.postalCode = this.postalCode.trim();
            if (this.postalCode.length() > 16) {
                this.postalCode = this.postalCode.substring(0, 16);
            }
        }
        c.d(TAG, this.line1 + ", " + this.line2 + ", " + this.city + ", " + this.countrySubdivision + ", " + this.postalCode + ", " + this.country);
    }
}

