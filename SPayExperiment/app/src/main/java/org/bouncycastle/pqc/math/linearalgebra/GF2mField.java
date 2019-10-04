/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.security.SecureRandom;
import org.bouncycastle.pqc.math.linearalgebra.LittleEndianConversions;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2;
import org.bouncycastle.pqc.math.linearalgebra.RandUtils;

public class GF2mField {
    private int degree = 0;
    private int polynomial;

    public GF2mField(int n) {
        if (n >= 32) {
            throw new IllegalArgumentException(" Error: the degree of field is too large ");
        }
        if (n < 1) {
            throw new IllegalArgumentException(" Error: the degree of field is non-positive ");
        }
        this.degree = n;
        this.polynomial = PolynomialRingGF2.getIrreduciblePolynomial(n);
    }

    public GF2mField(int n, int n2) {
        if (n != PolynomialRingGF2.degree(n2)) {
            throw new IllegalArgumentException(" Error: the degree is not correct");
        }
        if (!PolynomialRingGF2.isIrreducible(n2)) {
            throw new IllegalArgumentException(" Error: given polynomial is reducible");
        }
        this.degree = n;
        this.polynomial = n2;
    }

    public GF2mField(GF2mField gF2mField) {
        this.degree = gF2mField.degree;
        this.polynomial = gF2mField.polynomial;
    }

    public GF2mField(byte[] arrby) {
        if (arrby.length != 4) {
            throw new IllegalArgumentException("byte array is not an encoded finite field");
        }
        this.polynomial = LittleEndianConversions.OS2IP(arrby);
        if (!PolynomialRingGF2.isIrreducible(this.polynomial)) {
            throw new IllegalArgumentException("byte array is not an encoded finite field");
        }
        this.degree = PolynomialRingGF2.degree(this.polynomial);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static String polyToString(int n) {
        String string = "";
        if (n == 0) {
            return "0";
        }
        if ((byte)(n & 1) == 1) {
            string = "1";
        }
        int n2 = n >>> 1;
        int n3 = 1;
        while (n2 != 0) {
            if ((byte)(n2 & 1) == 1) {
                string = string + "+x^" + n3;
            }
            n2 >>>= 1;
            ++n3;
        }
        return string;
    }

    public int add(int n, int n2) {
        return n ^ n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String elementToStr(int n) {
        String string = "";
        int n2 = 0;
        while (n2 < this.degree) {
            string = (1 & (byte)n) == 0 ? "0" + string : "1" + string;
            n >>>= 1;
            ++n2;
        }
        return string;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (object == null || !(object instanceof GF2mField)) break block2;
                GF2mField gF2mField = (GF2mField)object;
                if (this.degree == gF2mField.degree && this.polynomial == gF2mField.polynomial) break block3;
            }
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int exp(int n, int n2) {
        int n3;
        if (n2 == 0) {
            return 1;
        }
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (n2 < 0) {
            n = this.inverse(n);
            n2 = -n2;
            n3 = 1;
        } else {
            n3 = 1;
        }
        while (n2 != 0) {
            if ((n2 & 1) == 1) {
                n3 = this.mult(n3, n);
            }
            n = this.mult(n, n);
            n2 >>>= 1;
        }
        return n3;
    }

    public int getDegree() {
        return this.degree;
    }

    public byte[] getEncoded() {
        return LittleEndianConversions.I2OSP(this.polynomial);
    }

    public int getPolynomial() {
        return this.polynomial;
    }

    public int getRandomElement(SecureRandom secureRandom) {
        return RandUtils.nextInt(secureRandom, 1 << this.degree);
    }

    public int getRandomNonZeroElement() {
        return this.getRandomNonZeroElement(new SecureRandom());
    }

    public int getRandomNonZeroElement(SecureRandom secureRandom) {
        int n;
        int n2 = RandUtils.nextInt(secureRandom, 1 << this.degree);
        for (n = 0; n2 == 0 && n < 1048576; ++n) {
            n2 = RandUtils.nextInt(secureRandom, 1 << this.degree);
        }
        if (n == 1048576) {
            return 1;
        }
        return n2;
    }

    public int hashCode() {
        return this.polynomial;
    }

    public int inverse(int n) {
        return this.exp(n, -2 + (1 << this.degree));
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isElementOfThisField(int n) {
        if (this.degree == 31) {
            if (n >= 0) return true;
            return false;
        }
        if (n < 0 || n >= 1 << this.degree) return false;
        return true;
    }

    public int mult(int n, int n2) {
        return PolynomialRingGF2.modMultiply(n, n2, this.polynomial);
    }

    public int sqRoot(int n) {
        for (int i = 1; i < this.degree; ++i) {
            n = this.mult(n, n);
        }
        return n;
    }

    public String toString() {
        return "Finite Field GF(2^" + this.degree + ") = " + "GF(2)[X]/<" + GF2mField.polyToString(this.polynomial) + "> ";
    }
}

