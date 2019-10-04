/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.AbstractECMultiplier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.PreCompInfo;
import org.bouncycastle.math.ec.Tnaf;
import org.bouncycastle.math.ec.WTauNafPreCompInfo;
import org.bouncycastle.math.ec.ZTauElement;

public class WTauNafMultiplier
extends AbstractECMultiplier {
    static final String PRECOMP_NAME = "bc_wtnaf";

    /*
     * Enabled aggressive block sorting
     */
    private static ECPoint.F2m multiplyFromWTnaf(ECPoint.F2m f2m, byte[] arrby, PreCompInfo preCompInfo) {
        ECPoint.F2m[] arrf2m;
        ECCurve.F2m f2m2 = (ECCurve.F2m)f2m.getCurve();
        byte by = f2m2.getA().toBigInteger().byteValue();
        if (preCompInfo == null || !(preCompInfo instanceof WTauNafPreCompInfo)) {
            arrf2m = Tnaf.getPreComp(f2m, by);
            WTauNafPreCompInfo wTauNafPreCompInfo = new WTauNafPreCompInfo();
            wTauNafPreCompInfo.setPreComp(arrf2m);
            f2m2.setPreCompInfo(f2m, PRECOMP_NAME, wTauNafPreCompInfo);
        } else {
            arrf2m = ((WTauNafPreCompInfo)preCompInfo).getPreComp();
        }
        ECPoint.F2m f2m3 = (ECPoint.F2m)f2m.getCurve().getInfinity();
        int n = -1 + arrby.length;
        while (n >= 0) {
            f2m3 = Tnaf.tau(f2m3);
            byte by2 = arrby[n];
            if (by2 != 0) {
                f2m3 = by2 > 0 ? f2m3.addSimple(arrf2m[by2]) : f2m3.subtractSimple(arrf2m[-by2]);
            }
            --n;
        }
        return f2m3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private ECPoint.F2m multiplyWTnaf(ECPoint.F2m f2m, ZTauElement zTauElement, PreCompInfo preCompInfo, byte by, byte by2) {
        ZTauElement[] arrzTauElement = by == 0 ? Tnaf.alpha0 : Tnaf.alpha1;
        BigInteger bigInteger = Tnaf.getTw(by2, 4);
        return WTauNafMultiplier.multiplyFromWTnaf(f2m, Tnaf.tauAdicWNaf(by2, zTauElement, (byte)4, BigInteger.valueOf((long)16L), bigInteger, arrzTauElement), preCompInfo);
    }

    @Override
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        if (!(eCPoint instanceof ECPoint.F2m)) {
            throw new IllegalArgumentException("Only ECPoint.F2m can be used in WTauNafMultiplier");
        }
        ECPoint.F2m f2m = (ECPoint.F2m)eCPoint;
        ECCurve.F2m f2m2 = (ECCurve.F2m)f2m.getCurve();
        int n = f2m2.getM();
        byte by = f2m2.getA().toBigInteger().byteValue();
        byte by2 = f2m2.getMu();
        return this.multiplyWTnaf(f2m, Tnaf.partModReduction(bigInteger, n, by, f2m2.getSi(), by2, (byte)10), f2m2.getPreCompInfo(f2m, PRECOMP_NAME), by, by2);
    }
}

