/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package org.bouncycastle.math.ec.custom.djb;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.djb.Curve25519Field;
import org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement;
import org.bouncycastle.math.raw.Nat256;

public class Curve25519Point
extends ECPoint.AbstractFp {
    public Curve25519Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public Curve25519Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
        boolean bl2 = true;
        super(eCCurve, eCFieldElement, eCFieldElement2);
        boolean bl3 = eCFieldElement == null ? bl2 : false;
        if (eCFieldElement2 != null) {
            bl2 = false;
        }
        if (bl3 != bl2) {
            throw new IllegalArgumentException("Exactly one of the field elements is null");
        }
        this.withCompression = bl;
    }

    Curve25519Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
        super(eCCurve, eCFieldElement, eCFieldElement2, arreCFieldElement);
        this.withCompression = bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ECPoint add(ECPoint eCPoint) {
        int[] arrn;
        boolean bl;
        int[] arrn2;
        int[] arrn3;
        int[] arrn4;
        if (this.isInfinity()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return this;
        }
        if (this == eCPoint) {
            return this.twice();
        }
        ECCurve eCCurve = this.getCurve();
        Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement)this.x;
        Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement)this.y;
        Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement)this.zs[0];
        Curve25519FieldElement curve25519FieldElement4 = (Curve25519FieldElement)eCPoint.getXCoord();
        Curve25519FieldElement curve25519FieldElement5 = (Curve25519FieldElement)eCPoint.getYCoord();
        Curve25519FieldElement curve25519FieldElement6 = (Curve25519FieldElement)eCPoint.getZCoord(0);
        int[] arrn5 = Nat256.createExt();
        int[] arrn6 = Nat256.create();
        int[] arrn7 = Nat256.create();
        int[] arrn8 = Nat256.create();
        boolean bl2 = curve25519FieldElement3.isOne();
        if (bl2) {
            int[] arrn9 = curve25519FieldElement4.x;
            arrn = curve25519FieldElement5.x;
            arrn3 = arrn9;
        } else {
            Curve25519Field.square(curve25519FieldElement3.x, arrn7);
            Curve25519Field.multiply(arrn7, curve25519FieldElement4.x, arrn6);
            Curve25519Field.multiply(arrn7, curve25519FieldElement3.x, arrn7);
            Curve25519Field.multiply(arrn7, curve25519FieldElement5.x, arrn7);
            arrn = arrn7;
            arrn3 = arrn6;
        }
        if (bl = curve25519FieldElement6.isOne()) {
            int[] arrn10 = curve25519FieldElement.x;
            arrn2 = curve25519FieldElement2.x;
            arrn4 = arrn10;
        } else {
            Curve25519Field.square(curve25519FieldElement6.x, arrn8);
            Curve25519Field.multiply(arrn8, curve25519FieldElement.x, arrn5);
            Curve25519Field.multiply(arrn8, curve25519FieldElement6.x, arrn8);
            Curve25519Field.multiply(arrn8, curve25519FieldElement2.x, arrn8);
            arrn2 = arrn8;
            arrn4 = arrn5;
        }
        int[] arrn11 = Nat256.create();
        Curve25519Field.subtract(arrn4, arrn3, arrn11);
        Curve25519Field.subtract(arrn2, arrn, arrn6);
        if (Nat256.isZero(arrn11)) {
            if (Nat256.isZero(arrn6)) {
                return this.twice();
            }
            return eCCurve.getInfinity();
        }
        int[] arrn12 = Nat256.create();
        Curve25519Field.square(arrn11, arrn12);
        int[] arrn13 = Nat256.create();
        Curve25519Field.multiply(arrn12, arrn11, arrn13);
        Curve25519Field.multiply(arrn12, arrn4, arrn7);
        Curve25519Field.negate(arrn13, arrn13);
        Nat256.mul(arrn2, arrn13, arrn5);
        Curve25519Field.reduce27(Nat256.addBothTo(arrn7, arrn7, arrn13), arrn13);
        Curve25519FieldElement curve25519FieldElement7 = new Curve25519FieldElement(arrn8);
        Curve25519Field.square(arrn6, curve25519FieldElement7.x);
        Curve25519Field.subtract(curve25519FieldElement7.x, arrn13, curve25519FieldElement7.x);
        Curve25519FieldElement curve25519FieldElement8 = new Curve25519FieldElement(arrn13);
        Curve25519Field.subtract(arrn7, curve25519FieldElement7.x, curve25519FieldElement8.x);
        Curve25519Field.multiplyAddToExt(curve25519FieldElement8.x, arrn6, arrn5);
        Curve25519Field.reduce(arrn5, curve25519FieldElement8.x);
        Curve25519FieldElement curve25519FieldElement9 = new Curve25519FieldElement(arrn11);
        if (!bl2) {
            Curve25519Field.multiply(curve25519FieldElement9.x, curve25519FieldElement3.x, curve25519FieldElement9.x);
        }
        if (!bl) {
            Curve25519Field.multiply(curve25519FieldElement9.x, curve25519FieldElement6.x, curve25519FieldElement9.x);
        }
        int[] arrn14 = bl2 && bl ? arrn12 : null;
        return new Curve25519Point(eCCurve, curve25519FieldElement7, curve25519FieldElement8, new ECFieldElement[]{curve25519FieldElement9, this.calculateJacobianModifiedW(curve25519FieldElement9, arrn14)}, this.withCompression);
    }

    protected Curve25519FieldElement calculateJacobianModifiedW(Curve25519FieldElement curve25519FieldElement, int[] arrn) {
        Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement)this.getCurve().getA();
        if (curve25519FieldElement.isOne()) {
            return curve25519FieldElement2;
        }
        Curve25519FieldElement curve25519FieldElement3 = new Curve25519FieldElement();
        if (arrn == null) {
            arrn = curve25519FieldElement3.x;
            Curve25519Field.square(curve25519FieldElement.x, arrn);
        }
        Curve25519Field.square(arrn, curve25519FieldElement3.x);
        Curve25519Field.multiply(curve25519FieldElement3.x, curve25519FieldElement2.x, curve25519FieldElement3.x);
        return curve25519FieldElement3;
    }

    @Override
    protected ECPoint detach() {
        return new Curve25519Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }

    protected Curve25519FieldElement getJacobianModifiedW() {
        Curve25519FieldElement curve25519FieldElement = (Curve25519FieldElement)this.zs[1];
        if (curve25519FieldElement == null) {
            ECFieldElement[] arreCFieldElement = this.zs;
            curve25519FieldElement = this.calculateJacobianModifiedW((Curve25519FieldElement)this.zs[0], null);
            arreCFieldElement[1] = curve25519FieldElement;
        }
        return curve25519FieldElement;
    }

    @Override
    public ECFieldElement getZCoord(int n) {
        if (n == 1) {
            return this.getJacobianModifiedW();
        }
        return super.getZCoord(n);
    }

    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new Curve25519Point(this.getCurve(), this.x, this.y.negate(), this.zs, this.withCompression);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ECPoint threeTimes() {
        if (this.isInfinity() || this.y.isZero()) {
            return this;
        }
        return this.twiceJacobianModified(false).add(this);
    }

    @Override
    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        }
        ECCurve eCCurve = this.getCurve();
        if (this.y.isZero()) {
            return eCCurve.getInfinity();
        }
        return this.twiceJacobianModified(true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected Curve25519Point twiceJacobianModified(boolean bl) {
        Curve25519FieldElement curve25519FieldElement;
        Curve25519FieldElement curve25519FieldElement2 = (Curve25519FieldElement)this.x;
        Curve25519FieldElement curve25519FieldElement3 = (Curve25519FieldElement)this.y;
        Curve25519FieldElement curve25519FieldElement4 = (Curve25519FieldElement)this.zs[0];
        Curve25519FieldElement curve25519FieldElement5 = this.getJacobianModifiedW();
        int[] arrn = Nat256.create();
        Curve25519Field.square(curve25519FieldElement2.x, arrn);
        Curve25519Field.reduce27(Nat256.addBothTo(arrn, arrn, arrn) + Nat256.addTo(curve25519FieldElement5.x, arrn), arrn);
        int[] arrn2 = Nat256.create();
        Curve25519Field.twice(curve25519FieldElement3.x, arrn2);
        int[] arrn3 = Nat256.create();
        Curve25519Field.multiply(arrn2, curve25519FieldElement3.x, arrn3);
        int[] arrn4 = Nat256.create();
        Curve25519Field.multiply(arrn3, curve25519FieldElement2.x, arrn4);
        Curve25519Field.twice(arrn4, arrn4);
        int[] arrn5 = Nat256.create();
        Curve25519Field.square(arrn3, arrn5);
        Curve25519Field.twice(arrn5, arrn5);
        Curve25519FieldElement curve25519FieldElement6 = new Curve25519FieldElement(arrn3);
        Curve25519Field.square(arrn, curve25519FieldElement6.x);
        Curve25519Field.subtract(curve25519FieldElement6.x, arrn4, curve25519FieldElement6.x);
        Curve25519Field.subtract(curve25519FieldElement6.x, arrn4, curve25519FieldElement6.x);
        Curve25519FieldElement curve25519FieldElement7 = new Curve25519FieldElement(arrn4);
        Curve25519Field.subtract(arrn4, curve25519FieldElement6.x, curve25519FieldElement7.x);
        Curve25519Field.multiply(curve25519FieldElement7.x, arrn, curve25519FieldElement7.x);
        Curve25519Field.subtract(curve25519FieldElement7.x, arrn5, curve25519FieldElement7.x);
        Curve25519FieldElement curve25519FieldElement8 = new Curve25519FieldElement(arrn2);
        if (!Nat256.isOne(curve25519FieldElement4.x)) {
            Curve25519Field.multiply(curve25519FieldElement8.x, curve25519FieldElement4.x, curve25519FieldElement8.x);
        }
        if (bl) {
            Curve25519FieldElement curve25519FieldElement9 = new Curve25519FieldElement(arrn5);
            Curve25519Field.multiply(curve25519FieldElement9.x, curve25519FieldElement5.x, curve25519FieldElement9.x);
            Curve25519Field.twice(curve25519FieldElement9.x, curve25519FieldElement9.x);
            curve25519FieldElement = curve25519FieldElement9;
            do {
                return new Curve25519Point(this.getCurve(), curve25519FieldElement6, curve25519FieldElement7, new ECFieldElement[]{curve25519FieldElement8, curve25519FieldElement}, this.withCompression);
                break;
            } while (true);
        }
        curve25519FieldElement = null;
        return new Curve25519Point(this.getCurve(), curve25519FieldElement6, curve25519FieldElement7, new ECFieldElement[]{curve25519FieldElement8, curve25519FieldElement}, this.withCompression);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public ECPoint twicePlus(ECPoint eCPoint) {
        if (this == eCPoint) {
            return this.threeTimes();
        }
        if (this.isInfinity()) return eCPoint;
        if (eCPoint.isInfinity()) {
            return this.twice();
        }
        if (this.y.isZero()) return eCPoint;
        return this.twiceJacobianModified(false).add(eCPoint);
    }
}

