/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mobile_api.bytes;

public interface ByteArray {
    public ByteArray append(ByteArray var1);

    public ByteArray appendByte(byte var1);

    public void appendByteArrayAsLV(ByteArray var1);

    public ByteArray appendBytes(byte[] var1, int var2);

    public ByteArray bitWiseAnd(ByteArray var1);

    public void clear();

    public ByteArray clone();

    public void copyBufferToArray(byte[] var1, int var2, int var3, int var4);

    public void copyBytes(ByteArray var1, int var2, int var3, int var4);

    public ByteArray copyOfRange(int var1, int var2);

    public ByteArray fill(byte var1);

    public byte getByte(int var1);

    public byte[] getBytes();

    public String getHexString();

    public int getLength();

    public String getString();

    public ByteArray getUTF8();

    public boolean isEqual(ByteArray var1);

    public ByteArray makeXor(ByteArray var1);

    public void parityFix();

    public void setByte(int var1, byte var2);

    public void setBytes(byte[] var1);

    public void setShort(int var1, short var2);
}

