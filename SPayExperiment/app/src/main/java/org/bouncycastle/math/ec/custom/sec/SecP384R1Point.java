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
import org.bouncycastle.math.ec.custom.sec.SecP384R1Field;
import org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat384;

public class SecP384R1Point
extends ECPoint.AbstractFp {
    public SecP384R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public SecP384R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
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

    SecP384R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
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
        SecP384R1FieldElement secP384R1FieldElement = (SecP384R1FieldElement)this.x;
        SecP384R1FieldElement secP384R1FieldElement2 = (SecP384R1FieldElement)this.y;
        SecP384R1FieldElement secP384R1FieldElement3 = (SecP384R1FieldElement)eCPoint.getXCoord();
        SecP384R1FieldElement secP384R1FieldElement4 = (SecP384R1FieldElement)eCPoint.getYCoord();
        SecP384R1FieldElement secP384R1FieldElement5 = (SecP384R1FieldElement)this.zs[0];
        SecP384R1FieldElement secP384R1FieldElement6 = (SecP384R1FieldElement)eCPoint.getZCoord(0);
        int[] arrn5 = Nat.create(24);
        int[] arrn6 = Nat.create(24);
        int[] arrn7 = Nat.create(12);
        int[] arrn8 = Nat.create(12);
        boolean bl2 = secP384R1FieldElement5.isOne();
        if (bl2) {
            int[] arrn9 = secP384R1FieldElement3.x;
            arrn = secP384R1FieldElement4.x;
            arrn3 = arrn9;
        } else {
            SecP384R1Field.square(secP384R1FieldElement5.x, arrn7);
            SecP384R1Field.multiply(arrn7, secP384R1FieldElement3.x, arrn6);
            SecP384R1Field.multiply(arrn7, secP384R1FieldElement5.x, arrn7);
            SecP384R1Field.multiply(arrn7, secP384R1FieldElement4.x, arrn7);
            arrn = arrn7;
            arrn3 = arrn6;
        }
        if (bl = secP384R1FieldElement6.isOne()) {
            int[] arrn10 = secP384R1FieldElement.x;
            arrn2 = secP384R1FieldElement2.x;
            arrn4 = arrn10;
        } else {
            SecP384R1Field.square(secP384R1FieldElement6.x, arrn8);
            SecP384R1Field.multiply(arrn8, secP384R1FieldElement.x, arrn5);
            SecP384R1Field.multiply(arrn8, secP384R1FieldElement6.x, arrn8);
            SecP384R1Field.multiply(arrn8, secP384R1FieldElement2.x, arrn8);
            arrn2 = arrn8;
            arrn4 = arrn5;
        }
        int[] arrn11 = Nat.create(12);
        SecP384R1Field.subtract(arrn4, arrn3, arrn11);
        int[] arrn12 = Nat.create(12);
        SecP384R1Field.subtract(arrn2, arrn, arrn12);
        if (Nat.isZero(12, arrn11)) {
            if (Nat.isZero(12, arrn12)) {
                return this.twice();
            }
            return eCCurve.getInfinity();
        }
        SecP384R1Field.square(arrn11, arrn7);
        int[] arrn13 = Nat.create(12);
        SecP384R1Field.multiply(arrn7, arrn11, arrn13);
        SecP384R1Field.multiply(arrn7, arrn4, arrn7);
        SecP384R1Field.negate(arrn13, arrn13);
        Nat384.mul(arrn2, arrn13, arrn5);
        SecP384R1Field.reduce32(Nat.addBothTo(12, arrn7, arrn7, arrn13), arrn13);
        SecP384R1FieldElement secP384R1FieldElement7 = new SecP384R1FieldElement(arrn8);
        SecP384R1Field.square(arrn12, secP384R1FieldElement7.x);
        SecP384R1Field.subtract(secP384R1FieldElement7.x, arrn13, secP384R1FieldElement7.x);
        SecP384R1FieldElement secP384R1FieldElement8 = new SecP384R1FieldElement(arrn13);
        SecP384R1Field.subtract(arrn7, secP384R1FieldElement7.x, secP384R1FieldElement8.x);
        Nat384.mul(secP384R1FieldElement8.x, arrn12, arrn6);
        SecP384R1Field.addExt(arrn5, arrn6, arrn5);
        SecP384R1Field.reduce(arrn5, secP384R1FieldElement8.x);
        SecP384R1FieldElement secP384R1FieldElement9 = new SecP384R1FieldElement(arrn11);
        if (!bl2) {
            SecP384R1Field.multiply(secP384R1FieldElement9.x, secP384R1FieldElement5.x, secP384R1FieldElement9.x);
        }
        if (!bl) {
            SecP384R1Field.multiply(secP384R1FieldElement9.x, secP384R1FieldElement6.x, secP384R1FieldElement9.x);
        }
        return new SecP384R1Point(eCCurve, secP384R1FieldElement7, secP384R1FieldElement8, new ECFieldElement[]{secP384R1FieldElement9}, this.withCompression);
    }

    @Override
    protected ECPoint detach() {
        return new SecP384R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }

    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP384R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
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
        SecP384R1FieldElement secP384R1FieldElement = (SecP384R1FieldElement)this.y;
        if (secP384R1FieldElement.isZero()) {
            return eCCurve.getInfinity();
        }
        SecP384R1FieldElement secP384R1FieldElement2 = (SecP384R1FieldElement)this.x;
        SecP384R1FieldElement secP384R1FieldElement3 = (SecP384R1FieldElement)this.zs[0];
        int[] arrn = Nat.create(12);
        int[] arrn2 = Nat.create(12);
        int[] arrn3 = Nat.create(12);
        SecP384R1Field.square(secP384R1FieldElement.x, arrn3);
        int[] arrn4 = Nat.create(12);
        SecP384R1Field.square(arrn3, arrn4);
        boolean bl = secP384R1FieldElement3.isOne();
        int[] arrn5 = secP384R1FieldElement3.x;
        if (!bl) {
            SecP384R1Field.square(secP384R1FieldElement3.x, arrn2);
            arrn5 = arrn2;
        }
        SecP384R1Field.subtract(secP384R1FieldElement2.x, arrn5, arrn);
        SecP384R1Field.add(secP384R1FieldElement2.x, arrn5, arrn2);
        SecP384R1Field.multiply(arrn2, arrn, arrn2);
        SecP384R1Field.reduce32(Nat.addBothTo(12, arrn2, arrn2, arrn2), arrn2);
        SecP384R1Field.multiply(arrn3, secP384R1FieldElement2.x, arrn3);
        SecP384R1Field.reduce32(Nat.shiftUpBits(12, arrn3, 2, 0), arrn3);
        SecP384R1Field.reduce32(Nat.shiftUpBits(12, arrn4, 3, 0, arrn), arrn);
        SecP384R1FieldElement secP384R1FieldElement4 = new SecP384R1FieldElement(arrn4);
        SecP384R1Field.square(arrn2, secP384R1FieldElement4.x);
        SecP384R1Field.subtract(secP384R1FieldElement4.x, arrn3, secP384R1FieldElement4.x);
        SecP384R1Field.subtract(secP384R1FieldElement4.x, arrn3, secP384R1FieldElement4.x);
        SecP384R1FieldElement secP384R1FieldElement5 = new SecP384R1FieldElement(arrn3);
        SecP384R1Field.subtract(arrn3, secP384R1FieldElement4.x, secP384R1FieldElement5.x);
        SecP384R1Field.multiply(secP384R1FieldElement5.x, arrn2, secP384R1FieldElement5.x);
        SecP384R1Field.subtract(secP384R1FieldElement5.x, arrn, secP384R1FieldElement5.x);
        SecP384R1FieldElement secP384R1FieldElement6 = new SecP384R1FieldElement(arrn2);
        SecP384R1Field.twice(secP384R1FieldElement.x, secP384R1FieldElement6.x);
        if (!bl) {
            SecP384R1Field.multiply(secP384R1FieldElement6.x, secP384R1FieldElement3.x, secP384R1FieldElement6.x);
        }
        return new SecP384R1Point(eCCurve, secP384R1FieldElement4, secP384R1FieldElement5, new ECFieldElement[]{secP384R1FieldElement6}, this.withCompression);
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

