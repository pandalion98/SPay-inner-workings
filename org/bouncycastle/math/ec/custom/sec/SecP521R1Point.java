package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.AbstractFp;
import org.bouncycastle.math.raw.Nat;

public class SecP521R1Point extends AbstractFp {
    public SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    public SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
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

    SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
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
        SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement) this.x;
        SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement) this.y;
        SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement) eCPoint.getXCoord();
        SecP521R1FieldElement secP521R1FieldElement4 = (SecP521R1FieldElement) eCPoint.getYCoord();
        SecP521R1FieldElement secP521R1FieldElement5 = (SecP521R1FieldElement) this.zs[0];
        SecP521R1FieldElement secP521R1FieldElement6 = (SecP521R1FieldElement) eCPoint.getZCoord(0);
        int[] create = Nat.create(17);
        int[] create2 = Nat.create(17);
        int[] create3 = Nat.create(17);
        int[] create4 = Nat.create(17);
        boolean isOne = secP521R1FieldElement5.isOne();
        if (isOne) {
            iArr = secP521R1FieldElement3.f377x;
            iArr2 = secP521R1FieldElement4.f377x;
            iArr3 = iArr;
        } else {
            SecP521R1Field.square(secP521R1FieldElement5.f377x, create3);
            SecP521R1Field.multiply(create3, secP521R1FieldElement3.f377x, create2);
            SecP521R1Field.multiply(create3, secP521R1FieldElement5.f377x, create3);
            SecP521R1Field.multiply(create3, secP521R1FieldElement4.f377x, create3);
            iArr2 = create3;
            iArr3 = create2;
        }
        boolean isOne2 = secP521R1FieldElement6.isOne();
        if (isOne2) {
            iArr = secP521R1FieldElement.f377x;
            iArr4 = secP521R1FieldElement2.f377x;
            iArr5 = iArr;
        } else {
            SecP521R1Field.square(secP521R1FieldElement6.f377x, create4);
            SecP521R1Field.multiply(create4, secP521R1FieldElement.f377x, create);
            SecP521R1Field.multiply(create4, secP521R1FieldElement6.f377x, create4);
            SecP521R1Field.multiply(create4, secP521R1FieldElement2.f377x, create4);
            iArr4 = create4;
            iArr5 = create;
        }
        iArr = Nat.create(17);
        SecP521R1Field.subtract(iArr5, iArr3, iArr);
        SecP521R1Field.subtract(iArr4, iArr2, create2);
        if (Nat.isZero(17, iArr)) {
            return Nat.isZero(17, create2) ? twice() : curve.getInfinity();
        } else {
            SecP521R1Field.square(iArr, create3);
            iArr3 = Nat.create(17);
            SecP521R1Field.multiply(create3, iArr, iArr3);
            SecP521R1Field.multiply(create3, iArr5, create3);
            SecP521R1Field.multiply(iArr4, iArr3, create);
            ECFieldElement secP521R1FieldElement7 = new SecP521R1FieldElement(create4);
            SecP521R1Field.square(create2, secP521R1FieldElement7.f377x);
            SecP521R1Field.add(secP521R1FieldElement7.f377x, iArr3, secP521R1FieldElement7.f377x);
            SecP521R1Field.subtract(secP521R1FieldElement7.f377x, create3, secP521R1FieldElement7.f377x);
            SecP521R1Field.subtract(secP521R1FieldElement7.f377x, create3, secP521R1FieldElement7.f377x);
            ECFieldElement secP521R1FieldElement8 = new SecP521R1FieldElement(iArr3);
            SecP521R1Field.subtract(create3, secP521R1FieldElement7.f377x, secP521R1FieldElement8.f377x);
            SecP521R1Field.multiply(secP521R1FieldElement8.f377x, create2, create2);
            SecP521R1Field.subtract(create2, create, secP521R1FieldElement8.f377x);
            secP521R1FieldElement = new SecP521R1FieldElement(iArr);
            if (!isOne) {
                SecP521R1Field.multiply(secP521R1FieldElement.f377x, secP521R1FieldElement5.f377x, secP521R1FieldElement.f377x);
            }
            if (!isOne2) {
                SecP521R1Field.multiply(secP521R1FieldElement.f377x, secP521R1FieldElement6.f377x, secP521R1FieldElement.f377x);
            }
            return new SecP521R1Point(curve, secP521R1FieldElement7, secP521R1FieldElement8, new ECFieldElement[]{secP521R1FieldElement}, this.withCompression);
        }
    }

    protected ECPoint detach() {
        return new SecP521R1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    protected ECFieldElement doubleProductFromSquares(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3, ECFieldElement eCFieldElement4) {
        return eCFieldElement.add(eCFieldElement2).square().subtract(eCFieldElement3).subtract(eCFieldElement4);
    }

    protected ECFieldElement eight(ECFieldElement eCFieldElement) {
        return four(two(eCFieldElement));
    }

    protected ECFieldElement four(ECFieldElement eCFieldElement) {
        return two(two(eCFieldElement));
    }

    public ECPoint negate() {
        return isInfinity() ? this : new SecP521R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    protected ECFieldElement three(ECFieldElement eCFieldElement) {
        return two(eCFieldElement).add(eCFieldElement);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twice().add(this);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement) this.y;
        if (secP521R1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement) this.x;
        SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement) this.zs[0];
        int[] create = Nat.create(17);
        int[] create2 = Nat.create(17);
        int[] create3 = Nat.create(17);
        SecP521R1Field.square(secP521R1FieldElement.f377x, create3);
        int[] create4 = Nat.create(17);
        SecP521R1Field.square(create3, create4);
        boolean isOne = secP521R1FieldElement3.isOne();
        int[] iArr = secP521R1FieldElement3.f377x;
        if (!isOne) {
            SecP521R1Field.square(secP521R1FieldElement3.f377x, create2);
            iArr = create2;
        }
        SecP521R1Field.subtract(secP521R1FieldElement2.f377x, iArr, create);
        SecP521R1Field.add(secP521R1FieldElement2.f377x, iArr, create2);
        SecP521R1Field.multiply(create2, create, create2);
        Nat.addBothTo(17, create2, create2, create2);
        SecP521R1Field.reduce23(create2);
        SecP521R1Field.multiply(create3, secP521R1FieldElement2.f377x, create3);
        Nat.shiftUpBits(17, create3, 2, 0);
        SecP521R1Field.reduce23(create3);
        Nat.shiftUpBits(17, create4, 3, 0, create);
        SecP521R1Field.reduce23(create);
        ECFieldElement secP521R1FieldElement4 = new SecP521R1FieldElement(create4);
        SecP521R1Field.square(create2, secP521R1FieldElement4.f377x);
        SecP521R1Field.subtract(secP521R1FieldElement4.f377x, create3, secP521R1FieldElement4.f377x);
        SecP521R1Field.subtract(secP521R1FieldElement4.f377x, create3, secP521R1FieldElement4.f377x);
        ECFieldElement secP521R1FieldElement5 = new SecP521R1FieldElement(create3);
        SecP521R1Field.subtract(create3, secP521R1FieldElement4.f377x, secP521R1FieldElement5.f377x);
        SecP521R1Field.multiply(secP521R1FieldElement5.f377x, create2, secP521R1FieldElement5.f377x);
        SecP521R1Field.subtract(secP521R1FieldElement5.f377x, create, secP521R1FieldElement5.f377x);
        SecP521R1FieldElement secP521R1FieldElement6 = new SecP521R1FieldElement(create2);
        SecP521R1Field.twice(secP521R1FieldElement.f377x, secP521R1FieldElement6.f377x);
        if (!isOne) {
            SecP521R1Field.multiply(secP521R1FieldElement6.f377x, secP521R1FieldElement3.f377x, secP521R1FieldElement6.f377x);
        }
        return new SecP521R1Point(curve, secP521R1FieldElement4, secP521R1FieldElement5, new ECFieldElement[]{secP521R1FieldElement6}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : !isInfinity() ? eCPoint.isInfinity() ? twice() : !this.y.isZero() ? twice().add(eCPoint) : eCPoint : eCPoint;
    }

    protected ECFieldElement two(ECFieldElement eCFieldElement) {
        return eCFieldElement.add(eCFieldElement);
    }
}
