package com.samsung.android.spaytui;

public class AuthNonce {
    private boolean isFromCache;
    private byte[] nonce;

    public AuthNonce(byte[] bArr, boolean z) {
        this.nonce = bArr;
        this.isFromCache = z;
    }

    public byte[] getNonce() {
        return this.nonce;
    }

    public boolean isFromCache() {
        return this.isFromCache;
    }
}
