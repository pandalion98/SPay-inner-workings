/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.ScaleXPointMap;
import org.bouncycastle.math.ec.endo.GLVEndomorphism;
import org.bouncycastle.math.ec.endo.GLVTypeBParameters;

public class GLVTypeBEndomorphism
implements GLVEndomorphism {
    protected final ECCurve curve;
    protected final GLVTypeBParameters parameters;
    protected final ECPointMap pointMap;

    public GLVTypeBEndomorphism(ECCurve eCCurve, GLVTypeBParameters gLVTypeBParameters) {
        this.curve = eCCurve;
        this.parameters = gLVTypeBParameters;
        this.pointMap = new ScaleXPointMap(eCCurve.fromBigInteger(gLVTypeBParameters.getBeta()));
    }

    /*
     * Enabled aggressive block sorting
     */
    protected BigInteger calculateB(BigInteger bigInteger, BigInteger bigInteger2, int n) {
        boolean bl = bigInteger2.signum() < 0;
        BigInteger bigInteger3 = bigInteger.multiply(bigInteger2.abs());
        boolean bl2 = bigInteger3.testBit(n - 1);
        BigInteger bigInteger4 = bigInteger3.shiftRight(n);
        if (bl2) {
            bigInteger4 = bigInteger4.add(ECConstants.ONE);
        }
        if (bl) {
            return bigInteger4.negate();
        }
        return bigInteger4;
    }

    @Override
    public BigInteger[] decomposeScalar(BigInteger bigInteger) {
        int n = this.parameters.getBits();
        BigInteger bigInteger2 = this.calculateB(bigInteger, this.parameters.getG1(), n);
        BigInteger bigInteger3 = this.calculateB(bigInteger, this.parameters.getG2(), n);
        BigInteger[] arrbigInteger = this.parameters.getV1();
        BigInteger[] arrbigInteger2 = this.parameters.getV2();
        return new BigInteger[]{bigInteger.subtract(bigInteger2.multiply(arrbigInteger[0]).add(bigInteger3.multiply(arrbigInteger2[0]))), bigInteger2.multiply(arrbigInteger[1]).add(bigInteger3.multiply(arrbigInteger2[1])).negate()};
    }

    @Override
    public ECPointMap getPointMap() {
        return this.pointMap;
    }

    @Override
    public boolean hasEfficientPointMap() {
        return true;
    }
}

