package org.bouncycastle.math.ec;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.math.BigInteger;

public class NafL2RMultiplier extends AbstractECMultiplier {
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint normalize = eCPoint.normalize();
        ECPoint negate = normalize.negate();
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int length = generateCompactNaf.length;
        ECPoint eCPoint2 = infinity;
        while (true) {
            int i = length - 1;
            if (i < 0) {
                return eCPoint2;
            }
            length = generateCompactNaf[i];
            eCPoint2 = eCPoint2.twicePlus((length >> 16) < 0 ? negate : normalize).timesPow2(HCEClientConstants.HIGHEST_ATC_DEC_VALUE & length);
            length = i;
        }
    }
}
