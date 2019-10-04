/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.StreamCipher;

public abstract class StreamBlockCipher
implements BlockCipher,
StreamCipher {
    private final BlockCipher cipher;

    protected StreamBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
    }

    protected abstract byte calculateByte(byte var1);

    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        if (n4 + n3 > arrby2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (n2 + n3 > arrby.length) {
            throw new DataLengthException("input buffer too small");
        }
        int n5 = n2 + n3;
        while (n2 < n5) {
            int n6 = n4 + 1;
            int n7 = n2 + 1;
            arrby2[n4] = this.calculateByte(arrby[n2]);
            n4 = n6;
            n2 = n7;
        }
        return n3;
    }

    @Override
    public final byte returnByte(byte by) {
        return this.calculateByte(by);
    }
}

