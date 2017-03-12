package com.mastercard.mcbp.crypto;

import com.mastercard.mobile_api.bytes.ByteArray;

public abstract class MCBPCryptoService {
    private static MCBPCryptoService instance;

    public abstract ByteArray RSA(ByteArray byteArray);

    public abstract ByteArray SHA1(ByteArray byteArray);

    public abstract ByteArray SHA256(ByteArray byteArray);

    public abstract int initRSAPrivateKey(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4, ByteArray byteArray5);

    public static MCBPCryptoService getInstance() {
        return instance;
    }

    public static void setInstance(MCBPCryptoService mCBPCryptoService) {
        instance = mCBPCryptoService;
    }
}
