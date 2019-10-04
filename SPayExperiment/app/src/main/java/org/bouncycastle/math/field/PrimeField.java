/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.field;

import java.math.BigInteger;
import org.bouncycastle.math.field.FiniteField;

class PrimeField
implements FiniteField {
    protected final BigInteger characteristic;

    PrimeField(BigInteger bigInteger) {
        this.characteristic = bigInteger;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PrimeField)) {
            return false;
        }
        PrimeField primeField = (PrimeField)object;
        return this.characteristic.equals((Object)primeField.characteristic);
    }

    @Override
    public BigInteger getCharacteristic() {
        return this.characteristic;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    public int hashCode() {
        return this.characteristic.hashCode();
    }
}

