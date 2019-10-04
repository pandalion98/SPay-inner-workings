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
import org.bouncycastle.math.ec.custom.sec.SecP256R1Field;
import org.bouncycastle.math.ec.custom.sec.SecP256R1FieldElement;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class SecP256R1Point
extends ECPoint.AbstractFp {
    public SecP256R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public SecP256R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
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

    SecP256R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
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
        SecP256R1FieldElement secP256R1FieldElement = (SecP256R1FieldElement)this.x;
        SecP256R1FieldElement secP256R1FieldElement2 = (SecP256R1FieldElement)this.y;
        SecP256R1FieldElement secP256R1FieldElement3 = (SecP256R1FieldElement)eCPoint.getXCoord();
        SecP256R1FieldElement secP256R1FieldElement4 = (SecP256R1FieldElement)eCPoint.getYCoord();
        SecP256R1FieldElement secP256R1FieldElement5 = (SecP256R1FieldElement)this.zs[0];
        SecP256R1FieldElement secP256R1FieldElement6 = (SecP256R1FieldElement)eCPoint.getZCoord(0);
        int[] arrn5 = Nat256.createExt();
        int[] arrn6 = Nat256.create();
        int[] arrn7 = Nat256.create();
        int[] arrn8 = Nat256.create();
        boolean bl2 = secP256R1FieldElement5.isOne();
        if (bl2) {
            int[] arrn9 = secP256R1FieldElement3.x;
            arrn2 = secP256R1FieldElement4.x;
            arrn = arrn9;
        } else {
            SecP256R1Field.square(secP256R1FieldElement5.x, arrn7);
            SecP256R1Field.multiply(arrn7, secP256R1FieldElement3.x, arrn6);
            SecP256R1Field.multiply(arrn7, secP256R1FieldElement5.x, arrn7);
            SecP256R1Field.multiply(arrn7, secP256R1FieldElement4.x, arrn7);
            arrn2 = arrn7;
            arrn = arrn6;
        }
        if (bl = secP256R1FieldElement6.isOne()) {
            int[] arrn10 = secP256R1FieldElement.x;
            arrn4 = secP256R1FieldElement2.x;
            arrn3 = arrn10;
        } else {
            SecP256R1Field.square(secP256R1FieldElement6.x, arrn8);
            SecP256R1Field.multiply(arrn8, secP256R1FieldElement.x, arrn5);
            SecP256R1Field.multiply(arrn8, secP256R1FieldElement6.x, arrn8);
            SecP256R1Field.multiply(arrn8, secP256R1FieldElement2.x, arrn8);
            arrn4 = arrn8;
            arrn3 = arrn5;
        }
        int[] arrn11 = Nat256.create();
        SecP256R1Field.subtract(arrn3, arrn, arrn11);
        SecP256R1Field.subtract(arrn4, arrn2, arrn6);
        if (Nat256.isZero(arrn11)) {
            if (Nat256.isZero(arrn6)) {
                return this.twice();
            }
            return eCCurve.getInfinity();
        }
        SecP256R1Field.square(arrn11, arrn7);
        int[] arrn12 = Nat256.create();
        SecP256R1Field.multiply(arrn7, arrn11, arrn12);
        SecP256R1Field.multiply(arrn7, arrn3, arrn7);
        SecP256R1Field.negate(arrn12, arrn12);
        Nat256.mul(arrn4, arrn12, arrn5);
        SecP256R1Field.reduce32(Nat256.addBothTo(arrn7, arrn7, arrn12), arrn12);
        SecP256R1FieldElement secP256R1FieldElement7 = new SecP256R1FieldElement(arrn8);
        SecP256R1Field.square(arrn6, secP256R1FieldElement7.x);
        SecP256R1Field.subtract(secP256R1FieldElement7.x, arrn12, secP256R1FieldElement7.x);
        SecP256R1FieldElement secP256R1FieldElement8 = new SecP256R1FieldElement(arrn12);
        SecP256R1Field.subtract(arrn7, secP256R1FieldElement7.x, secP256R1FieldElement8.x);
        SecP256R1Field.multiplyAddToExt(secP256R1FieldElement8.x, arrn6, arrn5);
        SecP256R1Field.reduce(arrn5, secP256R1FieldElement8.x);
        SecP256R1FieldElement secP256R1FieldElement9 = new SecP256R1FieldElement(arrn11);
        if (!bl2) {
            SecP256R1Field.multiply(secP256R1FieldElement9.x, secP256R1FieldElement5.x, secP256R1FieldElement9.x);
        }
        if (!bl) {
            SecP256R1Field.multiply(secP256R1FieldElement9.x, secP256R1FieldElement6.x, secP256R1FieldElement9.x);
        }
        return new SecP256R1Point(eCCurve, secP256R1FieldElement7, secP256R1FieldElement8, new ECFieldElement[]{secP256R1FieldElement9}, this.withCompression);
    }

    @Override
    protected ECPoint detach() {
        return new SecP256R1Point(null, this.getAffineXCoord(), this.getAffineYCoord());
    }

    @Override
    public ECPoint negate() {
        if (this.isInfinity()) {
            return this;
        }
        return new SecP256R1Point(this.curve, this.x, this.y.negate(), this.zs, this.withCompression);
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
        SecP256R1FieldElement secP256R1FieldElement = (SecP256R1FieldElement)this.y;
        if (secP256R1FieldElement.isZero()) {
            return eCCurve.getInfinity();
        }
        SecP256R1FieldElement secP256R1FieldElement2 = (SecP256R1FieldElement)this.x;
        SecP256R1FieldElement secP256R1FieldElement3 = (SecP256R1FieldElement)this.zs[0];
        int[] arrn = Nat256.create();
        int[] arrn2 = Nat256.create();
        int[] arrn3 = Nat256.create();
        SecP256R1Field.square(secP256R1FieldElement.x, arrn3);
        int[] arrn4 = Nat256.create();
        SecP256R1Field.square(arrn3, arrn4);
        boolean bl = secP256R1FieldElement3.isOne();
        int[] arrn5 = secP256R1FieldElement3.x;
        if (!bl) {
            SecP256R1Field.square(secP256R1FieldElement3.x, arrn2);
            arrn5 = arrn2;
        }
        SecP256R1Field.subtract(secP256R1FieldElement2.x, arrn5, arrn);
        SecP256R1Field.add(secP256R1FieldElement2.x, arrn5, arrn2);
        SecP256R1Field.multiply(arrn2, arrn, arrn2);
        SecP256R1Field.reduce32(Nat256.addBothTo(arrn2, arrn2, arrn2), arrn2);
        SecP256R1Field.multiply(arrn3, secP256R1FieldElement2.x, arrn3);
        SecP256R1Field.reduce32(Nat.shiftUpBits(8, arrn3, 2, 0), arrn3);
        SecP256R1Field.reduce32(Nat.shiftUpBits(8, arrn4, 3, 0, arrn), arrn);
        SecP256R1FieldElement secP256R1FieldElement4 = new SecP256R1FieldElement(arrn4);
        SecP256R1Field.square(arrn2, secP256R1FieldElement4.x);
        SecP256R1Field.subtract(secP256R1FieldElement4.x, arrn3, secP256R1FieldElement4.x);
        SecP256R1Field.subtract(secP256R1FieldElement4.x, arrn3, secP256R1FieldElement4.x);
        SecP256R1FieldElement secP256R1FieldElement5 = new SecP256R1FieldElement(arrn3);
        SecP256R1Field.subtract(arrn3, secP256R1FieldElement4.x, secP256R1FieldElement5.x);
        SecP256R1Field.multiply(secP256R1FieldElement5.x, arrn2, secP256R1FieldElement5.x);
        SecP256R1Field.subtract(secP256R1FieldElement5.x, arrn, secP256R1FieldElement5.x);
        SecP256R1FieldElement secP256R1FieldElement6 = new SecP256R1FieldElement(arrn2);
        SecP256R1Field.twice(secP256R1FieldElement.x, secP256R1FieldElement6.x);
        if (!bl) {
            SecP256R1Field.multiply(secP256R1FieldElement6.x, secP256R1FieldElement3.x, secP256R1FieldElement6.x);
        }
        return new SecP256R1Point(eCCurve, secP256R1FieldElement4, secP256R1FieldElement5, new ECFieldElement[]{secP256R1FieldElement6}, this.withCompression);
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

