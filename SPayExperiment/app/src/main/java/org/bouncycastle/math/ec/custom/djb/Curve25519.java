/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.custom.djb;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.djb.Curve25519Field;
import org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement;
import org.bouncycastle.math.ec.custom.djb.Curve25519Point;
import org.bouncycastle.math.raw.Nat256;
import org.bouncycastle.util.encoders.Hex;

public class Curve25519
extends ECCurve.AbstractFp {
    private static final int Curve25519_DEFAULT_COORDS = 4;
    public static final BigInteger q = Nat256.toBigInteger(Curve25519Field.P);
    protected Curve25519Point infinity = new Curve25519Point(this, null, null);

    public Curve25519() {
        super(q);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA984914A144")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("7B425ED097B425ED097B425ED097B425ED097B425ED097B4260B5E9C7710C864")));
        this.order = new BigInteger(1, Hex.decode("1000000000000000000000000000000014DEF9DEA2F79CD65812631A5CF5D3ED"));
        this.cofactor = BigInteger.valueOf((long)8L);
        this.coord = 4;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new Curve25519();
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
        return new Curve25519Point(this, eCFieldElement, eCFieldElement2, bl);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
        return new Curve25519Point(this, eCFieldElement, eCFieldElement2, arreCFieldElement, bl);
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new Curve25519FieldElement(bigInteger);
    }

    @Override
    public int getFieldSize() {
        return q.bitLength();
    }

    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }

    public BigInteger getQ() {
        return q;
    }

    @Override
    public boolean supportsCoordinateSystem(int n) {
        switch (n) {
            default: {
                return false;
            }
            case 4: 
        }
        return true;
    }
}

