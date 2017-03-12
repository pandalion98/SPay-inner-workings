package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat192;
import org.bouncycastle.util.Arrays;

public class SecP192R1FieldElement extends ECFieldElement {
    public static final BigInteger f349Q;
    protected int[] f350x;

    static {
        f349Q = SecP192R1Curve.f346q;
    }

    public SecP192R1FieldElement() {
        this.f350x = Nat192.create();
    }

    public SecP192R1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(f349Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP192R1FieldElement");
        }
        this.f350x = SecP192R1Field.fromBigInteger(bigInteger);
    }

    protected SecP192R1FieldElement(int[] iArr) {
        this.f350x = iArr;
    }

    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] create = Nat192.create();
        SecP192R1Field.add(this.f350x, ((SecP192R1FieldElement) eCFieldElement).f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public ECFieldElement addOne() {
        int[] create = Nat192.create();
        SecP192R1Field.addOne(this.f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] create = Nat192.create();
        Mod.invert(SecP192R1Field.f348P, ((SecP192R1FieldElement) eCFieldElement).f350x, create);
        SecP192R1Field.multiply(create, this.f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SecP192R1FieldElement)) {
            return false;
        }
        return Nat192.eq(this.f350x, ((SecP192R1FieldElement) obj).f350x);
    }

    public String getFieldName() {
        return "SecP192R1Field";
    }

    public int getFieldSize() {
        return f349Q.bitLength();
    }

    public int hashCode() {
        return f349Q.hashCode() ^ Arrays.hashCode(this.f350x, 0, 6);
    }

    public ECFieldElement invert() {
        int[] create = Nat192.create();
        Mod.invert(SecP192R1Field.f348P, this.f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public boolean isOne() {
        return Nat192.isOne(this.f350x);
    }

    public boolean isZero() {
        return Nat192.isZero(this.f350x);
    }

    public ECFieldElement multiply(ECFieldElement eCFieldElement) {
        int[] create = Nat192.create();
        SecP192R1Field.multiply(this.f350x, ((SecP192R1FieldElement) eCFieldElement).f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public ECFieldElement negate() {
        int[] create = Nat192.create();
        SecP192R1Field.negate(this.f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public ECFieldElement sqrt() {
        int[] iArr = this.f350x;
        if (Nat192.isZero(iArr) || Nat192.isOne(iArr)) {
            return this;
        }
        int[] create = Nat192.create();
        int[] create2 = Nat192.create();
        SecP192R1Field.square(iArr, create);
        SecP192R1Field.multiply(create, iArr, create);
        SecP192R1Field.squareN(create, 2, create2);
        SecP192R1Field.multiply(create2, create, create2);
        SecP192R1Field.squareN(create2, 4, create);
        SecP192R1Field.multiply(create, create2, create);
        SecP192R1Field.squareN(create, 8, create2);
        SecP192R1Field.multiply(create2, create, create2);
        SecP192R1Field.squareN(create2, 16, create);
        SecP192R1Field.multiply(create, create2, create);
        SecP192R1Field.squareN(create, 32, create2);
        SecP192R1Field.multiply(create2, create, create2);
        SecP192R1Field.squareN(create2, 64, create);
        SecP192R1Field.multiply(create, create2, create);
        SecP192R1Field.squareN(create, 62, create);
        SecP192R1Field.square(create, create2);
        return Nat192.eq(iArr, create2) ? new SecP192R1FieldElement(create) : null;
    }

    public ECFieldElement square() {
        int[] create = Nat192.create();
        SecP192R1Field.square(this.f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] create = Nat192.create();
        SecP192R1Field.subtract(this.f350x, ((SecP192R1FieldElement) eCFieldElement).f350x, create);
        return new SecP192R1FieldElement(create);
    }

    public boolean testBitZero() {
        return Nat192.getBit(this.f350x, 0) == 1;
    }

    public BigInteger toBigInteger() {
        return Nat192.toBigInteger(this.f350x);
    }
}
