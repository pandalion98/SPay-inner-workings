/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public final class DSRPResultCode
extends Enum<DSRPResultCode> {
    private static final /* synthetic */ DSRPResultCode[] $VALUES;
    public static final /* enum */ DSRPResultCode ERROR_INVALID_INPUT;
    public static final /* enum */ DSRPResultCode ERROR_UNEXPECTED_DATA;
    public static final /* enum */ DSRPResultCode INTERNAL_ERROR;
    public static final /* enum */ DSRPResultCode OK;

    static {
        OK = new DSRPResultCode();
        INTERNAL_ERROR = new DSRPResultCode();
        ERROR_UNEXPECTED_DATA = new DSRPResultCode();
        ERROR_INVALID_INPUT = new DSRPResultCode();
        DSRPResultCode[] arrdSRPResultCode = new DSRPResultCode[]{OK, INTERNAL_ERROR, ERROR_UNEXPECTED_DATA, ERROR_INVALID_INPUT};
        $VALUES = arrdSRPResultCode;
    }

    public static DSRPResultCode valueOf(String string) {
        return (DSRPResultCode)Enum.valueOf(DSRPResultCode.class, (String)string);
    }

    public static DSRPResultCode[] values() {
        return (DSRPResultCode[])$VALUES.clone();
    }
}

