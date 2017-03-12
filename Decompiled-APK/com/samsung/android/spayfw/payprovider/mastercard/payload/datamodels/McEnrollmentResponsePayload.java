package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McEligibilityReceipt;

public class McEnrollmentResponsePayload {
    private McEligibilityReceipt eligibilityReceipt;

    public void setEligibilityReceipt(McEligibilityReceipt mcEligibilityReceipt) {
        this.eligibilityReceipt = mcEligibilityReceipt;
    }

    public McEligibilityReceipt getMcEligibilityReceipt() {
        return this.eligibilityReceipt;
    }
}
