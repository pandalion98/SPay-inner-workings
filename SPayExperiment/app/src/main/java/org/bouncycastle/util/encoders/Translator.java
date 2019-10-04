/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.util.encoders;

public interface Translator {
    public int decode(byte[] var1, int var2, int var3, byte[] var4, int var5);

    public int encode(byte[] var1, int var2, int var3, byte[] var4, int var5);

    public int getDecodedBlockSize();

    public int getEncodedBlockSize();
}

