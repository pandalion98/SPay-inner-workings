/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.custom.djb;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.custom.djb.Curve25519;
import org.bouncycastle.math.ec.custom.djb.Curve25519Field;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.Arrays;

public class Curve25519FieldElement
extends ECFieldElement {
    private static final int[] PRECOMP_POW2;
    public static final BigInteger Q;
    protected int[] x;

    static {
        Q = Curve25519.q;
        PRECOMP_POW2 = new int[]{1242472624, -991028441, -1389370248, 792926214, 1039914919, 726466713, 1338105611, 730014848};
    }

    public Curve25519FieldElement() {
        this.x = Nat256.create();
    }

    public Curve25519FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for Curve25519FieldElement");
        }
        this.x = Curve25519Field.fromBigInteger(bigInteger);
    }

    protected Curve25519FieldElement(int[] arrn) {
        this.x = arrn;
    }

    @Override
    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        Curve25519Field.add(this.x, ((Curve25519FieldElement)eCFieldElement).x, arrn);
        return new Curve25519FieldElement(arrn);
    }

    @Override
    public ECFieldElement addOne() {
        int[] arrn = Nat256.create();
        Curve25519Field.addOne(this.x, arrn);
        return new Curve25519FieldElement(arrn);
    }

    @Override
    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        Mod.invert(Curve25519Field.P, ((Curve25519FieldElement)eCFieldElement).x, arrn);
        Curve25519Field.multiply(arrn, this.x, arrn);
        return new Curve25519FieldElement(arrn);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Curve25519FieldElement)) {
            return false;
        }
        Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement)object;
        return Nat256.eq(this.x, curve25519FieldElement.x);
    }

    @Override
    public String getFieldName() {
        return "Curve25519Field";
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
        Mod.invert(Curve25519Field.P, this.x, arrn);
        return new Curve25519FieldElement(arrn);
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
        Curve25519Field.multiply(this.x, ((Curve25519FieldElement)eCFieldElement).x, arrn);
        return new Curve25519FieldElement(arrn);
    }

    @Override
    public ECFieldElement negate() {
        int[] arrn = Nat256.create();
        Curve25519Field.negate(this.x, arrn);
        return new Curve25519FieldElement(arrn);
    }

    @Override
    public ECFieldElement sqrt() {
        int[] arrn = this.x;
        if (Nat256.isZero(arrn) || Nat256.isOne(arrn)) {
            return this;
        }
        int[] arrn2 = Nat256.create();
        Curve25519Field.square(arrn, arrn2);
        Curve25519Field.multiply(arrn2, arrn, arrn2);
        Curve25519Field.square(arrn2, arrn2);
        Curve25519Field.multiply(arrn2, arrn, arrn2);
        int[] arrn3 = Nat256.create();
        Curve25519Field.square(arrn2, arrn3);
        Curve25519Field.multiply(arrn3, arrn, arrn3);
        int[] arrn4 = Nat256.create();
        Curve25519Field.squareN(arrn3, 3, arrn4);
        Curve25519Field.multiply(arrn4, arrn2, arrn4);
        Curve25519Field.squareN(arrn4, 4, arrn2);
        Curve25519Field.multiply(arrn2, arrn3, arrn2);
        Curve25519Field.squareN(arrn2, 4, arrn4);
        Curve25519Field.multiply(arrn4, arrn3, arrn4);
        Curve25519Field.squareN(arrn4, 15, arrn3);
        Curve25519Field.multiply(arrn3, arrn4, arrn3);
        Curve25519Field.squareN(arrn3, 30, arrn4);
        Curve25519Field.multiply(arrn4, arrn3, arrn4);
        Curve25519Field.squareN(arrn4, 60, arrn3);
        Curve25519Field.multiply(arrn3, arrn4, arrn3);
        Curve25519Field.squareN(arrn3, 11, arrn4);
        Curve25519Field.multiply(arrn4, arrn2, arrn4);
        Curve25519Field.squareN(arrn4, 120, arrn2);
        Curve25519Field.multiply(arrn2, arrn3, arrn2);
        Curve25519Field.square(arrn2, arrn2);
        Curve25519Field.square(arrn2, arrn3);
        if (Nat256.eq(arrn, arrn3)) {
            return new Curve25519FieldElement(arrn2);
        }
        Curve25519Field.multiply(arrn2, PRECOMP_POW2, arrn2);
        Curve25519Field.square(arrn2, arrn3);
        if (Nat256.eq(arrn, arrn3)) {
            return new Curve25519FieldElement(arrn2);
        }
        return null;
    }

    @Override
    public ECFieldElement square() {
        int[] arrn = Nat256.create();
        Curve25519Field.square(this.x, arrn);
        return new Curve25519FieldElement(arrn);
    }

    @Override
    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] arrn = Nat256.create();
        Curve25519Field.subtract(this.x, ((Curve25519FieldElement)eCFieldElement).x, arrn);
        return new Curve25519FieldElement(arrn);
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

