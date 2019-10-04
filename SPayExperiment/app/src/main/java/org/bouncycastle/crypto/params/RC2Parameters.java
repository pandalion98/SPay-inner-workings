/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class RC2Parameters
implements CipherParameters {
    private int bits;
    private byte[] key;

    /*
     * Enabled aggressive block sorting
     */
    public RC2Parameters(byte[] arrby) {
        int n2 = arrby.length > 128 ? 1024 : 8 * arrby.length;
        this(arrby, n2);
    }

    public RC2Parameters(byte[] arrby, int n2) {
        this.key = new byte[arrby.length];
        this.bits = n2;
        System.arraycopy((Object)arrby, (int)0, (Object)this.key, (int)0, (int)arrby.length);
    }

    public int getEffectiveKeyBits() {
        return this.bits;
    }

    public byte[] getKey() {
        return this.key;
    }
}

