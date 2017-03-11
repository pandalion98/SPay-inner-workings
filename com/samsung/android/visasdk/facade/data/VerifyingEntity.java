package com.samsung.android.visasdk.facade.data;

import com.mastercard.mobile_api.utils.apdu.emv.EMVGetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;

public enum VerifyingEntity {
    NO_CD_CVM((byte) 0),
    VMPA(Tnaf.POW_2_WIDTH),
    MG(VerifyPINApdu.INS),
    CO_RESIDING_SE((byte) 48),
    TEE(EMVGetStatusApdu.P1),
    MOBILE_APP(GetTemplateApdu.TAG_APPLICATION_LABEL_50),
    TERMINAL((byte) 96),
    VERIFIED_CLOUD((byte) 112),
    VERIFIED_MOBILE_DEVICE(VerifyPINApdu.P2_PLAINTEXT);
    
    private final byte entity;

    private VerifyingEntity(byte b) {
        this.entity = b;
    }

    public byte getByte() {
        return this.entity;
    }
}
