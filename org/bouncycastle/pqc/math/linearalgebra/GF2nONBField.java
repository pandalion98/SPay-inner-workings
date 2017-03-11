package org.bouncycastle.pqc.math.linearalgebra;

import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.Vector;

public class GF2nONBField extends GF2nField {
    private static final int MAXLONG = 64;
    private int mBit;
    private int mLength;
    int[][] mMult;
    private int mType;

    public GF2nONBField(int i) {
        if (i < 3) {
            throw new IllegalArgumentException("k must be at least 3");
        }
        this.mDegree = i;
        this.mLength = this.mDegree / MAXLONG;
        this.mBit = this.mDegree & 63;
        if (this.mBit == 0) {
            this.mBit = MAXLONG;
        } else {
            this.mLength++;
        }
        computeType();
        if (this.mType < 3) {
            this.mMult = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.mDegree, 2});
            for (int i2 = 0; i2 < this.mDegree; i2++) {
                this.mMult[i2][0] = -1;
                this.mMult[i2][1] = -1;
            }
            computeMultMatrix();
            computeFieldPolynomial();
            this.fields = new Vector();
            this.matrices = new Vector();
            return;
        }
        throw new RuntimeException("\nThe type of this field is " + this.mType);
    }

    private void computeMultMatrix() {
        if ((this.mType & 7) != 0) {
            int i;
            int i2 = (this.mType * this.mDegree) + 1;
            int[] iArr = new int[i2];
            int elementOfOrder = this.mType == 1 ? 1 : this.mType == 2 ? i2 - 1 : elementOfOrder(this.mType, i2);
            int i3 = 1;
            for (i = 0; i < this.mType; i++) {
                int i4 = i3;
                for (int i5 = 0; i5 < this.mDegree; i5++) {
                    iArr[i4] = i5;
                    i4 = (i4 << 1) % i2;
                    if (i4 < 0) {
                        i4 += i2;
                    }
                }
                i3 = (i3 * elementOfOrder) % i2;
                if (i3 < 0) {
                    i3 += i2;
                }
            }
            if (this.mType == 1) {
                for (elementOfOrder = 1; elementOfOrder < i2 - 1; elementOfOrder++) {
                    if (this.mMult[iArr[elementOfOrder + 1]][0] == -1) {
                        this.mMult[iArr[elementOfOrder + 1]][0] = iArr[i2 - elementOfOrder];
                    } else {
                        this.mMult[iArr[elementOfOrder + 1]][1] = iArr[i2 - elementOfOrder];
                    }
                }
                i = this.mDegree >> 1;
                for (elementOfOrder = 1; elementOfOrder <= i; elementOfOrder++) {
                    if (this.mMult[elementOfOrder - 1][0] == -1) {
                        this.mMult[elementOfOrder - 1][0] = (i + elementOfOrder) - 1;
                    } else {
                        this.mMult[elementOfOrder - 1][1] = (i + elementOfOrder) - 1;
                    }
                    if (this.mMult[(i + elementOfOrder) - 1][0] == -1) {
                        this.mMult[(i + elementOfOrder) - 1][0] = elementOfOrder - 1;
                    } else {
                        this.mMult[(i + elementOfOrder) - 1][1] = elementOfOrder - 1;
                    }
                }
                return;
            } else if (this.mType == 2) {
                for (elementOfOrder = 1; elementOfOrder < i2 - 1; elementOfOrder++) {
                    if (this.mMult[iArr[elementOfOrder + 1]][0] == -1) {
                        this.mMult[iArr[elementOfOrder + 1]][0] = iArr[i2 - elementOfOrder];
                    } else {
                        this.mMult[iArr[elementOfOrder + 1]][1] = iArr[i2 - elementOfOrder];
                    }
                }
                return;
            } else {
                throw new RuntimeException("only type 1 or type 2 implemented");
            }
        }
        throw new RuntimeException("bisher nur fuer Gausssche Normalbasen implementiert");
    }

    private void computeType() {
        if ((this.mDegree & 7) == 0) {
            throw new RuntimeException("The extension degree is divisible by 8!");
        }
        this.mType = 1;
        int i = 0;
        while (i != 1) {
            int i2 = (this.mType * this.mDegree) + 1;
            if (IntegerFunctions.isPrime(i2)) {
                i = IntegerFunctions.gcd((this.mType * this.mDegree) / IntegerFunctions.order(2, i2), this.mDegree);
            }
            this.mType++;
        }
        this.mType--;
        if (this.mType == 1) {
            i = (this.mDegree << 1) + 1;
            if (IntegerFunctions.isPrime(i)) {
                if (IntegerFunctions.gcd((this.mDegree << 1) / IntegerFunctions.order(2, i), this.mDegree) == 1) {
                    this.mType++;
                }
            }
        }
    }

    private int elementOfOrder(int i, int i2) {
        Random random = new Random();
        int i3 = 0;
        while (i3 == 0) {
            i3 = random.nextInt() % (i2 - 1);
            if (i3 < 0) {
                i3 += i2 - 1;
            }
        }
        int order = IntegerFunctions.order(i3, i2);
        while (true) {
            if (order % i == 0 && order != 0) {
                break;
            }
            while (i3 == 0) {
                i3 = random.nextInt() % (i2 - 1);
                if (i3 < 0) {
                    i3 += i2 - 1;
                }
            }
            order = IntegerFunctions.order(i3, i2);
        }
        int i4 = i / order;
        int i5 = i3;
        for (order = 2; order <= i4; order++) {
            i5 *= i3;
        }
        return i5;
    }

    protected void computeCOBMatrix(GF2nField gF2nField) {
        if (this.mDegree != gF2nField.mDegree) {
            throw new IllegalArgumentException("GF2nField.computeCOBMatrix: B1 has a different degree and thus cannot be coverted to!");
        }
        int i;
        GF2nElement randomRoot;
        Object obj = new GF2Polynomial[this.mDegree];
        for (i = 0; i < this.mDegree; i++) {
            obj[i] = new GF2Polynomial(this.mDegree);
        }
        do {
            randomRoot = gF2nField.getRandomRoot(this.fieldPolynomial);
        } while (randomRoot.isZero());
        GF2nPolynomialElement[] gF2nPolynomialElementArr = new GF2nPolynomialElement[this.mDegree];
        gF2nPolynomialElementArr[0] = (GF2nElement) randomRoot.clone();
        for (i = 1; i < this.mDegree; i++) {
            gF2nPolynomialElementArr[i] = gF2nPolynomialElementArr[i - 1].square();
        }
        for (i = 0; i < this.mDegree; i++) {
            for (int i2 = 0; i2 < this.mDegree; i2++) {
                if (gF2nPolynomialElementArr[i].testBit(i2)) {
                    obj[(this.mDegree - i2) - 1].setBit((this.mDegree - i) - 1);
                }
            }
        }
        this.fields.addElement(gF2nField);
        this.matrices.addElement(obj);
        gF2nField.fields.addElement(this);
        gF2nField.matrices.addElement(invertMatrix(obj));
    }

    protected void computeFieldPolynomial() {
        int i = 1;
        if (this.mType == 1) {
            this.fieldPolynomial = new GF2Polynomial(this.mDegree + 1, EnrollCardInfo.CARD_PRESENTATION_MODE_ALL);
        } else if (this.mType == 2) {
            GF2Polynomial gF2Polynomial = new GF2Polynomial(this.mDegree + 1, "ONE");
            GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.mDegree + 1, "X");
            gF2Polynomial2.addToThis(gF2Polynomial);
            GF2Polynomial gF2Polynomial3 = gF2Polynomial;
            while (i < this.mDegree) {
                gF2Polynomial = gF2Polynomial2.shiftLeft();
                gF2Polynomial.addToThis(gF2Polynomial3);
                i++;
                gF2Polynomial3 = gF2Polynomial2;
                gF2Polynomial2 = gF2Polynomial;
            }
            this.fieldPolynomial = gF2Polynomial2;
        }
    }

    int getONBBit() {
        return this.mBit;
    }

    int getONBLength() {
        return this.mLength;
    }

    protected GF2nElement getRandomRoot(GF2Polynomial gF2Polynomial) {
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(gF2Polynomial, (GF2nField) this);
        int degree = gF2nPolynomial.getDegree();
        GF2nPolynomial gF2nPolynomial2 = gF2nPolynomial;
        while (degree > 1) {
            int degree2;
            while (true) {
                GF2nElement gF2nONBElement = new GF2nONBElement(this, new Random());
                GF2nPolynomial gF2nPolynomial3 = new GF2nPolynomial(2, GF2nONBElement.ZERO(this));
                gF2nPolynomial3.set(1, gF2nONBElement);
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

    int[][] invMatrix(int[][] iArr) {
        int i = 0;
        int[][] iArr2 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.mDegree, this.mDegree});
        iArr2 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{this.mDegree, this.mDegree});
        for (int i2 = 0; i2 < this.mDegree; i2++) {
            iArr2[i2][i2] = 1;
        }
        while (i < this.mDegree) {
            for (int i3 = i; i3 < this.mDegree; i3++) {
                iArr[(this.mDegree - 1) - i][i3] = iArr[i][i];
            }
            i++;
        }
        return (int[][]) null;
    }
}
