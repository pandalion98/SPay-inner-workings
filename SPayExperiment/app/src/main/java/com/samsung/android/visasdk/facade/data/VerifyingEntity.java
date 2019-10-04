/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

public final class VerifyingEntity
extends Enum<VerifyingEntity> {
    private static final /* synthetic */ VerifyingEntity[] $VALUES;
    public static final /* enum */ VerifyingEntity CO_RESIDING_SE;
    public static final /* enum */ VerifyingEntity MG;
    public static final /* enum */ VerifyingEntity MOBILE_APP;
    public static final /* enum */ VerifyingEntity NO_CD_CVM;
    public static final /* enum */ VerifyingEntity TEE;
    public static final /* enum */ VerifyingEntity TERMINAL;
    public static final /* enum */ VerifyingEntity VERIFIED_CLOUD;
    public static final /* enum */ VerifyingEntity VERIFIED_MOBILE_DEVICE;
    public static final /* enum */ VerifyingEntity VMPA;
    private final byte entity;

    static {
        NO_CD_CVM = new VerifyingEntity(0);
        VMPA = new VerifyingEntity(16);
        MG = new VerifyingEntity(32);
        CO_RESIDING_SE = new VerifyingEntity(48);
        TEE = new VerifyingEntity(64);
        MOBILE_APP = new VerifyingEntity(80);
        TERMINAL = new VerifyingEntity(96);
        VERIFIED_CLOUD = new VerifyingEntity(112);
        VERIFIED_MOBILE_DEVICE = new VerifyingEntity(-128);
        VerifyingEntity[] arrverifyingEntity = new VerifyingEntity[]{NO_CD_CVM, VMPA, MG, CO_RESIDING_SE, TEE, MOBILE_APP, TERMINAL, VERIFIED_CLOUD, VERIFIED_MOBILE_DEVICE};
        $VALUES = arrverifyingEntity;
    }

    private VerifyingEntity(byte by) {
        this.entity = by;
    }

    public static VerifyingEntity valueOf(String string) {
        return (VerifyingEntity)Enum.valueOf(VerifyingEntity.class, (String)string);
    }

    public static VerifyingEntity[] values() {
        return (VerifyingEntity[])$VALUES.clone();
    }

    public byte getByte() {
        return this.entity;
    }
}

