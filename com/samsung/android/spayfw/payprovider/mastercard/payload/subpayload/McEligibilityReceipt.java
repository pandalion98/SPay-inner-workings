package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McEligibilityReceipt {
    private int validForMinutes;
    private String value;

    public void setValue(String str) {
        this.value = str;
    }

    public void setValidForMinutes(int i) {
        this.validForMinutes = i;
    }

    public String getValue() {
        return this.value;
    }

    public int getValidForMinutes() {
        return this.validForMinutes;
    }
}
