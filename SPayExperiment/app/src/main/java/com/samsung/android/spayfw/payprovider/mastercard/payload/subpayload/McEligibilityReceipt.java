/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McEligibilityReceipt {
    private int validForMinutes;
    private String value;

    public int getValidForMinutes() {
        return this.validForMinutes;
    }

    public String getValue() {
        return this.value;
    }

    public void setValidForMinutes(int n2) {
        this.validForMinutes = n2;
    }

    public void setValue(String string) {
        this.value = string;
    }
}

