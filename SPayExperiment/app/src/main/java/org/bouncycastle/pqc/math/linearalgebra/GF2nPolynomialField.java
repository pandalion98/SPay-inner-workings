/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.util.Random
 *  java.util.Vector
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.util.Random;
import java.util.Vector;
import org.bouncycastle.pqc.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomial;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomialElement;
import org.bouncycastle.pqc.math.linearalgebra.GFElement;

public class GF2nPolynomialField
extends GF2nField {
    private boolean isPentanomial = false;
    private boolean isTrinomial = false;
    private int[] pc = new int[3];
    GF2Polynomial[] squaringMatrix;
    private int tc;

    public GF2nPolynomialField(int n) {
        if (n < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = n;
        this.computeFieldPolynomial();
        this.computeSquaringMatrix();
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    public GF2nPolynomialField(int n, GF2Polynomial gF2Polynomial) {
        if (n < 3) {
            throw new IllegalArgumentException("degree must be at least 3");
        }
        if (gF2Polynomial.getLength() != n + 1) {
            throw new RuntimeException();
        }
        if (!gF2Polynomial.isIrreducible()) {
            throw new RuntimeException();
        }
        this.mDegree = n;
        this.fieldPolynomial = gF2Polynomial;
        this.computeSquaringMatrix();
        int n2 = 2;
        for (int i = 1; i < -1 + this.fieldPolynomial.getLength(); ++i) {
            if (!this.fieldPolynomial.testBit(i)) continue;
            if (++n2 == 3) {
                this.tc = i;
            }
            if (n2 > 5) continue;
            this.pc[n2 - 3] = i;
        }
        if (n2 == 3) {
            this.isTrinomial = true;
        }
        if (n2 == 5) {
            this.isPentanomial = true;
        }
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    /*
     * Enabled aggressive block sorting
     */
    public GF2nPolynomialField(int n, boolean bl) {
        if (n < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = n;
        if (bl) {
            this.computeFieldPolynomial();
        } else {
            this.computeFieldPolynomial2();
        }
        this.computeSquaringMatrix();
        this.fields = new Vector();
        this.matrices = new Vector();
    }

    private void computeSquaringMatrix() {
        GF2Polynomial[] arrgF2Polynomial = new GF2Polynomial[-1 + this.mDegree];
        this.squaringMatrix = new GF2Polynomial[this.mDegree];
        int n = 0;
        do {
            int n2 = this.squaringMatrix.length;
            if (n >= n2) break;
            this.squaringMatrix[n] = new GF2Polynomial(this.mDegree, "ZERO");
            ++n;
        } while (true);
        for (int i = 0; i < -1 + this.mDegree; ++i) {
            arrgF2Polynomial[i] = new GF2Polynomial(1, "ONE").shiftLeft(i + this.mDegree).remainder(this.fieldPolynomial);
        }
        for (int i = 1; i <= Math.abs((int)(this.mDegree >> 1)); ++i) {
            for (int j = 1; j <= this.mDegree; ++j) {
                if (!arrgF2Polynomial[this.mDegree - (i << 1)].testBit(this.mDegree - j)) continue;
                this.squaringMatrix[j - 1].setBit(this.mDegree - i);
            }
        }
        for (int i = 1 + Math.abs((int)(this.mDegree >> 1)); i <= this.mDegree; ++i) {
            this.squaringMatrix[-1 + ((i << 1) - this.mDegree)].setBit(this.mDegree - i);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean testPentanomials() {
        this.fieldPolynomial = new GF2Polynomial(1 + this.mDegree);
        this.fieldPolynomial.setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        boolean bl = false;
        int n = 0;
        block0 : for (int i = 1; i <= -3 + this.mDegree && !bl; ++i) {
            this.fieldPolynomial.setBit(i);
            int n2 = i + 1;
            do {
                if (n2 <= -2 + this.mDegree && !bl) {
                    this.fieldPolynomial.setBit(n2);
                } else {
                    this.fieldPolynomial.resetBit(i);
                    continue block0;
                }
                for (int j = n2 + 1; j <= -1 + this.mDegree && !bl; ++j) {
                    this.fieldPolynomial.setBit(j);
                    boolean bl2 = (1 & this.mDegree) != 0;
                    boolean bl3 = (i & 1) != 0;
                    boolean bl4 = bl2 | bl3;
                    boolean bl5 = (n2 & 1) != 0;
                    boolean bl6 = bl4 | bl5;
                    boolean bl7 = (j & 1) != 0;
                    if (bl7 | bl6) {
                        bl = this.fieldPolynomial.isIrreducible();
                        ++n;
                        if (bl) {
                            this.isPentanomial = true;
                            this.pc[0] = i;
                            this.pc[1] = n2;
                            this.pc[2] = j;
                            return bl;
                        }
                    }
                    this.fieldPolynomial.resetBit(j);
                }
                this.fieldPolynomial.resetBit(n2);
                ++n2;
            } while (true);
        }
        return bl;
    }

    private boolean testRandom() {
        this.fieldPolynomial = new GF2Polynomial(1 + this.mDegree);
        int n = 0;
        do {
            ++n;
            this.fieldPolynomial.randomize();
            this.fieldPolynomial.setBit(this.mDegree);
            this.fieldPolynomial.setBit(0);
        } while (!this.fieldPolynomial.isIrreducible());
        return true;
    }

    private boolean testTrinomials() {
        boolean bl = false;
        this.fieldPolynomial = new GF2Polynomial(1 + this.mDegree);
        this.fieldPolynomial.setBit(0);
        this.fieldPolynomial.setBit(this.mDegree);
        int n = 0;
        int n2 = 1;
        do {
            block4 : {
                block3 : {
                    if (n2 >= this.mDegree || bl) break block3;
                    this.fieldPolynomial.setBit(n2);
                    bl = this.fieldPolynomial.isIrreducible();
                    ++n;
                    if (!bl) break block4;
                    this.isTrinomial = true;
                    this.tc = n2;
                }
                return bl;
            }
            this.fieldPolynomial.resetBit(n2);
            bl = this.fieldPolynomial.isIrreducible();
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void computeCOBMatrix(GF2nField gF2nField) {
        GF2nElement[] arrgF2nElement;
        GF2nElement gF2nElement;
        if (this.mDegree != gF2nField.mDegree) {
            throw new IllegalArgumentException("GF2nPolynomialField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
        }
        if (gF2nField instanceof GF2nONBField) {
            gF2nField.computeCOBMatrix(this);
            return;
        }
        GF2Polynomial[] arrgF2Polynomial = new GF2Polynomial[this.mDegree];
        for (int i = 0; i < this.mDegree; ++i) {
            arrgF2Polynomial[i] = new GF2Polynomial(this.mDegree);
        }
        while ((gF2nElement = gF2nField.getRandomRoot(this.fieldPolynomial)).isZero()) {
        }
        if (gF2nElement instanceof GF2nONBElement) {
            arrgF2nElement = new GF2nONBElement[this.mDegree];
            arrgF2nElement[-1 + this.mDegree] = GF2nONBElement.ONE((GF2nONBField)gF2nField);
        } else {
            arrgF2nElement = new GF2nPolynomialElement[this.mDegree];
            arrgF2nElement[-1 + this.mDegree] = GF2nPolynomialElement.ONE((GF2nPolynomialField)gF2nField);
        }
        arrgF2nElement[-2 + this.mDegree] = gF2nElement;
        for (int i = -3 + this.mDegree; i >= 0; --i) {
            arrgF2nElement[i] = (GF2nElement)arrgF2nElement[i + 1].multiply((GFElement)gF2nElement);
        }
        if (gF2nField instanceof GF2nONBField) {
            for (int i = 0; i < this.mDegree; ++i) {
                for (int j = 0; j < this.mDegree; ++j) {
                    if (!arrgF2nElement[i].testBit(-1 + (this.mDegree - j))) continue;
                    arrgF2Polynomial[-1 + (this.mDegree - j)].setBit(-1 + (this.mDegree - i));
                }
            }
        } else {
            for (int i = 0; i < this.mDegree; ++i) {
                for (int j = 0; j < this.mDegree; ++j) {
                    if (!arrgF2nElement[i].testBit(j)) continue;
                    arrgF2Polynomial[-1 + (this.mDegree - j)].setBit(-1 + (this.mDegree - i));
                }
            }
        }
        this.fields.addElement((Object)gF2nField);
        this.matrices.addElement((Object)arrgF2Polynomial);
        gF2nField.fields.addElement((Object)this);
        gF2nField.matrices.addElement((Object)this.invertMatrix(arrgF2Polynomial));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void computeFieldPolynomial() {
        if (this.testTrinomials() || this.testPentanomials()) {
            return;
        }
        this.testRandom();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void computeFieldPolynomial2() {
        if (this.testTrinomials() || this.testPentanomials()) {
            return;
        }
        this.testRandom();
    }

    public int[] getPc() {
        if (!this.isPentanomial) {
            throw new RuntimeException();
        }
        int[] arrn = new int[3];
        System.arraycopy((Object)this.pc, (int)0, (Object)arrn, (int)0, (int)3);
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected GF2nElement getRandomRoot(GF2Polynomial gF2Polynomial) {
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(gF2Polynomial, this);
        int n = gF2nPolynomial.getDegree();
        GF2nPolynomial gF2nPolynomial2 = gF2nPolynomial;
        while (n > 1) {
            int n2;
            GF2nPolynomial gF2nPolynomial3;
            int n3;
            do {
                GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this, new Random());
                GF2nPolynomial gF2nPolynomial4 = new GF2nPolynomial(2, GF2nPolynomialElement.ZERO(this));
                gF2nPolynomial4.set(1, gF2nPolynomialElement);
                GF2nPolynomial gF2nPolynomial5 = new GF2nPolynomial(gF2nPolynomial4);
                for (int i = 1; i <= -1 + this.mDegree; ++i) {
                    gF2nPolynomial5 = gF2nPolynomial5.multiplyAndReduce(gF2nPolynomial5, gF2nPolynomial2).add(gF2nPolynomial4);
                }
                gF2nPolynomial3 = gF2nPolynomial5.gcd(gF2nPolynomial2);
                n3 = gF2nPolynomial3.getDegree();
                n2 = gF2nPolynomial2.getDegree();
            } while (n3 == 0 || n3 == n2);
            GF2nPolynomial gF2nPolynomial6 = n3 << 1 > n2 ? gF2nPolynomial2.quotient(gF2nPolynomial3) : new GF2nPolynomial(gF2nPolynomial3);
            int n4 = gF2nPolynomial6.getDegree();
            gF2nPolynomial2 = gF2nPolynomial6;
            n = n4;
        }
        return gF2nPolynomial2.at(0);
    }

    public GF2Polynomial getSquaringVector(int n) {
        return new GF2Polynomial(this.squaringMatrix[n]);
    }

    public int getTc() {
        if (!this.isTrinomial) {
            throw new RuntimeException();
        }
        return this.tc;
    }

    public boolean isPentanomial() {
        return this.isPentanomial;
    }

    public boolean isTrinomial() {
        return this.isTrinomial;
    }
}

