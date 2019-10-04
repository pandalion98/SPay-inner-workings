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
import org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Point;
import org.bouncycastle.util.encoders.Hex;

public class SecP256K1Curve
extends ECCurve.AbstractFp {
    private static final int SECP256K1_DEFAULT_COORDS = 2;
    public static final BigInteger q = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F"));
    protected SecP256K1Point infinity = new SecP256K1Point(this, null, null);

    public SecP256K1Curve() {
        super(q);
        this.a = this.fromBigInteger(ECConstants.ZERO);
        this.b = this.fromBigInteger(BigInteger.valueOf((long)7L));
        this.order = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141"));
        this.cofactor = BigInteger.valueOf((long)1L);
        this.coord = 2;
    }

    @Override
    protected ECCurve cloneCurve() {
        return new SecP256K1Curve();
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
        return new SecP256K1Point(this, eCFieldElement, eCFieldElement2, bl);
    }

    @Override
    protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
        return new SecP256K1Point(this, eCFieldElement, eCFieldElement2, arreCFieldElement, bl);
    }

    @Override
    public ECFieldElement fromBigInteger(BigInteger bigInteger) {
        return new SecP256K1FieldElement(bigInteger);
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

