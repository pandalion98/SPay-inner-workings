/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class RC5Parameters
implements CipherParameters {
    private byte[] key;
    private int rounds;

    public RC5Parameters(byte[] arrby, int n2) {
        if (arrby.length > 255) {
            throw new IllegalArgumentException("RC5 key length can be no greater than 255");
        }
        this.key = new byte[arrby.length];
        this.rounds = n2;
        System.arraycopy((Object)arrby, (int)0, (Object)this.key, (int)0, (int)arrby.length);
    }

    public byte[] getKey() {
        return this.key;
    }

    public int getRounds() {
        return this.rounds;
    }
}

