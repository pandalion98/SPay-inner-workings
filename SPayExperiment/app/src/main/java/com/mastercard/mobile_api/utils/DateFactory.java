/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils;

import com.mastercard.mobile_api.utils.Date;

public abstract class DateFactory {
    static DateFactory INSTANCE;

    public static DateFactory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(DateFactory dateFactory) {
        INSTANCE = dateFactory;
    }

    public abstract Date getTodaysDate();
}

