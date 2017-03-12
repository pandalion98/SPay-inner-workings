package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Arrays;

public class SecP521R1FieldElement extends ECFieldElement {
    public static final BigInteger f376Q;
    protected int[] f377x;

    static {
        f376Q = SecP521R1Curve.f374q;
    }

    public SecP521R1FieldElement() {
        this.f377x = Nat.create(17);
    }

    public SecP521R1FieldElement(BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0 || bigInteger.compareTo(f376Q) >= 0) {
            throw new IllegalArgumentException("x value invalid for SecP521R1FieldElement");
        }
        this.f377x = SecP521R1Field.fromBigInteger(bigInteger);
    }

    protected SecP521R1FieldElement(int[] iArr) {
        this.f377x = iArr;
    }

    public ECFieldElement add(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(17);
        SecP521R1Field.add(this.f377x, ((SecP521R1FieldElement) eCFieldElement).f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public ECFieldElement addOne() {
        int[] create = Nat.create(17);
        SecP521R1Field.addOne(this.f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public ECFieldElement divide(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(17);
        Mod.invert(SecP521R1Field.f375P, ((SecP521R1FieldElement) eCFieldElement).f377x, create);
        SecP521R1Field.multiply(create, this.f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SecP521R1FieldElement)) {
            return false;
        }
        return Nat.eq(17, this.f377x, ((SecP521R1FieldElement) obj).f377x);
    }

    public String getFieldName() {
        return "SecP521R1Field";
    }

    public int getFieldSize() {
        return f376Q.bitLength();
    }

    public int hashCode() {
        return f376Q.hashCode() ^ Arrays.hashCode(this.f377x, 0, 17);
    }

    public ECFieldElement invert() {
        int[] create = Nat.create(17);
        Mod.invert(SecP521R1Field.f375P, this.f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public boolean isOne() {
        return Nat.isOne(17, this.f377x);
    }

    public boolean isZero() {
        return Nat.isZero(17, this.f377x);
    }

    public ECFieldElement multiply(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(17);
        SecP521R1Field.multiply(this.f377x, ((SecP521R1FieldElement) eCFieldElement).f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public ECFieldElement negate() {
        int[] create = Nat.create(17);
        SecP521R1Field.negate(this.f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public ECFieldElement sqrt() {
        int[] iArr = this.f377x;
        if (Nat.isZero(17, iArr) || Nat.isOne(17, iArr)) {
            return this;
        }
        int[] create = Nat.create(17);
        int[] create2 = Nat.create(17);
        SecP521R1Field.squareN(iArr, 519, create);
        SecP521R1Field.square(create, create2);
        return Nat.eq(17, iArr, create2) ? new SecP521R1FieldElement(create) : null;
    }

    public ECFieldElement square() {
        int[] create = Nat.create(17);
        SecP521R1Field.square(this.f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public ECFieldElement subtract(ECFieldElement eCFieldElement) {
        int[] create = Nat.create(17);
        SecP521R1Field.subtract(this.f377x, ((SecP521R1FieldElement) eCFieldElement).f377x, create);
        return new SecP521R1FieldElement(create);
    }

    public boolean testBitZero() {
        return Nat.getBit(this.f377x, 0) == 1;
    }

    public BigInteger toBigInteger() {
        return Nat.toBigInteger(17, this.f377x);
    }
}
