package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Arrays;

public class SecP384R1FieldElement extends ECFieldElement {
    public static final BigInteger f372Q;
    protected int[] f373x;

    static {
        f372Q = SecP384R1Curve.f369q;
    }

    public SecP384R1FieldElement() {
        this.f373x = Nat.create(12);
    }

    public SecP384R1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(f372Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP384R1FieldElement");
        }
        this.f373x = SecP384R1Field.fromBigInteger(bigInteger);
    }

    protected SecP384R1FieldElement(int[] iArr) {
        this.f373x = iArr;
    }

    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(12);
        SecP384R1Field.add(this.f373x, ((SecP384R1FieldElement) eCFieldElement).f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public ECFieldElement addOne() {
        int[] create = Nat.create(12);
        SecP384R1Field.addOne(this.f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(12);
        Mod.invert(SecP384R1Field.f371P, ((SecP384R1FieldElement) eCFieldElement).f373x, create);
        SecP384R1Field.multiply(create, this.f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SecP384R1FieldElement)) {
            return false;
        }
        return Nat.eq(12, this.f373x, ((SecP384R1FieldElement) obj).f373x);
    }

    public String getFieldName() {
        return "SecP384R1Field";
    }

    public int getFieldSize() {
        return f372Q.bitLength();
    }

    public int hashCode() {
        return f372Q.hashCode() ^ Arrays.hashCode(this.f373x, 0, 12);
    }

    public ECFieldElement invert() {
        int[] create = Nat.create(12);
        Mod.invert(SecP384R1Field.f371P, this.f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public boolean isOne() {
        return Nat.isOne(12, this.f373x);
    }

    public boolean isZero() {
        return Nat.isZero(12, this.f373x);
    }

    public ECFieldElement multiply(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(12);
        SecP384R1Field.multiply(this.f373x, ((SecP384R1FieldElement) eCFieldElement).f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public ECFieldElement negate() {
        int[] create = Nat.create(12);
        SecP384R1Field.negate(this.f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public ECFieldElement sqrt() {
        int[] iArr = this.f373x;
        if (Nat.isZero(12, iArr) || Nat.isOne(12, iArr)) {
            return this;
        }
        int[] create = Nat.create(12);
        int[] create2 = Nat.create(12);
        int[] create3 = Nat.create(12);
        int[] create4 = Nat.create(12);
        SecP384R1Field.square(iArr, create);
        SecP384R1Field.multiply(create, iArr, create);
        SecP384R1Field.squareN(create, 2, create2);
        SecP384R1Field.multiply(create2, create, create2);
        SecP384R1Field.square(create2, create2);
        SecP384R1Field.multiply(create2, iArr, create2);
        SecP384R1Field.squareN(create2, 5, create3);
        SecP384R1Field.multiply(create3, create2, create3);
        SecP384R1Field.squareN(create3, 5, create4);
        SecP384R1Field.multiply(create4, create2, create4);
        SecP384R1Field.squareN(create4, 15, create2);
        SecP384R1Field.multiply(create2, create4, create2);
        SecP384R1Field.squareN(create2, 2, create3);
        SecP384R1Field.multiply(create, create3, create);
        SecP384R1Field.squareN(create3, 28, create3);
        SecP384R1Field.multiply(create2, create3, create2);
        SecP384R1Field.squareN(create2, 60, create3);
        SecP384R1Field.multiply(create3, create2, create3);
        SecP384R1Field.squareN(create3, EACTags.COMPATIBLE_TAG_ALLOCATION_AUTHORITY, create2);
        SecP384R1Field.multiply(create2, create3, create2);
        SecP384R1Field.squareN(create2, 15, create2);
        SecP384R1Field.multiply(create2, create4, create2);
        SecP384R1Field.squareN(create2, 33, create2);
        SecP384R1Field.multiply(create2, create, create2);
        SecP384R1Field.squareN(create2, 64, create2);
        SecP384R1Field.multiply(create2, iArr, create2);
        SecP384R1Field.squareN(create2, 30, create);
        SecP384R1Field.square(create, create2);
        return Nat.eq(12, iArr, create2) ? new SecP384R1FieldElement(create) : null;
    }

    public ECFieldElement square() {
        int[] create = Nat.create(12);
        SecP384R1Field.square(this.f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(12);
        SecP384R1Field.subtract(this.f373x, ((SecP384R1FieldElement) eCFieldElement).f373x, create);
        return new SecP384R1FieldElement(create);
    }

    public boolean testBitZero() {
        return Nat.getBit(this.f373x, 0) == 1;
    }

    public BigInteger toBigInteger() {
        return Nat.toBigInteger(12, this.f373x);
    }
}
