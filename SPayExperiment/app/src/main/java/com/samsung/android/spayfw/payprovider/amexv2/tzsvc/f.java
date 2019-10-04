/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

public interface f {
    public int close(byte[] var1);

    public int computeAC(byte[] var1, byte[] var2);

    public int getSignatureData(byte[] var1, byte[] var2);

    public int init(byte[] var1);

    public int initializeSecureChannel(byte[] var1, byte[] var2);

    public int lcm(int var1);

    public int open(byte[] var1);

    public int perso(int var1, byte[] var2, byte[] var3);

    public int reqMST(byte[] var1, byte[] var2, byte[] var3);

    public int sign(byte[] var1, byte[] var2, byte[] var3);

    public int unwrap(int var1, byte[] var2, int var3, byte[] var4, int var5);

    public int update(byte[] var1);

    public int updateSecureChannel(byte[] var1, byte[] var2);

    public int verify(byte[] var1, byte[] var2);

    public int wrap(byte[] var1, int var2, byte[] var3, int var4);
}

