/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.GF2mVector;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.math.linearalgebra.RandUtils;

public class PolynomialGF2mSmallM {
    public static final char RANDOM_IRREDUCIBLE_POLYNOMIAL = 'I';
    private int[] coefficients;
    private int degree;
    private GF2mField field;

    public PolynomialGF2mSmallM(GF2mField gF2mField) {
        this.field = gF2mField;
        this.degree = -1;
        this.coefficients = new int[1];
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, int n) {
        this.field = gF2mField;
        this.degree = n;
        this.coefficients = new int[n + 1];
        this.coefficients[n] = 1;
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, int n, char c, SecureRandom secureRandom) {
        this.field = gF2mField;
        switch (c) {
            default: {
                throw new IllegalArgumentException(" Error: type " + c + " is not defined for GF2smallmPolynomial");
            }
            case 'I': 
        }
        this.coefficients = this.createRandomIrreduciblePolynomial(n, secureRandom);
        this.computeDegree();
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, byte[] arrby) {
        int n;
        this.field = gF2mField;
        int n2 = 1;
        for (n = 8; gF2mField.getDegree() > n; n += 8) {
            ++n2;
        }
        if (arrby.length % n2 != 0) {
            throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
        }
        this.coefficients = new int[arrby.length / n2];
        int n3 = 0;
        for (int i = 0; i < this.coefficients.length; ++i) {
            for (int j = 0; j < n; j += 8) {
                int[] arrn = this.coefficients;
                int n4 = arrn[i];
                int n5 = n3 + 1;
                arrn[i] = n4 ^ (255 & arrby[n3]) << j;
                n3 = n5;
            }
            if (this.field.isElementOfThisField(this.coefficients[i])) continue;
            throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
        }
        if (this.coefficients.length != 1 && this.coefficients[-1 + this.coefficients.length] == 0) {
            throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
        }
        this.computeDegree();
    }

    public PolynomialGF2mSmallM(GF2mField gF2mField, int[] arrn) {
        this.field = gF2mField;
        this.coefficients = PolynomialGF2mSmallM.normalForm(arrn);
        this.computeDegree();
    }

    public PolynomialGF2mSmallM(GF2mVector gF2mVector) {
        this(gF2mVector.getField(), gF2mVector.getIntArrayForm());
    }

    public PolynomialGF2mSmallM(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.field = polynomialGF2mSmallM.field;
        this.degree = polynomialGF2mSmallM.degree;
        this.coefficients = IntUtils.clone(polynomialGF2mSmallM.coefficients);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int[] add(int[] arrn, int[] arrn2) {
        int[] arrn3;
        if (arrn.length < arrn2.length) {
            arrn3 = new int[arrn2.length];
            System.arraycopy((Object)arrn2, (int)0, (Object)arrn3, (int)0, (int)arrn2.length);
        } else {
            arrn3 = new int[arrn.length];
            System.arraycopy((Object)arrn, (int)0, (Object)arrn3, (int)0, (int)arrn.length);
            arrn = arrn2;
        }
        int n = -1 + arrn.length;
        while (n >= 0) {
            arrn3[n] = this.field.add(arrn3[n], arrn[n]);
            --n;
        }
        return arrn3;
    }

    private static int computeDegree(int[] arrn) {
        int n;
        for (n = -1 + arrn.length; n >= 0 && arrn[n] == 0; --n) {
        }
        return n;
    }

    private void computeDegree() {
        this.degree = -1 + this.coefficients.length;
        while (this.degree >= 0 && this.coefficients[this.degree] == 0) {
            this.degree = -1 + this.degree;
        }
    }

    private int[] createRandomIrreduciblePolynomial(int n, SecureRandom secureRandom) {
        int n2;
        int[] arrn = new int[n + 1];
        arrn[n] = n2;
        arrn[0] = this.field.getRandomNonZeroElement(secureRandom);
        for (n2 = 1; n2 < n; ++n2) {
            arrn[n2] = this.field.getRandomElement(secureRandom);
        }
        while (!this.isIrreducible(arrn)) {
            int n3 = RandUtils.nextInt(secureRandom, n);
            if (n3 == 0) {
                arrn[0] = this.field.getRandomNonZeroElement(secureRandom);
                continue;
            }
            arrn[n3] = this.field.getRandomElement(secureRandom);
        }
        return arrn;
    }

    private int[][] div(int[] arrn, int[] arrn2) {
        int n = PolynomialGF2mSmallM.computeDegree(arrn2);
        int n2 = 1 + PolynomialGF2mSmallM.computeDegree(arrn);
        if (n == -1) {
            throw new ArithmeticException("Division by zero.");
        }
        int[][] arrarrn = new int[][]{new int[1], new int[n2]};
        int n3 = PolynomialGF2mSmallM.headCoefficient(arrn2);
        int n4 = this.field.inverse(n3);
        arrarrn[0][0] = 0;
        System.arraycopy((Object)arrn, (int)0, (Object)arrarrn[1], (int)0, (int)arrarrn[1].length);
        while (n <= PolynomialGF2mSmallM.computeDegree(arrarrn[1])) {
            int[] arrn3 = new int[]{this.field.mult(PolynomialGF2mSmallM.headCoefficient(arrarrn[1]), n4)};
            int[] arrn4 = this.multWithElement(arrn2, arrn3[0]);
            int n5 = PolynomialGF2mSmallM.computeDegree(arrarrn[1]) - n;
            int[] arrn5 = PolynomialGF2mSmallM.multWithMonomial(arrn4, n5);
            arrarrn[0] = this.add(PolynomialGF2mSmallM.multWithMonomial(arrn3, n5), arrarrn[0]);
            arrarrn[1] = this.add(arrn5, arrarrn[1]);
        }
        return arrarrn;
    }

    private int[] gcd(int[] arrn, int[] arrn2) {
        if (PolynomialGF2mSmallM.computeDegree(arrn) == -1) {
            return arrn2;
        }
        while (PolynomialGF2mSmallM.computeDegree(arrn2) != -1) {
            int[] arrn3 = this.mod(arrn, arrn2);
            arrn = new int[arrn2.length];
            System.arraycopy((Object)arrn2, (int)0, (Object)arrn, (int)0, (int)arrn.length);
            arrn2 = new int[arrn3.length];
            System.arraycopy((Object)arrn3, (int)0, (Object)arrn2, (int)0, (int)arrn2.length);
        }
        return this.multWithElement(arrn, this.field.inverse(PolynomialGF2mSmallM.headCoefficient(arrn)));
    }

    private static int headCoefficient(int[] arrn) {
        int n = PolynomialGF2mSmallM.computeDegree(arrn);
        if (n == -1) {
            return 0;
        }
        return arrn[n];
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean isEqual(int[] arrn, int[] arrn2) {
        int n = PolynomialGF2mSmallM.computeDegree(arrn);
        if (n == PolynomialGF2mSmallM.computeDegree(arrn2)) {
            int n2 = 0;
            do {
                if (n2 > n) {
                    return true;
                }
                if (arrn[n2] != arrn2[n2]) break;
                ++n2;
            } while (true);
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isIrreducible(int[] arrn) {
        if (arrn[0] != 0) {
            int n = PolynomialGF2mSmallM.computeDegree(arrn) >> 1;
            int[] arrn2 = new int[]{0, 1};
            int[] arrn3 = new int[]{0, 1};
            int n2 = this.field.getDegree();
            int n3 = 0;
            do {
                if (n3 >= n) {
                    return true;
                }
                for (int i = n2 - 1; i >= 0; --i) {
                    arrn2 = this.modMultiply(arrn2, arrn2, arrn);
                }
                if (PolynomialGF2mSmallM.computeDegree(this.gcd(this.add(arrn2 = PolynomialGF2mSmallM.normalForm(arrn2), arrn3), arrn)) != 0) break;
                ++n3;
            } while (true);
        }
        return false;
    }

    private int[] mod(int[] arrn, int[] arrn2) {
        int n = PolynomialGF2mSmallM.computeDegree(arrn2);
        if (n == -1) {
            throw new ArithmeticException("Division by zero");
        }
        int[] arrn3 = new int[arrn.length];
        int n2 = PolynomialGF2mSmallM.headCoefficient(arrn2);
        int n3 = this.field.inverse(n2);
        System.arraycopy((Object)arrn, (int)0, (Object)arrn3, (int)0, (int)arrn3.length);
        while (n <= PolynomialGF2mSmallM.computeDegree(arrn3)) {
            int n4 = this.field.mult(PolynomialGF2mSmallM.headCoefficient(arrn3), n3);
            arrn3 = this.add(this.multWithElement(PolynomialGF2mSmallM.multWithMonomial(arrn2, PolynomialGF2mSmallM.computeDegree(arrn3) - n), n4), arrn3);
        }
        return arrn3;
    }

    private int[] modDiv(int[] arrn, int[] arrn2, int[] arrn3) {
        int[] arrn4 = PolynomialGF2mSmallM.normalForm(arrn3);
        int[] arrn5 = this.mod(arrn2, arrn3);
        int[] arrn6 = new int[]{0};
        int[] arrn7 = this.mod(arrn, arrn3);
        while (PolynomialGF2mSmallM.computeDegree(arrn5) != -1) {
            int[][] arrn8 = this.div(arrn4, arrn5);
            arrn4 = PolynomialGF2mSmallM.normalForm(arrn5);
            arrn5 = PolynomialGF2mSmallM.normalForm(arrn8[1]);
            int[] arrn9 = this.add(arrn6, this.modMultiply(arrn8[0], arrn7, arrn3));
            arrn6 = PolynomialGF2mSmallM.normalForm(arrn7);
            arrn7 = PolynomialGF2mSmallM.normalForm(arrn9);
        }
        int n = PolynomialGF2mSmallM.headCoefficient(arrn4);
        return this.multWithElement(arrn6, this.field.inverse(n));
    }

    private int[] modMultiply(int[] arrn, int[] arrn2, int[] arrn3) {
        return this.mod(this.multiply(arrn, arrn2), arrn3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int[] multWithElement(int[] arrn, int n) {
        int n2 = PolynomialGF2mSmallM.computeDegree(arrn);
        if (n2 == -1) return new int[1];
        if (n == 0) {
            return new int[1];
        }
        if (n == 1) {
            return IntUtils.clone(arrn);
        }
        int[] arrn2 = new int[n2 + 1];
        while (n2 >= 0) {
            arrn2[n2] = this.field.mult(arrn[n2], n);
            --n2;
        }
        return arrn2;
    }

    private static int[] multWithMonomial(int[] arrn, int n) {
        int n2 = PolynomialGF2mSmallM.computeDegree(arrn);
        if (n2 == -1) {
            return new int[1];
        }
        int[] arrn2 = new int[1 + (n2 + n)];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)n, (int)(n2 + 1));
        return arrn2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int[] multiply(int[] arrn, int[] arrn2) {
        if (PolynomialGF2mSmallM.computeDegree(arrn) >= PolynomialGF2mSmallM.computeDegree(arrn2)) {
            int[] arrn3 = arrn2;
            arrn2 = arrn;
            arrn = arrn3;
        }
        int[] arrn4 = PolynomialGF2mSmallM.normalForm(arrn2);
        int[] arrn5 = PolynomialGF2mSmallM.normalForm(arrn);
        if (arrn5.length == 1) {
            return this.multWithElement(arrn4, arrn5[0]);
        }
        int n = arrn4.length;
        int n2 = arrn5.length;
        new int[-1 + (n + n2)];
        if (n2 != n) {
            int[] arrn6 = new int[n2];
            int[] arrn7 = new int[n - n2];
            System.arraycopy((Object)arrn4, (int)0, (Object)arrn6, (int)0, (int)arrn6.length);
            System.arraycopy((Object)arrn4, (int)n2, (Object)arrn7, (int)0, (int)arrn7.length);
            return this.add(this.multiply(arrn6, arrn5), PolynomialGF2mSmallM.multWithMonomial(this.multiply(arrn7, arrn5), n2));
        }
        int n3 = n + 1 >>> 1;
        int n4 = n - n3;
        int[] arrn8 = new int[n3];
        int[] arrn9 = new int[n3];
        int[] arrn10 = new int[n4];
        int[] arrn11 = new int[n4];
        System.arraycopy((Object)arrn4, (int)0, (Object)arrn8, (int)0, (int)arrn8.length);
        System.arraycopy((Object)arrn4, (int)n3, (Object)arrn10, (int)0, (int)arrn10.length);
        System.arraycopy((Object)arrn5, (int)0, (Object)arrn9, (int)0, (int)arrn9.length);
        System.arraycopy((Object)arrn5, (int)n3, (Object)arrn11, (int)0, (int)arrn11.length);
        int[] arrn12 = this.add(arrn8, arrn10);
        int[] arrn13 = this.add(arrn9, arrn11);
        int[] arrn14 = this.multiply(arrn8, arrn9);
        int[] arrn15 = this.multiply(arrn12, arrn13);
        int[] arrn16 = this.multiply(arrn10, arrn11);
        return this.add(PolynomialGF2mSmallM.multWithMonomial(this.add(this.add(this.add(arrn15, arrn14), arrn16), PolynomialGF2mSmallM.multWithMonomial(arrn16, n3)), n3), arrn14);
    }

    private static int[] normalForm(int[] arrn) {
        int n = PolynomialGF2mSmallM.computeDegree(arrn);
        if (n == -1) {
            return new int[1];
        }
        if (arrn.length == n + 1) {
            return IntUtils.clone(arrn);
        }
        int[] arrn2 = new int[n + 1];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)(n + 1));
        return arrn2;
    }

    public PolynomialGF2mSmallM add(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] arrn = this.add(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM addMonomial(int n) {
        int[] arrn = new int[n + 1];
        arrn[n] = 1;
        int[] arrn2 = this.add(this.coefficients, arrn);
        return new PolynomialGF2mSmallM(this.field, arrn2);
    }

    public void addToThis(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.coefficients = this.add(this.coefficients, polynomialGF2mSmallM.coefficients);
        this.computeDegree();
    }

    public PolynomialGF2mSmallM[] div(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[][] arrn = this.div(this.coefficients, polynomialGF2mSmallM.coefficients);
        PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM = new PolynomialGF2mSmallM[]{new PolynomialGF2mSmallM(this.field, arrn[0]), new PolynomialGF2mSmallM(this.field, arrn[1])};
        return arrpolynomialGF2mSmallM;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (object == null || !(object instanceof PolynomialGF2mSmallM)) break block2;
                PolynomialGF2mSmallM polynomialGF2mSmallM = (PolynomialGF2mSmallM)object;
                if (this.field.equals(polynomialGF2mSmallM.field) && this.degree == polynomialGF2mSmallM.degree && PolynomialGF2mSmallM.isEqual(this.coefficients, polynomialGF2mSmallM.coefficients)) break block3;
            }
            return false;
        }
        return true;
    }

    public int evaluateAt(int n) {
        int n2 = this.coefficients[this.degree];
        for (int i = -1 + this.degree; i >= 0; --i) {
            n2 = this.field.mult(n2, n) ^ this.coefficients[i];
        }
        return n2;
    }

    public PolynomialGF2mSmallM gcd(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] arrn = this.gcd(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public int getCoefficient(int n) {
        if (n < 0 || n > this.degree) {
            return 0;
        }
        return this.coefficients[n];
    }

    public int getDegree() {
        int n = -1 + this.coefficients.length;
        if (this.coefficients[n] == 0) {
            n = -1;
        }
        return n;
    }

    public byte[] getEncoded() {
        int n;
        int n2 = 1;
        for (n = 8; this.field.getDegree() > n; n += 8) {
            ++n2;
        }
        byte[] arrby = new byte[n2 * this.coefficients.length];
        int n3 = 0;
        for (int i = 0; i < this.coefficients.length; ++i) {
            for (int j = 0; j < n; j += 8) {
                int n4 = n3 + 1;
                arrby[n3] = (byte)(this.coefficients[i] >>> j);
                n3 = n4;
            }
        }
        return arrby;
    }

    public int getHeadCoefficient() {
        if (this.degree == -1) {
            return 0;
        }
        return this.coefficients[this.degree];
    }

    public int hashCode() {
        int n = this.field.hashCode();
        for (int i = 0; i < this.coefficients.length; ++i) {
            n = n * 31 + this.coefficients[i];
        }
        return n;
    }

    public PolynomialGF2mSmallM mod(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] arrn = this.mod(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM modDiv(PolynomialGF2mSmallM polynomialGF2mSmallM, PolynomialGF2mSmallM polynomialGF2mSmallM2) {
        int[] arrn = this.modDiv(this.coefficients, polynomialGF2mSmallM.coefficients, polynomialGF2mSmallM2.coefficients);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM modInverse(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] arrn = this.modDiv(new int[]{1}, this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM modMultiply(PolynomialGF2mSmallM polynomialGF2mSmallM, PolynomialGF2mSmallM polynomialGF2mSmallM2) {
        int[] arrn = this.modMultiply(this.coefficients, polynomialGF2mSmallM.coefficients, polynomialGF2mSmallM2.coefficients);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM[] modPolynomialToFracton(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int n = polynomialGF2mSmallM.degree >> 1;
        int[] arrn = PolynomialGF2mSmallM.normalForm(polynomialGF2mSmallM.coefficients);
        int[] arrn2 = this.mod(this.coefficients, polynomialGF2mSmallM.coefficients);
        int[] arrn3 = new int[]{0};
        int[] arrn4 = new int[]{1};
        while (PolynomialGF2mSmallM.computeDegree(arrn2) > n) {
            int[][] arrn5 = this.div(arrn, arrn2);
            int[] arrn6 = arrn5[1];
            int[] arrn7 = this.add(arrn3, this.modMultiply(arrn5[0], arrn4, polynomialGF2mSmallM.coefficients));
            arrn3 = arrn4;
            arrn4 = arrn7;
            arrn = arrn2;
            arrn2 = arrn6;
        }
        PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM = new PolynomialGF2mSmallM[]{new PolynomialGF2mSmallM(this.field, arrn2), new PolynomialGF2mSmallM(this.field, arrn4)};
        return arrpolynomialGF2mSmallM;
    }

    /*
     * Enabled aggressive block sorting
     */
    public PolynomialGF2mSmallM modSquareMatrix(PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM) {
        int n = arrpolynomialGF2mSmallM.length;
        int[] arrn = new int[n];
        int[] arrn2 = new int[n];
        for (int i = 0; i < this.coefficients.length; ++i) {
            arrn2[i] = this.field.mult(this.coefficients[i], this.coefficients[i]);
        }
        int n2 = 0;
        while (n2 < n) {
            for (int i = 0; i < n; ++i) {
                if (n2 >= arrpolynomialGF2mSmallM[i].coefficients.length) continue;
                int n3 = this.field.mult(arrpolynomialGF2mSmallM[i].coefficients[n2], arrn2[i]);
                arrn[n2] = this.field.add(arrn[n2], n3);
            }
            ++n2;
        }
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM modSquareRoot(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] arrn = IntUtils.clone(this.coefficients);
        int[] arrn2 = this.modMultiply(arrn, arrn, polynomialGF2mSmallM.coefficients);
        while (!PolynomialGF2mSmallM.isEqual(arrn2, this.coefficients)) {
            arrn = PolynomialGF2mSmallM.normalForm(arrn2);
            arrn2 = this.modMultiply(arrn, arrn, polynomialGF2mSmallM.coefficients);
        }
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    /*
     * Enabled aggressive block sorting
     */
    public PolynomialGF2mSmallM modSquareRootMatrix(PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM) {
        int n = arrpolynomialGF2mSmallM.length;
        int[] arrn = new int[n];
        int n2 = 0;
        do {
            int n3 = 0;
            if (n2 < n) {
            } else {
                do {
                    if (n3 >= n) {
                        return new PolynomialGF2mSmallM(this.field, arrn);
                    }
                    arrn[n3] = this.field.sqRoot(arrn[n3]);
                    ++n3;
                } while (true);
            }
            for (int i = 0; i < n; ++i) {
                if (n2 >= arrpolynomialGF2mSmallM[i].coefficients.length || i >= this.coefficients.length) continue;
                int n4 = this.field.mult(arrpolynomialGF2mSmallM[i].coefficients[n2], this.coefficients[i]);
                arrn[n2] = this.field.add(arrn[n2], n4);
            }
            ++n2;
        } while (true);
    }

    public void multThisWithElement(int n) {
        if (!this.field.isElementOfThisField(n)) {
            throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
        }
        this.coefficients = this.multWithElement(this.coefficients, n);
        this.computeDegree();
    }

    public PolynomialGF2mSmallM multWithElement(int n) {
        if (!this.field.isElementOfThisField(n)) {
            throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
        }
        int[] arrn = this.multWithElement(this.coefficients, n);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM multWithMonomial(int n) {
        int[] arrn = PolynomialGF2mSmallM.multWithMonomial(this.coefficients, n);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public PolynomialGF2mSmallM multiply(PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] arrn = this.multiply(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM(this.field, arrn);
    }

    public String toString() {
        String string = " Polynomial over " + this.field.toString() + ": \n";
        for (int i = 0; i < this.coefficients.length; ++i) {
            string = string + this.field.elementToStr(this.coefficients[i]) + "Y^" + i + "+";
        }
        return string + ";";
    }
}

