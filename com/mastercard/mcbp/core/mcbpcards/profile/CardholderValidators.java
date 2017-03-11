package com.mastercard.mcbp.core.mcbpcards.profile;

public class CardholderValidators {
    private String CVM;

    public String getCVM() {
        return this.CVM;
    }

    public void setCVM(String str) {
        this.CVM = str;
    }

    public String toString() {
        return "CardholderValidators [CVM=" + this.CVM + "]";
    }
}
