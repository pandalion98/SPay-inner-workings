/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.Vector;

public class GF2mVector
extends Vector {
    private GF2mField field;
    private int[] vector;

    public GF2mVector(GF2mField gF2mField, byte[] arrby) {
        int n;
        this.field = new GF2mField(gF2mField);
        int n2 = 1;
        for (n = 8; gF2mField.getDegree() > n; n += 8) {
            ++n2;
        }
        if (arrby.length % n2 != 0) {
            throw new IllegalArgumentException("Byte array is not an encoded vector over the given finite field.");
        }
        this.length = arrby.length / n2;
        this.vector = new int[this.length];
        int n3 = 0;
        for (int i = 0; i < this.vector.length; ++i) {
            for (int j = 0; j < n; j += 8) {
                int[] arrn = this.vector;
                int n4 = arrn[i];
                int n5 = n3 + 1;
                arrn[i] = n4 | (255 & arrby[n3]) << j;
                n3 = n5;
            }
            if (gF2mField.isElementOfThisField(this.vector[i])) continue;
            throw new IllegalArgumentException("Byte array is not an encoded vector over the given finite field.");
        }
    }

    public GF2mVector(GF2mField gF2mField, int[] arrn) {
        this.field = gF2mField;
        this.length = arrn.length;
        for (int i = -1 + arrn.length; i >= 0; --i) {
            if (gF2mField.isElementOfThisField(arrn[i])) continue;
            throw new ArithmeticException("Element array is not specified over the given finite field.");
        }
        this.vector = IntUtils.clone(arrn);
    }

    public GF2mVector(GF2mVector gF2mVector) {
        this.field = new GF2mField(gF2mVector.field);
        this.length = gF2mVector.length;
        this.vector = IntUtils.clone(gF2mVector.vector);
    }

    @Override
    public Vector add(Vector vector) {
        throw new RuntimeException("not implemented");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean equals(Object object) {
        GF2mVector gF2mVector;
        block3 : {
            block2 : {
                if (!(object instanceof GF2mVector)) break block2;
                gF2mVector = (GF2mVector)object;
                if (this.field.equals(gF2mVector.field)) break block3;
            }
            return false;
        }
        return IntUtils.equals(this.vector, gF2mVector.vector);
    }

    @Override
    public byte[] getEncoded() {
        int n;
        int n2 = 1;
        for (n = 8; this.field.getDegree() > n; n += 8) {
            ++n2;
        }
        byte[] arrby = new byte[n2 * this.vector.length];
        int n3 = 0;
        for (int i = 0; i < this.vector.length; ++i) {
            for (int j = 0; j < n; j += 8) {
                int n4 = n3 + 1;
                arrby[n3] = (byte)(this.vector[i] >>> j);
                n3 = n4;
            }
        }
        return arrby;
    }

    public GF2mField getField() {
        return this.field;
    }

    public int[] getIntArrayForm() {
        return IntUtils.clone(this.vector);
    }

    @Override
    public int hashCode() {
        return 31 * this.field.hashCode() + this.vector.hashCode();
    }

    @Override
    public boolean isZero() {
        for (int i = -1 + this.vector.length; i >= 0; --i) {
            if (this.vector[i] == 0) continue;
            return false;
        }
        return true;
    }

    @Override
    public Vector multiply(Permutation permutation) {
        int[] arrn = permutation.getVector();
        if (this.length != arrn.length) {
            throw new ArithmeticException("permutation size and vector size mismatch");
        }
        int[] arrn2 = new int[this.length];
        for (int i = 0; i < arrn.length; ++i) {
            arrn2[i] = this.vector[arrn[i]];
        }
        return new GF2mVector(this.field, arrn2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int n = 0;
        while (n < this.vector.length) {
            for (int i = 0; i < this.field.getDegree(); ++i) {
                if ((1 << (i & 31) & this.vector[n]) != 0) {
                    stringBuffer.append('1');
                    continue;
                }
                stringBuffer.append('0');
            }
            stringBuffer.append(' ');
            ++n;
        }
        return stringBuffer.toString();
    }
}

