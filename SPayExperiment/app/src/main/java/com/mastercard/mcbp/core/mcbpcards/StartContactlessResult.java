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

public final class StartContactlessResult
extends Enum<StartContactlessResult> {
    private static final /* synthetic */ StartContactlessResult[] ENUM$VALUES;
    public static final /* enum */ StartContactlessResult INTERNAL_ERROR;
    public static final /* enum */ StartContactlessResult OK;

    static {
        OK = new StartContactlessResult();
        INTERNAL_ERROR = new StartContactlessResult();
        StartContactlessResult[] arrstartContactlessResult = new StartContactlessResult[]{OK, INTERNAL_ERROR};
        ENUM$VALUES = arrstartContactlessResult;
    }

    public static StartContactlessResult valueOf(String string) {
        return (StartContactlessResult)Enum.valueOf(StartContactlessResult.class, (String)string);
    }

    public static StartContactlessResult[] values() {
        StartContactlessResult[] arrstartContactlessResult = ENUM$VALUES;
        int n2 = arrstartContactlessResult.length;
        StartContactlessResult[] arrstartContactlessResult2 = new StartContactlessResult[n2];
        System.arraycopy((Object)arrstartContactlessResult, (int)0, (Object)arrstartContactlessResult2, (int)0, (int)n2);
        return arrstartContactlessResult2;
    }
}

