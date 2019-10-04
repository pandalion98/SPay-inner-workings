/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public final class CryptogramType
extends Enum<CryptogramType> {
    private static final /* synthetic */ CryptogramType[] $VALUES;
    public static final /* enum */ CryptogramType DE55;
    public static final /* enum */ CryptogramType UCAF;

    static {
        UCAF = new CryptogramType();
        DE55 = new CryptogramType();
        CryptogramType[] arrcryptogramType = new CryptogramType[]{UCAF, DE55};
        $VALUES = arrcryptogramType;
    }

    public static CryptogramType valueOf(String string) {
        return (CryptogramType)Enum.valueOf(CryptogramType.class, (String)string);
    }

    public static CryptogramType[] values() {
        return (CryptogramType[])$VALUES.clone();
    }
}

