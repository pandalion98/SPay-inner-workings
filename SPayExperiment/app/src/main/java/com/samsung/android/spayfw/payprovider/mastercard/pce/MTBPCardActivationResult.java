/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

public final class MTBPCardActivationResult
extends Enum<MTBPCardActivationResult> {
    private static final /* synthetic */ MTBPCardActivationResult[] $VALUES;
    public static final /* enum */ MTBPCardActivationResult CARD_ACTIVATION_INTERNAL_ERROR;
    public static final /* enum */ MTBPCardActivationResult CARD_ACTIVATION_INVALID_PROFILE;
    public static final /* enum */ MTBPCardActivationResult CARD_ACTIVATION_SUCCESS;

    static {
        CARD_ACTIVATION_SUCCESS = new MTBPCardActivationResult();
        CARD_ACTIVATION_INVALID_PROFILE = new MTBPCardActivationResult();
        CARD_ACTIVATION_INTERNAL_ERROR = new MTBPCardActivationResult();
        MTBPCardActivationResult[] arrmTBPCardActivationResult = new MTBPCardActivationResult[]{CARD_ACTIVATION_SUCCESS, CARD_ACTIVATION_INVALID_PROFILE, CARD_ACTIVATION_INTERNAL_ERROR};
        $VALUES = arrmTBPCardActivationResult;
    }

    public static MTBPCardActivationResult valueOf(String string) {
        return (MTBPCardActivationResult)Enum.valueOf(MTBPCardActivationResult.class, (String)string);
    }

    public static MTBPCardActivationResult[] values() {
        return (MTBPCardActivationResult[])$VALUES.clone();
    }
}

