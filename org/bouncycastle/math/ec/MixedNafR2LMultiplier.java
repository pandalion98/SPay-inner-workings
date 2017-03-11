package org.bouncycastle.math.ec;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.math.BigInteger;

public class MixedNafR2LMultiplier extends AbstractECMultiplier {
    protected int additionCoord;
    protected int doublingCoord;

    public MixedNafR2LMultiplier() {
        this(2, 4);
    }

    public MixedNafR2LMultiplier(int i, int i2) {
        this.additionCoord = i;
        this.doublingCoord = i2;
    }

    protected ECCurve configureCurve(ECCurve eCCurve, int i) {
        if (eCCurve.getCoordinateSystem() == i) {
            return eCCurve;
        }
        if (eCCurve.supportsCoordinateSystem(i)) {
            return eCCurve.configure().setCoordinateSystem(i).create();
        }
        throw new IllegalArgumentException("Coordinate system " + i + " not supported by this curve");
    }

    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int i = 0;
        ECCurve curve = eCPoint.getCurve();
        ECCurve configureCurve = configureCurve(curve, this.additionCoord);
        ECCurve configureCurve2 = configureCurve(curve, this.doublingCoord);
        int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint infinity = configureCurve.getInfinity();
        ECPoint importPoint = configureCurve2.importPoint(eCPoint);
        int i2 = 0;
        while (i < generateCompactNaf.length) {
            int i3 = generateCompactNaf[i];
            int i4 = i3 >> 16;
            importPoint = importPoint.timesPow2(i2 + (i3 & HCEClientConstants.HIGHEST_ATC_DEC_VALUE));
            ECPoint importPoint2 = configureCurve.importPoint(importPoint);
            if (i4 < 0) {
                importPoint2 = importPoint2.negate();
            }
            infinity = infinity.add(importPoint2);
            i2 = 1;
            i++;
        }
        return curve.importPoint(infinity);
    }
}
