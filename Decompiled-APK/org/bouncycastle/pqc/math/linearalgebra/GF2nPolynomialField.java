package org.bouncycastle.pqc.math.linearalgebra;

import java.util.Random;
import java.util.Vector;

public class GF2nPolynomialField extends GF2nField {
    private boolean isPentanomial;
    private boolean isTrinomial;
    private int[] pc;
    GF2Polynomial[] squaringMatrix;
    private int tc;

    public GF2nPolynomialField(int i) {
        this.isTrinomial = false;
        this.isPentanomial = false;
        this.pc = new int[3];
        if (i < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = i;
        computeFieldPolynomial();
        computeSquaringMatrix();
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    public GF2nPolynomialField(int i, GF2Polynomial gF2Polynomial) {
        this.isTrinomial = false;
        this.isPentanomial = false;
        this.pc = new int[3];
        if (i < 3) {
            throw new IllegalArgumentException("degree must be at least 3");
        } else if (gF2Polynomial.getLength() != i + 1) {
            throw new RuntimeException();
        } else if (gF2Polynomial.isIrreducible()) {
            this.mDegree = i;
            this.fieldPolynomial = gF2Polynomial;
            computeSquaringMatrix();
            int i2 = 2;
            for (int i3 = 1; i3 < this.fieldPolynomial.getLength() - 1; i3++) {
                if (this.fieldPolynomial.testBit(i3)) {
                    i2++;
                    if (i2 == 3) {
                        this.tc = i3;
                    }
                    if (i2 <= 5) {
                        this.pc[i2 - 3] = i3;
                    }
                }
            }
            if (i2 == 3) {
                this.isTrinomial = true;
            }
            if (i2 == 5) {
                this.isPentanomial = true;
            }
            this.fields = new Vector();
            this.matrices = new Vector();
        } else {
            throw new RuntimeException();
        }
    }

    public GF2nPolynomialField(int i, boolean z) {
        this.isTrinomial = false;
        this.isPentanomial = false;
        this.pc = new int[3];
        if (i < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = i;
        if (z) {
            computeFieldPolynomial();
        } else {
            computeFieldPolynomial2();
        }
        computeSquaringMatrix();
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    private void computeSquaringMatrix() {
        int i;
        int i2 = 0;
        GF2Polynomial[] gF2PolynomialArr = new GF2Polynomial[(this.mDegree - 1)];
        this.squaringMatrix = new GF2Polynomial[this.mDegree];
        for (i = 0; i < this.squaringMatrix.length; i++) {
            this.squaringMatrix[i] = new GF2Polynomial(this.mDegree, "ZERO");
        }
        while (i2 < this.mDegree - 1) {
            gF2PolynomialArr[i2] = new GF2Polynomial(1, "ONE").shiftLeft(this.mDegree + i2).remainder(this.fieldPolynomial);
            i2++;
        }
        for (i = 1; i <= Math.abs(this.mDegree >> 1); i++) {
            for (i2 = 1; i2 <= this.mDegree; i2++) {
                if (gF2PolynomialArr[this.mDegree - (i << 1)].testBit(this.mDegree - i2)) {
                    this.squaringMatrix[i2 - 1].setBit(this.mDegree - i);
                }
            }
        }
        for (i = Math.abs(this.mDegree >> 1) + 1; i <= this.mDegree; i++) {
            this.squaringMatrix[((i << 1) - this.mDegree) - 1].setBit(this.mDegree - i);
        }
    }

    private boolean testPentanomials() {
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        this.fieldPolynomial.setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        boolean z = false;
        int i = 0;
        loop0:
        for (int i2 = 1; i2 <= this.mDegree - 3 && !r0; i2++) {
            this.fieldPolynomial.setBit(i2);
            for (int i3 = i2 + 1; i3 <= this.mDegree - 2 && !r0; i3++) {
                this.fieldPolynomial.setBit(i3);
                for (int i4 = i3 + 1; i4 <= this.mDegree - 1 && !r0; i4++) {
                    this.fieldPolynomial.setBit(i4);
                    if ((((i4 & 1) != 0 ? 1 : 0) | ((((this.mDegree & 1) != 0 ? 1 : 0) | ((i2 & 1) != 0 ? 1 : 0)) | ((i3 & 1) != 0 ? 1 : 0))) != 0) {
                        z = this.fieldPolynomial.isIrreducible();
                        i++;
                        if (z) {
                            this.isPentanomial = true;
                            this.pc[0] = i2;
                            this.pc[1] = i3;
                            this.pc[2] = i4;
                            break loop0;
                        }
                    }
                    this.fieldPolynomial.resetBit(i4);
                }
                this.fieldPolynomial.resetBit(i3);
            }
            this.fieldPolynomial.resetBit(i2);
        }
        return z;
    }

    private boolean testRandom() {
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        int i = 0;
        do {
            i++;
            this.fieldPolynomial.randomize();
            this.fieldPolynomial.setBit(this.mDegree);
            this.fieldPolynomial.setBit(0);
        } while (!this.fieldPolynomial.isIrreducible());
        return true;
    }

    private boolean testTrinomials() {
        boolean z = false;
        this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1);
        this.fieldPolynomial.setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        int i = 0;
        for (int i2 = 1; i2 < this.mDegree && !r0; i2++) {
            this.fieldPolynomial.setBit(i2);
            z = this.fieldPolynomial.isIrreducible();
            i++;
            if (z) {
                this.isTrinomial = true;
                this.tc = i2;
                break;
            }
            this.fieldPolynomial.resetBit(i2);
            z = this.fieldPolynomial.isIrreducible();
        }
        return z;
    }

    protected void computeCOBMatrix(GF2nField gF2nField) {
        if (this.mDegree != gF2nField.mDegree) {
            throw new IllegalArgumentException("GF2nPolynomialField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
        } else if (gF2nField instanceof GF2nONBField) {
            gF2nField.computeCOBMatrix(this);
        } else {
            int i;
            GFElement randomRoot;
            GF2nONBElement[] gF2nONBElementArr;
            int i2;
            Object obj = new GF2Polynomial[this.mDegree];
            for (i = 0; i < this.mDegree; i++) {
                obj[i] = new GF2Polynomial(this.mDegree);
            }
            do {
                randomRoot = gF2nField.getRandomRoot(this.fieldPolynomial);
            } while (randomRoot.isZero());
            if (randomRoot instanceof GF2nONBElement) {
                gF2nONBElementArr = new GF2nONBElement[this.mDegree];
                gF2nONBElementArr[this.mDegree - 1] = GF2nONBElement.ONE((GF2nONBField) gF2nField);
            } else {
                gF2nONBElementArr = new GF2nPolynomialElement[this.mDegree];
                gF2nONBElementArr[this.mDegree - 1] = GF2nPolynomialElement.ONE((GF2nPolynomialField) gF2nField);
            }
            gF2nONBElementArr[this.mDegree - 2] = randomRoot;
            for (i2 = this.mDegree - 3; i2 >= 0; i2--) {
                gF2nONBElementArr[i2] = (GF2nElement) gF2nONBElementArr[i2 + 1].multiply(randomRoot);
            }
            if (gF2nField instanceof GF2nONBField) {
                for (i = 0; i < this.mDegree; i++) {
                    for (i2 = 0; i2 < this.mDegree; i2++) {
                        if (gF2nONBElementArr[i].testBit((this.mDegree - i2) - 1)) {
                            obj[(this.mDegree - i2) - 1].setBit((this.mDegree - i) - 1);
                        }
                    }
                }
            } else {
                for (i = 0; i < this.mDegree; i++) {
                    for (i2 = 0; i2 < this.mDegree; i2++) {
                        if (gF2nONBElementArr[i].testBit(i2)) {
                            obj[(this.mDegree - i2) - 1].setBit((this.mDegree - i) - 1);
                        }
                    }
                }
            }
            this.fields.addElement(gF2nField);
            this.matrices.addElement(obj);
            gF2nField.fields.addElement(this);
            gF2nField.matrices.addElement(invertMatrix(obj));
        }
    }

    protected void computeFieldPolynomial() {
        if (!testTrinomials() && !testPentanomials()) {
            testRandom();
        }
    }

    protected void computeFieldPolynomial2() {
        if (!testTrinomials() && !testPentanomials()) {
            testRandom();
        }
    }

    public int[] getPc() {
        if (this.isPentanomial) {
            Object obj = new int[3];
            System.arraycopy(this.pc, 0, obj, 0, 3);
            return obj;
        }
        throw new RuntimeException();
    }

    protected GF2nElement getRandomRoot(GF2Polynomial gF2Polynomial) {
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(gF2Polynomial, (GF2nField) this);
        int degree = gF2nPolynomial.getDegree();
        GF2nPolynomial gF2nPolynomial2 = gF2nPolynomial;
        while (degree > 1) {
            int degree2;
            while (true) {
                GF2nElement gF2nPolynomialElement = new GF2nPolynomialElement(this, new Random());
                GF2nPolynomial gF2nPolynomial3 = new GF2nPolynomial(2, GF2nPolynomialElement.ZERO(this));
                gF2nPolynomial3.set(1, gF2nPolynomialElement);
                gF2nPolynomial = new GF2nPolynomial(gF2nPolynomial3);
                for (degree = 1; degree <= this.mDegree - 1; degree++) {
                    gF2nPolynomial = gF2nPolynomial.multiplyAndReduce(gF2nPolynomial, gF2nPolynomial2).add(gF2nPolynomial3);
                }
                gF2nPolynomial = gF2nPolynomial.gcd(gF2nPolynomial2);
                degree = gF2nPolynomial.getDegree();
                degree2 = gF2nPolynomial2.getDegree();
                if (degree != 0 && degree != degree2) {
                    break;
                }
            }
            GF2nPolynomial quotient = (degree << 1) > degree2 ? gF2nPolynomial2.quotient(gF2nPolynomial) : new GF2nPolynomial(gF2nPolynomial);
            gF2nPolynomial2 = quotient;
            degree = quotient.getDegree();
        }
        return gF2nPolynomial2.at(0);
    }

    public GF2Polynomial getSquaringVector(int i) {
        return new GF2Polynomial(this.squaringMatrix[i]);
    }

    public int getTc() {
        if (this.isTrinomial) {
            return this.tc;
        }
        throw new RuntimeException();
    }

    public boolean isPentanomial() {
        return this.isPentanomial;
    }

    public boolean isTrinomial() {
        return this.isTrinomial;
    }
}
