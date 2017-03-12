package com.mastercard.mobile_api.bytes;

public interface ByteArray {
    ByteArray append(ByteArray byteArray);

    ByteArray appendByte(byte b);

    void appendByteArrayAsLV(ByteArray byteArray);

    ByteArray appendBytes(byte[] bArr, int i);

    ByteArray bitWiseAnd(ByteArray byteArray);

    void clear();

    ByteArray clone();

    void copyBufferToArray(byte[] bArr, int i, int i2, int i3);

    void copyBytes(ByteArray byteArray, int i, int i2, int i3);

    ByteArray copyOfRange(int i, int i2);

    ByteArray fill(byte b);

    byte getByte(int i);

    byte[] getBytes();

    String getHexString();

    int getLength();

    String getString();

    ByteArray getUTF8();

    boolean isEqual(ByteArray byteArray);

    ByteArray makeXor(ByteArray byteArray);

    void parityFix();

    void setByte(int i, byte b);

    void setBytes(byte[] bArr);

    void setShort(int i, short s);
}
