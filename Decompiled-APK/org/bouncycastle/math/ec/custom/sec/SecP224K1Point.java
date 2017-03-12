package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.AbstractFp;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat224;

public class SecP224K1Point extends AbstractFp {
    public SecP224K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    public SecP224K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
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

    SecP224K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
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
        SecP224K1FieldElement secP224K1FieldElement = (SecP224K1FieldElement) this.x;
        SecP224K1FieldElement secP224K1FieldElement2 = (SecP224K1FieldElement) this.y;
        SecP224K1FieldElement secP224K1FieldElement3 = (SecP224K1FieldElement) eCPoint.getXCoord();
        SecP224K1FieldElement secP224K1FieldElement4 = (SecP224K1FieldElement) eCPoint.getYCoord();
        SecP224K1FieldElement secP224K1FieldElement5 = (SecP224K1FieldElement) this.zs[0];
        SecP224K1FieldElement secP224K1FieldElement6 = (SecP224K1FieldElement) eCPoint.getZCoord(0);
        int[] createExt = Nat224.createExt();
        int[] create = Nat224.create();
        int[] create2 = Nat224.create();
        int[] create3 = Nat224.create();
        boolean isOne = secP224K1FieldElement5.isOne();
        if (isOne) {
            iArr = secP224K1FieldElement3.f354x;
            iArr2 = secP224K1FieldElement4.f354x;
            iArr3 = iArr;
        } else {
            SecP224K1Field.square(secP224K1FieldElement5.f354x, create2);
            SecP224K1Field.multiply(create2, secP224K1FieldElement3.f354x, create);
            SecP224K1Field.multiply(create2, secP224K1FieldElement5.f354x, create2);
            SecP224K1Field.multiply(create2, secP224K1FieldElement4.f354x, create2);
            iArr2 = create2;
            iArr3 = create;
        }
        boolean isOne2 = secP224K1FieldElement6.isOne();
        if (isOne2) {
            iArr = secP224K1FieldElement.f354x;
            iArr4 = secP224K1FieldElement2.f354x;
            iArr5 = iArr;
        } else {
            SecP224K1Field.square(secP224K1FieldElement6.f354x, create3);
            SecP224K1Field.multiply(create3, secP224K1FieldElement.f354x, createExt);
            SecP224K1Field.multiply(create3, secP224K1FieldElement6.f354x, create3);
            SecP224K1Field.multiply(create3, secP224K1FieldElement2.f354x, create3);
            iArr4 = create3;
            iArr5 = createExt;
        }
        iArr = Nat224.create();
        SecP224K1Field.subtract(iArr5, iArr3, iArr);
        SecP224K1Field.subtract(iArr4, iArr2, create);
        if (Nat224.isZero(iArr)) {
            return Nat224.isZero(create) ? twice() : curve.getInfinity();
        } else {
            SecP224K1Field.square(iArr, create2);
            iArr3 = Nat224.create();
            SecP224K1Field.multiply(create2, iArr, iArr3);
            SecP224K1Field.multiply(create2, iArr5, create2);
            SecP224K1Field.negate(iArr3, iArr3);
            Nat224.mul(iArr4, iArr3, createExt);
            SecP224K1Field.reduce32(Nat224.addBothTo(create2, create2, iArr3), iArr3);
            ECFieldElement secP224K1FieldElement7 = new SecP224K1FieldElement(create3);
            SecP224K1Field.square(create, secP224K1FieldElement7.f354x);
            SecP224K1Field.subtract(secP224K1FieldElement7.f354x, iArr3, secP224K1FieldElement7.f354x);
            ECFieldElement secP224K1FieldElement8 = new SecP224K1FieldElement(iArr3);
            SecP224K1Field.subtract(create2, secP224K1FieldElement7.f354x, secP224K1FieldElement8.f354x);
            SecP224K1Field.multiplyAddToExt(secP224K1FieldElement8.f354x, create, createExt);
            SecP224K1Field.reduce(createExt, secP224K1FieldElement8.f354x);
            secP224K1FieldElement = new SecP224K1FieldElement(iArr);
            if (!isOne) {
                SecP224K1Field.multiply(secP224K1FieldElement.f354x, secP224K1FieldElement5.f354x, secP224K1FieldElement.f354x);
            }
            if (!isOne2) {
                SecP224K1Field.multiply(secP224K1FieldElement.f354x, secP224K1FieldElement6.f354x, secP224K1FieldElement.f354x);
            }
            return new SecP224K1Point(curve, secP224K1FieldElement7, secP224K1FieldElement8, new ECFieldElement[]{secP224K1FieldElement}, this.withCompression);
        }
    }

    protected ECPoint detach() {
        return new SecP224K1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    public ECPoint negate() {
        return isInfinity() ? this : new SecP224K1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twice().add(this);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP224K1FieldElement secP224K1FieldElement = (SecP224K1FieldElement) this.y;
        if (secP224K1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        SecP224K1FieldElement secP224K1FieldElement2 = (SecP224K1FieldElement) this.x;
        SecP224K1FieldElement secP224K1FieldElement3 = (SecP224K1FieldElement) this.zs[0];
        int[] create = Nat224.create();
        SecP224K1Field.square(secP224K1FieldElement.f354x, create);
        int[] create2 = Nat224.create();
        SecP224K1Field.square(create, create2);
        int[] create3 = Nat224.create();
        SecP224K1Field.square(secP224K1FieldElement2.f354x, create3);
        SecP224K1Field.reduce32(Nat224.addBothTo(create3, create3, create3), create3);
        SecP224K1Field.multiply(create, secP224K1FieldElement2.f354x, create);
        SecP224K1Field.reduce32(Nat.shiftUpBits(7, create, 2, 0), create);
        int[] create4 = Nat224.create();
        SecP224K1Field.reduce32(Nat.shiftUpBits(7, create2, 3, 0, create4), create4);
        ECFieldElement secP224K1FieldElement4 = new SecP224K1FieldElement(create2);
        SecP224K1Field.square(create3, secP224K1FieldElement4.f354x);
        SecP224K1Field.subtract(secP224K1FieldElement4.f354x, create, secP224K1FieldElement4.f354x);
        SecP224K1Field.subtract(secP224K1FieldElement4.f354x, create, secP224K1FieldElement4.f354x);
        ECFieldElement secP224K1FieldElement5 = new SecP224K1FieldElement(create);
        SecP224K1Field.subtract(create, secP224K1FieldElement4.f354x, secP224K1FieldElement5.f354x);
        SecP224K1Field.multiply(secP224K1FieldElement5.f354x, create3, secP224K1FieldElement5.f354x);
        SecP224K1Field.subtract(secP224K1FieldElement5.f354x, create4, secP224K1FieldElement5.f354x);
        SecP224K1FieldElement secP224K1FieldElement6 = new SecP224K1FieldElement(create3);
        SecP224K1Field.twice(secP224K1FieldElement.f354x, secP224K1FieldElement6.f354x);
        if (!secP224K1FieldElement3.isOne()) {
            SecP224K1Field.multiply(secP224K1FieldElement6.f354x, secP224K1FieldElement3.f354x, secP224K1FieldElement6.f354x);
        }
        return new SecP224K1Point(curve, secP224K1FieldElement4, secP224K1FieldElement5, new ECFieldElement[]{secP224K1FieldElement6}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : !isInfinity() ? eCPoint.isInfinity() ? twice() : !this.y.isZero() ? twice().add(eCPoint) : eCPoint : eCPoint;
    }
}
