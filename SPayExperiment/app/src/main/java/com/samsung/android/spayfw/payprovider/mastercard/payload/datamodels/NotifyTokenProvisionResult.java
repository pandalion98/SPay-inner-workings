/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.ApduResponse;

public class NotifyTokenProvisionResult {
    private ApduResponse[] apduResponses;

    public NotifyTokenProvisionResult(ApduResponse[] arrapduResponse) {
        this.apduResponses = arrapduResponse;
    }
}

