/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.facade.data.VerifyingEntity;
import com.samsung.android.visasdk.facade.data.VerifyingType;

public class CvmMode {
    private VerifyingEntity verifyingEntity;
    private VerifyingType verifyingType;

    public CvmMode(VerifyingEntity verifyingEntity, VerifyingType verifyingType) {
        this.verifyingEntity = verifyingEntity;
        this.verifyingType = verifyingType;
    }

    public byte getCvmByte() {
        return (byte)(this.verifyingEntity.getByte() | this.verifyingType.getByte());
    }

    public VerifyingEntity getVerifyingEntity() {
        return this.verifyingEntity;
    }

    public VerifyingType getVerifyingType() {
        return this.verifyingType;
    }
}

