package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.AbstractFp;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat384;

public class SecP384R1Point extends AbstractFp {
    public SecP384R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    public SecP384R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
        Object obj = 1;
        super(eCCurve, eCFieldElement, eCFieldElement2);
        Object obj2 = eCFieldElement == null ? 1 : null;
        if (eCFieldElement2 != null) {
            obj = null;
        }
        if (obj2 != obj) {
            throw new IllegalArgumentException("Exactly one of the field elements is null");
        }
        this.withCompression = z;
    }

    SecP384R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
        super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        this.withCompression = z;
    }

    public ECPoint add(ECPoint eCPoint) {
        if (isInfinity()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return this;
        }
        if (this == eCPoint) {
            return twice();
        }
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int[] iArr4;
        int[] iArr5;
        ECCurve curve = getCurve();
        SecP384R1FieldElement secP384R1FieldElement = (SecP384R1FieldElement) this.x;
        SecP384R1FieldElement secP384R1FieldElement2 = (SecP384R1FieldElement) this.y;
        SecP384R1FieldElement secP384R1FieldElement3 = (SecP384R1FieldElement) eCPoint.getXCoord();
        SecP384R1FieldElement secP384R1FieldElement4 = (SecP384R1FieldElement) eCPoint.getYCoord();
        SecP384R1FieldElement secP384R1FieldElement5 = (SecP384R1FieldElement) this.zs[0];
        SecP384R1FieldElement secP384R1FieldElement6 = (SecP384R1FieldElement) eCPoint.getZCoord(0);
        int[] create = Nat.create(24);
        int[] create2 = Nat.create(24);
        int[] create3 = Nat.create(12);
        int[] create4 = Nat.create(12);
        boolean isOne = secP384R1FieldElement5.isOne();
        if (isOne) {
            iArr = secP384R1FieldElement3.f373x;
            iArr2 = secP384R1FieldElement4.f373x;
            iArr3 = iArr;
        } else {
            SecP384R1Field.square(secP384R1FieldElement5.f373x, create3);
            SecP384R1Field.multiply(create3, secP384R1FieldElement3.f373x, create2);
            SecP384R1Field.multiply(create3, secP384R1FieldElement5.f373x, create3);
            SecP384R1Field.multiply(create3, secP384R1FieldElement4.f373x, create3);
            iArr2 = create3;
            iArr3 = create2;
        }
        boolean isOne2 = secP384R1FieldElement6.isOne();
        if (isOne2) {
            iArr = secP384R1FieldElement.f373x;
            iArr4 = secP384R1FieldElement2.f373x;
            iArr5 = iArr;
        } else {
            SecP384R1Field.square(secP384R1FieldElement6.f373x, create4);
            SecP384R1Field.multiply(create4, secP384R1FieldElement.f373x, create);
            SecP384R1Field.multiply(create4, secP384R1FieldElement6.f373x, create4);
            SecP384R1Field.multiply(create4, secP384R1FieldElement2.f373x, create4);
            iArr4 = create4;
            iArr5 = create;
        }
        iArr = Nat.create(12);
        SecP384R1Field.subtract(iArr5, iArr3, iArr);
        iArr3 = Nat.create(12);
        SecP384R1Field.subtract(iArr4, iArr2, iArr3);
        if (Nat.isZero(12, iArr)) {
            return Nat.isZero(12, iArr3) ? twice() : curve.getInfinity();
        } else {
            SecP384R1Field.square(iArr, create3);
            int[] create5 = Nat.create(12);
            SecP384R1Field.multiply(create3, iArr, create5);
            SecP384R1Field.multiply(create3, iArr5, create3);
            SecP384R1Field.negate(create5, create5);
            Nat384.mul(iArr4, create5, create);
            SecP384R1Field.reduce32(Nat.addBothTo(12, create3, create3, create5), create5);
            ECFieldElement secP384R1FieldElement7 = new SecP384R1FieldElement(create4);
            SecP384R1Field.square(iArr3, secP384R1FieldElement7.f373x);
            SecP384R1Field.subtract(secP384R1FieldElement7.f373x, create5, secP384R1FieldElement7.f373x);
            ECFieldElement secP384R1FieldElement8 = new SecP384R1FieldElement(create5);
            SecP384R1Field.subtract(create3, secP384R1FieldElement7.f373x, secP384R1FieldElement8.f373x);
            Nat384.mul(secP384R1FieldElement8.f373x, iArr3, create2);
            SecP384R1Field.addExt(create, create2, create);
            SecP384R1Field.reduce(create, secP384R1FieldElement8.f373x);
            secP384R1FieldElement = new SecP384R1FieldElement(iArr);
            if (!isOne) {
                SecP384R1Field.multiply(secP384R1FieldElement.f373x, secP384R1FieldElement5.f373x, secP384R1FieldElement.f373x);
            }
            if (!isOne2) {
                SecP384R1Field.multiply(secP384R1FieldElement.f373x, secP384R1FieldElement6.f373x, secP384R1FieldElement.f373x);
            }
            return new SecP384R1Point(curve, secP384R1FieldElement7, secP384R1FieldElement8, new ECFieldElement[]{secP384R1FieldElement}, this.withCompression);
        }
    }

    protected ECPoint detach() {
        return new SecP384R1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    public ECPoint negate() {
        return isInfinity() ? this : new SecP384R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twice().add(this);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP384R1FieldElement secP384R1FieldElement = (SecP384R1FieldElement) this.y;
        if (secP384R1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        SecP384R1FieldElement secP384R1FieldElement2 = (SecP384R1FieldElement) this.x;
        SecP384R1FieldElement secP384R1FieldElement3 = (SecP384R1FieldElement) this.zs[0];
        int[] create = Nat.create(12);
        int[] create2 = Nat.create(12);
        int[] create3 = Nat.create(12);
        SecP384R1Field.square(secP384R1FieldElement.f373x, create3);
        int[] create4 = Nat.create(12);
        SecP384R1Field.square(create3, create4);
        boolean isOne = secP384R1FieldElement3.isOne();
        int[] iArr = secP384R1FieldElement3.f373x;
        if (!isOne) {
            SecP384R1Field.square(secP384R1FieldElement3.f373x, create2);
            iArr = create2;
        }
        SecP384R1Field.subtract(secP384R1FieldElement2.f373x, iArr, create);
        SecP384R1Field.add(secP384R1FieldElement2.f373x, iArr, create2);
        SecP384R1Field.multiply(create2, create, create2);
        SecP384R1Field.reduce32(Nat.addBothTo(12, create2, create2, create2), create2);
        SecP384R1Field.multiply(create3, secP384R1FieldElement2.f373x, create3);
        SecP384R1Field.reduce32(Nat.shiftUpBits(12, create3, 2, 0), create3);
        SecP384R1Field.reduce32(Nat.shiftUpBits(12, create4, 3, 0, create), create);
        ECFieldElement secP384R1FieldElement4 = new SecP384R1FieldElement(create4);
        SecP384R1Field.square(create2, secP384R1FieldElement4.f373x);
        SecP384R1Field.subtract(secP384R1FieldElement4.f373x, create3, secP384R1FieldElement4.f373x);
        SecP384R1Field.subtract(secP384R1FieldElement4.f373x, create3, secP384R1FieldElement4.f373x);
        ECFieldElement secP384R1FieldElement5 = new SecP384R1FieldElement(create3);
        SecP384R1Field.subtract(create3, secP384R1FieldElement4.f373x, secP384R1FieldElement5.f373x);
        SecP384R1Field.multiply(secP384R1FieldElement5.f373x, create2, secP384R1FieldElement5.f373x);
        SecP384R1Field.subtract(secP384R1FieldElement5.f373x, create, secP384R1FieldElement5.f373x);
        SecP384R1FieldElement secP384R1FieldElement6 = new SecP384R1FieldElement(create2);
        SecP384R1Field.twice(secP384R1FieldElement.f373x, secP384R1FieldElement6.f373x);
        if (!isOne) {
            SecP384R1Field.multiply(secP384R1FieldElement6.f373x, secP384R1FieldElement3.f373x, secP384R1FieldElement6.f373x);
        }
        return new SecP384R1Point(curve, secP384R1FieldElement4, secP384R1FieldElement5, new ECFieldElement[]{secP384R1FieldElement6}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : !isInfinity() ? eCPoint.isInfinity() ? twice() : !this.y.isZero() ? twice().add(eCPoint) : eCPoint : eCPoint;
    }
}
