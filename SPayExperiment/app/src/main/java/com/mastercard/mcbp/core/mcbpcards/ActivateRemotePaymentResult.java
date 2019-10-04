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

public final class ActivateRemotePaymentResult
extends Enum<ActivateRemotePaymentResult> {
    private static final /* synthetic */ ActivateRemotePaymentResult[] ENUM$VALUES;
    public static final /* enum */ ActivateRemotePaymentResult ERROR_INVALID_INPUT;
    public static final /* enum */ ActivateRemotePaymentResult ERROR_NOT_SUPPORTED;
    public static final /* enum */ ActivateRemotePaymentResult ERROR_UNINITIALIZED;
    public static final /* enum */ ActivateRemotePaymentResult INTERNAL_ERROR;
    public static final /* enum */ ActivateRemotePaymentResult OK;

    static {
        OK = new ActivateRemotePaymentResult();
        ERROR_UNINITIALIZED = new ActivateRemotePaymentResult();
        ERROR_INVALID_INPUT = new ActivateRemotePaymentResult();
        ERROR_NOT_SUPPORTED = new ActivateRemotePaymentResult();
        INTERNAL_ERROR = new ActivateRemotePaymentResult();
        ActivateRemotePaymentResult[] arractivateRemotePaymentResult = new ActivateRemotePaymentResult[]{OK, ERROR_UNINITIALIZED, ERROR_INVALID_INPUT, ERROR_NOT_SUPPORTED, INTERNAL_ERROR};
        ENUM$VALUES = arractivateRemotePaymentResult;
    }

    public static ActivateRemotePaymentResult valueOf(String string) {
        return (ActivateRemotePaymentResult)Enum.valueOf(ActivateRemotePaymentResult.class, (String)string);
    }

    public static ActivateRemotePaymentResult[] values() {
        ActivateRemotePaymentResult[] arractivateRemotePaymentResult = ENUM$VALUES;
        int n2 = arractivateRemotePaymentResult.length;
        ActivateRemotePaymentResult[] arractivateRemotePaymentResult2 = new ActivateRemotePaymentResult[n2];
        System.arraycopy((Object)arractivateRemotePaymentResult, (int)0, (Object)arractivateRemotePaymentResult2, (int)0, (int)n2);
        return arractivateRemotePaymentResult2;
    }
}

