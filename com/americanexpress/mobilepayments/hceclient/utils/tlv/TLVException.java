package com.americanexpress.mobilepayments.hceclient.utils.tlv;

public class TLVException extends RuntimeException {
    public TLVException(String str) {
        super(str);
    }

    public TLVException(String str, Throwable th) {
        super(str, th);
    }

    public TLVException(Throwable th) {
        super(th);
    }
}
