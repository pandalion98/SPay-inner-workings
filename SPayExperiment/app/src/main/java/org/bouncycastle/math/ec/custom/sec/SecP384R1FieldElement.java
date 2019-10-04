/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP384R1Curve;
import org.bouncycastle.math.ec.custom.sec.SecP384R1Field;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Arrays;

public class SecP384R1FieldElement
extends ECFieldElement {
    public static final BigInteger Q = SecP384R1Curve.q;
    protected int[] x;

    public SecP384R1FieldElement() {
        this.x = Nat.create(12);
    }

    public SecP384R1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP384R1FieldElement");
        }
        this.x = SecP384R1Field.fromBigInteger(bigInteger);
    }

    protected SecP384R1FieldElement(int[] arrn) {
        this.x = arrn;
    }

    @Override
    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] arrn = Nat.create(12);
        SecP384R1Field.add(this.x, ((SecP384R1FieldElement)eCFieldElement).x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement addOne() {
        int[] arrn = Nat.create(12);
        SecP384R1Field.addOne(this.x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] arrn = Nat.create(12);
        Mod.invert(SecP384R1Field.P, ((SecP384R1FieldElement)eCFieldElement).x, arrn);
        SecP384R1Field.multiply(arrn, this.x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SecP384R1FieldElement)) {
            return false;
        }
        SecP384R1FieldElement secP384R1FieldElement = (SecP384R1FieldElement)object;
        return Nat.eq(12, this.x, secP384R1FieldElement.x);
    }

    @Override
    public String getFieldName() {
        return "SecP384R1Field";
    }

    @Override
    public int getFieldSize() {
        return Q.bitLength();
    }

    public int hashCode() {
        return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 12);
    }

    @Override
    public ECFieldElement invert() {
        int[] arrn = Nat.create(12);
        Mod.invert(SecP384R1Field.P, this.x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    @Override
    public boolean isOne() {
        return Nat.isOne(12, this.x);
    }

    @Override
    public boolean isZero() {
        return Nat.isZero(12, this.x);
    }

    @Override
    public ECFieldElement multiply(ECFieldElement eCFieldElement) {
        int[] arrn = Nat.create(12);
        SecP384R1Field.multiply(this.x, ((SecP384R1FieldElement)eCFieldElement).x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement negate() {
        int[] arrn = Nat.create(12);
        SecP384R1Field.negate(this.x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement sqrt() {
        int[] arrn = this.x;
        if (Nat.isZero(12, arrn) || Nat.isOne(12, arrn)) {
            return this;
        }
        int[] arrn2 = Nat.create(12);
        int[] arrn3 = Nat.create(12);
        int[] arrn4 = Nat.create(12);
        int[] arrn5 = Nat.create(12);
        SecP384R1Field.square(arrn, arrn2);
        SecP384R1Field.multiply(arrn2, arrn, arrn2);
        SecP384R1Field.squareN(arrn2, 2, arrn3);
        SecP384R1Field.multiply(arrn3, arrn2, arrn3);
        SecP384R1Field.square(arrn3, arrn3);
        SecP384R1Field.multiply(arrn3, arrn, arrn3);
        SecP384R1Field.squareN(arrn3, 5, arrn4);
        SecP384R1Field.multiply(arrn4, arrn3, arrn4);
        SecP384R1Field.squareN(arrn4, 5, arrn5);
        SecP384R1Field.multiply(arrn5, arrn3, arrn5);
        SecP384R1Field.squareN(arrn5, 15, arrn3);
        SecP384R1Field.multiply(arrn3, arrn5, arrn3);
        SecP384R1Field.squareN(arrn3, 2, arrn4);
        SecP384R1Field.multiply(arrn2, arrn4, arrn2);
        SecP384R1Field.squareN(arrn4, 28, arrn4);
        SecP384R1Field.multiply(arrn3, arrn4, arrn3);
        SecP384R1Field.squareN(arrn3, 60, arrn4);
        SecP384R1Field.multiply(arrn4, arrn3, arrn4);
        SecP384R1Field.squareN(arrn4, 120, arrn3);
        SecP384R1Field.multiply(arrn3, arrn4, arrn3);
        SecP384R1Field.squareN(arrn3, 15, arrn3);
        SecP384R1Field.multiply(arrn3, arrn5, arrn3);
        SecP384R1Field.squareN(arrn3, 33, arrn3);
        SecP384R1Field.multiply(arrn3, arrn2, arrn3);
        SecP384R1Field.squareN(arrn3, 64, arrn3);
        SecP384R1Field.multiply(arrn3, arrn, arrn3);
        SecP384R1Field.squareN(arrn3, 30, arrn2);
        SecP384R1Field.square(arrn2, arrn3);
        if (Nat.eq(12, arrn, arrn3)) {
            return new SecP384R1FieldElement(arrn2);
        }
        return null;
    }

    @Override
    public ECFieldElement square() {
        int[] arrn = Nat.create(12);
        SecP384R1Field.square(this.x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] arrn = Nat.create(12);
        SecP384R1Field.subtract(this.x, ((SecP384R1FieldElement)eCFieldElement).x, arrn);
        return new SecP384R1FieldElement(arrn);
    }

    @Override
    public boolean testBitZero() {
        return Nat.getBit(this.x, 0) == 1;
    }

    @Override
    public BigInteger toBigInteger() {
        return Nat.toBigInteger(12, this.x);
    }
}

