package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

public class NotifyTokenProvisionResult {
    private ApduResponse[] apduResponses;

    public NotifyTokenProvisionResult(ApduResponse[] apduResponseArr) {
        this.apduResponses = apduResponseArr;
    }
}
