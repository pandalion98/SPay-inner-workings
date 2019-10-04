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
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Field;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.Arrays;

public class SecP256K1FieldElement
extends ECFieldElement {
    public static final BigInteger Q = SecP256K1Curve.q;
    protected int[] x;

    public SecP256K1FieldElement() {
        this.x = Nat256.create();
    }

    public SecP256K1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP256K1FieldElement");
        }
        this.x = SecP256K1Field.fromBigInteger(bigInteger);
    }

    protected SecP256K1FieldElement(int[] arrn) {
        this.x = arrn;
    }

    @Override
    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        SecP256K1Field.add(this.x, ((SecP256K1FieldElement)eCFieldElement).x, arrn);
        return new SecP256K1FieldElement(arrn);
    }

    @Override
    public ECFieldElement addOne() {
        int[] arrn = Nat256.create();
        SecP256K1Field.addOne(this.x, arrn);
        return new SecP256K1FieldElement(arrn);
    }

    @Override
    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        Mod.invert(SecP256K1Field.P, ((SecP256K1FieldElement)eCFieldElement).x, arrn);
        SecP256K1Field.multiply(arrn, this.x, arrn);
        return new SecP256K1FieldElement(arrn);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SecP256K1FieldElement)) {
            return false;
        }
        SecP256K1FieldElement secP256K1FieldElement = (SecP256K1FieldElement)object;
        return Nat256.eq(this.x, secP256K1FieldElement.x);
    }

    @Override
    public String getFieldName() {
        return "SecP256K1Field";
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
        Mod.invert(SecP256K1Field.P, this.x, arrn);
        return new SecP256K1FieldElement(arrn);
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
        SecP256K1Field.multiply(this.x, ((SecP256K1FieldElement)eCFieldElement).x, arrn);
        return new SecP256K1FieldElement(arrn);
    }

    @Override
    public ECFieldElement negate() {
        int[] arrn = Nat256.create();
        SecP256K1Field.negate(this.x, arrn);
        return new SecP256K1FieldElement(arrn);
    }

    @Override
    public ECFieldElement sqrt() {
        int[] arrn = this.x;
        if (Nat256.isZero(arrn) || Nat256.isOne(arrn)) {
            return this;
        }
        int[] arrn2 = Nat256.create();
        SecP256K1Field.square(arrn, arrn2);
        SecP256K1Field.multiply(arrn2, arrn, arrn2);
        int[] arrn3 = Nat256.create();
        SecP256K1Field.square(arrn2, arrn3);
        SecP256K1Field.multiply(arrn3, arrn, arrn3);
        int[] arrn4 = Nat256.create();
        SecP256K1Field.squareN(arrn3, 3, arrn4);
        SecP256K1Field.multiply(arrn4, arrn3, arrn4);
        SecP256K1Field.squareN(arrn4, 3, arrn4);
        SecP256K1Field.multiply(arrn4, arrn3, arrn4);
        SecP256K1Field.squareN(arrn4, 2, arrn4);
        SecP256K1Field.multiply(arrn4, arrn2, arrn4);
        int[] arrn5 = Nat256.create();
        SecP256K1Field.squareN(arrn4, 11, arrn5);
        SecP256K1Field.multiply(arrn5, arrn4, arrn5);
        SecP256K1Field.squareN(arrn5, 22, arrn4);
        SecP256K1Field.multiply(arrn4, arrn5, arrn4);
        int[] arrn6 = Nat256.create();
        SecP256K1Field.squareN(arrn4, 44, arrn6);
        SecP256K1Field.multiply(arrn6, arrn4, arrn6);
        int[] arrn7 = Nat256.create();
        SecP256K1Field.squareN(arrn6, 88, arrn7);
        SecP256K1Field.multiply(arrn7, arrn6, arrn7);
        SecP256K1Field.squareN(arrn7, 44, arrn6);
        SecP256K1Field.multiply(arrn6, arrn4, arrn6);
        SecP256K1Field.squareN(arrn6, 3, arrn4);
        SecP256K1Field.multiply(arrn4, arrn3, arrn4);
        SecP256K1Field.squareN(arrn4, 23, arrn4);
        SecP256K1Field.multiply(arrn4, arrn5, arrn4);
        SecP256K1Field.squareN(arrn4, 6, arrn4);
        SecP256K1Field.multiply(arrn4, arrn2, arrn4);
        SecP256K1Field.squareN(arrn4, 2, arrn4);
        SecP256K1Field.square(arrn4, arrn2);
        if (Nat256.eq(arrn, arrn2)) {
            return new SecP256K1FieldElement(arrn4);
        }
        return null;
    }

    @Override
    public ECFieldElement square() {
        int[] arrn = Nat256.create();
        SecP256K1Field.square(this.x, arrn);
        return new SecP256K1FieldElement(arrn);
    }

    @Override
    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        SecP256K1Field.subtract(this.x, ((SecP256K1FieldElement)eCFieldElement).x, arrn);
        return new SecP256K1FieldElement(arrn);
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

