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
import org.bouncycastle.math.field.Polynomial;
import org.bouncycastle.math.field.PolynomialExtensionField;
import org.bouncycastle.util.Integers;

class GenericPolynomialExtensionField
implements PolynomialExtensionField {
    protected final Polynomial minimalPolynomial;
    protected final FiniteField subfield;

    GenericPolynomialExtensionField(FiniteField finiteField, Polynomial polynomial) {
        this.subfield = finiteField;
        this.minimalPolynomial = polynomial;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (this == object) break block4;
                if (!(object instanceof GenericPolynomialExtensionField)) {
                    return false;
                }
                GenericPolynomialExtensionField genericPolynomialExtensionField = (GenericPolynomialExtensionField)object;
                if (!this.subfield.equals((Object)genericPolynomialExtensionField.subfield) || !this.minimalPolynomial.equals((Object)genericPolynomialExtensionField.minimalPolynomial)) break block5;
            }
            return true;
        }
        return false;
    }

    @Override
    public BigInteger getCharacteristic() {
        return this.subfield.getCharacteristic();
    }

    @Override
    public int getDegree() {
        return this.minimalPolynomial.getDegree();
    }

    @Override
    public int getDimension() {
        return this.subfield.getDimension() * this.minimalPolynomial.getDegree();
    }

    @Override
    public Polynomial getMinimalPolynomial() {
        return this.minimalPolynomial;
    }

    @Override
    public FiniteField getSubfield() {
        return this.subfield;
    }

    public int hashCode() {
        return this.subfield.hashCode() ^ Integers.rotateLeft(this.minimalPolynomial.hashCode(), 16);
    }
}

