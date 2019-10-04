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
import org.bouncycastle.math.ec.custom.sec.SecP384R1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP384R1Point;
import org.bouncycastle.util.encoders.Hex;

public class SecP384R1Curve
extends ECCurve.AbstractFp {
    private static final int SecP384R1_DEFAULT_COORDS = 2;
    public static final BigInteger q = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF"));
    protected SecP384R1Point infinity = new SecP384R1Point(this, null, null);

    public SecP384R1Curve() {
        super(q);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF")));
        this.order = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973"));
        this.cofactor = BigInteger.valueOf((long)1L);
        this.coord = 2;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecP384R1Curve();
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
        return new SecP384R1Point(this, eCFieldElement, eCFieldElement2, bl);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
        return new SecP384R1Point(this, eCFieldElement, eCFieldElement2, arreCFieldElement, bl);
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecP384R1FieldElement(bigInteger);
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

