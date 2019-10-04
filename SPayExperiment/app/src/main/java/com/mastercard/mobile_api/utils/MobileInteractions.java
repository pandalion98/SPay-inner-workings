/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils;

public abstract class MobileInteractions {
    static MobileInteractions INSTANCE;

    public static MobileInteractions getInstance() {
        return INSTANCE;
    }

    public static void setInstance(MobileInteractions mobileInteractions) {
        INSTANCE = mobileInteractions;
    }

    public abstract void vibrate();
}

