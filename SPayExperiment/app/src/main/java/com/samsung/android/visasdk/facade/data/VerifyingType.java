/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

public final class VerifyingType
extends Enum<VerifyingType> {
    private static final /* synthetic */ VerifyingType[] $VALUES;
    public static final /* enum */ VerifyingType MOBILE_DEVICE;
    public static final /* enum */ VerifyingType NO_CD_CVM;
    public static final /* enum */ VerifyingType ONLINE_PIN;
    public static final /* enum */ VerifyingType OTHER_CD_CVM;
    public static final /* enum */ VerifyingType PASSCODE;
    public static final /* enum */ VerifyingType SIGNATURE;
    private final byte type;

    static {
        NO_CD_CVM = new VerifyingType(0);
        PASSCODE = new VerifyingType(1);
        OTHER_CD_CVM = new VerifyingType(2);
        MOBILE_DEVICE = new VerifyingType(3);
        SIGNATURE = new VerifyingType(13);
        ONLINE_PIN = new VerifyingType(14);
        VerifyingType[] arrverifyingType = new VerifyingType[]{NO_CD_CVM, PASSCODE, OTHER_CD_CVM, MOBILE_DEVICE, SIGNATURE, ONLINE_PIN};
        $VALUES = arrverifyingType;
    }

    private VerifyingType(byte by) {
        this.type = by;
    }

    public static VerifyingType valueOf(String string) {
        return (VerifyingType)Enum.valueOf(VerifyingType.class, (String)string);
    }

    public static VerifyingType[] values() {
        return (VerifyingType[])$VALUES.clone();
    }

    public byte getByte() {
        return this.type;
    }
}

