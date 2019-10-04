/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

public final class TokenStatus
extends Enum<TokenStatus> {
    private static final /* synthetic */ TokenStatus[] $VALUES;
    public static final /* enum */ TokenStatus ACTIVE = new TokenStatus("ACTIVE");
    public static final /* enum */ TokenStatus DELETED;
    public static final /* enum */ TokenStatus INACTIVE;
    public static final /* enum */ TokenStatus NOT_FOUND;
    public static final /* enum */ TokenStatus RESUME;
    public static final /* enum */ TokenStatus SUSPENDED;
    private final String text;

    static {
        INACTIVE = new TokenStatus("INACTIVE");
        RESUME = new TokenStatus("RESUME");
        SUSPENDED = new TokenStatus("SUSPENDED");
        DELETED = new TokenStatus("DELETED");
        NOT_FOUND = new TokenStatus("NOT_FOUND");
        TokenStatus[] arrtokenStatus = new TokenStatus[]{ACTIVE, INACTIVE, RESUME, SUSPENDED, DELETED, NOT_FOUND};
        $VALUES = arrtokenStatus;
    }

    private TokenStatus(String string2) {
        this.text = string2;
    }

    public static TokenStatus valueOf(String string) {
        return (TokenStatus)Enum.valueOf(TokenStatus.class, (String)string);
    }

    public static TokenStatus[] values() {
        return (TokenStatus[])$VALUES.clone();
    }

    public String getStatus() {
        return this.text;
    }

    public String toString() {
        return this.text;
    }
}

