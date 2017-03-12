package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint.F2m;

public class WTauNafMultiplier extends AbstractECMultiplier {
    static final String PRECOMP_NAME = "bc_wtnaf";

    private static F2m multiplyFromWTnaf(F2m f2m, byte[] bArr, PreCompInfo preCompInfo) {
        F2m[] preComp;
        ECCurve.F2m f2m2 = (ECCurve.F2m) f2m.getCurve();
        byte byteValue = f2m2.getA().toBigInteger().byteValue();
        if (preCompInfo == null || !(preCompInfo instanceof WTauNafPreCompInfo)) {
            preComp = Tnaf.getPreComp(f2m, byteValue);
            PreCompInfo wTauNafPreCompInfo = new WTauNafPreCompInfo();
            wTauNafPreCompInfo.setPreComp(preComp);
            f2m2.setPreCompInfo(f2m, PRECOMP_NAME, wTauNafPreCompInfo);
        } else {
            preComp = ((WTauNafPreCompInfo) preCompInfo).getPreComp();
        }
        F2m f2m3 = (F2m) f2m.getCurve().getInfinity();
        for (int length = bArr.length - 1; length >= 0; length--) {
            f2m3 = Tnaf.tau(f2m3);
            byte b = bArr[length];
            if (b != null) {
                f2m3 = b > null ? f2m3.addSimple(preComp[b]) : f2m3.subtractSimple(preComp[-b]);
            }
        }
        return f2m3;
    }

    private F2m multiplyWTnaf(F2m f2m, ZTauElement zTauElement, PreCompInfo preCompInfo, byte b, byte b2) {
        return multiplyFromWTnaf(f2m, Tnaf.tauAdicWNaf(b2, zTauElement, (byte) 4, BigInteger.valueOf(16), Tnaf.getTw(b2, 4), b == null ? Tnaf.alpha0 : Tnaf.alpha1), preCompInfo);
    }

    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        if (eCPoint instanceof F2m) {
            F2m f2m = (F2m) eCPoint;
            ECCurve.F2m f2m2 = (ECCurve.F2m) f2m.getCurve();
            int m = f2m2.getM();
            byte byteValue = f2m2.getA().toBigInteger().byteValue();
            byte mu = f2m2.getMu();
            return multiplyWTnaf(f2m, Tnaf.partModReduction(bigInteger, m, byteValue, f2m2.getSi(), mu, (byte) 10), f2m2.getPreCompInfo(f2m, PRECOMP_NAME), byteValue, mu);
        }
        throw new IllegalArgumentException("Only ECPoint.F2m can be used in WTauNafMultiplier");
    }
}
