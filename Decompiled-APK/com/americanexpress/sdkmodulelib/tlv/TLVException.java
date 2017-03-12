package com.americanexpress.sdkmodulelib.tlv;

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
