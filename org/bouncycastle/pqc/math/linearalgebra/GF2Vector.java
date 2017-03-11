package org.bouncycastle.pqc.math.linearalgebra;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.security.SecureRandom;

public class GF2Vector extends Vector {
    private int[] f471v;

    public GF2Vector(int i) {
        if (i < 0) {
            throw new ArithmeticException("Negative length.");
        }
        this.length = i;
        this.f471v = new int[((i + 31) >> 5)];
    }

    public GF2Vector(int i, int i2, SecureRandom secureRandom) {
        int i3 = 0;
        if (i2 > i) {
            throw new ArithmeticException("The hamming weight is greater than the length of vector.");
        }
        int i4;
        this.length = i;
        this.f471v = new int[((i + 31) >> 5)];
        int[] iArr = new int[i];
        for (i4 = 0; i4 < i; i4++) {
            iArr[i4] = i4;
        }
        while (i3 < i2) {
            i4 = RandUtils.nextInt(secureRandom, i);
            setBit(iArr[i4]);
            i--;
            iArr[i4] = iArr[i];
            i3++;
        }
    }

    public GF2Vector(int i, SecureRandom secureRandom) {
        int i2;
        this.length = i;
        int i3 = (i + 31) >> 5;
        this.f471v = new int[i3];
        for (i2 = i3 - 1; i2 >= 0; i2--) {
            this.f471v[i2] = secureRandom.nextInt();
        }
        i2 = i & 31;
        if (i2 != 0) {
            int[] iArr = this.f471v;
            i3--;
            iArr[i3] = ((1 << i2) - 1) & iArr[i3];
        }
    }

    public GF2Vector(int i, int[] iArr) {
        if (i < 0) {
            throw new ArithmeticException("negative length");
        }
        this.length = i;
        int i2 = (i + 31) >> 5;
        if (iArr.length != i2) {
            throw new ArithmeticException("length mismatch");
        }
        this.f471v = IntUtils.clone(iArr);
        int i3 = i & 31;
        if (i3 != 0) {
            int[] iArr2 = this.f471v;
            i2--;
            iArr2[i2] = ((1 << i3) - 1) & iArr2[i2];
        }
    }

    public GF2Vector(GF2Vector gF2Vector) {
        this.length = gF2Vector.length;
        this.f471v = IntUtils.clone(gF2Vector.f471v);
    }

    protected GF2Vector(int[] iArr, int i) {
        this.f471v = iArr;
        this.length = i;
    }

    public static GF2Vector OS2VP(int i, byte[] bArr) {
        if (i < 0) {
            throw new ArithmeticException("negative length");
        }
        if (bArr.length <= ((i + 7) >> 3)) {
            return new GF2Vector(i, LittleEndianConversions.toIntArray(bArr));
        }
        throw new ArithmeticException("length mismatch");
    }

    public Vector add(Vector vector) {
        if (vector instanceof GF2Vector) {
            if (this.length != ((GF2Vector) vector).length) {
                throw new ArithmeticException("length mismatch");
            }
            int[] clone = IntUtils.clone(((GF2Vector) vector).f471v);
            for (int length = clone.length - 1; length >= 0; length--) {
                clone[length] = clone[length] ^ this.f471v[length];
            }
            return new GF2Vector(this.length, clone);
        }
        throw new ArithmeticException("vector is not defined over GF(2)");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GF2Vector)) {
            return false;
        }
        GF2Vector gF2Vector = (GF2Vector) obj;
        return this.length == gF2Vector.length && IntUtils.equals(this.f471v, gF2Vector.f471v);
    }

    public GF2Vector extractLeftVector(int i) {
        if (i > this.length) {
            throw new ArithmeticException("invalid length");
        } else if (i == this.length) {
            return new GF2Vector(this);
        } else {
            GF2Vector gF2Vector = new GF2Vector(i);
            int i2 = i >> 5;
            int i3 = i & 31;
            System.arraycopy(this.f471v, 0, gF2Vector.f471v, 0, i2);
            if (i3 == 0) {
                return gF2Vector;
            }
            gF2Vector.f471v[i2] = ((1 << i3) - 1) & this.f471v[i2];
            return gF2Vector;
        }
    }

    public GF2Vector extractRightVector(int i) {
        int i2 = 0;
        if (i > this.length) {
            throw new ArithmeticException("invalid length");
        } else if (i == this.length) {
            return new GF2Vector(this);
        } else {
            GF2Vector gF2Vector = new GF2Vector(i);
            int i3 = (this.length - i) >> 5;
            int i4 = (this.length - i) & 31;
            int i5 = (i + 31) >> 5;
            if (i4 != 0) {
                while (i2 < i5 - 1) {
                    int i6 = i3 + 1;
                    gF2Vector.f471v[i2] = (this.f471v[i3] >>> i4) | (this.f471v[i6] << (32 - i4));
                    i2++;
                    i3 = i6;
                }
                int i7 = i3 + 1;
                gF2Vector.f471v[i5 - 1] = this.f471v[i3] >>> i4;
                if (i7 >= this.f471v.length) {
                    return gF2Vector;
                }
                int[] iArr = gF2Vector.f471v;
                i3 = i5 - 1;
                iArr[i3] = iArr[i3] | (this.f471v[i7] << (32 - i4));
                return gF2Vector;
            }
            System.arraycopy(this.f471v, i3, gF2Vector.f471v, 0, i5);
            return gF2Vector;
        }
    }

    public GF2Vector extractVector(int[] iArr) {
        int length = iArr.length;
        if (iArr[length - 1] > this.length) {
            throw new ArithmeticException("invalid index set");
        }
        GF2Vector gF2Vector = new GF2Vector(length);
        for (int i = 0; i < length; i++) {
            if ((this.f471v[iArr[i] >> 5] & (1 << (iArr[i] & 31))) != 0) {
                int[] iArr2 = gF2Vector.f471v;
                int i2 = i >> 5;
                iArr2[i2] = iArr2[i2] | (1 << (i & 31));
            }
        }
        return gF2Vector;
    }

    public int getBit(int i) {
        if (i >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        int i2 = i & 31;
        return (this.f471v[i >> 5] & (1 << i2)) >>> i2;
    }

    public byte[] getEncoded() {
        return LittleEndianConversions.toByteArray(this.f471v, (this.length + 7) >> 3);
    }

    public int getHammingWeight() {
        int i = 0;
        for (int i2 : this.f471v) {
            for (int i3 = 0; i3 < 32; i3++) {
                if ((r4 & 1) != 0) {
                    i++;
                }
                int i4 >>>= 1;
            }
        }
        return i;
    }

    public int[] getVecArray() {
        return this.f471v;
    }

    public int hashCode() {
        return (this.length * 31) + this.f471v.hashCode();
    }

    public boolean isZero() {
        for (int length = this.f471v.length - 1; length >= 0; length--) {
            if (this.f471v[length] != 0) {
                return false;
            }
        }
        return true;
    }

    public Vector multiply(Permutation permutation) {
        int[] vector = permutation.getVector();
        if (this.length != vector.length) {
            throw new ArithmeticException("length mismatch");
        }
        Vector gF2Vector = new GF2Vector(this.length);
        for (int i = 0; i < vector.length; i++) {
            if ((this.f471v[vector[i] >> 5] & (1 << (vector[i] & 31))) != 0) {
                int[] iArr = gF2Vector.f471v;
                int i2 = i >> 5;
                iArr[i2] = iArr[i2] | (1 << (i & 31));
            }
        }
        return gF2Vector;
    }

    public void setBit(int i) {
        if (i >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        int[] iArr = this.f471v;
        int i2 = i >> 5;
        iArr[i2] = iArr[i2] | (1 << (i & 31));
    }

    public GF2mVector toExtensionFieldVector(GF2mField gF2mField) {
        int degree = gF2mField.getDegree();
        if (this.length % degree != 0) {
            throw new ArithmeticException("conversion is impossible");
        }
        degree = this.length / degree;
        int[] iArr = new int[degree];
        int i = 0;
        for (int i2 = degree - 1; i2 >= 0; i2--) {
            for (degree = gF2mField.getDegree() - 1; degree >= 0; degree--) {
                if (((this.f471v[i >>> 5] >>> (i & 31)) & 1) == 1) {
                    iArr[i2] = iArr[i2] ^ (1 << degree);
                }
                i++;
            }
        }
        return new GF2mVector(gF2mField, iArr);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (i < this.length) {
            if (i != 0 && (i & 31) == 0) {
                stringBuffer.append(' ');
            }
            if ((this.f471v[i >> 5] & (1 << (i & 31))) == 0) {
                stringBuffer.append(LLVARUtil.EMPTY_STRING);
            } else {
                stringBuffer.append(LLVARUtil.PLAIN_TEXT);
            }
            i++;
        }
        return stringBuffer.toString();
    }
}
