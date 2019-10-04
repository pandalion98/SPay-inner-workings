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
import org.bouncycastle.math.ec.custom.sec.SecP256R1Curve;
import org.bouncycastle.math.ec.custom.sec.SecP256R1Field;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.Arrays;

public class SecP256R1FieldElement
extends ECFieldElement {
    public static final BigInteger Q = SecP256R1Curve.q;
    protected int[] x;

    public SecP256R1FieldElement() {
        this.x = Nat256.create();
    }

    public SecP256R1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP256R1FieldElement");
        }
        this.x = SecP256R1Field.fromBigInteger(bigInteger);
    }

    protected SecP256R1FieldElement(int[] arrn) {
        this.x = arrn;
    }

    @Override
    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        SecP256R1Field.add(this.x, ((SecP256R1FieldElement)eCFieldElement).x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement addOne() {
        int[] arrn = Nat256.create();
        SecP256R1Field.addOne(this.x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        Mod.invert(SecP256R1Field.P, ((SecP256R1FieldElement)eCFieldElement).x, arrn);
        SecP256R1Field.multiply(arrn, this.x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SecP256R1FieldElement)) {
            return false;
        }
        SecP256R1FieldElement secP256R1FieldElement = (SecP256R1FieldElement)object;
        return Nat256.eq(this.x, secP256R1FieldElement.x);
    }

    @Override
    public String getFieldName() {
        return "SecP256R1Field";
    }

    @Override
    public int getFieldSize() {
        return Q.bitLength();
    }

    public int hashCode() {
        return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 8);
    }

    @Override
    public ECFieldElement invert() {
        int[] arrn = Nat256.create();
        Mod.invert(SecP256R1Field.P, this.x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    @Override
    public boolean isOne() {
        return Nat256.isOne(this.x);
    }

    @Override
    public boolean isZero() {
        return Nat256.isZero(this.x);
    }

    @Override
    public ECFieldElement multiply(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        SecP256R1Field.multiply(this.x, ((SecP256R1FieldElement)eCFieldElement).x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement negate() {
        int[] arrn = Nat256.create();
        SecP256R1Field.negate(this.x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement sqrt() {
        int[] arrn = this.x;
        if (Nat256.isZero(arrn) || Nat256.isOne(arrn)) {
            return this;
        }
        int[] arrn2 = Nat256.create();
        int[] arrn3 = Nat256.create();
        SecP256R1Field.square(arrn, arrn2);
        SecP256R1Field.multiply(arrn2, arrn, arrn2);
        SecP256R1Field.squareN(arrn2, 2, arrn3);
        SecP256R1Field.multiply(arrn3, arrn2, arrn3);
        SecP256R1Field.squareN(arrn3, 4, arrn2);
        SecP256R1Field.multiply(arrn2, arrn3, arrn2);
        SecP256R1Field.squareN(arrn2, 8, arrn3);
        SecP256R1Field.multiply(arrn3, arrn2, arrn3);
        SecP256R1Field.squareN(arrn3, 16, arrn2);
        SecP256R1Field.multiply(arrn2, arrn3, arrn2);
        SecP256R1Field.squareN(arrn2, 32, arrn2);
        SecP256R1Field.multiply(arrn2, arrn, arrn2);
        SecP256R1Field.squareN(arrn2, 96, arrn2);
        SecP256R1Field.multiply(arrn2, arrn, arrn2);
        SecP256R1Field.squareN(arrn2, 94, arrn2);
        SecP256R1Field.square(arrn2, arrn3);
        if (Nat256.eq(arrn, arrn3)) {
            return new SecP256R1FieldElement(arrn2);
        }
        return null;
    }

    @Override
    public ECFieldElement square() {
        int[] arrn = Nat256.create();
        SecP256R1Field.square(this.x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        SecP256R1Field.subtract(this.x, ((SecP256R1FieldElement)eCFieldElement).x, arrn);
        return new SecP256R1FieldElement(arrn);
    }

    @Override
    public boolean testBitZero() {
        return Nat256.getBit(this.x, 0) == 1;
    }

    @Override
    public BigInteger toBigInteger() {
        return Nat256.toBigInteger(this.x);
    }
}

