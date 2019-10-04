/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.data;

public final class ReturnCode
extends Enum<ReturnCode> {
    private static final /* synthetic */ ReturnCode[] $VALUES;
    public static final /* enum */ ReturnCode CRYPTO_ERROR;
    public static final /* enum */ ReturnCode ERROR_CREDENTIALS;
    public static final /* enum */ ReturnCode ERROR_INCOMPATIBLE_PROFILE;
    public static final /* enum */ ReturnCode ERROR_INVALID_INPUT;
    public static final /* enum */ ReturnCode OK;
    public static final /* enum */ ReturnCode STATE_ERROR;

    static {
        OK = new ReturnCode();
        ERROR_INCOMPATIBLE_PROFILE = new ReturnCode();
        ERROR_CREDENTIALS = new ReturnCode();
        ERROR_INVALID_INPUT = new ReturnCode();
        CRYPTO_ERROR = new ReturnCode();
        STATE_ERROR = new ReturnCode();
        ReturnCode[] arrreturnCode = new ReturnCode[]{OK, ERROR_INCOMPATIBLE_PROFILE, ERROR_CREDENTIALS, ERROR_INVALID_INPUT, CRYPTO_ERROR, STATE_ERROR};
        $VALUES = arrreturnCode;
    }

    public static ReturnCode valueOf(String string) {
        return (ReturnCode)Enum.valueOf(ReturnCode.class, (String)string);
    }

    public static ReturnCode[] values() {
        return (ReturnCode[])$VALUES.clone();
    }
}

