/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.reflect.Array
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.GF2mVector;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public final class GoppaCode {
    private GoppaCode() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static MaMaPe computeSystematicForm(GF2Matrix gF2Matrix, SecureRandom secureRandom) {
        int n = gF2Matrix.getNumColumns();
        GF2Matrix gF2Matrix2 = null;
        do {
            boolean bl;
            GF2Matrix gF2Matrix3;
            Permutation permutation = new Permutation(n, secureRandom);
            GF2Matrix gF2Matrix4 = (GF2Matrix)gF2Matrix.rightMultiply(permutation);
            GF2Matrix gF2Matrix5 = gF2Matrix4.getLeftSubMatrix();
            try {
                gF2Matrix3 = (GF2Matrix)gF2Matrix5.computeInverse();
                bl = true;
            }
            catch (ArithmeticException arithmeticException) {
                gF2Matrix3 = gF2Matrix2;
                bl = false;
            }
            if (bl) {
                return new MaMaPe(gF2Matrix5, ((GF2Matrix)gF2Matrix3.rightMultiply(gF2Matrix4)).getRightSubMatrix(), permutation);
            }
            gF2Matrix2 = gF2Matrix3;
        } while (true);
    }

    public static GF2Matrix createCanonicalCheckMatrix(GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int n = gF2mField.getDegree();
        int n2 = 1 << n;
        int n3 = polynomialGF2mSmallM.getDegree();
        int[] arrn = new int[]{n3, n2};
        int[][] arrn2 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int[] arrn3 = new int[]{n3, n2};
        int[][] arrn4 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn3);
        for (int i = 0; i < n2; ++i) {
            arrn4[0][i] = gF2mField.inverse(polynomialGF2mSmallM.evaluateAt(i));
        }
        for (int i = 1; i < n3; ++i) {
            for (int j = 0; j < n2; ++j) {
                arrn4[i][j] = gF2mField.mult(arrn4[i - 1][j], j);
            }
        }
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < n2; ++j) {
                for (int k = 0; k <= i; ++k) {
                    arrn2[i][j] = gF2mField.add(arrn2[i][j], gF2mField.mult(arrn4[k][j], polynomialGF2mSmallM.getCoefficient(n3 + k - i)));
                }
            }
        }
        int[] arrn5 = new int[]{n3 * n, n2 + 31 >>> 5};
        int[][] arrn6 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn5);
        for (int i = 0; i < n2; ++i) {
            int n4 = i >>> 5;
            int n5 = 1 << (i & 31);
            for (int j = 0; j < n3; ++j) {
                int n6 = arrn2[j][i];
                for (int k = 0; k < n; ++k) {
                    if ((1 & n6 >>> k) == 0) continue;
                    int[] arrn7 = arrn6[-1 + (n * (j + 1) - k)];
                    arrn7[n4] = n5 ^ arrn7[n4];
                }
            }
        }
        return new GF2Matrix(n2, arrn6);
    }

    public static GF2Vector syndromeDecode(GF2Vector gF2Vector, GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM, PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM) {
        int n = 1 << gF2mField.getDegree();
        GF2Vector gF2Vector2 = new GF2Vector(n);
        if (!gF2Vector.isZero()) {
            PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM2 = new PolynomialGF2mSmallM(gF2Vector.toExtensionFieldVector(gF2mField)).modInverse(polynomialGF2mSmallM).addMonomial(1).modSquareRootMatrix(arrpolynomialGF2mSmallM).modPolynomialToFracton(polynomialGF2mSmallM);
            PolynomialGF2mSmallM polynomialGF2mSmallM2 = arrpolynomialGF2mSmallM2[0].multiply(arrpolynomialGF2mSmallM2[0]).add(arrpolynomialGF2mSmallM2[1].multiply(arrpolynomialGF2mSmallM2[1]).multWithMonomial(1));
            PolynomialGF2mSmallM polynomialGF2mSmallM3 = polynomialGF2mSmallM2.multWithElement(gF2mField.inverse(polynomialGF2mSmallM2.getHeadCoefficient()));
            for (int i = 0; i < n; ++i) {
                if (polynomialGF2mSmallM3.evaluateAt(i) != 0) continue;
                gF2Vector2.setBit(i);
            }
        }
        return gF2Vector2;
    }

    public static class MaMaPe {
        private GF2Matrix h;
        private Permutation p;
        private GF2Matrix s;

        public MaMaPe(GF2Matrix gF2Matrix, GF2Matrix gF2Matrix2, Permutation permutation) {
            this.s = gF2Matrix;
            this.h = gF2Matrix2;
            this.p = permutation;
        }

        public GF2Matrix getFirstMatrix() {
            return this.s;
        }

        public Permutation getPermutation() {
            return this.p;
        }

        public GF2Matrix getSecondMatrix() {
            return this.h;
        }
    }

    public static class MatrixSet {
        private GF2Matrix g;
        private int[] setJ;

        public MatrixSet(GF2Matrix gF2Matrix, int[] arrn) {
            this.g = gF2Matrix;
            this.setJ = arrn;
        }

        public GF2Matrix getG() {
            return this.g;
        }

        public int[] getSetJ() {
            return this.setJ;
        }
    }

}

