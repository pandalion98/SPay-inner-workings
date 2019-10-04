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

public final class StopContactlessResult
extends Enum<StopContactlessResult> {
    private static final /* synthetic */ StopContactlessResult[] ENUM$VALUES;
    public static final /* enum */ StopContactlessResult ERROR_UNINITIALIZED;
    public static final /* enum */ StopContactlessResult INTERNAL_ERROR;
    public static final /* enum */ StopContactlessResult OK;

    static {
        OK = new StopContactlessResult();
        ERROR_UNINITIALIZED = new StopContactlessResult();
        INTERNAL_ERROR = new StopContactlessResult();
        StopContactlessResult[] arrstopContactlessResult = new StopContactlessResult[]{OK, ERROR_UNINITIALIZED, INTERNAL_ERROR};
        ENUM$VALUES = arrstopContactlessResult;
    }

    public static StopContactlessResult valueOf(String string) {
        return (StopContactlessResult)Enum.valueOf(StopContactlessResult.class, (String)string);
    }

    public static StopContactlessResult[] values() {
        StopContactlessResult[] arrstopContactlessResult = ENUM$VALUES;
        int n2 = arrstopContactlessResult.length;
        StopContactlessResult[] arrstopContactlessResult2 = new StopContactlessResult[n2];
        System.arraycopy((Object)arrstopContactlessResult, (int)0, (Object)arrstopContactlessResult2, (int)0, (int)n2);
        return arrstopContactlessResult2;
    }
}

