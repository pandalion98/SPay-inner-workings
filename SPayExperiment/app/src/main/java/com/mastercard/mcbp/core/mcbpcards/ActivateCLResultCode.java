/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.mastercard.mcbp.core.mcbpcards;

public final class ActivateCLResultCode
extends Enum<ActivateCLResultCode> {
    private static final /* synthetic */ ActivateCLResultCode[] ENUM$VALUES;
    public static final /* enum */ ActivateCLResultCode ERROR_NOT_SUPPORTED;
    public static final /* enum */ ActivateCLResultCode INTERNAL_ERROR;
    public static final /* enum */ ActivateCLResultCode OK;

    static {
        OK = new ActivateCLResultCode();
        INTERNAL_ERROR = new ActivateCLResultCode();
        ERROR_NOT_SUPPORTED = new ActivateCLResultCode();
        ActivateCLResultCode[] arractivateCLResultCode = new ActivateCLResultCode[]{OK, INTERNAL_ERROR, ERROR_NOT_SUPPORTED};
        ENUM$VALUES = arractivateCLResultCode;
    }

    public static ActivateCLResultCode valueOf(String string) {
        return (ActivateCLResultCode)Enum.valueOf(ActivateCLResultCode.class, (String)string);
    }

    public static ActivateCLResultCode[] values() {
        ActivateCLResultCode[] arractivateCLResultCode = ENUM$VALUES;
        int n2 = arractivateCLResultCode.length;
        ActivateCLResultCode[] arractivateCLResultCode2 = new ActivateCLResultCode[n2];
        System.arraycopy((Object)arractivateCLResultCode, (int)0, (Object)arractivateCLResultCode2, (int)0, (int)n2);
        return arractivateCLResultCode2;
    }
}

