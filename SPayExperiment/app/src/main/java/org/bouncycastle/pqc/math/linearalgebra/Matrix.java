/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.Vector;

public abstract class Matrix {
    public static final char MATRIX_TYPE_RANDOM_LT = 'L';
    public static final char MATRIX_TYPE_RANDOM_REGULAR = 'R';
    public static final char MATRIX_TYPE_RANDOM_UT = 'U';
    public static final char MATRIX_TYPE_UNIT = 'I';
    public static final char MATRIX_TYPE_ZERO = 'Z';
    protected int numColumns;
    protected int numRows;

    public abstract Matrix computeInverse();

    public abstract byte[] getEncoded();

    public int getNumColumns() {
        return this.numColumns;
    }

    public int getNumRows() {
        return this.numRows;
    }

    public abstract boolean isZero();

    public abstract Vector leftMultiply(Vector var1);

    public abstract Matrix rightMultiply(Matrix var1);

    public abstract Matrix rightMultiply(Permutation var1);

    public abstract Vector rightMultiply(Vector var1);

    public abstract String toString();
}

