package com.mastercard.mobile_api.payment.cld;

public interface CLDDisplayable {
    public static final int BLOCKED = 2;
    public static final int DEFAULT = 0;
    public static final int EXPIRED = 1;

    CLD getCLD();

    int getState();
}
