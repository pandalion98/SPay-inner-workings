/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spaytui;

public class AuthNonce {
    private boolean isFromCache;
    private byte[] nonce;

    public AuthNonce(byte[] arrby, boolean bl) {
        this.nonce = arrby;
        this.isFromCache = bl;
    }

    public byte[] getNonce() {
        return this.nonce;
    }

    public boolean isFromCache() {
        return this.isFromCache;
    }
}

