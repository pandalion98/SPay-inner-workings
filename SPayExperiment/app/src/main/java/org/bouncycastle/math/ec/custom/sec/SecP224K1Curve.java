/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP224K1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP224K1Point;
import org.bouncycastle.util.encoders.Hex;

public class SecP224K1Curve
extends ECCurve.AbstractFp {
    private static final int SECP224K1_DEFAULT_COORDS = 2;
    public static final BigInteger q = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFE56D"));
    protected SecP224K1Point infinity = new SecP224K1Point(this, null, null);

    public SecP224K1Curve() {
        super(q);
        this.a = this.fromBigInteger(ECConstants.ZERO);
        this.b = this.fromBigInteger(BigInteger.valueOf((long)5L));
        this.order = new BigInteger(1, Hex.decode("010000000000000000000000000001DCE8D2EC6184CAF0A971769FB1F7"));
        this.cofactor = BigInteger.valueOf((long)1L);
        this.coord = 2;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecP224K1Curve();
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
        return new SecP224K1Point(this, eCFieldElement, eCFieldElement2, bl);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
        return new SecP224K1Point(this, eCFieldElement, eCFieldElement2, arreCFieldElement, bl);
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecP224K1FieldElement(bigInteger);
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
            case 2: 
        }
        return true;
    }
}

