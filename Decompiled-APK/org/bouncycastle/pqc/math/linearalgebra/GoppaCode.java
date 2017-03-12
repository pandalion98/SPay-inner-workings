package org.bouncycastle.pqc.math.linearalgebra;

import java.lang.reflect.Array;
import java.security.SecureRandom;

public final class GoppaCode {

    public static class MaMaPe {
        private GF2Matrix f472h;
        private Permutation f473p;
        private GF2Matrix f474s;

        public MaMaPe(GF2Matrix gF2Matrix, GF2Matrix gF2Matrix2, Permutation permutation) {
            this.f474s = gF2Matrix;
            this.f472h = gF2Matrix2;
            this.f473p = permutation;
        }

        public GF2Matrix getFirstMatrix() {
            return this.f474s;
        }

        public Permutation getPermutation() {
            return this.f473p;
        }

        public GF2Matrix getSecondMatrix() {
            return this.f472h;
        }
    }

    public static class MatrixSet {
        private GF2Matrix f475g;
        private int[] setJ;

        public MatrixSet(GF2Matrix gF2Matrix, int[] iArr) {
            this.f475g = gF2Matrix;
            this.setJ = iArr;
        }

        public GF2Matrix getG() {
            return this.f475g;
        }

        public int[] getSetJ() {
            return this.setJ;
        }
    }

    private GoppaCode() {
    }

    public static MaMaPe computeSystematicForm(GF2Matrix gF2Matrix, SecureRandom secureRandom) {
        int numColumns = gF2Matrix.getNumColumns();
        GF2Matrix gF2Matrix2 = null;
        while (true) {
            GF2Matrix gF2Matrix3;
            Object obj;
            Permutation permutation = new Permutation(numColumns, secureRandom);
            Matrix matrix = (GF2Matrix) gF2Matrix.rightMultiply(permutation);
            GF2Matrix leftSubMatrix = matrix.getLeftSubMatrix();
            try {
                gF2Matrix3 = (GF2Matrix) leftSubMatrix.computeInverse();
                obj = 1;
            } catch (ArithmeticException e) {
                gF2Matrix3 = gF2Matrix2;
                obj = null;
            }
            if (obj != null) {
                return new MaMaPe(leftSubMatrix, ((GF2Matrix) gF2Matrix3.rightMultiply(matrix)).getRightSubMatrix(), permutation);
            }
            gF2Matrix2 = gF2Matrix3;
        }
    }

    public static GF2Matrix createCanonicalCheckMatrix(GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int i;
        int i2;
        int i3;
        int degree = gF2mField.getDegree();
        int i4 = 1 << degree;
        int degree2 = polynomialGF2mSmallM.getDegree();
        int[][] iArr = (int[][]) Array.newInstance(Integer.TYPE, new int[]{degree2, i4});
        int[][] iArr2 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{degree2, i4});
        for (i = 0; i < i4; i++) {
            iArr2[0][i] = gF2mField.inverse(polynomialGF2mSmallM.evaluateAt(i));
        }
        for (i2 = 1; i2 < degree2; i2++) {
            for (i = 0; i < i4; i++) {
                iArr2[i2][i] = gF2mField.mult(iArr2[i2 - 1][i], i);
            }
        }
        for (i3 = 0; i3 < degree2; i3++) {
            for (i2 = 0; i2 < i4; i2++) {
                for (i = 0; i <= i3; i++) {
                    iArr[i3][i2] = gF2mField.add(iArr[i3][i2], gF2mField.mult(iArr2[i][i2], polynomialGF2mSmallM.getCoefficient((degree2 + i) - i3)));
                }
            }
        }
        iArr2 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{degree2 * degree, (i4 + 31) >>> 5});
        for (i3 = 0; i3 < i4; i3++) {
            int i5 = i3 >>> 5;
            int i6 = 1 << (i3 & 31);
            for (i2 = 0; i2 < degree2; i2++) {
                int i7 = iArr[i2][i3];
                for (i = 0; i < degree; i++) {
                    if (((i7 >>> i) & 1) != 0) {
                        int[] iArr3 = iArr2[(((i2 + 1) * degree) - i) - 1];
                        iArr3[i5] = iArr3[i5] ^ i6;
                    }
                }
            }
        }
        return new GF2Matrix(i4, iArr2);
    }

    public static GF2Vector syndromeDecode(GF2Vector gF2Vector, GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM, PolynomialGF2mSmallM[] polynomialGF2mSmallMArr) {
        int i = 0;
        int degree = 1 << gF2mField.getDegree();
        GF2Vector gF2Vector2 = new GF2Vector(degree);
        if (!gF2Vector.isZero()) {
            PolynomialGF2mSmallM[] modPolynomialToFracton = new PolynomialGF2mSmallM(gF2Vector.toExtensionFieldVector(gF2mField)).modInverse(polynomialGF2mSmallM).addMonomial(1).modSquareRootMatrix(polynomialGF2mSmallMArr).modPolynomialToFracton(polynomialGF2mSmallM);
            PolynomialGF2mSmallM add = modPolynomialToFracton[0].multiply(modPolynomialToFracton[0]).add(modPolynomialToFracton[1].multiply(modPolynomialToFracton[1]).multWithMonomial(1));
            add = add.multWithElement(gF2mField.inverse(add.getHeadCoefficient()));
            while (i < degree) {
                if (add.evaluateAt(i) == 0) {
                    gF2Vector2.setBit(i);
                }
                i++;
            }
        }
        return gF2Vector2;
    }
}
