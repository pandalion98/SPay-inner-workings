package com.mastercard.mobile_api.utils.tlv;

public abstract class TLVHandler {
    public abstract void parseTag(byte b, int i, byte[] bArr, int i2);

    public abstract void parseTag(short s, int i, byte[] bArr, int i2);
}
