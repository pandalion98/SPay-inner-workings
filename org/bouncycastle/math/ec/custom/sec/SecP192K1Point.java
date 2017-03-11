package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.AbstractFp;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat192;

public class SecP192K1Point extends AbstractFp {
    public SecP192K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    public SecP192K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
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

    SecP192K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
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
        SecP192K1FieldElement secP192K1FieldElement = (SecP192K1FieldElement) this.x;
        SecP192K1FieldElement secP192K1FieldElement2 = (SecP192K1FieldElement) this.y;
        SecP192K1FieldElement secP192K1FieldElement3 = (SecP192K1FieldElement) eCPoint.getXCoord();
        SecP192K1FieldElement secP192K1FieldElement4 = (SecP192K1FieldElement) eCPoint.getYCoord();
        SecP192K1FieldElement secP192K1FieldElement5 = (SecP192K1FieldElement) this.zs[0];
        SecP192K1FieldElement secP192K1FieldElement6 = (SecP192K1FieldElement) eCPoint.getZCoord(0);
        int[] createExt = Nat192.createExt();
        int[] create = Nat192.create();
        int[] create2 = Nat192.create();
        int[] create3 = Nat192.create();
        boolean isOne = secP192K1FieldElement5.isOne();
        if (isOne) {
            iArr = secP192K1FieldElement3.f345x;
            iArr2 = secP192K1FieldElement4.f345x;
            iArr3 = iArr;
        } else {
            SecP192K1Field.square(secP192K1FieldElement5.f345x, create2);
            SecP192K1Field.multiply(create2, secP192K1FieldElement3.f345x, create);
            SecP192K1Field.multiply(create2, secP192K1FieldElement5.f345x, create2);
            SecP192K1Field.multiply(create2, secP192K1FieldElement4.f345x, create2);
            iArr2 = create2;
            iArr3 = create;
        }
        boolean isOne2 = secP192K1FieldElement6.isOne();
        if (isOne2) {
            iArr = secP192K1FieldElement.f345x;
            iArr4 = secP192K1FieldElement2.f345x;
            iArr5 = iArr;
        } else {
            SecP192K1Field.square(secP192K1FieldElement6.f345x, create3);
            SecP192K1Field.multiply(create3, secP192K1FieldElement.f345x, createExt);
            SecP192K1Field.multiply(create3, secP192K1FieldElement6.f345x, create3);
            SecP192K1Field.multiply(create3, secP192K1FieldElement2.f345x, create3);
            iArr4 = create3;
            iArr5 = createExt;
        }
        iArr = Nat192.create();
        SecP192K1Field.subtract(iArr5, iArr3, iArr);
        SecP192K1Field.subtract(iArr4, iArr2, create);
        if (Nat192.isZero(iArr)) {
            return Nat192.isZero(create) ? twice() : curve.getInfinity();
        } else {
            SecP192K1Field.square(iArr, create2);
            iArr3 = Nat192.create();
            SecP192K1Field.multiply(create2, iArr, iArr3);
            SecP192K1Field.multiply(create2, iArr5, create2);
            SecP192K1Field.negate(iArr3, iArr3);
            Nat192.mul(iArr4, iArr3, createExt);
            SecP192K1Field.reduce32(Nat192.addBothTo(create2, create2, iArr3), iArr3);
            ECFieldElement secP192K1FieldElement7 = new SecP192K1FieldElement(create3);
            SecP192K1Field.square(create, secP192K1FieldElement7.f345x);
            SecP192K1Field.subtract(secP192K1FieldElement7.f345x, iArr3, secP192K1FieldElement7.f345x);
            ECFieldElement secP192K1FieldElement8 = new SecP192K1FieldElement(iArr3);
            SecP192K1Field.subtract(create2, secP192K1FieldElement7.f345x, secP192K1FieldElement8.f345x);
            SecP192K1Field.multiplyAddToExt(secP192K1FieldElement8.f345x, create, createExt);
            SecP192K1Field.reduce(createExt, secP192K1FieldElement8.f345x);
            secP192K1FieldElement = new SecP192K1FieldElement(iArr);
            if (!isOne) {
                SecP192K1Field.multiply(secP192K1FieldElement.f345x, secP192K1FieldElement5.f345x, secP192K1FieldElement.f345x);
            }
            if (!isOne2) {
                SecP192K1Field.multiply(secP192K1FieldElement.f345x, secP192K1FieldElement6.f345x, secP192K1FieldElement.f345x);
            }
            return new SecP192K1Point(curve, secP192K1FieldElement7, secP192K1FieldElement8, new ECFieldElement[]{secP192K1FieldElement}, this.withCompression);
        }
    }

    protected ECPoint detach() {
        return new SecP192K1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    public ECPoint negate() {
        return isInfinity() ? this : new SecP192K1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twice().add(this);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP192K1FieldElement secP192K1FieldElement = (SecP192K1FieldElement) this.y;
        if (secP192K1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        SecP192K1FieldElement secP192K1FieldElement2 = (SecP192K1FieldElement) this.x;
        SecP192K1FieldElement secP192K1FieldElement3 = (SecP192K1FieldElement) this.zs[0];
        int[] create = Nat192.create();
        SecP192K1Field.square(secP192K1FieldElement.f345x, create);
        int[] create2 = Nat192.create();
        SecP192K1Field.square(create, create2);
        int[] create3 = Nat192.create();
        SecP192K1Field.square(secP192K1FieldElement2.f345x, create3);
        SecP192K1Field.reduce32(Nat192.addBothTo(create3, create3, create3), create3);
        SecP192K1Field.multiply(create, secP192K1FieldElement2.f345x, create);
        SecP192K1Field.reduce32(Nat.shiftUpBits(6, create, 2, 0), create);
        int[] create4 = Nat192.create();
        SecP192K1Field.reduce32(Nat.shiftUpBits(6, create2, 3, 0, create4), create4);
        ECFieldElement secP192K1FieldElement4 = new SecP192K1FieldElement(create2);
        SecP192K1Field.square(create3, secP192K1FieldElement4.f345x);
        SecP192K1Field.subtract(secP192K1FieldElement4.f345x, create, secP192K1FieldElement4.f345x);
        SecP192K1Field.subtract(secP192K1FieldElement4.f345x, create, secP192K1FieldElement4.f345x);
        ECFieldElement secP192K1FieldElement5 = new SecP192K1FieldElement(create);
        SecP192K1Field.subtract(create, secP192K1FieldElement4.f345x, secP192K1FieldElement5.f345x);
        SecP192K1Field.multiply(secP192K1FieldElement5.f345x, create3, secP192K1FieldElement5.f345x);
        SecP192K1Field.subtract(secP192K1FieldElement5.f345x, create4, secP192K1FieldElement5.f345x);
        SecP192K1FieldElement secP192K1FieldElement6 = new SecP192K1FieldElement(create3);
        SecP192K1Field.twice(secP192K1FieldElement.f345x, secP192K1FieldElement6.f345x);
        if (!secP192K1FieldElement3.isOne()) {
            SecP192K1Field.multiply(secP192K1FieldElement6.f345x, secP192K1FieldElement3.f345x, secP192K1FieldElement6.f345x);
        }
        return new SecP192K1Point(curve, secP192K1FieldElement4, secP192K1FieldElement5, new ECFieldElement[]{secP192K1FieldElement6}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : !isInfinity() ? eCPoint.isInfinity() ? twice() : !this.y.isZero() ? twice().add(eCPoint) : eCPoint : eCPoint;
    }
}
