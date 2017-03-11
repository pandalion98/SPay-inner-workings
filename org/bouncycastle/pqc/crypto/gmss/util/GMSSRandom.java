package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class GMSSRandom {
    private Digest messDigestTree;

    public GMSSRandom(Digest digest) {
        this.messDigestTree = digest;
    }

    private void addByteArrays(byte[] bArr, byte[] bArr2) {
        int i = 0;
        int i2 = 0;
        while (i < bArr.length) {
            i2 += (bArr[i] & GF2Field.MASK) + (bArr2[i] & GF2Field.MASK);
            bArr[i] = (byte) i2;
            i2 = (byte) (i2 >> 8);
            i++;
        }
    }

    private void addOne(byte[] bArr) {
        int i = 1;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            i += bArr[i2] & GF2Field.MASK;
            bArr[i2] = (byte) i;
            i = (byte) (i >> 8);
        }
    }

    public byte[] nextSeed(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        this.messDigestTree.update(bArr, 0, bArr.length);
        bArr2 = new byte[this.messDigestTree.getDigestSize()];
        this.messDigestTree.doFinal(bArr2, 0);
        addByteArrays(bArr, bArr2);
        addOne(bArr);
        return bArr2;
    }
}
