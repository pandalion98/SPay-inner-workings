package com.mastercard.mobile_api.utils;

public abstract class MobileInteractions {
    static MobileInteractions INSTANCE;

    public abstract void vibrate();

    public static MobileInteractions getInstance() {
        return INSTANCE;
    }

    public static void setInstance(MobileInteractions mobileInteractions) {
        INSTANCE = mobileInteractions;
    }
}
