/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv;

public class TLVException
extends RuntimeException {
    public TLVException(String string) {
        super(string);
    }

    public TLVException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public TLVException(Throwable throwable) {
        super(throwable);
    }
}

