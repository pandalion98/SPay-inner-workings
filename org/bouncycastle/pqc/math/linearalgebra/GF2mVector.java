package org.bouncycastle.pqc.math.linearalgebra;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class GF2mVector extends Vector {
    private GF2mField field;
    private int[] vector;

    public GF2mVector(GF2mField gF2mField, byte[] bArr) {
        this.field = new GF2mField(gF2mField);
        int i = 8;
        int i2 = 1;
        while (gF2mField.getDegree() > i) {
            i2++;
            i += 8;
        }
        if (bArr.length % i2 != 0) {
            throw new IllegalArgumentException("Byte array is not an encoded vector over the given finite field.");
        }
        this.length = bArr.length / i2;
        this.vector = new int[this.length];
        i2 = 0;
        int i3 = 0;
        while (i2 < this.vector.length) {
            int i4 = 0;
            while (i4 < i) {
                int[] iArr = this.vector;
                int i5 = i3 + 1;
                iArr[i2] = ((bArr[i3] & GF2Field.MASK) << i4) | iArr[i2];
                i4 += 8;
                i3 = i5;
            }
            if (gF2mField.isElementOfThisField(this.vector[i2])) {
                i2++;
            } else {
                throw new IllegalArgumentException("Byte array is not an encoded vector over the given finite field.");
            }
        }
    }

    public GF2mVector(GF2mField gF2mField, int[] iArr) {
        this.field = gF2mField;
        this.length = iArr.length;
        int length = iArr.length - 1;
        while (length >= 0) {
            if (gF2mField.isElementOfThisField(iArr[length])) {
                length--;
            } else {
                throw new ArithmeticException("Element array is not specified over the given finite field.");
            }
        }
        this.vector = IntUtils.clone(iArr);
    }

    public GF2mVector(GF2mVector gF2mVector) {
        this.field = new GF2mField(gF2mVector.field);
        this.length = gF2mVector.length;
        this.vector = IntUtils.clone(gF2mVector.vector);
    }

    public Vector add(Vector vector) {
        throw new RuntimeException("not implemented");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GF2mVector)) {
            return false;
        }
        GF2mVector gF2mVector = (GF2mVector) obj;
        return this.field.equals(gF2mVector.field) ? IntUtils.equals(this.vector, gF2mVector.vector) : false;
    }

    public byte[] getEncoded() {
        int i = 8;
        int i2 = 1;
        while (this.field.getDegree() > i) {
            i2++;
            i += 8;
        }
        byte[] bArr = new byte[(i2 * this.vector.length)];
        int i3 = 0;
        for (int i4 : this.vector) {
            int i5 = 0;
            while (i5 < i) {
                int i6 = i3 + 1;
                bArr[i3] = (byte) (i4 >>> i5);
                i5 += 8;
                i3 = i6;
            }
        }
        return bArr;
    }

    public GF2mField getField() {
        return this.field;
    }

    public int[] getIntArrayForm() {
        return IntUtils.clone(this.vector);
    }

    public int hashCode() {
        return (this.field.hashCode() * 31) + this.vector.hashCode();
    }

    public boolean isZero() {
        for (int length = this.vector.length - 1; length >= 0; length--) {
            if (this.vector[length] != 0) {
                return false;
            }
        }
        return true;
    }

    public Vector multiply(Permutation permutation) {
        int[] vector = permutation.getVector();
        if (this.length != vector.length) {
            throw new ArithmeticException("permutation size and vector size mismatch");
        }
        int[] iArr = new int[this.length];
        for (int i = 0; i < vector.length; i++) {
            iArr[i] = this.vector[vector[i]];
        }
        return new GF2mVector(this.field, iArr);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i : this.vector) {
            for (int i2 = 0; i2 < this.field.getDegree(); i2++) {
                if (((1 << (i2 & 31)) & i) != 0) {
                    stringBuffer.append(LLVARUtil.PLAIN_TEXT);
                } else {
                    stringBuffer.append(LLVARUtil.EMPTY_STRING);
                }
            }
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }
}
