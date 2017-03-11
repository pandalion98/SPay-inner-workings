package org.bouncycastle.math.ec;

import com.samsung.android.spaytui.SpayTuiTAInfo;
import java.math.BigInteger;

public class FixedPointCombMultiplier extends AbstractECMultiplier {
    protected int getWidthForCombSize(int i) {
        return i > SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI ? 6 : 5;
    }

    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECCurve curve = eCPoint.getCurve();
        int combSize = FixedPointUtil.getCombSize(curve);
        if (bigInteger.bitLength() > combSize) {
            throw new IllegalStateException("fixed-point comb doesn't support scalars larger than the curve order");
        }
        FixedPointPreCompInfo precompute = FixedPointUtil.precompute(eCPoint, getWidthForCombSize(combSize));
        ECPoint[] preComp = precompute.getPreComp();
        int width = precompute.getWidth();
        int i = ((combSize + width) - 1) / width;
        int i2 = (i * width) - 1;
        width = 0;
        ECPoint infinity = curve.getInfinity();
        while (width < i) {
            int i3 = 0;
            for (combSize = i2 - width; combSize >= 0; combSize -= i) {
                i3 <<= 1;
                if (bigInteger.testBit(combSize)) {
                    i3 |= 1;
                }
            }
            width++;
            infinity = infinity.twicePlus(preComp[i3]);
        }
        return infinity;
    }
}
