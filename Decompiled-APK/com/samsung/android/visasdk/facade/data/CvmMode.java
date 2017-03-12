package com.samsung.android.visasdk.facade.data;

public class CvmMode {
    private VerifyingEntity verifyingEntity;
    private VerifyingType verifyingType;

    public CvmMode(VerifyingEntity verifyingEntity, VerifyingType verifyingType) {
        this.verifyingEntity = verifyingEntity;
        this.verifyingType = verifyingType;
    }

    public VerifyingEntity getVerifyingEntity() {
        return this.verifyingEntity;
    }

    public VerifyingType getVerifyingType() {
        return this.verifyingType;
    }

    public byte getCvmByte() {
        return (byte) (this.verifyingEntity.getByte() | this.verifyingType.getByte());
    }
}
