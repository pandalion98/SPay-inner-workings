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

public final class ProfileState
extends Enum<ProfileState> {
    private static final /* synthetic */ ProfileState[] ENUM$VALUES;
    public static final /* enum */ ProfileState INITIALIZED;
    public static final /* enum */ ProfileState UNINITIALIZED;

    static {
        UNINITIALIZED = new ProfileState();
        INITIALIZED = new ProfileState();
        ProfileState[] arrprofileState = new ProfileState[]{UNINITIALIZED, INITIALIZED};
        ENUM$VALUES = arrprofileState;
    }

    public static ProfileState valueOf(String string) {
        return (ProfileState)Enum.valueOf(ProfileState.class, (String)string);
    }

    public static ProfileState[] values() {
        ProfileState[] arrprofileState = ENUM$VALUES;
        int n2 = arrprofileState.length;
        ProfileState[] arrprofileState2 = new ProfileState[n2];
        System.arraycopy((Object)arrprofileState, (int)0, (Object)arrprofileState2, (int)0, (int)n2);
        return arrprofileState2;
    }
}

