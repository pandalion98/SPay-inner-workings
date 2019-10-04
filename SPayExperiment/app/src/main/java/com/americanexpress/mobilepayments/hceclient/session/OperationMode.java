/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.session;

public final class OperationMode
extends Enum<OperationMode> {
    private static final /* synthetic */ OperationMode[] $VALUES;
    public static final /* enum */ OperationMode LCM;
    public static final /* enum */ OperationMode PAYMENT;
    public static final /* enum */ OperationMode PROVISION;
    public static final /* enum */ OperationMode REFRESH;
    public static final /* enum */ OperationMode TAP_PAYMENT;
    private long timeAllowed;

    static {
        PROVISION = new OperationMode(15000L);
        PAYMENT = new OperationMode(300L);
        REFRESH = new OperationMode(15000L);
        TAP_PAYMENT = new OperationMode(300L);
        LCM = new OperationMode(300L);
        OperationMode[] arroperationMode = new OperationMode[]{PROVISION, PAYMENT, REFRESH, TAP_PAYMENT, LCM};
        $VALUES = arroperationMode;
    }

    private OperationMode(long l2) {
        this.timeAllowed = l2;
    }

    public static OperationMode valueOf(String string) {
        return (OperationMode)Enum.valueOf(OperationMode.class, (String)string);
    }

    public static OperationMode[] values() {
        return (OperationMode[])$VALUES.clone();
    }

    public long getTimeAllowed() {
        return this.timeAllowed;
    }

    public void setTimeAllowed(long l2) {
        this.timeAllowed = l2;
    }
}

