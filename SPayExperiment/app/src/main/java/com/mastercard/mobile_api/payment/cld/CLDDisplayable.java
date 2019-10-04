/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.payment.cld.CLD;

public interface CLDDisplayable {
    public static final int BLOCKED = 2;
    public static final int DEFAULT = 0;
    public static final int EXPIRED = 1;

    public CLD getCLD();

    public int getState();
}

