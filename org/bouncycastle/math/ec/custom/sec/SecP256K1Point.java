package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.AbstractFp;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class SecP256K1Point extends AbstractFp {
    public SecP256K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    public SecP256K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
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

    SecP256K1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
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
        SecP256K1FieldElement secP256K1FieldElement = (SecP256K1FieldElement) this.x;
        SecP256K1FieldElement secP256K1FieldElement2 = (SecP256K1FieldElement) this.y;
        SecP256K1FieldElement secP256K1FieldElement3 = (SecP256K1FieldElement) eCPoint.getXCoord();
        SecP256K1FieldElement secP256K1FieldElement4 = (SecP256K1FieldElement) eCPoint.getYCoord();
        SecP256K1FieldElement secP256K1FieldElement5 = (SecP256K1FieldElement) this.zs[0];
        SecP256K1FieldElement secP256K1FieldElement6 = (SecP256K1FieldElement) eCPoint.getZCoord(0);
        int[] createExt = Nat256.createExt();
        int[] create = Nat256.create();
        int[] create2 = Nat256.create();
        int[] create3 = Nat256.create();
        boolean isOne = secP256K1FieldElement5.isOne();
        if (isOne) {
            iArr = secP256K1FieldElement3.f363x;
            iArr2 = secP256K1FieldElement4.f363x;
            iArr3 = iArr;
        } else {
            SecP256K1Field.square(secP256K1FieldElement5.f363x, create2);
            SecP256K1Field.multiply(create2, secP256K1FieldElement3.f363x, create);
            SecP256K1Field.multiply(create2, secP256K1FieldElement5.f363x, create2);
            SecP256K1Field.multiply(create2, secP256K1FieldElement4.f363x, create2);
            iArr2 = create2;
            iArr3 = create;
        }
        boolean isOne2 = secP256K1FieldElement6.isOne();
        if (isOne2) {
            iArr = secP256K1FieldElement.f363x;
            iArr4 = secP256K1FieldElement2.f363x;
            iArr5 = iArr;
        } else {
            SecP256K1Field.square(secP256K1FieldElement6.f363x, create3);
            SecP256K1Field.multiply(create3, secP256K1FieldElement.f363x, createExt);
            SecP256K1Field.multiply(create3, secP256K1FieldElement6.f363x, create3);
            SecP256K1Field.multiply(create3, secP256K1FieldElement2.f363x, create3);
            iArr4 = create3;
            iArr5 = createExt;
        }
        iArr = Nat256.create();
        SecP256K1Field.subtract(iArr5, iArr3, iArr);
        SecP256K1Field.subtract(iArr4, iArr2, create);
        if (Nat256.isZero(iArr)) {
            return Nat256.isZero(create) ? twice() : curve.getInfinity();
        } else {
            SecP256K1Field.square(iArr, create2);
            iArr3 = Nat256.create();
            SecP256K1Field.multiply(create2, iArr, iArr3);
            SecP256K1Field.multiply(create2, iArr5, create2);
            SecP256K1Field.negate(iArr3, iArr3);
            Nat256.mul(iArr4, iArr3, createExt);
            SecP256K1Field.reduce32(Nat256.addBothTo(create2, create2, iArr3), iArr3);
            ECFieldElement secP256K1FieldElement7 = new SecP256K1FieldElement(create3);
            SecP256K1Field.square(create, secP256K1FieldElement7.f363x);
            SecP256K1Field.subtract(secP256K1FieldElement7.f363x, iArr3, secP256K1FieldElement7.f363x);
            ECFieldElement secP256K1FieldElement8 = new SecP256K1FieldElement(iArr3);
            SecP256K1Field.subtract(create2, secP256K1FieldElement7.f363x, secP256K1FieldElement8.f363x);
            SecP256K1Field.multiplyAddToExt(secP256K1FieldElement8.f363x, create, createExt);
            SecP256K1Field.reduce(createExt, secP256K1FieldElement8.f363x);
            secP256K1FieldElement = new SecP256K1FieldElement(iArr);
            if (!isOne) {
                SecP256K1Field.multiply(secP256K1FieldElement.f363x, secP256K1FieldElement5.f363x, secP256K1FieldElement.f363x);
            }
            if (!isOne2) {
                SecP256K1Field.multiply(secP256K1FieldElement.f363x, secP256K1FieldElement6.f363x, secP256K1FieldElement.f363x);
            }
            return new SecP256K1Point(curve, secP256K1FieldElement7, secP256K1FieldElement8, new ECFieldElement[]{secP256K1FieldElement}, this.withCompression);
        }
    }

    protected ECPoint detach() {
        return new SecP256K1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    public ECPoint negate() {
        return isInfinity() ? this : new SecP256K1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twice().add(this);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP256K1FieldElement secP256K1FieldElement = (SecP256K1FieldElement) this.y;
        if (secP256K1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        SecP256K1FieldElement secP256K1FieldElement2 = (SecP256K1FieldElement) this.x;
        SecP256K1FieldElement secP256K1FieldElement3 = (SecP256K1FieldElement) this.zs[0];
        int[] create = Nat256.create();
        SecP256K1Field.square(secP256K1FieldElement.f363x, create);
        int[] create2 = Nat256.create();
        SecP256K1Field.square(create, create2);
        int[] create3 = Nat256.create();
        SecP256K1Field.square(secP256K1FieldElement2.f363x, create3);
        SecP256K1Field.reduce32(Nat256.addBothTo(create3, create3, create3), create3);
        SecP256K1Field.multiply(create, secP256K1FieldElement2.f363x, create);
        SecP256K1Field.reduce32(Nat.shiftUpBits(8, create, 2, 0), create);
        int[] create4 = Nat256.create();
        SecP256K1Field.reduce32(Nat.shiftUpBits(8, create2, 3, 0, create4), create4);
        ECFieldElement secP256K1FieldElement4 = new SecP256K1FieldElement(create2);
        SecP256K1Field.square(create3, secP256K1FieldElement4.f363x);
        SecP256K1Field.subtract(secP256K1FieldElement4.f363x, create, secP256K1FieldElement4.f363x);
        SecP256K1Field.subtract(secP256K1FieldElement4.f363x, create, secP256K1FieldElement4.f363x);
        ECFieldElement secP256K1FieldElement5 = new SecP256K1FieldElement(create);
        SecP256K1Field.subtract(create, secP256K1FieldElement4.f363x, secP256K1FieldElement5.f363x);
        SecP256K1Field.multiply(secP256K1FieldElement5.f363x, create3, secP256K1FieldElement5.f363x);
        SecP256K1Field.subtract(secP256K1FieldElement5.f363x, create4, secP256K1FieldElement5.f363x);
        SecP256K1FieldElement secP256K1FieldElement6 = new SecP256K1FieldElement(create3);
        SecP256K1Field.twice(secP256K1FieldElement.f363x, secP256K1FieldElement6.f363x);
        if (!secP256K1FieldElement3.isOne()) {
            SecP256K1Field.multiply(secP256K1FieldElement6.f363x, secP256K1FieldElement3.f363x, secP256K1FieldElement6.f363x);
        }
        return new SecP256K1Point(curve, secP256K1FieldElement4, secP256K1FieldElement5, new ECFieldElement[]{secP256K1FieldElement6}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : !isInfinity() ? eCPoint.isInfinity() ? twice() : !this.y.isZero() ? twice().add(eCPoint) : eCPoint : eCPoint;
    }
}
