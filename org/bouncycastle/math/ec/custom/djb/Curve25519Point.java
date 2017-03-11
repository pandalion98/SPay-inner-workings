package org.bouncycastle.math.ec.custom.djb;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPoint.AbstractFp;
import org.bouncycastle.math.raw.Nat256;

public class Curve25519Point extends AbstractFp {
    public Curve25519Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    public Curve25519Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
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

    Curve25519Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
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
        Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement) this.x;
        Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement) this.y;
        Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement) this.zs[0];
        Curve25519FieldElement curve25519FieldElement4 = (Curve25519FieldElement) eCPoint.getXCoord();
        Curve25519FieldElement curve25519FieldElement5 = (Curve25519FieldElement) eCPoint.getYCoord();
        Curve25519FieldElement curve25519FieldElement6 = (Curve25519FieldElement) eCPoint.getZCoord(0);
        int[] createExt = Nat256.createExt();
        int[] create = Nat256.create();
        int[] create2 = Nat256.create();
        int[] create3 = Nat256.create();
        boolean isOne = curve25519FieldElement3.isOne();
        if (isOne) {
            iArr = curve25519FieldElement4.f341x;
            iArr2 = curve25519FieldElement5.f341x;
            iArr3 = iArr;
        } else {
            Curve25519Field.square(curve25519FieldElement3.f341x, create2);
            Curve25519Field.multiply(create2, curve25519FieldElement4.f341x, create);
            Curve25519Field.multiply(create2, curve25519FieldElement3.f341x, create2);
            Curve25519Field.multiply(create2, curve25519FieldElement5.f341x, create2);
            iArr2 = create2;
            iArr3 = create;
        }
        boolean isOne2 = curve25519FieldElement6.isOne();
        if (isOne2) {
            iArr = curve25519FieldElement.f341x;
            iArr4 = curve25519FieldElement2.f341x;
            iArr5 = iArr;
        } else {
            Curve25519Field.square(curve25519FieldElement6.f341x, create3);
            Curve25519Field.multiply(create3, curve25519FieldElement.f341x, createExt);
            Curve25519Field.multiply(create3, curve25519FieldElement6.f341x, create3);
            Curve25519Field.multiply(create3, curve25519FieldElement2.f341x, create3);
            iArr4 = create3;
            iArr5 = createExt;
        }
        iArr = Nat256.create();
        Curve25519Field.subtract(iArr5, iArr3, iArr);
        Curve25519Field.subtract(iArr4, iArr2, create);
        if (Nat256.isZero(iArr)) {
            return Nat256.isZero(create) ? twice() : curve.getInfinity();
        } else {
            iArr3 = Nat256.create();
            Curve25519Field.square(iArr, iArr3);
            int[] create4 = Nat256.create();
            Curve25519Field.multiply(iArr3, iArr, create4);
            Curve25519Field.multiply(iArr3, iArr5, create2);
            Curve25519Field.negate(create4, create4);
            Nat256.mul(iArr4, create4, createExt);
            Curve25519Field.reduce27(Nat256.addBothTo(create2, create2, create4), create4);
            ECFieldElement curve25519FieldElement7 = new Curve25519FieldElement(create3);
            Curve25519Field.square(create, curve25519FieldElement7.f341x);
            Curve25519Field.subtract(curve25519FieldElement7.f341x, create4, curve25519FieldElement7.f341x);
            ECFieldElement curve25519FieldElement8 = new Curve25519FieldElement(create4);
            Curve25519Field.subtract(create2, curve25519FieldElement7.f341x, curve25519FieldElement8.f341x);
            Curve25519Field.multiplyAddToExt(curve25519FieldElement8.f341x, create, createExt);
            Curve25519Field.reduce(createExt, curve25519FieldElement8.f341x);
            Curve25519FieldElement curve25519FieldElement9 = new Curve25519FieldElement(iArr);
            if (!isOne) {
                Curve25519Field.multiply(curve25519FieldElement9.f341x, curve25519FieldElement3.f341x, curve25519FieldElement9.f341x);
            }
            if (!isOne2) {
                Curve25519Field.multiply(curve25519FieldElement9.f341x, curve25519FieldElement6.f341x, curve25519FieldElement9.f341x);
            }
            iArr4 = (isOne && isOne2) ? iArr3 : null;
            curve25519FieldElement = calculateJacobianModifiedW(curve25519FieldElement9, iArr4);
            return new Curve25519Point(curve, curve25519FieldElement7, curve25519FieldElement8, new ECFieldElement[]{curve25519FieldElement9, curve25519FieldElement}, this.withCompression);
        }
    }

    protected Curve25519FieldElement calculateJacobianModifiedW(Curve25519FieldElement curve25519FieldElement, int[] iArr) {
        Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement) getCurve().getA();
        if (curve25519FieldElement.isOne()) {
            return curve25519FieldElement2;
        }
        Curve25519FieldElement curve25519FieldElement3 = new Curve25519FieldElement();
        if (iArr == null) {
            iArr = curve25519FieldElement3.f341x;
            Curve25519Field.square(curve25519FieldElement.f341x, iArr);
        }
        Curve25519Field.square(iArr, curve25519FieldElement3.f341x);
        Curve25519Field.multiply(curve25519FieldElement3.f341x, curve25519FieldElement2.f341x, curve25519FieldElement3.f341x);
        return curve25519FieldElement3;
    }

    protected ECPoint detach() {
        return new Curve25519Point(null, getAffineXCoord(), getAffineYCoord());
    }

    protected Curve25519FieldElement getJacobianModifiedW() {
        Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement) this.zs[1];
        if (curve25519FieldElement != null) {
            return curve25519FieldElement;
        }
        ECFieldElement[] eCFieldElementArr = this.zs;
        curve25519FieldElement = calculateJacobianModifiedW((Curve25519FieldElement) this.zs[0], null);
        eCFieldElementArr[1] = curve25519FieldElement;
        return curve25519FieldElement;
    }

    public ECFieldElement getZCoord(int i) {
        return i == 1 ? getJacobianModifiedW() : super.getZCoord(i);
    }

    public ECPoint negate() {
        return isInfinity() ? this : new Curve25519Point(getCurve(), this.x, this.y.negate(), this.zs, this.withCompression);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twiceJacobianModified(false).add(this);
    }

    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        return this.y.isZero() ? getCurve().getInfinity() : twiceJacobianModified(true);
    }

    protected Curve25519Point twiceJacobianModified(boolean z) {
        ECFieldElement eCFieldElement;
        Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement) this.x;
        Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement) this.y;
        Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement) this.zs[0];
        Curve25519FieldElement jacobianModifiedW = getJacobianModifiedW();
        int[] create = Nat256.create();
        Curve25519Field.square(curve25519FieldElement.f341x, create);
        Curve25519Field.reduce27(Nat256.addBothTo(create, create, create) + Nat256.addTo(jacobianModifiedW.f341x, create), create);
        int[] create2 = Nat256.create();
        Curve25519Field.twice(curve25519FieldElement2.f341x, create2);
        int[] create3 = Nat256.create();
        Curve25519Field.multiply(create2, curve25519FieldElement2.f341x, create3);
        int[] create4 = Nat256.create();
        Curve25519Field.multiply(create3, curve25519FieldElement.f341x, create4);
        Curve25519Field.twice(create4, create4);
        int[] create5 = Nat256.create();
        Curve25519Field.square(create3, create5);
        Curve25519Field.twice(create5, create5);
        ECFieldElement curve25519FieldElement4 = new Curve25519FieldElement(create3);
        Curve25519Field.square(create, curve25519FieldElement4.f341x);
        Curve25519Field.subtract(curve25519FieldElement4.f341x, create4, curve25519FieldElement4.f341x);
        Curve25519Field.subtract(curve25519FieldElement4.f341x, create4, curve25519FieldElement4.f341x);
        ECFieldElement curve25519FieldElement5 = new Curve25519FieldElement(create4);
        Curve25519Field.subtract(create4, curve25519FieldElement4.f341x, curve25519FieldElement5.f341x);
        Curve25519Field.multiply(curve25519FieldElement5.f341x, create, curve25519FieldElement5.f341x);
        Curve25519Field.subtract(curve25519FieldElement5.f341x, create5, curve25519FieldElement5.f341x);
        Curve25519FieldElement curve25519FieldElement6 = new Curve25519FieldElement(create2);
        if (!Nat256.isOne(curve25519FieldElement3.f341x)) {
            Curve25519Field.multiply(curve25519FieldElement6.f341x, curve25519FieldElement3.f341x, curve25519FieldElement6.f341x);
        }
        if (z) {
            ECFieldElement curve25519FieldElement7 = new Curve25519FieldElement(create5);
            Curve25519Field.multiply(curve25519FieldElement7.f341x, jacobianModifiedW.f341x, curve25519FieldElement7.f341x);
            Curve25519Field.twice(curve25519FieldElement7.f341x, curve25519FieldElement7.f341x);
            eCFieldElement = curve25519FieldElement7;
        } else {
            eCFieldElement = null;
        }
        return new Curve25519Point(getCurve(), curve25519FieldElement4, curve25519FieldElement5, new ECFieldElement[]{curve25519FieldElement6, eCFieldElement}, this.withCompression);
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : !isInfinity() ? eCPoint.isInfinity() ? twice() : !this.y.isZero() ? twiceJacobianModified(false).add(eCPoint) : eCPoint : eCPoint;
    }
}
