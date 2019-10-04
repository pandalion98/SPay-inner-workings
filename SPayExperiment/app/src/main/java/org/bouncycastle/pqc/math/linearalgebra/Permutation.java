/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;
import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.math.linearalgebra.RandUtils;

public class Permutation {
    private int[] perm;

    public Permutation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("invalid length");
        }
        this.perm = new int[n];
        int n2 = n - 1;
        while (n2 >= 0) {
            this.perm[n2] = n2--;
        }
    }

    public Permutation(int n, SecureRandom secureRandom) {
        int n2 = 0;
        if (n <= 0) {
            throw new IllegalArgumentException("invalid length");
        }
        this.perm = new int[n];
        int[] arrn = new int[n];
        int n3 = 0;
        while (n3 < n) {
            arrn[n3] = n3++;
        }
        int n4 = n;
        while (n2 < n) {
            int n5 = RandUtils.nextInt(secureRandom, n4);
            this.perm[n2] = arrn[n5];
            arrn[n5] = arrn[--n4];
            ++n2;
        }
    }

    public Permutation(byte[] arrby) {
        int n;
        if (arrby.length <= 4) {
            throw new IllegalArgumentException("invalid encoding");
        }
        int n2 = LittleEndianConversions.OS2IP(arrby, 0);
        if (arrby.length != 4 + n2 * (n = IntegerFunctions.ceilLog256(n2 - 1))) {
            throw new IllegalArgumentException("invalid encoding");
        }
        this.perm = new int[n2];
        for (int i = 0; i < n2; ++i) {
            this.perm[i] = LittleEndianConversions.OS2IP(arrby, 4 + i * n, n);
        }
        if (!this.isPermutation(this.perm)) {
            throw new IllegalArgumentException("invalid encoding");
        }
    }

    public Permutation(int[] arrn) {
        if (!this.isPermutation(arrn)) {
            throw new IllegalArgumentException("array is not a permutation vector");
        }
        this.perm = IntUtils.clone(arrn);
    }

    private boolean isPermutation(int[] arrn) {
        int n = arrn.length;
        boolean[] arrbl = new boolean[n];
        for (int i = 0; i < n; ++i) {
            if (arrn[i] < 0 || arrn[i] >= n || arrbl[arrn[i]]) {
                return false;
            }
            arrbl[arrn[i]] = true;
        }
        return true;
    }

    public Permutation computeInverse() {
        Permutation permutation = new Permutation(this.perm.length);
        int n = -1 + this.perm.length;
        while (n >= 0) {
            permutation.perm[this.perm[n]] = n--;
        }
        return permutation;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Permutation)) {
            return false;
        }
        Permutation permutation = (Permutation)object;
        return IntUtils.equals(this.perm, permutation.perm);
    }

    public byte[] getEncoded() {
        int n = this.perm.length;
        int n2 = IntegerFunctions.ceilLog256(n - 1);
        byte[] arrby = new byte[4 + n * n2];
        LittleEndianConversions.I2OSP(n, arrby, 0);
        for (int i = 0; i < n; ++i) {
            LittleEndianConversions.I2OSP(this.perm[i], arrby, 4 + i * n2, n2);
        }
        return arrby;
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
        for (int i = -1 + this.perm.length; i >= 0; --i) {
            permutation2.perm[i] = this.perm[permutation.perm[i]];
        }
        return permutation2;
    }

    public String toString() {
        String string = "[" + this.perm[0];
        for (int i = 1; i < this.perm.length; ++i) {
            string = string + ", " + this.perm[i];
        }
        return string + "]";
    }
}

