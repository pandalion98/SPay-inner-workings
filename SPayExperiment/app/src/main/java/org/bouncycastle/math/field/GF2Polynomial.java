/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.field;

import org.bouncycastle.math.field.Polynomial;
import org.bouncycastle.util.Arrays;

class GF2Polynomial
implements Polynomial {
    protected final int[] exponents;

    GF2Polynomial(int[] arrn) {
        this.exponents = Arrays.clone(arrn);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GF2Polynomial)) {
            return false;
        }
        GF2Polynomial gF2Polynomial = (GF2Polynomial)object;
        return Arrays.areEqual(this.exponents, gF2Polynomial.exponents);
    }

    @Override
    public int getDegree() {
        return this.exponents[-1 + this.exponents.length];
    }

    @Override
    public int[] getExponentsPresent() {
        return Arrays.clone(this.exponents);
    }

    public int hashCode() {
        return Arrays.hashCode(this.exponents);
    }
}

