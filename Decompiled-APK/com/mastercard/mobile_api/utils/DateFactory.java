package com.mastercard.mobile_api.utils;

public abstract class DateFactory {
    static DateFactory INSTANCE;

    public abstract Date getTodaysDate();

    public static DateFactory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(DateFactory dateFactory) {
        INSTANCE = dateFactory;
    }
}
