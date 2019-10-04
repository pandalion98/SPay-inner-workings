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

public final class SetValidatorResult
extends Enum<SetValidatorResult> {
    private static final /* synthetic */ SetValidatorResult[] ENUM$VALUES;
    public static final /* enum */ SetValidatorResult ERROR_INVALID_INPUT;
    public static final /* enum */ SetValidatorResult OK;

    static {
        OK = new SetValidatorResult();
        ERROR_INVALID_INPUT = new SetValidatorResult();
        SetValidatorResult[] arrsetValidatorResult = new SetValidatorResult[]{OK, ERROR_INVALID_INPUT};
        ENUM$VALUES = arrsetValidatorResult;
    }

    public static SetValidatorResult valueOf(String string) {
        return (SetValidatorResult)Enum.valueOf(SetValidatorResult.class, (String)string);
    }

    public static SetValidatorResult[] values() {
        SetValidatorResult[] arrsetValidatorResult = ENUM$VALUES;
        int n2 = arrsetValidatorResult.length;
        SetValidatorResult[] arrsetValidatorResult2 = new SetValidatorResult[n2];
        System.arraycopy((Object)arrsetValidatorResult, (int)0, (Object)arrsetValidatorResult2, (int)0, (int)n2);
        return arrsetValidatorResult2;
    }
}

