/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public interface TlsCipher {
    public byte[] decodeCiphertext(long var1, short var3, byte[] var4, int var5, int var6);

    public byte[] encodePlaintext(long var1, short var3, byte[] var4, int var5, int var6);

    public int getPlaintextLimit(int var1);
}

