/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.IndexOutOfBoundsException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.GF2mVector;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.RandUtils;
import org.bouncycastle.pqc.math.linearalgebra.Vector;

public class GF2Vector
extends Vector {
    private int[] v;

    public GF2Vector(int n) {
        if (n < 0) {
            throw new ArithmeticException("Negative length.");
        }
        this.length = n;
        this.v = new int[n + 31 >> 5];
    }

    public GF2Vector(int n, int n2, SecureRandom secureRandom) {
        if (n2 > n) {
            throw new ArithmeticException("The hamming weight is greater than the length of vector.");
        }
        this.length = n;
        this.v = new int[n + 31 >> 5];
        int[] arrn = new int[n];
        int n3 = 0;
        do {
            if (n3 >= n) break;
            arrn[n3] = n3++;
        } while (true);
        for (int i = 0; i < n2; ++i) {
            int n4 = RandUtils.nextInt(secureRandom, n);
            this.setBit(arrn[n4]);
            arrn[n4] = arrn[--n];
        }
    }

    public GF2Vector(int n, SecureRandom secureRandom) {
        this.length = n;
        int n2 = n + 31 >> 5;
        this.v = new int[n2];
        for (int i = n2 - 1; i >= 0; --i) {
            this.v[i] = secureRandom.nextInt();
        }
        int n3 = n & 31;
        if (n3 != 0) {
            int[] arrn = this.v;
            int n4 = n2 - 1;
            arrn[n4] = arrn[n4] & -1 + (1 << n3);
        }
    }

    public GF2Vector(int n, int[] arrn) {
        if (n < 0) {
            throw new ArithmeticException("negative length");
        }
        this.length = n;
        int n2 = n + 31 >> 5;
        if (arrn.length != n2) {
            throw new ArithmeticException("length mismatch");
        }
        this.v = IntUtils.clone(arrn);
        int n3 = n & 31;
        if (n3 != 0) {
            int[] arrn2 = this.v;
            int n4 = n2 - 1;
            arrn2[n4] = arrn2[n4] & -1 + (1 << n3);
        }
    }

    public GF2Vector(GF2Vector gF2Vector) {
        this.length = gF2Vector.length;
        this.v = IntUtils.clone(gF2Vector.v);
    }

    protected GF2Vector(int[] arrn, int n) {
        this.v = arrn;
        this.length = n;
    }

    public static GF2Vector OS2VP(int n, byte[] arrby) {
        if (n < 0) {
            throw new ArithmeticException("negative length");
        }
        int n2 = n + 7 >> 3;
        if (arrby.length > n2) {
            throw new ArithmeticException("length mismatch");
        }
        return new GF2Vector(n, LittleEndianConversions.toIntArray(arrby));
    }

    @Override
    public Vector add(Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        GF2Vector gF2Vector = (GF2Vector)vector;
        if (this.length != gF2Vector.length) {
            throw new ArithmeticException("length mismatch");
        }
        int[] arrn = IntUtils.clone(((GF2Vector)vector).v);
        for (int i = -1 + arrn.length; i >= 0; --i) {
            arrn[i] = arrn[i] ^ this.v[i];
        }
        return new GF2Vector(this.length, arrn);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (!(object instanceof GF2Vector)) break block2;
                GF2Vector gF2Vector = (GF2Vector)object;
                if (this.length == gF2Vector.length && IntUtils.equals(this.v, gF2Vector.v)) break block3;
            }
            return false;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public GF2Vector extractLeftVector(int n) {
        if (n > this.length) {
            throw new ArithmeticException("invalid length");
        }
        if (n == this.length) {
            return new GF2Vector(this);
        }
        GF2Vector gF2Vector = new GF2Vector(n);
        int n2 = n >> 5;
        int n3 = n & 31;
        System.arraycopy((Object)this.v, (int)0, (Object)gF2Vector.v, (int)0, (int)n2);
        if (n3 == 0) return gF2Vector;
        gF2Vector.v[n2] = this.v[n2] & -1 + (1 << n3);
        return gF2Vector;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public GF2Vector extractRightVector(int n) {
        if (n > this.length) {
            throw new ArithmeticException("invalid length");
        }
        if (n == this.length) {
            return new GF2Vector(this);
        }
        GF2Vector gF2Vector = new GF2Vector(n);
        int n2 = this.length - n >> 5;
        int n3 = 31 & this.length - n;
        int n4 = n + 31 >> 5;
        if (n3 != 0) {
            for (int i = 0; i < n4 - 1; ++i) {
                int[] arrn = gF2Vector.v;
                int[] arrn2 = this.v;
                int n5 = n2 + 1;
                arrn[i] = arrn2[n2] >>> n3 | this.v[n5] << 32 - n3;
                n2 = n5;
            }
            int[] arrn = gF2Vector.v;
            int n6 = n4 - 1;
            int[] arrn3 = this.v;
            int n7 = n2 + 1;
            arrn[n6] = arrn3[n2] >>> n3;
            if (n7 >= this.v.length) return gF2Vector;
            int[] arrn4 = gF2Vector.v;
            int n8 = n4 - 1;
            arrn4[n8] = arrn4[n8] | this.v[n7] << 32 - n3;
            return gF2Vector;
        }
        System.arraycopy((Object)this.v, (int)n2, (Object)gF2Vector.v, (int)0, (int)n4);
        return gF2Vector;
    }

    public GF2Vector extractVector(int[] arrn) {
        int n = arrn.length;
        if (arrn[n - 1] > this.length) {
            throw new ArithmeticException("invalid index set");
        }
        GF2Vector gF2Vector = new GF2Vector(n);
        for (int i = 0; i < n; ++i) {
            if ((this.v[arrn[i] >> 5] & 1 << (31 & arrn[i])) == 0) continue;
            int[] arrn2 = gF2Vector.v;
            int n2 = i >> 5;
            arrn2[n2] = arrn2[n2] | 1 << (i & 31);
        }
        return gF2Vector;
    }

    public int getBit(int n) {
        if (n >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        int n2 = n >> 5;
        int n3 = n & 31;
        return (this.v[n2] & 1 << n3) >>> n3;
    }

    @Override
    public byte[] getEncoded() {
        int n = 7 + this.length >> 3;
        return LittleEndianConversions.toByteArray(this.v, n);
    }

    public int getHammingWeight() {
        int n = 0;
        for (int i = 0; i < this.v.length; ++i) {
            int n2 = this.v[i];
            for (int j = 0; j < 32; ++j) {
                if ((n2 & 1) != 0) {
                    ++n;
                }
                n2 >>>= 1;
            }
        }
        return n;
    }

    public int[] getVecArray() {
        return this.v;
    }

    @Override
    public int hashCode() {
        return 31 * this.length + this.v.hashCode();
    }

    @Override
    public boolean isZero() {
        for (int i = -1 + this.v.length; i >= 0; --i) {
            if (this.v[i] == 0) continue;
            return false;
        }
        return true;
    }

    @Override
    public Vector multiply(Permutation permutation) {
        int[] arrn = permutation.getVector();
        if (this.length != arrn.length) {
            throw new ArithmeticException("length mismatch");
        }
        GF2Vector gF2Vector = new GF2Vector(this.length);
        for (int i = 0; i < arrn.length; ++i) {
            if ((this.v[arrn[i] >> 5] & 1 << (31 & arrn[i])) == 0) continue;
            int[] arrn2 = gF2Vector.v;
            int n = i >> 5;
            arrn2[n] = arrn2[n] | 1 << (i & 31);
        }
        return gF2Vector;
    }

    public void setBit(int n) {
        if (n >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        int[] arrn = this.v;
        int n2 = n >> 5;
        arrn[n2] = arrn[n2] | 1 << (n & 31);
    }

    public GF2mVector toExtensionFieldVector(GF2mField gF2mField) {
        int n = gF2mField.getDegree();
        if (this.length % n != 0) {
            throw new ArithmeticException("conversion is impossible");
        }
        int n2 = this.length / n;
        int[] arrn = new int[n2];
        int n3 = 0;
        for (int i = n2 - 1; i >= 0; --i) {
            for (int j = -1 + gF2mField.getDegree(); j >= 0; --j) {
                int n4 = n3 >>> 5;
                int n5 = n3 & 31;
                if ((1 & this.v[n4] >>> n5) == 1) {
                    arrn[i] = arrn[i] ^ 1 << j;
                }
                ++n3;
            }
        }
        return new GF2mVector(gF2mField, arrn);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int n = 0;
        while (n < this.length) {
            int n2;
            int n3;
            if (n != 0 && (n & 31) == 0) {
                stringBuffer.append(' ');
            }
            if ((this.v[n3 = n >> 5] & 1 << (n2 = n & 31)) == 0) {
                stringBuffer.append('0');
            } else {
                stringBuffer.append('1');
            }
            ++n;
        }
        return stringBuffer.toString();
    }
}

