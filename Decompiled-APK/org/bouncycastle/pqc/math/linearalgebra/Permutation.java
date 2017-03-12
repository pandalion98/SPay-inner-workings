package org.bouncycastle.pqc.math.linearalgebra;

import java.security.SecureRandom;

public class Permutation {
    private int[] perm;

    public Permutation(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("invalid length");
        }
        this.perm = new int[i];
        for (int i2 = i - 1; i2 >= 0; i2--) {
            this.perm[i2] = i2;
        }
    }

    public Permutation(int i, SecureRandom secureRandom) {
        int i2 = 0;
        if (i <= 0) {
            throw new IllegalArgumentException("invalid length");
        }
        int i3;
        this.perm = new int[i];
        int[] iArr = new int[i];
        for (i3 = 0; i3 < i; i3++) {
            iArr[i3] = i3;
        }
        i3 = i;
        while (i2 < i) {
            int nextInt = RandUtils.nextInt(secureRandom, i3);
            i3--;
            this.perm[i2] = iArr[nextInt];
            iArr[nextInt] = iArr[i3];
            i2++;
        }
    }

    public Permutation(byte[] bArr) {
        int i = 0;
        if (bArr.length <= 4) {
            throw new IllegalArgumentException("invalid encoding");
        }
        int OS2IP = LittleEndianConversions.OS2IP(bArr, 0);
        int ceilLog256 = IntegerFunctions.ceilLog256(OS2IP - 1);
        if (bArr.length != (OS2IP * ceilLog256) + 4) {
            throw new IllegalArgumentException("invalid encoding");
        }
        this.perm = new int[OS2IP];
        while (i < OS2IP) {
            this.perm[i] = LittleEndianConversions.OS2IP(bArr, (i * ceilLog256) + 4, ceilLog256);
            i++;
        }
        if (!isPermutation(this.perm)) {
            throw new IllegalArgumentException("invalid encoding");
        }
    }

    public Permutation(int[] iArr) {
        if (isPermutation(iArr)) {
            this.perm = IntUtils.clone(iArr);
            return;
        }
        throw new IllegalArgumentException("array is not a permutation vector");
    }

    private boolean isPermutation(int[] iArr) {
        int length = iArr.length;
        boolean[] zArr = new boolean[length];
        int i = 0;
        while (i < length) {
            if (iArr[i] < 0 || iArr[i] >= length || zArr[iArr[i]]) {
                return false;
            }
            zArr[iArr[i]] = true;
            i++;
        }
        return true;
    }

    public Permutation computeInverse() {
        Permutation permutation = new Permutation(this.perm.length);
        for (int length = this.perm.length - 1; length >= 0; length--) {
            permutation.perm[this.perm[length]] = length;
        }
        return permutation;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Permutation)) {
            return false;
        }
        return IntUtils.equals(this.perm, ((Permutation) obj).perm);
    }

    public byte[] getEncoded() {
        int i = 0;
        int length = this.perm.length;
        int ceilLog256 = IntegerFunctions.ceilLog256(length - 1);
        byte[] bArr = new byte[((length * ceilLog256) + 4)];
        LittleEndianConversions.I2OSP(length, bArr, 0);
        while (i < length) {
            LittleEndianConversions.I2OSP(this.perm[i], bArr, (i * ceilLog256) + 4, ceilLog256);
            i++;
        }
        return bArr;
    }

    public int[] getVector() {
        return IntUtils.clone(this.perm);
    }

    public int hashCode() {
        return this.perm.hashCode();
    }

    public Permutation rightMultiply(Permutation permutation) {
        if (permutation.perm.length != this.perm.length) {
            throw new IllegalArgumentException("length mismatch");
        }
        Permutation permutation2 = new Permutation(this.perm.length);
        for (int length = this.perm.length - 1; length >= 0; length--) {
            permutation2.perm[length] = this.perm[permutation.perm[length]];
        }
        return permutation2;
    }

    public String toString() {
        String str = "[" + this.perm[0];
        for (int i = 1; i < this.perm.length; i++) {
            str = str + ", " + this.perm[i];
        }
        return str + "]";
    }
}
