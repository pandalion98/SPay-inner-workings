package org.bouncycastle.crypto.modes.gcm;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class Tables64kGCMMultiplier implements GCMMultiplier {
    private byte[] f201H;
    private int[][][] f202M;

    public void init(byte[] bArr) {
        int i;
        if (this.f202M == null) {
            this.f202M = (int[][][]) Array.newInstance(Integer.TYPE, new int[]{16, SkeinMac.SKEIN_256, 4});
        } else if (Arrays.areEqual(this.f201H, bArr)) {
            return;
        }
        this.f201H = Arrays.clone(bArr);
        GCMUtil.asInts(bArr, this.f202M[0][X509KeyUsage.digitalSignature]);
        for (i = 64; i >= 1; i >>= 1) {
            GCMUtil.multiplyP(this.f202M[0][i + i], this.f202M[0][i]);
        }
        i = 0;
        while (true) {
            int i2;
            for (int i3 = 2; i3 < SkeinMac.SKEIN_256; i3 += i3) {
                for (i2 = 1; i2 < i3; i2++) {
                    GCMUtil.xor(this.f202M[i][i3], this.f202M[i][i2], this.f202M[i][i3 + i2]);
                }
            }
            i++;
            if (i != 16) {
                for (i2 = X509KeyUsage.digitalSignature; i2 > 0; i2 >>= 1) {
                    GCMUtil.multiplyP8(this.f202M[i - 1][i2], this.f202M[i][i2]);
                }
            } else {
                return;
            }
        }
    }

    public void multiplyH(byte[] bArr) {
        int[] iArr = new int[4];
        for (int i = 15; i >= 0; i--) {
            int[] iArr2 = this.f202M[i][bArr[i] & GF2Field.MASK];
            iArr[0] = iArr[0] ^ iArr2[0];
            iArr[1] = iArr[1] ^ iArr2[1];
            iArr[2] = iArr[2] ^ iArr2[2];
            iArr[3] = iArr2[3] ^ iArr[3];
        }
        Pack.intToBigEndian(iArr, bArr, 0);
    }
}
