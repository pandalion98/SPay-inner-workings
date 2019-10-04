/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.reflect.Array
 *  java.util.Random
 *  java.util.Vector
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Vector;
import org.bouncycastle.pqc.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomial;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomialElement;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

public class GF2nONBField
extends GF2nField {
    private static final int MAXLONG = 64;
    private int mBit;
    private int mLength;
    int[][] mMult;
    private int mType;

    /*
     * Enabled aggressive block sorting
     */
    public GF2nONBField(int n) {
        if (n < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = n;
        this.mLength = this.mDegree / 64;
        this.mBit = 63 & this.mDegree;
        if (this.mBit == 0) {
            this.mBit = 64;
        } else {
            this.mLength = 1 + this.mLength;
        }
        this.computeType();
        if (this.mType >= 3) {
            throw new RuntimeException("\nThe type of this field is " + this.mType);
        }
        int[] arrn = new int[]{this.mDegree, 2};
        this.mMult = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n2 = 0;
        do {
            if (n2 >= this.mDegree) {
                this.computeMultMatrix();
                this.computeFieldPolynomial();
                this.fields = new Vector();
                this.matrices = new Vector();
                return;
            }
            this.mMult[n2][0] = -1;
            this.mMult[n2][1] = -1;
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void computeMultMatrix() {
        if ((7 & this.mType) == 0) {
            throw new RuntimeException("bisher nur fuer Gausssche Normalbasen implementiert");
        }
        int n = 1 + this.mType * this.mDegree;
        int[] arrn = new int[n];
        int n2 = this.mType == 1 ? 1 : (this.mType == 2 ? n - 1 : this.elementOfOrder(this.mType, n));
        int n3 = 1;
        for (int i = 0; i < this.mType; ++i) {
            int n4 = n3;
            for (int j = 0; j < this.mDegree; ++j) {
                arrn[n4] = j;
                if ((n4 = (n4 << 1) % n) >= 0) continue;
                n4 += n;
            }
            if ((n3 = n3 * n2 % n) >= 0) continue;
            n3 += n;
        }
        if (this.mType != 1) {
            if (this.mType != 2) {
                throw new RuntimeException("only type 1 or type 2 implemented");
            }
        } else {
            for (int i = 1; i < n - 1; ++i) {
                if (this.mMult[arrn[i + 1]][0] == -1) {
                    this.mMult[arrn[i + 1]][0] = arrn[n - i];
                    continue;
                }
                this.mMult[arrn[i + 1]][1] = arrn[n - i];
            }
            int n5 = this.mDegree >> 1;
            for (int i = 1; i <= n5; ++i) {
                if (this.mMult[i - 1][0] == -1) {
                    this.mMult[i - 1][0] = -1 + (n5 + i);
                } else {
                    this.mMult[i - 1][1] = -1 + (n5 + i);
                }
                if (this.mMult[-1 + (n5 + i)][0] == -1) {
                    this.mMult[-1 + (n5 + i)][0] = i - 1;
                    continue;
                }
                this.mMult[-1 + (n5 + i)][1] = i - 1;
            }
            return;
        }
        for (int i = 1; i < n - 1; ++i) {
            if (this.mMult[arrn[i + 1]][0] == -1) {
                this.mMult[arrn[i + 1]][0] = arrn[n - i];
                continue;
            }
            this.mMult[arrn[i + 1]][1] = arrn[n - i];
        }
    }

    private void computeType() {
        int n;
        int n2;
        if ((7 & this.mDegree) == 0) {
            throw new RuntimeException("The extension degree is divisible by 8!");
        }
        this.mType = 1;
        int n3 = 0;
        while (n3 != 1) {
            int n4 = 1 + this.mType * this.mDegree;
            if (IntegerFunctions.isPrime(n4)) {
                int n5 = IntegerFunctions.order(2, n4);
                n3 = IntegerFunctions.gcd(this.mType * this.mDegree / n5, this.mDegree);
            }
            this.mType = 1 + this.mType;
        }
        this.mType = -1 + this.mType;
        if (this.mType == 1 && IntegerFunctions.isPrime(n2 = 1 + (this.mDegree << 1)) && IntegerFunctions.gcd((this.mDegree << 1) / (n = IntegerFunctions.order(2, n2)), this.mDegree) == 1) {
            this.mType = 1 + this.mType;
        }
    }

    private int elementOfOrder(int n, int n2) {
        Random random = new Random();
        int n3 = 0;
        while (n3 == 0) {
            n3 = random.nextInt() % (n2 - 1);
            if (n3 >= 0) continue;
            n3 += n2 - 1;
        }
        int n4 = IntegerFunctions.order(n3, n2);
        while (n4 % n != 0 || n4 == 0) {
            while (n3 == 0) {
                n3 = random.nextInt() % (n2 - 1);
                if (n3 >= 0) continue;
                n3 += n2 - 1;
            }
            n4 = IntegerFunctions.order(n3, n2);
        }
        int n5 = n / n4;
        int n6 = n3;
        for (int i = 2; i <= n5; ++i) {
            n6 *= n3;
        }
        return n6;
    }

    @Override
    protected void computeCOBMatrix(GF2nField gF2nField) {
        GF2nElement gF2nElement;
        if (this.mDegree != gF2nField.mDegree) {
            throw new IllegalArgumentException("GF2nField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
        }
        GF2Polynomial[] arrgF2Polynomial = new GF2Polynomial[this.mDegree];
        for (int i = 0; i < this.mDegree; ++i) {
            arrgF2Polynomial[i] = new GF2Polynomial(this.mDegree);
        }
        while ((gF2nElement = gF2nField.getRandomRoot(this.fieldPolynomial)).isZero()) {
        }
        GF2nPolynomialElement[] arrgF2nPolynomialElement = new GF2nPolynomialElement[this.mDegree];
        arrgF2nPolynomialElement[0] = (GF2nElement)gF2nElement.clone();
        for (int i = 1; i < this.mDegree; ++i) {
            arrgF2nPolynomialElement[i] = ((GF2nElement)arrgF2nPolynomialElement[i - 1]).square();
        }
        for (int i = 0; i < this.mDegree; ++i) {
            for (int j = 0; j < this.mDegree; ++j) {
                if (!((GF2nElement)arrgF2nPolynomialElement[i]).testBit(j)) continue;
                arrgF2Polynomial[-1 + (this.mDegree - j)].setBit(-1 + (this.mDegree - i));
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
        int n = 1;
        if (this.mType == n) {
            this.fieldPolynomial = new GF2Polynomial(1 + this.mDegree, "ALL");
            return;
        } else {
            if (this.mType != 2) return;
            {
                GF2Polynomial gF2Polynomial = new GF2Polynomial(1 + this.mDegree, "ONE");
                GF2Polynomial gF2Polynomial2 = new GF2Polynomial(1 + this.mDegree, "X");
                gF2Polynomial2.addToThis(gF2Polynomial);
                GF2Polynomial gF2Polynomial3 = gF2Polynomial;
                do {
                    if (n >= this.mDegree) {
                        this.fieldPolynomial = gF2Polynomial2;
                        return;
                    }
                    GF2Polynomial gF2Polynomial4 = gF2Polynomial2.shiftLeft();
                    gF2Polynomial4.addToThis(gF2Polynomial3);
                    ++n;
                    gF2Polynomial3 = gF2Polynomial2;
                    gF2Polynomial2 = gF2Polynomial4;
                } while (true);
            }
        }
    }

    int getONBBit() {
        return this.mBit;
    }

    int getONBLength() {
        return this.mLength;
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
                GF2nONBElement gF2nONBElement = new GF2nONBElement(this, new Random());
                GF2nPolynomial gF2nPolynomial4 = new GF2nPolynomial(2, GF2nONBElement.ZERO(this));
                gF2nPolynomial4.set(1, gF2nONBElement);
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

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    int[][] invMatrix(int[][] var1_1) {
        var2_2 = new int[]{this.mDegree, this.mDegree};
        (int[][])Array.newInstance((Class)Integer.TYPE, (int[])var2_2);
        var4_3 = new int[]{this.mDegree, this.mDegree};
        var5_4 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])var4_3);
        var6_5 = 0;
        do {
            var7_6 = this.mDegree;
            var8_7 = 0;
            if (var6_5 >= var7_6) ** GOTO lbl14
            var5_4[var6_5][var6_5] = 1;
            ++var6_5;
        } while (true);
        {
            ++var8_7;
lbl14: // 2 sources:
            if (var8_7 >= this.mDegree) return null;
            var9_8 = var8_7;
            do {
                if (var9_8 >= this.mDegree) continue block1;
                var1_1[-1 + this.mDegree - var8_7][var9_8] = var1_1[var8_7][var8_7];
                ++var9_8;
            } while (true);
        }
    }
}

