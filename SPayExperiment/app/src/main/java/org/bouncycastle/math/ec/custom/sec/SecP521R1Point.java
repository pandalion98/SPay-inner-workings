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
import org.bouncycastle.math.ec.custom.sec.SecP521R1Field;
import org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement;
import org.bouncycastle.math.raw.Nat;

public class SecP521R1Point
extends ECPoint.AbstractFp {
    public SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
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

    SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
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
        SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement)this.x;
        SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement)this.y;
        SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement)eCPoint.getXCoord();
        SecP521R1FieldElement secP521R1FieldElement4 = (SecP521R1FieldElement)eCPoint.getYCoord();
        SecP521R1FieldElement secP521R1FieldElement5 = (SecP521R1FieldElement)this.zs[0];
        SecP521R1FieldElement secP521R1FieldElement6 = (SecP521R1FieldElement)eCPoint.getZCoord(0);
        int[] arrn5 = Nat.create(17);
        int[] arrn6 = Nat.create(17);
        int[] arrn7 = Nat.create(17);
        int[] arrn8 = Nat.create(17);
        boolean bl2 = secP521R1FieldElement5.isOne();
        if (bl2) {
            int[] arrn9 = secP521R1FieldElement3.x;
            arrn2 = secP521R1FieldElement4.x;
            arrn = arrn9;
        } else {
            SecP521R1Field.square(secP521R1FieldElement5.x, arrn7);
            SecP521R1Field.multiply(arrn7, secP521R1FieldElement3.x, arrn6);
            SecP521R1Field.multiply(arrn7, secP521R1FieldElement5.x, arrn7);
            SecP521R1Field.multiply(arrn7, secP521R1FieldElement4.x, arrn7);
            arrn2 = arrn7;
            arrn = arrn6;
        }
        if (bl = secP521R1FieldElement6.isOne()) {
            int[] arrn10 = secP521R1FieldElement.x;
            arrn4 = secP521R1FieldElement2.x;
            arrn3 = arrn10;
        } else {
            SecP521R1Field.square(secP521R1FieldElement6.x, arrn8);
            SecP521R1Field.multiply(arrn8, secP521R1FieldElement.x, arrn5);
            SecP521R1Field.multiply(arrn8, secP521R1FieldElement6.x, arrn8);
            SecP521R1Field.multiply(arrn8, secP521R1FieldElement2.x, arrn8);
            arrn4 = arrn8;
            arrn3 = arrn5;
        }
        int[] arrn11 = Nat.create(17);
        SecP521R1Field.subtract(arrn3, arrn, arrn11);
        SecP521R1Field.subtract(arrn4, arrn2, arrn6);
        if (Nat.isZero(17, arrn11)) {
            if (Nat.isZero(17, arrn6)) {
                return this.twice();
            }
            return eCCurve.getInfinity();
        }
        SecP521R1Field.square(arrn11, arrn7);
        int[] arrn12 = Nat.create(17);
        SecP521R1Field.multiply(arrn7, arrn11, arrn12);
        SecP521R1Field.multiply(arrn7, arrn3, arrn7);
        SecP521R1Field.multiply(arrn4, arrn12, arrn5);
        SecP521R1FieldElement secP521R1FieldElement7 = new SecP521R1FieldElement(arrn8);
        SecP521R1Field.square(arrn6, secP521R1FieldElement7.x);
        SecP521R1Field.add(secP521R1FieldElement7.x, arrn12, secP521R1FieldElement7.x);
        SecP521R1Field.subtract(secP521R1FieldElement7.x, arrn7, secP521R1FieldElement7.x);
        SecP521R1Field.subtract(secP521R1FieldElement7.x, arrn7, secP521R1FieldElement7.x);
        SecP521R1FieldElement secP521R1FieldElement8 = new SecP521R1FieldElement(arrn12);
        SecP521R1Field.subtract(arrn7, secP521R1FieldElement7.x, secP521R1FieldElement8.x);
        SecP521R1Field.multiply(secP521R1FieldElement8.x, arrn6, arrn6);
        SecP521R1Field.subtract(arrn6, arrn5, secP521R1FieldElement8.x);
        SecP521R1FieldElement secP521R1FieldElement9 = new SecP521R1FieldElement(arrn11);
        if (!bl2) {
            SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement5.x, secP521R1FieldElement9.x);
        }
        if (!bl) {
            SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement6.x, secP521R1FieldElement9.x);
        }
        return new SecP521R1Point(eCCurve, secP521R1FieldElement7, secP521R1FieldElement8, new ECFieldElement[]{secP521R1FieldElement9}, this.withCompression);
    }

    @Override
    protected ECPoint detach() {
        return new SecP521R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }

    protected ECFieldElement doubleProductFromSquares(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3, ECFieldElement eCFieldElement4) {
        return eCFieldElement.add(eCFieldElement2).square().subtract(eCFieldElement3).subtract(eCFieldElement4);
    }

    protected ECFieldElement eight(ECFieldElement eCFieldElement) {
        return this.four(this.two(eCFieldElement));
    }

    protected ECFieldElement four(ECFieldElement eCFieldElement) {
        return this.two(this.two(eCFieldElement));
    }

    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP521R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
    }

    protected ECFieldElement three(ECFieldElement eCFieldElement) {
        return this.two(eCFieldElement).add(eCFieldElement);
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
        SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement)this.y;
        if (secP521R1FieldElement.isZero()) {
            return eCCurve.getInfinity();
        }
        SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement)this.x;
        SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement)this.zs[0];
        int[] arrn = Nat.create(17);
        int[] arrn2 = Nat.create(17);
        int[] arrn3 = Nat.create(17);
        SecP521R1Field.square(secP521R1FieldElement.x, arrn3);
        int[] arrn4 = Nat.create(17);
        SecP521R1Field.square(arrn3, arrn4);
        boolean bl = secP521R1FieldElement3.isOne();
        int[] arrn5 = secP521R1FieldElement3.x;
        if (!bl) {
            SecP521R1Field.square(secP521R1FieldElement3.x, arrn2);
            arrn5 = arrn2;
        }
        SecP521R1Field.subtract(secP521R1FieldElement2.x, arrn5, arrn);
        SecP521R1Field.add(secP521R1FieldElement2.x, arrn5, arrn2);
        SecP521R1Field.multiply(arrn2, arrn, arrn2);
        Nat.addBothTo(17, arrn2, arrn2, arrn2);
        SecP521R1Field.reduce23(arrn2);
        SecP521R1Field.multiply(arrn3, secP521R1FieldElement2.x, arrn3);
        Nat.shiftUpBits(17, arrn3, 2, 0);
        SecP521R1Field.reduce23(arrn3);
        Nat.shiftUpBits(17, arrn4, 3, 0, arrn);
        SecP521R1Field.reduce23(arrn);
        SecP521R1FieldElement secP521R1FieldElement4 = new SecP521R1FieldElement(arrn4);
        SecP521R1Field.square(arrn2, secP521R1FieldElement4.x);
        SecP521R1Field.subtract(secP521R1FieldElement4.x, arrn3, secP521R1FieldElement4.x);
        SecP521R1Field.subtract(secP521R1FieldElement4.x, arrn3, secP521R1FieldElement4.x);
        SecP521R1FieldElement secP521R1FieldElement5 = new SecP521R1FieldElement(arrn3);
        SecP521R1Field.subtract(arrn3, secP521R1FieldElement4.x, secP521R1FieldElement5.x);
        SecP521R1Field.multiply(secP521R1FieldElement5.x, arrn2, secP521R1FieldElement5.x);
        SecP521R1Field.subtract(secP521R1FieldElement5.x, arrn, secP521R1FieldElement5.x);
        SecP521R1FieldElement secP521R1FieldElement6 = new SecP521R1FieldElement(arrn2);
        SecP521R1Field.twice(secP521R1FieldElement.x, secP521R1FieldElement6.x);
        if (!bl) {
            SecP521R1Field.multiply(secP521R1FieldElement6.x, secP521R1FieldElement3.x, secP521R1FieldElement6.x);
        }
        return new SecP521R1Point(eCCurve, secP521R1FieldElement4, secP521R1FieldElement5, new ECFieldElement[]{secP521R1FieldElement6}, this.withCompression);
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

    protected ECFieldElement two(ECFieldElement eCFieldElement) {
        return eCFieldElement.add(eCFieldElement);
    }
}

