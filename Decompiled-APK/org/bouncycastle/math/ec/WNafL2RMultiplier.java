package org.bouncycastle.math.ec;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.math.BigInteger;

public class WNafL2RMultiplier extends AbstractECMultiplier {
    protected int getWindowSize(int i) {
        return WNafUtil.getWindowSize(i);
    }

    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint timesPow2;
        int max = Math.max(2, Math.min(16, getWindowSize(bigInteger.bitLength())));
        WNafPreCompInfo precompute = WNafUtil.precompute(eCPoint, max, true);
        ECPoint[] preComp = precompute.getPreComp();
        ECPoint[] preCompNeg = precompute.getPreCompNeg();
        int[] generateCompactWindowNaf = WNafUtil.generateCompactWindowNaf(max, bigInteger);
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int length = generateCompactWindowNaf.length;
        if (length > 1) {
            int i = length - 1;
            length = generateCompactWindowNaf[i];
            int i2 = length >> 16;
            length &= HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
            int abs = Math.abs(i2);
            ECPoint[] eCPointArr = i2 < 0 ? preCompNeg : preComp;
            if ((abs << 2) < (1 << max)) {
                byte b = LongArray.bitLengths[abs];
                int i3 = max - b;
                infinity = eCPointArr[((1 << (max - 1)) - 1) >>> 1].add(eCPointArr[(((abs ^ (1 << (b - 1))) << i3) + 1) >>> 1]);
                length -= i3;
            } else {
                infinity = eCPointArr[abs >>> 1];
            }
            int i4 = i;
            timesPow2 = infinity.timesPow2(length);
            length = i4;
        } else {
            timesPow2 = infinity;
        }
        while (length > 0) {
            i2 = length - 1;
            length = generateCompactWindowNaf[i2];
            max = length >> 16;
            timesPow2 = timesPow2.twicePlus((max < 0 ? preCompNeg : preComp)[Math.abs(max) >>> 1]).timesPow2(length & HCEClientConstants.HIGHEST_ATC_DEC_VALUE);
            length = i2;
        }
        return timesPow2;
    }
}
