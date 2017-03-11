package org.bouncycastle.math.ec;

import java.math.BigInteger;

public class MontgomeryLadderMultiplier extends AbstractECMultiplier {
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint[] eCPointArr = new ECPoint[]{eCPoint.getCurve().getInfinity(), eCPoint};
        int bitLength = bigInteger.bitLength();
        while (true) {
            int i = bitLength - 1;
            if (i < 0) {
                return eCPointArr[0];
            }
            bitLength = bigInteger.testBit(i) ? 1 : 0;
            int i2 = 1 - bitLength;
            eCPointArr[i2] = eCPointArr[i2].add(eCPointArr[bitLength]);
            eCPointArr[bitLength] = eCPointArr[bitLength].twice();
            bitLength = i;
        }
    }
}
