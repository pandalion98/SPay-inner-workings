/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;

public class PolynomialRingGF2m {
    private GF2mField field;
    private PolynomialGF2mSmallM p;
    protected PolynomialGF2mSmallM[] sqMatrix;
    protected PolynomialGF2mSmallM[] sqRootMatrix;

    public PolynomialRingGF2m(GF2mField gF2mField, PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.field = gF2mField;
        this.p = polynomialGF2mSmallM;
        this.computeSquaringMatrix();
        this.computeSquareRootMatrix();
    }

    private void computeSquareRootMatrix() {
        int n = this.p.getDegree();
        PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM = new PolynomialGF2mSmallM[n];
        for (int i = n - 1; i >= 0; --i) {
            arrpolynomialGF2mSmallM[i] = new PolynomialGF2mSmallM(this.sqMatrix[i]);
        }
        this.sqRootMatrix = new PolynomialGF2mSmallM[n];
        for (int i = n - 1; i >= 0; --i) {
            this.sqRootMatrix[i] = new PolynomialGF2mSmallM(this.field, i);
        }
        for (int i = 0; i < n; ++i) {
            if (arrpolynomialGF2mSmallM[i].getCoefficient(i) == 0) {
                boolean bl = false;
                for (int j = i + 1; j < n; ++j) {
                    if (arrpolynomialGF2mSmallM[j].getCoefficient(i) == 0) continue;
                    bl = true;
                    PolynomialRingGF2m.swapColumns(arrpolynomialGF2mSmallM, i, j);
                    PolynomialRingGF2m.swapColumns(this.sqRootMatrix, i, j);
                    j = n;
                }
                if (!bl) {
                    throw new ArithmeticException("Squaring matrix is not invertible.");
                }
            }
            int n2 = arrpolynomialGF2mSmallM[i].getCoefficient(i);
            int n3 = this.field.inverse(n2);
            arrpolynomialGF2mSmallM[i].multThisWithElement(n3);
            this.sqRootMatrix[i].multThisWithElement(n3);
            for (int j = 0; j < n; ++j) {
                int n4;
                if (j == i || (n4 = arrpolynomialGF2mSmallM[j].getCoefficient(i)) == 0) continue;
                PolynomialGF2mSmallM polynomialGF2mSmallM = arrpolynomialGF2mSmallM[i].multWithElement(n4);
                PolynomialGF2mSmallM polynomialGF2mSmallM2 = this.sqRootMatrix[i].multWithElement(n4);
                arrpolynomialGF2mSmallM[j].addToThis(polynomialGF2mSmallM);
                this.sqRootMatrix[j].addToThis(polynomialGF2mSmallM2);
            }
        }
    }

    private void computeSquaringMatrix() {
        int n = this.p.getDegree();
        this.sqMatrix = new PolynomialGF2mSmallM[n];
        for (int i = 0; i < n >> 1; ++i) {
            int[] arrn = new int[1 + (i << 1)];
            arrn[i << 1] = 1;
            this.sqMatrix[i] = new PolynomialGF2mSmallM(this.field, arrn);
        }
        for (int i = n >> 1; i < n; ++i) {
            int[] arrn = new int[1 + (i << 1)];
            arrn[i << 1] = 1;
            PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(this.field, arrn);
            this.sqMatrix[i] = polynomialGF2mSmallM.mod(this.p);
        }
    }

    private static void swapColumns(PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM, int n, int n2) {
        PolynomialGF2mSmallM polynomialGF2mSmallM = arrpolynomialGF2mSmallM[n];
        arrpolynomialGF2mSmallM[n] = arrpolynomialGF2mSmallM[n2];
        arrpolynomialGF2mSmallM[n2] = polynomialGF2mSmallM;
    }

    public PolynomialGF2mSmallM[] getSquareRootMatrix() {
        return this.sqRootMatrix;
    }

    public PolynomialGF2mSmallM[] getSquaringMatrix() {
        return this.sqMatrix;
    }
}

