package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.util.Arrays;

public class BasicGCMExponentiator implements GCMExponentiator {
    private int[] f199x;

    public void exponentiateX(long j, byte[] bArr) {
        int[] oneAsInts = GCMUtil.oneAsInts();
        if (j > 0) {
            int[] clone = Arrays.clone(this.f199x);
            do {
                if ((1 & j) != 0) {
                    GCMUtil.multiply(oneAsInts, clone);
                }
                GCMUtil.multiply(clone, clone);
                j >>>= 1;
            } while (j > 0);
        }
        GCMUtil.asBytes(oneAsInts, bArr);
    }

    public void init(byte[] bArr) {
        this.f199x = GCMUtil.asInts(bArr);
    }
}
