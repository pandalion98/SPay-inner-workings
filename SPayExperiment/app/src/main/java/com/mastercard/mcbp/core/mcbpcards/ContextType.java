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

public final class ContextType
extends Enum<ContextType> {
    public static final /* enum */ ContextType CONTEXT_CONFLICT;
    private static final /* synthetic */ ContextType[] ENUM$VALUES;
    public static final /* enum */ ContextType MAGSTRIPE_COMPLETED;
    public static final /* enum */ ContextType MAGSTRIPE_FIRST_TAP;
    public static final /* enum */ ContextType MCHIP_COMPLETED;
    public static final /* enum */ ContextType MCHIP_FIRST_TAP;
    public static final /* enum */ ContextType UNSUPPORTED_TRANSIT;

    static {
        MCHIP_FIRST_TAP = new ContextType();
        MCHIP_COMPLETED = new ContextType();
        MAGSTRIPE_FIRST_TAP = new ContextType();
        MAGSTRIPE_COMPLETED = new ContextType();
        CONTEXT_CONFLICT = new ContextType();
        UNSUPPORTED_TRANSIT = new ContextType();
        ContextType[] arrcontextType = new ContextType[]{MCHIP_FIRST_TAP, MCHIP_COMPLETED, MAGSTRIPE_FIRST_TAP, MAGSTRIPE_COMPLETED, CONTEXT_CONFLICT, UNSUPPORTED_TRANSIT};
        ENUM$VALUES = arrcontextType;
    }

    public static ContextType valueOf(String string) {
        return (ContextType)Enum.valueOf(ContextType.class, (String)string);
    }

    public static ContextType[] values() {
        ContextType[] arrcontextType = ENUM$VALUES;
        int n2 = arrcontextType.length;
        ContextType[] arrcontextType2 = new ContextType[n2];
        System.arraycopy((Object)arrcontextType, (int)0, (Object)arrcontextType2, (int)0, (int)n2);
        return arrcontextType2;
    }
}

