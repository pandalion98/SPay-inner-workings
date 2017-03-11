package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.ScaleXPointMap;

public class GLVTypeBEndomorphism implements GLVEndomorphism {
    protected final ECCurve curve;
    protected final GLVTypeBParameters parameters;
    protected final ECPointMap pointMap;

    public GLVTypeBEndomorphism(ECCurve eCCurve, GLVTypeBParameters gLVTypeBParameters) {
        this.curve = eCCurve;
        this.parameters = gLVTypeBParameters;
        this.pointMap = new ScaleXPointMap(eCCurve.fromBigInteger(gLVTypeBParameters.getBeta()));
    }

    protected BigInteger calculateB(BigInteger bigInteger, BigInteger bigInteger2, int i) {
        Object obj = bigInteger2.signum() < 0 ? 1 : null;
        BigInteger multiply = bigInteger.multiply(bigInteger2.abs());
        boolean testBit = multiply.testBit(i - 1);
        multiply = multiply.shiftRight(i);
        if (testBit) {
            multiply = multiply.add(ECConstants.ONE);
        }
        return obj != null ? multiply.negate() : multiply;
    }

    public BigInteger[] decomposeScalar(BigInteger bigInteger) {
        int bits = this.parameters.getBits();
        BigInteger calculateB = calculateB(bigInteger, this.parameters.getG1(), bits);
        BigInteger calculateB2 = calculateB(bigInteger, this.parameters.getG2(), bits);
        BigInteger[] v1 = this.parameters.getV1();
        BigInteger[] v2 = this.parameters.getV2();
        BigInteger subtract = bigInteger.subtract(calculateB.multiply(v1[0]).add(calculateB2.multiply(v2[0])));
        calculateB2 = calculateB.multiply(v1[1]).add(calculateB2.multiply(v2[1])).negate();
        return new BigInteger[]{subtract, calculateB2};
    }

    public ECPointMap getPointMap() {
        return this.pointMap;
    }

    public boolean hasEfficientPointMap() {
        return true;
    }
}
