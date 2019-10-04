/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McEligibilityReceipt;

public class McEnrollmentResponsePayload {
    private McEligibilityReceipt eligibilityReceipt;

    public McEligibilityReceipt getMcEligibilityReceipt() {
        return this.eligibilityReceipt;
    }

    public void setEligibilityReceipt(McEligibilityReceipt mcEligibilityReceipt) {
        this.eligibilityReceipt = mcEligibilityReceipt;
    }
}

