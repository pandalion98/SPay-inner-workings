/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Random
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.pqc.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomialField;
import org.bouncycastle.pqc.math.linearalgebra.GFElement;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

public class GF2nPolynomialElement
extends GF2nElement {
    private static final int[] bitMask = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE, 0};
    private GF2Polynomial polynomial;

    public GF2nPolynomialElement(GF2nPolynomialElement gF2nPolynomialElement) {
        this.mField = gF2nPolynomialElement.mField;
        this.mDegree = gF2nPolynomialElement.mDegree;
        this.polynomial = new GF2Polynomial(gF2nPolynomialElement.polynomial);
    }

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, Random random) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(this.mDegree);
        this.randomize(random);
    }

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, GF2Polynomial gF2Polynomial) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(gF2Polynomial);
        this.polynomial.expandN(this.mDegree);
    }

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, byte[] arrby) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(this.mDegree, arrby);
        this.polynomial.expandN(this.mDegree);
    }

    public GF2nPolynomialElement(GF2nPolynomialField gF2nPolynomialField, int[] arrn) {
        this.mField = gF2nPolynomialField;
        this.mDegree = this.mField.getDegree();
        this.polynomial = new GF2Polynomial(this.mDegree, arrn);
        this.polynomial.expandN(gF2nPolynomialField.mDegree);
    }

    public static GF2nPolynomialElement ONE(GF2nPolynomialField gF2nPolynomialField) {
        return new GF2nPolynomialElement(gF2nPolynomialField, new GF2Polynomial(gF2nPolynomialField.getDegree(), new int[]{1}));
    }

    public static GF2nPolynomialElement ZERO(GF2nPolynomialField gF2nPolynomialField) {
        return new GF2nPolynomialElement(gF2nPolynomialField, new GF2Polynomial(gF2nPolynomialField.getDegree()));
    }

    private GF2Polynomial getGF2Polynomial() {
        return new GF2Polynomial(this.polynomial);
    }

    private GF2nPolynomialElement halfTrace() {
        if ((1 & this.mDegree) == 0) {
            throw new RuntimeException();
        }
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        for (int i = 1; i <= -1 + this.mDegree >> 1; ++i) {
            gF2nPolynomialElement.squareThis();
            gF2nPolynomialElement.squareThis();
            gF2nPolynomialElement.addToThis(this);
        }
        return gF2nPolynomialElement;
    }

    private void randomize(Random random) {
        this.polynomial.expandN(this.mDegree);
        this.polynomial.randomize(random);
    }

    private void reducePentanomialBitwise(int[] arrn) {
        int n = this.mDegree - arrn[2];
        int n2 = this.mDegree - arrn[1];
        int n3 = this.mDegree - arrn[0];
        for (int i = -1 + this.polynomial.getLength(); i >= this.mDegree; --i) {
            if (!this.polynomial.testBit(i)) continue;
            this.polynomial.xorBit(i);
            this.polynomial.xorBit(i - n);
            this.polynomial.xorBit(i - n2);
            this.polynomial.xorBit(i - n3);
            this.polynomial.xorBit(i - this.mDegree);
        }
        this.polynomial.reduceN();
        this.polynomial.expandN(this.mDegree);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void reduceThis() {
        if (this.polynomial.getLength() > this.mDegree) {
            int[] arrn;
            if (((GF2nPolynomialField)this.mField).isTrinomial()) {
                int n;
                try {
                    n = ((GF2nPolynomialField)this.mField).getTc();
                }
                catch (RuntimeException runtimeException) {
                    throw new RuntimeException("GF2nPolynomialElement.reduce: the field polynomial is not a trinomial");
                }
                if (this.mDegree - n > 32 && this.polynomial.getLength() <= this.mDegree << 1) {
                    this.polynomial.reduceTrinomial(this.mDegree, n);
                    return;
                }
                this.reduceTrinomialBitwise(n);
                return;
            }
            if (!((GF2nPolynomialField)this.mField).isPentanomial()) {
                this.polynomial = this.polynomial.remainder(this.mField.getFieldPolynomial());
                this.polynomial.expandN(this.mDegree);
                return;
            }
            try {
                arrn = ((GF2nPolynomialField)this.mField).getPc();
            }
            catch (RuntimeException runtimeException) {
                throw new RuntimeException("GF2nPolynomialElement.reduce: the field polynomial is not a pentanomial");
            }
            if (this.mDegree - arrn[2] > 32 && this.polynomial.getLength() <= this.mDegree << 1) {
                this.polynomial.reducePentanomial(this.mDegree, arrn);
                return;
            }
            this.reducePentanomialBitwise(arrn);
            return;
        }
        if (this.polynomial.getLength() >= this.mDegree) return;
        {
            this.polynomial.expandN(this.mDegree);
            return;
        }
    }

    private void reduceTrinomialBitwise(int n) {
        int n2 = this.mDegree - n;
        for (int i = -1 + this.polynomial.getLength(); i >= this.mDegree; --i) {
            if (!this.polynomial.testBit(i)) continue;
            this.polynomial.xorBit(i);
            this.polynomial.xorBit(i - n2);
            this.polynomial.xorBit(i - this.mDegree);
        }
        this.polynomial.reduceN();
        this.polynomial.expandN(this.mDegree);
    }

    @Override
    public GFElement add(GFElement gFElement) {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.addToThis(gFElement);
        return gF2nPolynomialElement;
    }

    @Override
    public void addToThis(GFElement gFElement) {
        if (!(gFElement instanceof GF2nPolynomialElement)) {
            throw new RuntimeException();
        }
        if (!this.mField.equals(((GF2nPolynomialElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        this.polynomial.addToThis(((GF2nPolynomialElement)gFElement).polynomial);
    }

    @Override
    void assignOne() {
        this.polynomial.assignOne();
    }

    @Override
    void assignZero() {
        this.polynomial.assignZero();
    }

    @Override
    public Object clone() {
        return new GF2nPolynomialElement(this);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean equals(Object object) {
        GF2nPolynomialElement gF2nPolynomialElement;
        block3 : {
            block2 : {
                if (object == null || !(object instanceof GF2nPolynomialElement)) break block2;
                gF2nPolynomialElement = (GF2nPolynomialElement)object;
                if (this.mField == gF2nPolynomialElement.mField || this.mField.getFieldPolynomial().equals(gF2nPolynomialElement.mField.getFieldPolynomial())) break block3;
            }
            return false;
        }
        return this.polynomial.equals(gF2nPolynomialElement.polynomial);
    }

    @Override
    public int hashCode() {
        return this.mField.hashCode() + this.polynomial.hashCode();
    }

    @Override
    public GF2nElement increase() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.increaseThis();
        return gF2nPolynomialElement;
    }

    @Override
    public void increaseThis() {
        this.polynomial.increaseThis();
    }

    @Override
    public GFElement invert() {
        return this.invertMAIA();
    }

    public GF2nPolynomialElement invertEEA() {
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        GF2Polynomial gF2Polynomial = new GF2Polynomial(32 + this.mDegree, "ONE");
        gF2Polynomial.reduceN();
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(32 + this.mDegree);
        gF2Polynomial2.reduceN();
        GF2Polynomial gF2Polynomial3 = this.getGF2Polynomial();
        GF2Polynomial gF2Polynomial4 = this.mField.getFieldPolynomial();
        gF2Polynomial3.reduceN();
        while (!gF2Polynomial3.isOne()) {
            gF2Polynomial3.reduceN();
            gF2Polynomial4.reduceN();
            int n = gF2Polynomial3.getLength() - gF2Polynomial4.getLength();
            if (n < 0) {
                n = -n;
                gF2Polynomial.reduceN();
                GF2Polynomial gF2Polynomial5 = gF2Polynomial3;
                gF2Polynomial3 = gF2Polynomial4;
                gF2Polynomial4 = gF2Polynomial5;
                GF2Polynomial gF2Polynomial6 = gF2Polynomial;
                gF2Polynomial = gF2Polynomial2;
                gF2Polynomial2 = gF2Polynomial6;
            }
            gF2Polynomial3.shiftLeftAddThis(gF2Polynomial4, n);
            gF2Polynomial.shiftLeftAddThis(gF2Polynomial2, n);
        }
        gF2Polynomial.reduceN();
        return new GF2nPolynomialElement((GF2nPolynomialField)this.mField, gF2Polynomial);
    }

    public GF2nPolynomialElement invertMAIA() {
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree, "ONE");
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.mDegree);
        GF2Polynomial gF2Polynomial3 = this.getGF2Polynomial();
        GF2Polynomial gF2Polynomial4 = this.mField.getFieldPolynomial();
        do {
            if (!gF2Polynomial3.testBit(0)) {
                gF2Polynomial3.shiftRightThis();
                if (!gF2Polynomial.testBit(0)) {
                    gF2Polynomial.shiftRightThis();
                    continue;
                }
                gF2Polynomial.addToThis(this.mField.getFieldPolynomial());
                gF2Polynomial.shiftRightThis();
                continue;
            }
            if (gF2Polynomial3.isOne()) {
                return new GF2nPolynomialElement((GF2nPolynomialField)this.mField, gF2Polynomial);
            }
            gF2Polynomial3.reduceN();
            gF2Polynomial4.reduceN();
            if (gF2Polynomial3.getLength() < gF2Polynomial4.getLength()) {
                GF2Polynomial gF2Polynomial5 = gF2Polynomial3;
                gF2Polynomial3 = gF2Polynomial4;
                gF2Polynomial4 = gF2Polynomial5;
                GF2Polynomial gF2Polynomial6 = gF2Polynomial;
                gF2Polynomial = gF2Polynomial2;
                gF2Polynomial2 = gF2Polynomial6;
            }
            gF2Polynomial3.addToThis(gF2Polynomial4);
            gF2Polynomial.addToThis(gF2Polynomial2);
        } while (true);
    }

    public GF2nPolynomialElement invertSquare() {
        if (this.isZero()) {
            throw new ArithmeticException();
        }
        int n = -1 + this.mField.getDegree();
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.polynomial.expandN(32 + (this.mDegree << 1));
        gF2nPolynomialElement.polynomial.reduceN();
        int n2 = -1 + IntegerFunctions.floorLog(n);
        int n3 = 1;
        for (int i = n2; i >= 0; --i) {
            GF2nPolynomialElement gF2nPolynomialElement2 = new GF2nPolynomialElement(gF2nPolynomialElement);
            for (int j = 1; j <= n3; ++j) {
                gF2nPolynomialElement2.squareThisPreCalc();
            }
            gF2nPolynomialElement.multiplyThisBy(gF2nPolynomialElement2);
            int n4 = n3 << 1;
            if ((n & bitMask[i]) != 0) {
                gF2nPolynomialElement.squareThisPreCalc();
                gF2nPolynomialElement.multiplyThisBy(this);
            }
            n3 = ++n4;
        }
        gF2nPolynomialElement.squareThisPreCalc();
        return gF2nPolynomialElement;
    }

    @Override
    public boolean isOne() {
        return this.polynomial.isOne();
    }

    @Override
    public boolean isZero() {
        return this.polynomial.isZero();
    }

    @Override
    public GFElement multiply(GFElement gFElement) {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.multiplyThisBy(gFElement);
        return gF2nPolynomialElement;
    }

    @Override
    public void multiplyThisBy(GFElement gFElement) {
        if (!(gFElement instanceof GF2nPolynomialElement)) {
            throw new RuntimeException();
        }
        if (!this.mField.equals(((GF2nPolynomialElement)gFElement).mField)) {
            throw new RuntimeException();
        }
        if (this.equals(gFElement)) {
            this.squareThis();
            return;
        }
        this.polynomial = this.polynomial.multiply(((GF2nPolynomialElement)gFElement).polynomial);
        this.reduceThis();
    }

    public GF2nPolynomialElement power(int n) {
        if (n == 1) {
            return new GF2nPolynomialElement(this);
        }
        GF2nPolynomialElement gF2nPolynomialElement = GF2nPolynomialElement.ONE((GF2nPolynomialField)this.mField);
        if (n == 0) {
            return gF2nPolynomialElement;
        }
        GF2nPolynomialElement gF2nPolynomialElement2 = new GF2nPolynomialElement(this);
        gF2nPolynomialElement2.polynomial.expandN(32 + (gF2nPolynomialElement2.mDegree << 1));
        gF2nPolynomialElement2.polynomial.reduceN();
        for (int i = 0; i < this.mDegree; ++i) {
            if ((n & 1 << i) != 0) {
                gF2nPolynomialElement.multiplyThisBy(gF2nPolynomialElement2);
            }
            gF2nPolynomialElement2.square();
        }
        return gF2nPolynomialElement;
    }

    @Override
    public GF2nElement solveQuadraticEquation() {
        GF2nPolynomialElement gF2nPolynomialElement;
        GF2nPolynomialElement gF2nPolynomialElement2;
        if (this.isZero()) {
            return GF2nPolynomialElement.ZERO((GF2nPolynomialField)this.mField);
        }
        if ((1 & this.mDegree) == 1) {
            return this.halfTrace();
        }
        do {
            GF2nPolynomialElement gF2nPolynomialElement3 = new GF2nPolynomialElement((GF2nPolynomialField)this.mField, new Random());
            gF2nPolynomialElement = GF2nPolynomialElement.ZERO((GF2nPolynomialField)this.mField);
            gF2nPolynomialElement2 = (GF2nPolynomialElement)gF2nPolynomialElement3.clone();
            for (int i = 1; i < this.mDegree; ++i) {
                gF2nPolynomialElement.squareThis();
                gF2nPolynomialElement2.squareThis();
                gF2nPolynomialElement.addToThis(gF2nPolynomialElement2.multiply(this));
                gF2nPolynomialElement2.addToThis(gF2nPolynomialElement3);
            }
        } while (gF2nPolynomialElement2.isZero());
        if (!this.equals(gF2nPolynomialElement.square().add((GFElement)gF2nPolynomialElement))) {
            throw new RuntimeException();
        }
        return gF2nPolynomialElement;
    }

    @Override
    public GF2nElement square() {
        return this.squarePreCalc();
    }

    public GF2nPolynomialElement squareBitwise() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareThisBitwise();
        gF2nPolynomialElement.reduceThis();
        return gF2nPolynomialElement;
    }

    public GF2nPolynomialElement squareMatrix() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareThisMatrix();
        gF2nPolynomialElement.reduceThis();
        return gF2nPolynomialElement;
    }

    public GF2nPolynomialElement squarePreCalc() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareThisPreCalc();
        gF2nPolynomialElement.reduceThis();
        return gF2nPolynomialElement;
    }

    @Override
    public GF2nElement squareRoot() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        gF2nPolynomialElement.squareRootThis();
        return gF2nPolynomialElement;
    }

    @Override
    public void squareRootThis() {
        this.polynomial.expandN(32 + (this.mDegree << 1));
        this.polynomial.reduceN();
        for (int i = 0; i < -1 + this.mField.getDegree(); ++i) {
            this.squareThis();
        }
    }

    @Override
    public void squareThis() {
        this.squareThisPreCalc();
    }

    public void squareThisBitwise() {
        this.polynomial.squareThisBitwise();
        this.reduceThis();
    }

    public void squareThisMatrix() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree);
        for (int i = 0; i < this.mDegree; ++i) {
            if (!this.polynomial.vectorMult(((GF2nPolynomialField)this.mField).squaringMatrix[-1 + (this.mDegree - i)])) continue;
            gF2Polynomial.setBit(i);
        }
        this.polynomial = gF2Polynomial;
    }

    public void squareThisPreCalc() {
        this.polynomial.squareThisPreCalc();
        this.reduceThis();
    }

    @Override
    boolean testBit(int n) {
        return this.polynomial.testBit(n);
    }

    @Override
    public boolean testRightmostBit() {
        return this.polynomial.testBit(0);
    }

    @Override
    public byte[] toByteArray() {
        return this.polynomial.toByteArray();
    }

    @Override
    public BigInteger toFlexiBigInt() {
        return this.polynomial.toFlexiBigInt();
    }

    @Override
    public String toString() {
        return this.polynomial.toString(16);
    }

    @Override
    public String toString(int n) {
        return this.polynomial.toString(n);
    }

    @Override
    public int trace() {
        GF2nPolynomialElement gF2nPolynomialElement = new GF2nPolynomialElement(this);
        for (int i = 1; i < this.mDegree; ++i) {
            gF2nPolynomialElement.squareThis();
            gF2nPolynomialElement.addToThis(this);
        }
        return gF2nPolynomialElement.isOne();
    }
}

