/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.math.linearalgebra.GF2Polynomial;
import org.bouncycastle.pqc.math.linearalgebra.GF2nElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nONBField;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomialElement;
import org.bouncycastle.pqc.math.linearalgebra.GF2nPolynomialField;
import org.bouncycastle.pqc.math.linearalgebra.GFElement;

public class GF2nPolynomial {
    private GF2nElement[] coeff;
    private int size;

    private GF2nPolynomial(int n) {
        this.size = n;
        this.coeff = new GF2nElement[this.size];
    }

    public GF2nPolynomial(int n, GF2nElement gF2nElement) {
        this.size = n;
        this.coeff = new GF2nElement[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.coeff[i] = (GF2nElement)gF2nElement.clone();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public GF2nPolynomial(GF2Polynomial gF2Polynomial, GF2nField gF2nField) {
        this.size = 1 + gF2nField.getDegree();
        this.coeff = new GF2nElement[this.size];
        if (!(gF2nField instanceof GF2nONBField)) {
            if (!(gF2nField instanceof GF2nPolynomialField)) {
                throw new IllegalArgumentException("PolynomialGF2n(Bitstring, GF2nField): B1 must be an instance of GF2nONBField or GF2nPolynomialField!");
            }
        } else {
            for (int i = 0; i < this.size; ++i) {
                this.coeff[i] = gF2Polynomial.testBit(i) ? GF2nONBElement.ONE((GF2nONBField)gF2nField) : GF2nONBElement.ZERO((GF2nONBField)gF2nField);
            }
            return;
        }
        for (int i = 0; i < this.size; ++i) {
            this.coeff[i] = gF2Polynomial.testBit(i) ? GF2nPolynomialElement.ONE((GF2nPolynomialField)gF2nField) : GF2nPolynomialElement.ZERO((GF2nPolynomialField)gF2nField);
        }
    }

    public GF2nPolynomial(GF2nPolynomial gF2nPolynomial) {
        this.coeff = new GF2nElement[gF2nPolynomial.size];
        this.size = gF2nPolynomial.size;
        for (int i = 0; i < this.size; ++i) {
            this.coeff[i] = (GF2nElement)gF2nPolynomial.coeff[i].clone();
        }
    }

    public final GF2nPolynomial add(GF2nPolynomial gF2nPolynomial) {
        int n;
        if (this.size() >= gF2nPolynomial.size()) {
            int n2;
            GF2nPolynomial gF2nPolynomial2 = new GF2nPolynomial(this.size());
            for (n2 = 0; n2 < gF2nPolynomial.size(); ++n2) {
                gF2nPolynomial2.coeff[n2] = (GF2nElement)this.coeff[n2].add((GFElement)gF2nPolynomial.coeff[n2]);
            }
            while (n2 < this.size()) {
                gF2nPolynomial2.coeff[n2] = this.coeff[n2];
                ++n2;
            }
            return gF2nPolynomial2;
        }
        GF2nPolynomial gF2nPolynomial3 = new GF2nPolynomial(gF2nPolynomial.size());
        for (n = 0; n < this.size(); ++n) {
            gF2nPolynomial3.coeff[n] = (GF2nElement)this.coeff[n].add((GFElement)gF2nPolynomial.coeff[n]);
        }
        while (n < gF2nPolynomial.size()) {
            gF2nPolynomial3.coeff[n] = gF2nPolynomial.coeff[n];
            ++n;
        }
        return gF2nPolynomial3;
    }

    public final void assignZeroToElements() {
        for (int i = 0; i < this.size; ++i) {
            this.coeff[i].assignZero();
        }
    }

    public final GF2nElement at(int n) {
        return this.coeff[n];
    }

    public final GF2nPolynomial[] divide(GF2nPolynomial gF2nPolynomial) {
        GF2nPolynomial[] arrgF2nPolynomial = new GF2nPolynomial[2];
        GF2nPolynomial gF2nPolynomial2 = new GF2nPolynomial(this);
        gF2nPolynomial2.shrink();
        int n = gF2nPolynomial.getDegree();
        GF2nElement gF2nElement = (GF2nElement)gF2nPolynomial.coeff[n].invert();
        if (gF2nPolynomial2.getDegree() < n) {
            arrgF2nPolynomial[0] = new GF2nPolynomial(this);
            arrgF2nPolynomial[0].assignZeroToElements();
            arrgF2nPolynomial[0].shrink();
            arrgF2nPolynomial[1] = new GF2nPolynomial(this);
            arrgF2nPolynomial[1].shrink();
            return arrgF2nPolynomial;
        }
        arrgF2nPolynomial[0] = new GF2nPolynomial(this);
        arrgF2nPolynomial[0].assignZeroToElements();
        int n2 = gF2nPolynomial2.getDegree() - n;
        GF2nPolynomial gF2nPolynomial3 = gF2nPolynomial2;
        int n3 = n2;
        while (n3 >= 0) {
            GF2nElement gF2nElement2 = (GF2nElement)gF2nPolynomial3.coeff[gF2nPolynomial3.getDegree()].multiply((GFElement)gF2nElement);
            GF2nPolynomial gF2nPolynomial4 = gF2nPolynomial.scalarMultiply(gF2nElement2);
            gF2nPolynomial4.shiftThisLeft(n3);
            gF2nPolynomial3 = gF2nPolynomial3.add(gF2nPolynomial4);
            gF2nPolynomial3.shrink();
            arrgF2nPolynomial[0].coeff[n3] = (GF2nElement)gF2nElement2.clone();
            n3 = gF2nPolynomial3.getDegree() - n;
        }
        arrgF2nPolynomial[1] = gF2nPolynomial3;
        arrgF2nPolynomial[0].shrink();
        return arrgF2nPolynomial;
    }

    public final void enlarge(int n) {
        if (n <= this.size) {
            return;
        }
        GF2nElement[] arrgF2nElement = new GF2nElement[n];
        System.arraycopy((Object)this.coeff, (int)0, (Object)arrgF2nElement, (int)0, (int)this.size);
        GF2nField gF2nField = this.coeff[0].getField();
        if (this.coeff[0] instanceof GF2nPolynomialElement) {
            for (int i = this.size; i < n; ++i) {
                arrgF2nElement[i] = GF2nPolynomialElement.ZERO((GF2nPolynomialField)gF2nField);
            }
        } else if (this.coeff[0] instanceof GF2nONBElement) {
            for (int i = this.size; i < n; ++i) {
                arrgF2nElement[i] = GF2nONBElement.ZERO((GF2nONBField)gF2nField);
            }
        }
        this.size = n;
        this.coeff = arrgF2nElement;
    }

    /*
     * Enabled aggressive block sorting
     */
    public final boolean equals(Object object) {
        if (object != null && object instanceof GF2nPolynomial) {
            GF2nPolynomial gF2nPolynomial = (GF2nPolynomial)object;
            if (this.getDegree() == gF2nPolynomial.getDegree()) {
                int n = 0;
                do {
                    if (n >= this.size) {
                        return true;
                    }
                    if (!this.coeff[n].equals((Object)gF2nPolynomial.coeff[n])) break;
                    ++n;
                } while (true);
            }
        }
        return false;
    }

    public final GF2nPolynomial gcd(GF2nPolynomial gF2nPolynomial) {
        GF2nPolynomial gF2nPolynomial2 = new GF2nPolynomial(this);
        GF2nPolynomial gF2nPolynomial3 = new GF2nPolynomial(gF2nPolynomial);
        gF2nPolynomial2.shrink();
        gF2nPolynomial3.shrink();
        while (!gF2nPolynomial3.isZero()) {
            GF2nPolynomial gF2nPolynomial4 = gF2nPolynomial2.remainder(gF2nPolynomial3);
            gF2nPolynomial2 = gF2nPolynomial3;
            gF2nPolynomial3 = gF2nPolynomial4;
        }
        return gF2nPolynomial2.scalarMultiply((GF2nElement)gF2nPolynomial2.coeff[gF2nPolynomial2.getDegree()].invert());
    }

    public final int getDegree() {
        for (int i = -1 + this.size; i >= 0; --i) {
            if (this.coeff[i].isZero()) continue;
            return i;
        }
        return -1;
    }

    public int hashCode() {
        return this.getDegree() + this.coeff.hashCode();
    }

    public final boolean isZero() {
        for (int i = 0; i < this.size; ++i) {
            if (this.coeff[i] == null || this.coeff[i].isZero()) continue;
            return false;
        }
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public final GF2nPolynomial multiply(GF2nPolynomial gF2nPolynomial) {
        int n = this.size();
        if (n != gF2nPolynomial.size()) {
            throw new IllegalArgumentException("PolynomialGF2n.multiply: this and b must have the same size!");
        }
        GF2nPolynomial gF2nPolynomial2 = new GF2nPolynomial(-1 + (n << 1));
        int n2 = 0;
        while (n2 < this.size()) {
            for (int i = 0; i < gF2nPolynomial.size(); ++i) {
                gF2nPolynomial2.coeff[n2 + i] = gF2nPolynomial2.coeff[n2 + i] == null ? (GF2nElement)this.coeff[n2].multiply((GFElement)gF2nPolynomial.coeff[i]) : (GF2nElement)gF2nPolynomial2.coeff[n2 + i].add(this.coeff[n2].multiply((GFElement)gF2nPolynomial.coeff[i]));
            }
            ++n2;
        }
        return gF2nPolynomial2;
    }

    public final GF2nPolynomial multiplyAndReduce(GF2nPolynomial gF2nPolynomial, GF2nPolynomial gF2nPolynomial2) {
        return this.multiply(gF2nPolynomial).reduce(gF2nPolynomial2);
    }

    public final GF2nPolynomial quotient(GF2nPolynomial gF2nPolynomial) {
        new GF2nPolynomial[2];
        return this.divide(gF2nPolynomial)[0];
    }

    public final GF2nPolynomial reduce(GF2nPolynomial gF2nPolynomial) {
        return this.remainder(gF2nPolynomial);
    }

    public final GF2nPolynomial remainder(GF2nPolynomial gF2nPolynomial) {
        new GF2nPolynomial[2];
        return this.divide(gF2nPolynomial)[1];
    }

    public final GF2nPolynomial scalarMultiply(GF2nElement gF2nElement) {
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(this.size());
        for (int i = 0; i < this.size(); ++i) {
            gF2nPolynomial.coeff[i] = (GF2nElement)this.coeff[i].multiply((GFElement)gF2nElement);
        }
        return gF2nPolynomial;
    }

    public final void set(int n, GF2nElement gF2nElement) {
        if (!(gF2nElement instanceof GF2nPolynomialElement) && !(gF2nElement instanceof GF2nONBElement)) {
            throw new IllegalArgumentException("PolynomialGF2n.set f must be an instance of either GF2nPolynomialElement or GF2nONBElement!");
        }
        this.coeff[n] = (GF2nElement)gF2nElement.clone();
    }

    public final GF2nPolynomial shiftLeft(int n) {
        if (n <= 0) {
            return new GF2nPolynomial(this);
        }
        GF2nPolynomial gF2nPolynomial = new GF2nPolynomial(n + this.size, this.coeff[0]);
        gF2nPolynomial.assignZeroToElements();
        for (int i = 0; i < this.size; ++i) {
            gF2nPolynomial.coeff[i + n] = this.coeff[i];
        }
        return gF2nPolynomial;
    }

    public final void shiftThisLeft(int n) {
        block3 : {
            GF2nField gF2nField;
            block4 : {
                if (n <= 0) break block3;
                int n2 = this.size;
                gF2nField = this.coeff[0].getField();
                this.enlarge(n + this.size);
                for (int i = n2 - 1; i >= 0; --i) {
                    this.coeff[i + n] = this.coeff[i];
                }
                if (!(this.coeff[0] instanceof GF2nPolynomialElement)) break block4;
                for (int i = n - 1; i >= 0; --i) {
                    this.coeff[i] = GF2nPolynomialElement.ZERO((GF2nPolynomialField)gF2nField);
                }
                break block3;
            }
            if (!(this.coeff[0] instanceof GF2nONBElement)) break block3;
            for (int i = n - 1; i >= 0; --i) {
                this.coeff[i] = GF2nONBElement.ZERO((GF2nONBField)gF2nField);
            }
        }
    }

    public final void shrink() {
        int n;
        for (n = -1 + this.size; this.coeff[n].isZero() && n > 0; --n) {
        }
        int n2 = n + 1;
        if (n2 < this.size) {
            GF2nElement[] arrgF2nElement = new GF2nElement[n2];
            System.arraycopy((Object)this.coeff, (int)0, (Object)arrgF2nElement, (int)0, (int)n2);
            this.coeff = arrgF2nElement;
            this.size = n2;
        }
    }

    public final int size() {
        return this.size;
    }
}

