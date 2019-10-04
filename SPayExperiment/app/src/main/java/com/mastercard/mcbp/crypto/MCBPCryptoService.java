/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.crypto;

import com.mastercard.mobile_api.bytes.ByteArray;

public abstract class MCBPCryptoService {
    private static MCBPCryptoService instance;

    public static MCBPCryptoService getInstance() {
        return instance;
    }

    public static void setInstance(MCBPCryptoService mCBPCryptoService) {
        instance = mCBPCryptoService;
    }

    public abstract ByteArray RSA(ByteArray var1);

    public abstract ByteArray SHA1(ByteArray var1);

    public abstract ByteArray SHA256(ByteArray var1);

    public abstract int initRSAPrivateKey(ByteArray var1, ByteArray var2, ByteArray var3, ByteArray var4, ByteArray var5);
}

