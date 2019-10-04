/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP192R1Field;
import org.bouncycastle.math.ec.custom.sec.SecP192R1FieldElement;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat192;

public class SecP192R1Point
extends ECPoint.AbstractFp {
    public SecP192R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public SecP192R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
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

    SecP192R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
        super(eCCurve, eCFieldElement, eCFieldElement2, arreCFieldElement);
        this.withCompression = bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ECPoint add(ECPoint eCPoint) {
        int[] arrn;
        int[] arrn2;
        boolean bl;
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
        SecP192R1FieldElement secP192R1FieldElement = (SecP192R1FieldElement)this.x;
        SecP192R1FieldElement secP192R1FieldElement2 = (SecP192R1FieldElement)this.y;
        SecP192R1FieldElement secP192R1FieldElement3 = (SecP192R1FieldElement)eCPoint.getXCoord();
        SecP192R1FieldElement secP192R1FieldElement4 = (SecP192R1FieldElement)eCPoint.getYCoord();
        SecP192R1FieldElement secP192R1FieldElement5 = (SecP192R1FieldElement)this.zs[0];
        SecP192R1FieldElement secP192R1FieldElement6 = (SecP192R1FieldElement)eCPoint.getZCoord(0);
        int[] arrn5 = Nat192.createExt();
        int[] arrn6 = Nat192.create();
        int[] arrn7 = Nat192.create();
        int[] arrn8 = Nat192.create();
        boolean bl2 = secP192R1FieldElement5.isOne();
        if (bl2) {
            int[] arrn9 = secP192R1FieldElement3.x;
            arrn2 = secP192R1FieldElement4.x;
            arrn = arrn9;
        } else {
            SecP192R1Field.square(secP192R1FieldElement5.x, arrn7);
            SecP192R1Field.multiply(arrn7, secP192R1FieldElement3.x, arrn6);
            SecP192R1Field.multiply(arrn7, secP192R1FieldElement5.x, arrn7);
            SecP192R1Field.multiply(arrn7, secP192R1FieldElement4.x, arrn7);
            arrn2 = arrn7;
            arrn = arrn6;
        }
        if (bl = secP192R1FieldElement6.isOne()) {
            int[] arrn10 = secP192R1FieldElement.x;
            arrn4 = secP192R1FieldElement2.x;
            arrn3 = arrn10;
        } else {
            SecP192R1Field.square(secP192R1FieldElement6.x, arrn8);
            SecP192R1Field.multiply(arrn8, secP192R1FieldElement.x, arrn5);
            SecP192R1Field.multiply(arrn8, secP192R1FieldElement6.x, arrn8);
            SecP192R1Field.multiply(arrn8, secP192R1FieldElement2.x, arrn8);
            arrn4 = arrn8;
            arrn3 = arrn5;
        }
        int[] arrn11 = Nat192.create();
        SecP192R1Field.subtract(arrn3, arrn, arrn11);
        SecP192R1Field.subtract(arrn4, arrn2, arrn6);
        if (Nat192.isZero(arrn11)) {
            if (Nat192.isZero(arrn6)) {
                return this.twice();
            }
            return eCCurve.getInfinity();
        }
        SecP192R1Field.square(arrn11, arrn7);
        int[] arrn12 = Nat192.create();
        SecP192R1Field.multiply(arrn7, arrn11, arrn12);
        SecP192R1Field.multiply(arrn7, arrn3, arrn7);
        SecP192R1Field.negate(arrn12, arrn12);
        Nat192.mul(arrn4, arrn12, arrn5);
        SecP192R1Field.reduce32(Nat192.addBothTo(arrn7, arrn7, arrn12), arrn12);
        SecP192R1FieldElement secP192R1FieldElement7 = new SecP192R1FieldElement(arrn8);
        SecP192R1Field.square(arrn6, secP192R1FieldElement7.x);
        SecP192R1Field.subtract(secP192R1FieldElement7.x, arrn12, secP192R1FieldElement7.x);
        SecP192R1FieldElement secP192R1FieldElement8 = new SecP192R1FieldElement(arrn12);
        SecP192R1Field.subtract(arrn7, secP192R1FieldElement7.x, secP192R1FieldElement8.x);
        SecP192R1Field.multiplyAddToExt(secP192R1FieldElement8.x, arrn6, arrn5);
        SecP192R1Field.reduce(arrn5, secP192R1FieldElement8.x);
        SecP192R1FieldElement secP192R1FieldElement9 = new SecP192R1FieldElement(arrn11);
        if (!bl2) {
            SecP192R1Field.multiply(secP192R1FieldElement9.x, secP192R1FieldElement5.x, secP192R1FieldElement9.x);
        }
        if (!bl) {
            SecP192R1Field.multiply(secP192R1FieldElement9.x, secP192R1FieldElement6.x, secP192R1FieldElement9.x);
        }
        return new SecP192R1Point(eCCurve, secP192R1FieldElement7, secP192R1FieldElement8, new ECFieldElement[]{secP192R1FieldElement9}, this.withCompression);
    }

    @Override
    protected ECPoint detach() {
        return new SecP192R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }

    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP192R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    @Override
    public ECPoint threeTimes() {
        if (this.isInfinity() || this.y.isZero()) {
            return this;
        }
        return this.twice().add(this);
    }

    @Override
    public ECPoint twice() {
        if (this.isInfinity()) {
            return this;
        }
        ECCurve eCCurve = this.getCurve();
        SecP192R1FieldElement secP192R1FieldElement = (SecP192R1FieldElement)this.y;
        if (secP192R1FieldElement.isZero()) {
            return eCCurve.getInfinity();
        }
        SecP192R1FieldElement secP192R1FieldElement2 = (SecP192R1FieldElement)this.x;
        SecP192R1FieldElement secP192R1FieldElement3 = (SecP192R1FieldElement)this.zs[0];
        int[] arrn = Nat192.create();
        int[] arrn2 = Nat192.create();
        int[] arrn3 = Nat192.create();
        SecP192R1Field.square(secP192R1FieldElement.x, arrn3);
        int[] arrn4 = Nat192.create();
        SecP192R1Field.square(arrn3, arrn4);
        boolean bl = secP192R1FieldElement3.isOne();
        int[] arrn5 = secP192R1FieldElement3.x;
        if (!bl) {
            SecP192R1Field.square(secP192R1FieldElement3.x, arrn2);
            arrn5 = arrn2;
        }
        SecP192R1Field.subtract(secP192R1FieldElement2.x, arrn5, arrn);
        SecP192R1Field.add(secP192R1FieldElement2.x, arrn5, arrn2);
        SecP192R1Field.multiply(arrn2, arrn, arrn2);
        SecP192R1Field.reduce32(Nat192.addBothTo(arrn2, arrn2, arrn2), arrn2);
        SecP192R1Field.multiply(arrn3, secP192R1FieldElement2.x, arrn3);
        SecP192R1Field.reduce32(Nat.shiftUpBits(6, arrn3, 2, 0), arrn3);
        SecP192R1Field.reduce32(Nat.shiftUpBits(6, arrn4, 3, 0, arrn), arrn);
        SecP192R1FieldElement secP192R1FieldElement4 = new SecP192R1FieldElement(arrn4);
        SecP192R1Field.square(arrn2, secP192R1FieldElement4.x);
        SecP192R1Field.subtract(secP192R1FieldElement4.x, arrn3, secP192R1FieldElement4.x);
        SecP192R1Field.subtract(secP192R1FieldElement4.x, arrn3, secP192R1FieldElement4.x);
        SecP192R1FieldElement secP192R1FieldElement5 = new SecP192R1FieldElement(arrn3);
        SecP192R1Field.subtract(arrn3, secP192R1FieldElement4.x, secP192R1FieldElement5.x);
        SecP192R1Field.multiply(secP192R1FieldElement5.x, arrn2, secP192R1FieldElement5.x);
        SecP192R1Field.subtract(secP192R1FieldElement5.x, arrn, secP192R1FieldElement5.x);
        SecP192R1FieldElement secP192R1FieldElement6 = new SecP192R1FieldElement(arrn2);
        SecP192R1Field.twice(secP192R1FieldElement.x, secP192R1FieldElement6.x);
        if (!bl) {
            SecP192R1Field.multiply(secP192R1FieldElement6.x, secP192R1FieldElement3.x, secP192R1FieldElement6.x);
        }
        return new SecP192R1Point(eCCurve, secP192R1FieldElement4, secP192R1FieldElement5, new ECFieldElement[]{secP192R1FieldElement6}, this.withCompression);
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
        return this.twice().add(eCPoint);
    }
}

