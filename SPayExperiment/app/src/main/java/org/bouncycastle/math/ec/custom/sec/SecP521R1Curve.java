/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP521R1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP521R1Point;
import org.bouncycastle.util.encoders.Hex;

public class SecP521R1Curve
extends ECCurve.AbstractFp {
    private static final int SecP521R1_DEFAULT_COORDS = 2;
    public static final BigInteger q = new BigInteger(1, Hex.decode("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"));
    protected SecP521R1Point infinity = new SecP521R1Point(this, null, null);

    public SecP521R1Curve() {
        super(q);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("0051953EB9618E1C9A1F929A21A0B68540EEA2DA725B99B315F3B8B489918EF109E156193951EC7E937B1652C0BD3BB1BF073573DF883D2C34F1EF451FD46B503F00")));
        this.order = new BigInteger(1, Hex.decode("01FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFA51868783BF2F966B7FCC0148F709A5D03BB5C9B8899C47AEBB6FB71E91386409"));
        this.cofactor = BigInteger.valueOf((long)1L);
        this.coord = 2;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecP521R1Curve();
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
        return new SecP521R1Point(this, eCFieldElement, eCFieldElement2, bl);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
        return new SecP521R1Point(this, eCFieldElement, eCFieldElement2, arreCFieldElement, bl);
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecP521R1FieldElement(bigInteger);
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

