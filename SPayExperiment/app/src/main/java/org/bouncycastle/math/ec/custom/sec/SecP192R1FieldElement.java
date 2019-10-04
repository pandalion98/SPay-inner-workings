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
import org.bouncycastle.math.ec.custom.sec.SecP192R1Curve;
import org.bouncycastle.math.ec.custom.sec.SecP192R1Field;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat192;
import org.bouncycastle.util.Arrays;

public class SecP192R1FieldElement
extends ECFieldElement {
    public static final BigInteger Q = SecP192R1Curve.q;
    protected int[] x;

    public SecP192R1FieldElement() {
        this.x = Nat192.create();
    }

    public SecP192R1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP192R1FieldElement");
        }
        this.x = SecP192R1Field.fromBigInteger(bigInteger);
    }

    protected SecP192R1FieldElement(int[] arrn) {
        this.x = arrn;
    }

    @Override
    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] arrn = Nat192.create();
        SecP192R1Field.add(this.x, ((SecP192R1FieldElement)eCFieldElement).x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement addOne() {
        int[] arrn = Nat192.create();
        SecP192R1Field.addOne(this.x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] arrn = Nat192.create();
        Mod.invert(SecP192R1Field.P, ((SecP192R1FieldElement)eCFieldElement).x, arrn);
        SecP192R1Field.multiply(arrn, this.x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SecP192R1FieldElement)) {
            return false;
        }
        SecP192R1FieldElement secP192R1FieldElement = (SecP192R1FieldElement)object;
        return Nat192.eq(this.x, secP192R1FieldElement.x);
    }

    @Override
    public String getFieldName() {
        return "SecP192R1Field";
    }

    @Override
    public int getFieldSize() {
        return Q.bitLength();
    }

    public int hashCode() {
        return Q.hashCode() ^ Arrays.hashCode(this.x, 0, 6);
    }

    @Override
    public ECFieldElement invert() {
        int[] arrn = Nat192.create();
        Mod.invert(SecP192R1Field.P, this.x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    @Override
    public boolean isOne() {
        return Nat192.isOne(this.x);
    }

    @Override
    public boolean isZero() {
        return Nat192.isZero(this.x);
    }

    @Override
    public ECFieldElement multiply(ECFieldElement eCFieldElement) {
        int[] arrn = Nat192.create();
        SecP192R1Field.multiply(this.x, ((SecP192R1FieldElement)eCFieldElement).x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement negate() {
        int[] arrn = Nat192.create();
        SecP192R1Field.negate(this.x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement sqrt() {
        int[] arrn = this.x;
        if (Nat192.isZero(arrn) || Nat192.isOne(arrn)) {
            return this;
        }
        int[] arrn2 = Nat192.create();
        int[] arrn3 = Nat192.create();
        SecP192R1Field.square(arrn, arrn2);
        SecP192R1Field.multiply(arrn2, arrn, arrn2);
        SecP192R1Field.squareN(arrn2, 2, arrn3);
        SecP192R1Field.multiply(arrn3, arrn2, arrn3);
        SecP192R1Field.squareN(arrn3, 4, arrn2);
        SecP192R1Field.multiply(arrn2, arrn3, arrn2);
        SecP192R1Field.squareN(arrn2, 8, arrn3);
        SecP192R1Field.multiply(arrn3, arrn2, arrn3);
        SecP192R1Field.squareN(arrn3, 16, arrn2);
        SecP192R1Field.multiply(arrn2, arrn3, arrn2);
        SecP192R1Field.squareN(arrn2, 32, arrn3);
        SecP192R1Field.multiply(arrn3, arrn2, arrn3);
        SecP192R1Field.squareN(arrn3, 64, arrn2);
        SecP192R1Field.multiply(arrn2, arrn3, arrn2);
        SecP192R1Field.squareN(arrn2, 62, arrn2);
        SecP192R1Field.square(arrn2, arrn3);
        if (Nat192.eq(arrn, arrn3)) {
            return new SecP192R1FieldElement(arrn2);
        }
        return null;
    }

    @Override
    public ECFieldElement square() {
        int[] arrn = Nat192.create();
        SecP192R1Field.square(this.x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    @Override
    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] arrn = Nat192.create();
        SecP192R1Field.subtract(this.x, ((SecP192R1FieldElement)eCFieldElement).x, arrn);
        return new SecP192R1FieldElement(arrn);
    }

    @Override
    public boolean testBitZero() {
        return Nat192.getBit(this.x, 0) == 1;
    }

    @Override
    public BigInteger toBigInteger() {
        return Nat192.toBigInteger(this.x);
    }
}

