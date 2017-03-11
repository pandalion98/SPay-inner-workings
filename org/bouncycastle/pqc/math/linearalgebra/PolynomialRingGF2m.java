package org.bouncycastle.pqc.math.linearalgebra;

public class PolynomialRingGF2m {
    private GF2mField field;
    private PolynomialGF2mSmallM f476p;
    protected PolynomialGF2mSmallM[] sqMatrix;
    protected PolynomialGF2mSmallM[] sqRootMatrix;

    public PolynomialRingGF2m(GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.field = gF2mField;
        this.f476p = polynomialGF2mSmallM;
        computeSquaringMatrix();
        computeSquareRootMatrix();
    }

    private void computeSquareRootMatrix() {
        int i;
        int degree = this.f476p.getDegree();
        PolynomialGF2mSmallM[] polynomialGF2mSmallMArr = new PolynomialGF2mSmallM[degree];
        for (i = degree - 1; i >= 0; i--) {
            polynomialGF2mSmallMArr[i] = new PolynomialGF2mSmallM(this.sqMatrix[i]);
        }
        this.sqRootMatrix = new PolynomialGF2mSmallM[degree];
        for (i = degree - 1; i >= 0; i--) {
            this.sqRootMatrix[i] = new PolynomialGF2mSmallM(this.field, i);
        }
        for (int i2 = 0; i2 < degree; i2++) {
            if (polynomialGF2mSmallMArr[i2].getCoefficient(i2) == 0) {
                i = i2 + 1;
                Object obj = null;
                while (i < degree) {
                    if (polynomialGF2mSmallMArr[i].getCoefficient(i2) != 0) {
                        obj = 1;
                        swapColumns(polynomialGF2mSmallMArr, i2, i);
                        swapColumns(this.sqRootMatrix, i2, i);
                        i = degree;
                    }
                    i++;
                }
                if (obj == null) {
                    throw new ArithmeticException("Squaring matrix is not invertible.");
                }
            }
            i = this.field.inverse(polynomialGF2mSmallMArr[i2].getCoefficient(i2));
            polynomialGF2mSmallMArr[i2].multThisWithElement(i);
            this.sqRootMatrix[i2].multThisWithElement(i);
            for (i = 0; i < degree; i++) {
                if (i != i2) {
                    int coefficient = polynomialGF2mSmallMArr[i].getCoefficient(i2);
                    if (coefficient != 0) {
                        PolynomialGF2mSmallM multWithElement = polynomialGF2mSmallMArr[i2].multWithElement(coefficient);
                        PolynomialGF2mSmallM multWithElement2 = this.sqRootMatrix[i2].multWithElement(coefficient);
                        polynomialGF2mSmallMArr[i].addToThis(multWithElement);
                        this.sqRootMatrix[i].addToThis(multWithElement2);
                    }
                }
            }
        }
    }

    private void computeSquaringMatrix() {
        int i;
        int degree = this.f476p.getDegree();
        this.sqMatrix = new PolynomialGF2mSmallM[degree];
        for (i = 0; i < (degree >> 1); i++) {
            int[] iArr = new int[((i << 1) + 1)];
            iArr[i << 1] = 1;
            this.sqMatrix[i] = new PolynomialGF2mSmallM(this.field, iArr);
        }
        for (i = degree >> 1; i < degree; i++) {
            iArr = new int[((i << 1) + 1)];
            iArr[i << 1] = 1;
            this.sqMatrix[i] = new PolynomialGF2mSmallM(this.field, iArr).mod(this.f476p);
        }
    }

    private static void swapColumns(PolynomialGF2mSmallM[] polynomialGF2mSmallMArr, int i, int i2) {
        PolynomialGF2mSmallM polynomialGF2mSmallM = polynomialGF2mSmallMArr[i];
        polynomialGF2mSmallMArr[i] = polynomialGF2mSmallMArr[i2];
        polynomialGF2mSmallMArr[i2] = polynomialGF2mSmallM;
    }

    public PolynomialGF2mSmallM[] getSquareRootMatrix() {
        return this.sqRootMatrix;
    }

    public PolynomialGF2mSmallM[] getSquaringMatrix() {
        return this.sqMatrix;
    }
}
