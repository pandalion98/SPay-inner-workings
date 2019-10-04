/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class KeyParameter
implements CipherParameters {
    private byte[] key;

    public KeyParameter(byte[] arrby) {
        this(arrby, 0, arrby.length);
    }

    public KeyParameter(byte[] arrby, int n2, int n3) {
        this.key = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)this.key, (int)0, (int)n3);
    }

    public byte[] getKey() {
        return this.key;
    }
}

