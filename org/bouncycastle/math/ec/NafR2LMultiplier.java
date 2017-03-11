package org.bouncycastle.math.ec;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.math.BigInteger;

public class NafR2LMultiplier extends AbstractECMultiplier {
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int i = 0;
        int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int i2 = 0;
        while (i < generateCompactNaf.length) {
            int i3 = generateCompactNaf[i];
            int i4 = i3 >> 16;
            eCPoint = eCPoint.timesPow2(i2 + (i3 & HCEClientConstants.HIGHEST_ATC_DEC_VALUE));
            infinity = infinity.add(i4 < 0 ? eCPoint.negate() : eCPoint);
            i2 = 1;
            i++;
        }
        return infinity;
    }
}
