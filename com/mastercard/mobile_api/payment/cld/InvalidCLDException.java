package com.mastercard.mobile_api.payment.cld;

public class InvalidCLDException extends Exception {
    private static final long serialVersionUID = 1;

    public InvalidCLDException(String str) {
        super(str);
    }
}
